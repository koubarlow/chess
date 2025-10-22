package dataaccess;

import model.AuthData;
import model.UserData;

import javax.xml.crypto.Data;

public interface UserDAO extends BaseDAO {
    // Register (POST)
    //
    //    Description	Register a new user.
    //    URL path	/user
    //    HTTP Method	POST
    //    Body	{ "username":"", "password":"", "email":"" }
    //    Success response	[200] { "username":"", "authToken":"" }
    //    Failure response	[400] { "message": "Error: bad request" }
    //    Failure response	[403] { "message": "Error: already taken" }
    //    Failure response	[500] { "message": "Error: (description of error)" }

    // GetUser -> Returns UserData by given username

    UserData getUser(String username) throws DataAccessException;

    UserData createUser(UserData userData) throws DataAccessException;

    void clearUsers() throws DataAccessException;
}
