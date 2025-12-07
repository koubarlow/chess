package server.websocket;

import chess.*;
import com.google.gson.Gson;
import dataaccess.auth.AuthDAO;
import dataaccess.auth.MySqlAuthDAO;
import dataaccess.exceptions.UnauthorizedException;
import dataaccess.game.GameDAO;
import dataaccess.game.MySqlGameDAO;
import dataaccess.user.MySqlUserDAO;
import dataaccess.user.UserDAO;
import exception.ResponseException;
import io.javalin.websocket.*;
import model.GameData;
import model.UpdateGameRequest;
import model.UserData;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.ServerMessage;
import java.io.IOException;
import java.util.Objects;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

    private final ConnectionManager connections = new ConnectionManager();
    MySqlGameDAO gameDAO;

    @Override
    public void handleConnect(WsConnectContext ctx) {
        System.out.println("Websocket connected");
        ctx.enableAutomaticPings();
    }

    @Override
    public void handleMessage(@NotNull WsMessageContext ctx) throws Exception {

        int gameId = -1;
        Session session = ctx.session;

        try {
            UserGameCommand command = new Gson().fromJson(ctx.message(), UserGameCommand.class);
            gameId = command.getGameID();

            AuthDAO authDAO = new MySqlAuthDAO();
            UserDAO userDAO = new MySqlUserDAO();
            this.gameDAO = new MySqlGameDAO();

            String username = authDAO.getUsername(command.getAuthToken());
            UserData user = userDAO.getUser(username);
            if (username == null || user == null) {
                throw new UnauthorizedException("Error: unauthorized");
            }

            GameData game = gameDAO.getGame(gameId);
            if (game == null) {
                throw new ResponseException(ResponseException.Code.ClientError, "Error: game not found");
            }

            ChessGame.TeamColor color = null;

            if (Objects.equals(game.whiteUsername(), username)) {
                color = ChessGame.TeamColor.WHITE;
            } else if (Objects.equals(game.blackUsername(), username)) {
                color = ChessGame.TeamColor.BLACK;
            }

            ChessMove move = command.getMove();
            if (move != null) {
                ChessPiece movedPiece = game.game().getBoard().getPiece(move.getStartPosition());
                ChessGame.TeamColor colorOfMovedPiece = movedPiece.getTeamColor();

                if (colorOfMovedPiece != null && colorOfMovedPiece != color) {
                    throw new ResponseException(ResponseException.Code.ClientError, "Error: unable to move opponent's piece!");
                }

                if (game.game().isInCheckmate(color)) {
                    throw new ResponseException(ResponseException.Code.ClientError, "Error: already in checkmate!");
                } else if (game.game().isInStalemate(color)) {
                    throw new ResponseException(ResponseException.Code.ClientError, "Error: already in stalemate!");
                }

                if (colorOfMovedPiece != null && game.game().getTeamTurn() != colorOfMovedPiece) {
                    throw new ResponseException(ResponseException.Code.ClientError, "Error: not your team's turn!");
                }
            }

            switch (command.getCommandType()) {
                case CONNECT -> connect(session, gameId, username, color, game);
                case LEAVE -> leave(session, gameId, username, color, command.getAuthToken());
                case RESIGN -> resign(session, gameId, username, color, game, command.getAuthToken());
                case MAKE_MOVE -> makeMove(session, gameId, username, color, game, move, command.getAuthToken());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            var errorMessage = new ErrorMessage(ex.getMessage());
            session.getRemote().sendString(new Gson().toJson(errorMessage));
        } catch (Exception ex) {
            var errorMessage = new ErrorMessage(ex.getMessage());
            session.getRemote().sendString(new Gson().toJson(errorMessage));
        }
    }

    @Override
    public void handleClose(@NotNull WsCloseContext ctx) { System.out.println("Websocket closed");}

    private void connect(Session session, int gameId, String username, ChessGame.TeamColor color, GameData game) throws IOException {
        connections.addSessionToGame(gameId, session);
        String teamColor = "";
        if (color == null) {
            teamColor = "an OBSERVER";
        } else {
            teamColor = color.name();
        }

        var loadGameMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, null, game);
        session.getRemote().sendString(new Gson().toJson(loadGameMessage));

        var serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, String.format("%s joined the game as %s", username, teamColor), null);
        connections.broadcast(gameId, session, serverMessage);
    }

    private void leave(Session session, int gameId, String username, ChessGame.TeamColor color, String authToken) throws Exception {
        connections.removeSessionFromGame(gameId, session);
        String teamColor = "";
        if (color == null) {
            teamColor = "OBSERVER";
        } else {
            teamColor = color.name();
        }

        GameData game = gameDAO.getGame(gameId);
        GameData newGameData = null;

        if (color != null) {
            if (color == ChessGame.TeamColor.WHITE) {
                newGameData = new GameData(game.gameID(), null, game.blackUsername(), game.gameName(), game.game());
            } else {
                newGameData = new GameData(game.gameID(), game.whiteUsername(), null, game.gameName(), game.game());
            }

            gameDAO.updateGame(new UpdateGameRequest(null, gameId, null, newGameData), authToken);
        }

        var serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, String.format("%s(%s) has left the game", username, teamColor), null);
        connections.broadcast(gameId, session, serverMessage);
    }

    private void resign(Session session, int gameId, String username, ChessGame.TeamColor color, GameData game, String authToken) throws Exception {
        if (!game.game().isGameOver()) {
            game.game().setGameOver(true);
            gameDAO.updateGame(new UpdateGameRequest(color, gameId, null, game), authToken);
            var serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, String.format("%s(%s) has resigned. Good game!", username, color.name()), null);
            connections.broadcast(gameId, null, serverMessage);
        } else {
            var gameAlreadyOver = new ErrorMessage("Game is already over.");
            session.getRemote().sendString(new Gson().toJson(gameAlreadyOver));
        }
    }

    private void makeMove(Session session, int gameId, String username, ChessGame.TeamColor color, GameData game, ChessMove move, String authToken) throws Exception {

        if (game.game().isGameOver()) {
            var gameAlreadyOver = new ErrorMessage("Game is already over.");
            session.getRemote().sendString(new Gson().toJson(gameAlreadyOver));
            return;
        }

        ChessGame.TeamColor opposingColor = ChessGame.TeamColor.BLACK;
        if (color == ChessGame.TeamColor.BLACK) {
            opposingColor = ChessGame.TeamColor.WHITE;
        }

        boolean isInCheckmate = game.game().isInCheckmate(color);
        boolean isOpponentInCheckmate = game.game().isInCheckmate(opposingColor);
        if (isInCheckmate || isOpponentInCheckmate) {
            var checkmateMessage = new ErrorMessage(String.format("%s has been checkmated! Good game.", color.name()));
            connections.broadcast(gameId, null, checkmateMessage);
            return;
        } else if (game.game().isInCheck(color)) {
            var checkmateMessage = new ErrorMessage(String.format("%s is in check!", color.name()));
            connections.broadcast(gameId, null, checkmateMessage);
            return;
        } else if (game.game().isInStalemate(color)) {
            var checkmateMessage = new ErrorMessage("Stalemate! Good game.");
            connections.broadcast(gameId, null, checkmateMessage);
            return;
        }

        try {
            game.game().makeMove(move);
        } catch (InvalidMoveException e) {
            var invalidMoveMessage = new ErrorMessage("Error: invalid move!");
            session.getRemote().sendString(new Gson().toJson(invalidMoveMessage));
            return;
        }

        var loadGameMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, null, game);
        connections.broadcast(gameId, null, loadGameMessage);
        //session.getRemote().sendString(new Gson().toJson(loadGameMessage));

        ChessPiece pieceToDisplay = game.game().getBoard().getPiece(move.getEndPosition());
        String pieceName = "a piece";
        if (pieceToDisplay != null) {
            pieceName = pieceToDisplay.getPieceType().name();
        }

        gameDAO.updateGame(new UpdateGameRequest(color, gameId, null, game), authToken);

        var serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, String.format("%s(%s) has moved %s from %s to %s", username, color.name(), pieceName, move.getStartPosition().toChessTablePosition(), move.getEndPosition().toChessTablePosition()), null);
        connections.broadcast(gameId, session, serverMessage);
    }

//    public void notifyOfConnection() throws ResponseException {
//        try {
//            var serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, "someone connected");
//            connections.broadcast(1, null, serverMessage);
//        } catch (Exception ex) {
//            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
//        }
//    }
}
