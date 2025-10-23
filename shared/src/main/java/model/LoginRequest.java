package model;

import com.google.gson.Gson;

import java.util.Objects;

public record LoginRequest(String username, String password) {

    public String toString() {
        return new Gson().toJson(this);
    }
}
