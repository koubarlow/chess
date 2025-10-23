package dataaccess;

import model.AuthData;
import model.LoginRequest;
import model.UserData;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MemoryAuthDAO implements AuthDAO {

    private HashMap<String, String> sessions = new HashMap<>();

    public MemoryAuthDAO() {
    }

    public MemoryAuthDAO(HashMap<String, String> sessions) {
        this.sessions = sessions;
    }

    public AuthData login(LoginRequest loginRequest, UserData existingUser) throws Exception {
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

    public boolean authenticateUser(LoginRequest request, UserData existingUser) throws Exception {
        String username = request.username();
        String password = request.password();

        if (existingUser == null) { throw new UnauthorizedException("Error: unauthorized"); }
        return Objects.equals(existingUser.username(), username) && Objects.equals(existingUser.password(), password);
    }

    public void logout(String authToken) throws Exception {
        if (sessions.get(authToken) != null) {
            sessions.remove(authToken);
            return;
        }
        throw new UnauthorizedException("Error: unauthorized");
    }

    public void clearSessions() throws DataAccessException {
        this.sessions.clear();
    }
}
