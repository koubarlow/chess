package dataaccess;

import model.*;

public interface GameDAO extends BaseDAO {
    // 'game' endpoint
    // Create Game (POST), Join Game (PUT), List Game (GET)
    //

    //    List Games
    //    Note that whiteUsername and blackUsername may be null.
    //    property	value
    //    Description	Gives a list of all games.
    //    URL path	/game
    //    HTTP Method	GET
    //    Headers	authorization: <authToken>
    //    Success response	[200] { "games": [{"gameID": 1234, "whiteUsername":"", "blackUsername":"", "gameName:""} ]}
    //    Failure response	[401] { "message": "Error: unauthorized" }
    //    Failure response	[500] { "message": "Error: (description of error)" }
    //
    //    Create Game
    //    property	value
    //    Description	Creates a new game.
    //            URL path	/game
    //    HTTP Method	POST
    //    Headers	authorization: <authToken>
    //            Body	{ "gameName":"" }
    //    Success response	[200] { "gameID": 1234 }
    //    Failure response	[400] { "message": "Error: bad request" }
    //    Failure response	[401] { "message": "Error: unauthorized" }
    //    Failure response	[500] { "message": "Error: (description of error)" }
    //
    //    Join Game
    //    property	value
    //    Description	Verifies that the specified game exists and adds the caller as the requested color to the game.
    //    URL path	/game
    //    HTTP Method	PUT
    //    Headers	authorization: <authToken>
    //            Body	{ "playerColor":"WHITE/BLACK", "gameID": 1234 }
    //    Success response	[200] {}
    //    Failure response	[400] { "message": "Error: bad request" }
    //    Failure response	[401] { "message": "Error: unauthorized" }
    //    Failure response	[403] { "message": "Error: already taken" }
    //    Failure response	[500] { "message": "Error: (description of error)" }

    GameList listGames() throws Exception;

    GameData createGame(CreateGameRequest createGameRequest) throws Exception;

    void joinGame(JoinGameRequest joinGameRequest) throws Exception;

    void clearGames() throws Exception;
}
