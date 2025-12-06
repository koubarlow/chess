package client.websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import com.google.gson.Gson;
import exception.ResponseException;
import jakarta.websocket.*;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {

    Session session;
    NotificationHandler notificationHandler;

    public WebSocketFacade(String url, NotificationHandler notificationHandler) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage notification = new Gson().fromJson(message, ServerMessage.class);
                    notificationHandler.notify(notification);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void connect(String authToken, int gameId, String username, ChessGame.TeamColor color) throws ResponseException {
        sendCommand(UserGameCommand.CommandType.CONNECT, authToken, gameId, username, null, color, null);
    }

    public void makeMove(String authToken, int gameId, ChessMove move, String username, ChessGame.TeamColor color, ChessPiece piece) throws ResponseException {
        sendCommand(UserGameCommand.CommandType.MAKE_MOVE, authToken, gameId, username, move, color, piece);
    }

    public void leave(String authToken, int gameId, String username, ChessGame.TeamColor color) throws ResponseException {
        sendCommand(UserGameCommand.CommandType.LEAVE, authToken, gameId, username, null, color, null);
    }

    public void resign(String authToken, int gameId, String username, ChessGame.TeamColor color) throws ResponseException {
        sendCommand(UserGameCommand.CommandType.RESIGN, authToken, gameId, username, null, color, null);
    }

    private void sendCommand(UserGameCommand.CommandType commandType, String authToken, int gameId, String username, ChessMove move, ChessGame.TeamColor color, ChessPiece piece) throws ResponseException {
        try {
            var command = new UserGameCommand(commandType, authToken, gameId, username, move, color, piece);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }
    }
}
