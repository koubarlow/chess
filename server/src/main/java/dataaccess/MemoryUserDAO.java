package dataaccess;

import model.UserData;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {

    final private HashMap<String, UserData> users = new HashMap<>();

    public UserData getUser(String username) {
        return users.get(username);
    }

    public UserData createUser(UserData userData) {
        UserData user = new UserData(userData.username(), userData.password(), userData.email());
        users.put(user.username(), user);
        return user;
    }
}
