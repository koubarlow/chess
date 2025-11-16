package dataaccess.exceptions;

import com.google.gson.Gson;
import exception.ResponseException;

import java.util.Map;

/**
 * Indicates there was an error connecting to the database
 */
public class DataAccessException extends ResponseException {
    public DataAccessException(String message) {
        super(Code.ServerError, message);
    }

    public String toJson() {
        return new Gson().toJson(Map.of("message", getMessage()));
    }
}
