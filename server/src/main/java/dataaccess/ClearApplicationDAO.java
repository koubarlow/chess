package dataaccess;

public interface ClearApplicationDAO extends BaseDAO {
    // 'db' endpoint

    //    Clear application
    //    property	value
    //    Description	Clears the database. Removes all users, games, and authTokens.
    //    URL path	/db
    //    HTTP Method	DELETE
    //    Success response	[200] {}
    //    Failure response	[500] { "message": "Error: (description of error)" }
}
