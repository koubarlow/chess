package dataaccess.user;

import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {

    private HashMap<String, UserData> users = new HashMap<>();

    public MemoryUserDAO() {}

    public MemoryUserDAO(HashMap<String, UserData> users) {
        this.users = users;
    }

    public UserData getUser(String username) {
        return users.get(username);
    }

    public UserData createUser(UserData userData) {
        String hashedPassword = BCrypt.hashpw(userData.password(), BCrypt.gensalt());
        UserData user = new UserData(userData.username(), hashedPassword, userData.email());
        users.put(user.username(), user);
        return user;
    }

    public void clearUsers() {
        this.users.clear();
    }
}
