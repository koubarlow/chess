package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import com.google.gson.Gson;
import dataaccess.auth.AuthDAO;
import dataaccess.auth.MySqlAuthDAO;
import dataaccess.game.MySqlGameDAO;
import io.javalin.websocket.*;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.Objects;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

    private final ConnectionManager connections = new ConnectionManager();

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
            MySqlGameDAO gameDAO = new MySqlGameDAO();

            String username = authDAO.getUsername(command.getAuthToken());
            GameData game = gameDAO.getGame(gameId);

            ChessMove move = command.getMove();
            ChessGame.TeamColor color = null;
            if (Objects.equals(game.whiteUsername(), username)) {
                color = ChessGame.TeamColor.WHITE;
            } else if (Objects.equals(game.blackUsername(), username)) {
                color = ChessGame.TeamColor.BLACK;
            }

            switch (command.getCommandType()) {
                case CONNECT -> connect(session, gameId, username, color, game);
                case LEAVE -> leave(session, gameId, username, color);
                case RESIGN -> resign(session, gameId, username, color);
                case MAKE_MOVE -> makeMove(session, gameId, username, color, game, move);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            connections.broadcast(gameId, session, new ServerMessage(ServerMessage.ServerMessageType.ERROR, ex.getMessage(), null));
        } catch (Exception ex) {
            connections.broadcast(gameId, session, new ServerMessage(ServerMessage.ServerMessageType.ERROR, ex.getMessage(), null));
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

    private void leave(Session session, int gameId, String username, ChessGame.TeamColor color) throws IOException {
        connections.removeSessionFromGame(gameId, session);
        var serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, String.format("%s(%s) has left the game", username, color.toString()), null);
        connections.broadcast(gameId, session, serverMessage);
    }

    private void resign(Session session, int gameId, String username, ChessGame.TeamColor color) throws IOException {
        connections.removeSessionFromGame(gameId, session);
        var serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, String.format("%s(%s) has resigned. Good game!", username, color.toString()), null);
        connections.broadcast(gameId, session, serverMessage);
    }

    private void makeMove(Session session, int gameId, String username, ChessGame.TeamColor color, GameData game, ChessMove move) throws IOException {
        var loadGameMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, null, game);
        connections.broadcast(gameId, null, loadGameMessage);
        //session.getRemote().sendString(new Gson().toJson(loadGameMessage));

        ChessPiece pieceToDisplay = game.game().getBoard().getPiece(move.getEndPosition());
        String pieceName = "a piece";
        if (pieceToDisplay != null) {
            pieceName = pieceToDisplay.getPieceType().name();
        }

        var serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, String.format("%s(%s) has moved %s from %s to %s", username, color.toString(), pieceName, move.getStartPosition().toChessTablePosition(), move.getEndPosition().toChessTablePosition()), null);
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
