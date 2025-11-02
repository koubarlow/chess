package dataaccess.auth;

import dataaccess.BaseDAO;
import dataaccess.exceptions.UnauthorizedException;
import model.AuthData;
import model.LoginRequest;
import model.UserData;

import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO {

    private HashMap<String, String> sessions = new HashMap<>();

    public MemoryAuthDAO() {
    }

    public MemoryAuthDAO(HashMap<String, String> sessions) {
        this.sessions = sessions;
    }

    public AuthData createAuth(LoginRequest loginRequest, UserData existingUser) throws Exception {
        if (authenticateUser(loginRequest, existingUser)) {
            AuthData authData = new AuthData(BaseDAO.generateId(), loginRequest.username());
            this.sessions.put(authData.authToken(), authData.username());
            return authData;
        }
        throw new UnauthorizedException("Error: unauthorized");
    }

    public boolean sessionExistsForAuthToken(String authToken) {
        return sessions.get(authToken) != null;
    }

    public String getUsername(String authToken) {
        return sessions.get(authToken);
    }

    public void logout(String authToken) throws Exception {
        if (sessions.get(authToken) != null) {
            sessions.remove(authToken);
            return;
        }
        throw new UnauthorizedException("Error: unauthorized");
    }

    public void clearAuth() {
        this.sessions.clear();
    }
}
