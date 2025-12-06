package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import com.google.gson.Gson;
import dataaccess.exceptions.UnauthorizedException;
import exception.ResponseException;
import io.javalin.websocket.*;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;

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
            String username = command.getUsername();
            ChessGame.TeamColor color = command.getColor();
            ChessMove move = command.getMove();
            String pieceName = command.getPieceName();

            switch (command.getCommandType()) {
                case CONNECT -> connect(session, gameId, username, color);
                case LEAVE -> leave(session, gameId, username, color);
                case RESIGN -> resign(session, gameId, username, color);
                case MAKE_MOVE -> makeMove(session, gameId, username, color, pieceName, move.getStartPosition().toString(), move.getEndPosition().toString());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            connections.broadcast(gameId, session, new ServerMessage(ServerMessage.ServerMessageType.ERROR, ex.getMessage()));
        } catch (Exception ex) {
            connections.broadcast(gameId, session, new ServerMessage(ServerMessage.ServerMessageType.ERROR, ex.getMessage()));
        }
    }

    @Override
    public void handleClose(@NotNull WsCloseContext ctx) { System.out.println("Websocket closed");}

    private void connect(Session session, int gameId, String username, ChessGame.TeamColor color) throws IOException {
        connections.addSessionToGame(gameId, session);
        String teamColor = color.toString();
        if (teamColor == null) {
            teamColor = "OBSERVER";
        }
        var serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, String.format("%s joined the game as %s", username, teamColor));
        connections.broadcast(gameId, session, serverMessage);
    }

    private void leave(Session session, int gameId, String username, ChessGame.TeamColor color) throws IOException {
        connections.removeSessionFromGame(gameId, session);
        var serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, String.format("%s(%s) has left the game", username, color.toString()));
        connections.broadcast(gameId, session, serverMessage);
    }

    private void resign(Session session, int gameId, String username, ChessGame.TeamColor color) throws IOException {
        connections.removeSessionFromGame(gameId, session);
        var serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, String.format("%s(%s) has resigned. Good game!", username, color.toString()));
        connections.broadcast(gameId, session, serverMessage);
    }

    public void makeMove(Session session, int gameId, String username, ChessGame.TeamColor color, String pieceName, String startingPos, String endPos) throws IOException {
        var serverMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, String.format("%s(%s) has moved %s from %s to %s", username, color.toString(), pieceName, startingPos, endPos));
        connections.broadcast(gameId, session, serverMessage);
    }
}
