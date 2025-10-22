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

    public AuthData login(LoginRequest loginRequest) throws DataAccessException {
        if (usernameAndPasswordMatch(loginRequest) && !(sessionExistsFor(loginRequest.username()))) {
            AuthData authData = new AuthData(AuthDAO.generateToken(), loginRequest.username());
            this.sessions.put(authData.authToken(), authData.username());
            return authData;
        }
        return null;
    }

    public boolean sessionExistsFor(String username) {
        for (Map.Entry<String, String> session: sessions.entrySet()) {
            if (Objects.equals(username, session.getValue())) {
                return true;
            }
        }
        return false;
    }

    public boolean usernameAndPasswordMatch(LoginRequest request) throws DataAccessException {
        String username = request.username();
        String password = request.password();
        UserData user = userDAO.getUser(username);

        return Objects.equals(user.username(), username) && Objects.equals(user.password(), password);
    }

    public void logout(String authToken) throws DataAccessException {
        if (sessions.get(authToken) != null) {
            sessions.remove(authToken);
            return;
        }
        throw new DataAccessException("Already logged out");
    }
}
