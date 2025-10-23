package dataaccess;

import model.AuthData;
import model.LoginRequest;

import java.util.UUID;

public interface AuthDAO extends BaseDAO {
    // 'session' endpoint
    // Login (POST), Logout (DELETE)

    //    Login
    //    property	value
    //    Description	Logs in an existing user (returns a new authToken).
    //    URL path	/session
    //    HTTP Method	POST
    //    Body	{ "username":"", "password":"" }
    //    Success response	[200] { "username":"", "authToken":"" }
    //    Failure response	[400] { "message": "Error: bad request" }
    //    Failure response	[401] { "message": "Error: unauthorized" }
    //    Failure response	[500] { "message": "Error: (description of error)" }
    //
    //    Logout
    //    property	value
    //    Description	Logs out the user represented by the authToken.
    //    URL path	/session
    //    HTTP Method	DELETE
    //    Headers	authorization: <authToken>
    //    Success response	[200] {}
    //    Failure response	[401] { "message": "Error: unauthorized" }
    //    Failure response	[500] { "message": "Error: (description of error)" }

    // GetAuth -> Returns AuthData by authToken
    AuthData login(LoginRequest loginRequest) throws Exception;
    boolean sessionExistsForAuthToken(String authToken);
    String getUsername(String authToken);
    boolean authenticateUser(LoginRequest loginRequest) throws Exception;
    void logout(String authToken) throws Exception;
    void clearSessions() throws Exception;
}
