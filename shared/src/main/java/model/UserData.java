package model;

import com.google.gson.Gson;

import java.util.Objects;

public record UserData(String username, String password, String email) {

    public UserData {
        Objects.requireNonNull(username, "username cannot be empty");
        Objects.requireNonNull(password, "password cannot be empty");
        Objects.requireNonNull(email, "email cannot be empty");
    }

    public String toString() {
        return new Gson().toJson(this);
    }
}
