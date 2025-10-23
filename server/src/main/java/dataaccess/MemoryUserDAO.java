package dataaccess;

import model.UserData;

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
        UserData user = new UserData(userData.username(), userData.password(), userData.email());
        users.put(user.username(), user);
        return user;
    }

    public void clearUsers() {
        this.users.clear();
    }
}
