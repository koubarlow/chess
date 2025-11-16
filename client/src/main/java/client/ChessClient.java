package client;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import model.*;
import server.ServerFacade;
import ui.BoardDrawer;

import java.util.*;

import static ui.EscapeSequences.*;
import static ui.EscapeSequences.SET_TEXT_COLOR_GREEN;

public class ChessClient {

    private String username = null;
    private AuthData authData = null;
    private final ServerFacade server;
    private State state = State.SIGNEDOUT;
    private HashMap<Integer, GameData> games;

    public ChessClient(String serverUrl) throws ResponseException {
        server = new ServerFacade(serverUrl);
        games = new HashMap<>();
    }

    public void run() {
        System.out.println(" ♕ Welcome to chess. Sign in to start. ♕");
        System.out.println(help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = eval(line);
                System.out.println(SET_TEXT_COLOR_BLUE + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.println(msg);
            }
        }

        System.out.println();
    }

    private void printPrompt() {
        System.out.print("\n" + RESET_TEXT_COLOR + ">>> " + SET_TEXT_COLOR_GREEN);
    }

    public String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);

            return switch (cmd) {
                case "register" -> register(params);
                case "login" -> login(params);
                case "create" -> createGame(params);
                case "list" -> listGames();
                case "join" -> joinGame(params);
                case "observe" -> observeGame(params);
                case "logout" -> logout();
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String register(String... params) throws ResponseException {
        if (params.length >= 3) {
            this.username = params[0];
            String password = params[1];
            String email = params[2];
            this.authData = server.register(new RegisterRequest(username, password, email));
            if (this.authData != null) {
                state = State.SIGNEDIN;
            }
            return String.format("You registered and signed in as %s.", username);
        }
        throw new ResponseException(ResponseException.Code.ClientError, "Expected: <USERNAME> <PASSWORD> <EMAIL>");
    }

    public String login(String... params) throws ResponseException {
        if (params.length >= 2) {
            this.username = params[0];
            String password = params[1];
            this.authData = server.login(new LoginRequest(username, password));
            if (authData != null) { state = State.SIGNEDIN; }
            return String.format("You signed in as %s.", username);
        }
        throw new ResponseException(ResponseException.Code.ClientError, "Expected: <USERNAME> <PASSWORD>");
    }

    public String createGame(String... params) throws ResponseException {
        assertSignedIn();
        if (params.length >= 1) {
            String gameName = params[0];
            server.createGame(new CreateGameRequest(gameName), this.authData.authToken());
            return String.format("You created a game named: %s.", gameName);
        }
        throw new ResponseException(ResponseException.Code.ClientError, "Expected: <NAME>");
    }

    public String listGames() throws ResponseException {
        assertSignedIn();
        GamesWrapper gameList = server.listGames(this.authData.authToken());
        var result = new StringBuilder();
        int gameNumber = 1;
        for (GameData game : gameList.games()) {
            this.games.put(gameNumber, game);
            gameNumber++;
        }

        for (Map.Entry<Integer, GameData> entry : this.games.entrySet()) {
            String gameString = getGameAsString(entry);
            result.append(gameString);
        }

        return result.toString();
    }

    private static String getGameAsString(Map.Entry<Integer, GameData> entry) {
        String whiteName = "[join as WHITE]";
        if (entry.getValue().whiteUsername() != null) {
            whiteName = entry.getValue().whiteUsername();
        }

        String blackName = "[join as BLACK]";
        if (entry.getValue().blackUsername() != null) {
            blackName = entry.getValue().blackUsername();
        }

        String gameName = entry.getValue().gameName();

        return "[" + entry.getKey().toString() + "] " + gameName + " | " + whiteName + " VS " + blackName + "\n";
    }

    public String joinGame(String... params) throws ResponseException {
        assertSignedIn();
        if (params.length >= 2) {
            ChessGame.TeamColor teamColor = null;
            
            int gameId = Integer.parseInt(params[0]);
            if (Objects.equals(params[1], "white")) {
                teamColor = ChessGame.TeamColor.WHITE;
            } else if (Objects.equals(params[1], "black")) {
                teamColor = ChessGame.TeamColor.BLACK;
            }

            if (this.games.get(gameId) == null) {
                throw new ResponseException(ResponseException.Code.ClientError, "Exception: game not found");
            }

            server.joinGame(new JoinGameRequest(teamColor, this.games.get(gameId).gameID(), username), this.authData.authToken());
            GameData game = server.getGameById(this.authData.authToken(), gameId);
            BoardDrawer.drawBoard(game.game(), teamColor);
            return String.format("You joined game %s as %s.", gameId, teamColor);
        }
        throw new ResponseException(ResponseException.Code.ClientError, "Exception: <ID> <WHITE|BLACK>");
    }


    public String observeGame(String... params) throws ResponseException {
        assertSignedIn();
        if (params.length >= 1) {
            int gameId = Integer.parseInt(params[0]);

            ChessGame.TeamColor teamColor = ChessGame.TeamColor.WHITE;
            if (params.length >= 2 && Objects.equals(params[1], "black")) {
                teamColor = ChessGame.TeamColor.BLACK;
            }

            if (this.games.get(gameId) == null) {
                throw new ResponseException(ResponseException.Code.ClientError, "Exception: game not found");
            }

            GameData game = server.getGameById(this.authData.authToken(), this.games.get(gameId).gameID());
            BoardDrawer.drawBoard(game.game(), teamColor);
            return String.format("You're observing game %s as %s.", gameId, teamColor);
        }
        throw new ResponseException(ResponseException.Code.ClientError, "Exception: <ID> <WHITE|BLACK>");
    }

    public String logout() throws ResponseException {
        assertSignedIn();
        state = State.SIGNEDOUT;
        server.logout(this.authData.authToken());
        this.authData = null;
        String usernameToSayGoodbyeTo = this.username;
        this.username = null;
        return String.format("You signed out. Thank you for playing, %s.", usernameToSayGoodbyeTo);
    }

    public String help() {
        if (state == State.SIGNEDOUT) {
            return """
                    register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                    login <USERNAME> <PASSWORD> - to play chess
                    quit - playing chess
                    help - with possible commands
                    """;
        }
        return """
                create <NAME> - a game
                list - games
                join <ID> [WHITE|BLACK] - game
                observe <ID> <WHITE|BLACK> - a game (default white)
                logout - when you are done
                quit - playing chess
                help - with possible commands
                """;
    }

    private void assertSignedIn() throws ResponseException {
        if (state == State.SIGNEDOUT) {
            throw new ResponseException(ResponseException.Code.ClientError, "You must sign in");
        }
    }
}
