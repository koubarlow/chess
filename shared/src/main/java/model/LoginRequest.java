package model;

import com.google.gson.Gson;

import java.util.Objects;

public record LoginRequest(String username, String password) {

    public LoginRequest {
        Objects.requireNonNull(username, "username cannot be empty");
        Objects.requireNonNull(password, "password cannot be empty");
    }

    public String toString() {
        return new Gson().toJson(this);
    }
}
