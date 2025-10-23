package dataaccess;

import model.AuthData;
import model.LoginRequest;
import model.UserData;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MemoryAuthDAO implements AuthDAO {

    final private HashMap<String, String> sessions = new HashMap<>();
    UserDAO userDAO;

    public MemoryAuthDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public AuthData login(LoginRequest loginRequest) throws Exception {
        if (authenticateUser(loginRequest)) {
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

    public boolean authenticateUser(LoginRequest request) throws Exception {
        String username = request.username();
        String password = request.password();

        UserData existingUser = userDAO.getUser(username);
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
