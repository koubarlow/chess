package server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {

    public final HashMap<Integer, Set<Session>> connections = new HashMap<>();

    public void addSessionToGame(int gameId, Session session) {

        Set<Session> setOfSessions = connections.get(gameId);
        if (setOfSessions == null) {
            setOfSessions = new HashSet<>();
        }
        setOfSessions.add(session);
        connections.put(gameId, setOfSessions);
    }

    public void removeSessionFromGame(int gameId, Session session) {
        Set<Session> setOfSessions = connections.get(gameId);
        setOfSessions.remove(session);
        connections.put(gameId, setOfSessions);
    }

    public void broadcast(int gameId, Session excludeSession, ServerMessage serverMessage) throws IOException {
        String msg = serverMessage.getMessage();
        for (Session c : connections.get(gameId)) {
            if (c.isOpen()) {
                if (!c.equals(excludeSession)) {
                    c.getRemote().sendString(msg);
                }
            }
        }
    }
}













