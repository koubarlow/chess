package client;

import chess.*;
import client.websocket.NotificationHandler;
import client.websocket.WebSocketFacade;
import exception.ResponseException;
import model.*;
import server.ServerFacade;
import ui.BoardDrawer;
import websocket.messages.ServerMessage;

import java.util.*;

import static ui.EscapeSequences.*;
import static ui.EscapeSequences.SET_TEXT_COLOR_GREEN;

public class ChessClient implements NotificationHandler {

    private final ServerFacade server;
    private final WebSocketFacade ws;

    private String username = null;
    private AuthData authData = null;
    private State state = State.SIGNEDOUT;
    private final HashMap<Integer, GameData> games;
    private ChessGame.TeamColor currentTeamColor;
    private int currentGameId;
    private final HashMap<Character, Integer> columnTable;
    private final HashMap<Integer, Integer> rowMapper;

    public ChessClient(String serverUrl) throws ResponseException {
        server = new ServerFacade(serverUrl);
        ws = new WebSocketFacade(serverUrl, this);

        games = new HashMap<>();
        columnTable = new HashMap<>();
        rowMapper = new HashMap<>();
        columnTable.put('a', 1);
        columnTable.put('b', 2);
        columnTable.put('c', 3);
        columnTable.put('d', 4);
        columnTable.put('e', 5);
        columnTable.put('f', 6);
        columnTable.put('g', 7);
        columnTable.put('h', 8);

        rowMapper.put(1, 8);
        rowMapper.put(2, 7);
        rowMapper.put(3, 6);
        rowMapper.put(4, 5);
        rowMapper.put(5, 4);
        rowMapper.put(6, 3);
        rowMapper.put(7, 2);
        rowMapper.put(8, 1);
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

    public void notify(ServerMessage serverMessage) {
        try {
            switch (serverMessage.getServerMessageType()) {
                case NOTIFICATION -> displayNotification(serverMessage.getMessage());
                case ERROR -> displayError(serverMessage.getMessage());
                case LOAD_GAME -> loadGame();
            }
        } catch (ResponseException ex) {
            System.out.println(ex.getMessage());
        }
        printPrompt();
    }

    private void displayNotification(String msg) {
        System.out.println(SET_TEXT_COLOR_MAGENTA + msg);
    }

    private void displayError(String msg) {
        System.out.println(SET_TEXT_COLOR_RED + msg);
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
                case "redraw" -> redrawBoard();
                case "leave" -> leaveGame();
                case "move" -> makeMove(params);
                case "resign" -> resign();
                case "highlight" -> highlightLegalMoves(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String register(String... params) throws ResponseException {
        assertSignedOut();
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
        assertSignedOut();
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
        String whiteName = "[join]";
        if (entry.getValue().whiteUsername() != null) {
            whiteName = entry.getValue().whiteUsername();
        }

        String blackName = "[join]";
        if (entry.getValue().blackUsername() != null) {
            blackName = entry.getValue().blackUsername();
        }

        String gameName = entry.getValue().gameName();

        return "[" + entry.getKey().toString() + "] " + gameName + " - " + whiteName + "(white)" + " VS " + blackName + "(black)" + "\n";
    }

    public String joinGame(String... params) throws ResponseException {
        assertSignedIn();
        if (params.length >= 2) {
            this.currentTeamColor = null;

            int gameId;
            try {
                gameId = Integer.parseInt(params[0]);
            } catch (NumberFormatException e) {
                throw new ResponseException(ResponseException.Code.ClientError, "Exception: please enter a valid game number");
            }

            if (Objects.equals(params[1], "white")) {
                this.currentTeamColor = ChessGame.TeamColor.WHITE;
            } else if (Objects.equals(params[1], "black")) {
                this.currentTeamColor = ChessGame.TeamColor.BLACK;
            }

            if (this.games.get(gameId) == null) {
                throw new ResponseException(ResponseException.Code.ClientError, "Exception: game not found");
            }

            server.updateGame(new UpdateGameRequest(this.currentTeamColor, this.games.get(gameId).gameID(), username, null), this.authData.authToken());
            this.currentGameId = gameId;
            this.state = State.GAMEPLAY;

            ws.connect(authData.authToken(), this.currentGameId);
            return String.format("You joined game %s as %s.", gameId, this.currentTeamColor);
        }
        throw new ResponseException(ResponseException.Code.ClientError, "Exception: <ID> <WHITE|BLACK>");
    }


    public String observeGame(String... params) throws ResponseException {
        assertSignedIn();
        if (params.length >= 1) {

            int gameId;
            try {
                gameId = Integer.parseInt(params[0]);
            } catch (NumberFormatException e) {
                throw new ResponseException(ResponseException.Code.ClientError, "Exception: please enter a valid game number");
            }

            this.currentTeamColor = ChessGame.TeamColor.WHITE;
            if (params.length >= 2 && Objects.equals(params[1], "black")) {
                this.currentTeamColor = ChessGame.TeamColor.BLACK;
            }

            if (this.games.get(gameId) == null) {
                throw new ResponseException(ResponseException.Code.ClientError, "Exception: game not found");
            }

            GameData game = server.getGameById(this.authData.authToken(), this.games.get(gameId).gameID());
            this.currentGameId = gameId;

            ws.connect(authData.authToken(), this.currentGameId);
            this.state = State.OBSERVING;
            BoardDrawer.drawBoard(game.game(), this.currentTeamColor, false, null);
            return String.format("You're observing game %s as %s.", gameId, this.currentTeamColor);
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

    public String redrawBoard() throws ResponseException {
        assertPlayingOrWatching();
        System.out.println();
        GameData game = server.getGameById(this.authData.authToken(), this.games.get(currentGameId).gameID());
        BoardDrawer.drawBoard(game.game(), this.currentTeamColor, false, null);
        return "Board redrawn.";
    }

    private void loadGame() throws ResponseException {
        redrawBoard();
    }

    public String leaveGame() throws ResponseException {
        assertPlayingOrWatching();
        ws.leave(authData.authToken(), this.currentGameId);
        GameData game = server.getGameById(this.authData.authToken(), this.games.get(currentGameId).gameID());
        // gotta update game! dont forget
        this.state = State.SIGNEDIN;
        this.currentGameId = 0;
        this.currentTeamColor = null;
        return "Leaving game...";
    }

    public String makeMove(String... params) throws ResponseException {
        assertInGamePlay();
        if (params.length > 1 && params[0].length() == 2 && params[1].length() == 2) {
            try {
                int initialCol = this.columnTable.get(Character.toLowerCase(params[0].charAt(0)));
                int initialRow = Integer.parseInt(String.valueOf(params[0].charAt(1)));
                int endCol = this.columnTable.get(params[1].charAt(0));
                int endRow = Integer.parseInt(String.valueOf(params[1].charAt(1)));

                GameData game = server.getGameById(this.authData.authToken(), this.games.get(currentGameId).gameID());
                ChessPosition beginningPos = new ChessPosition(initialRow, initialCol);
                ChessPosition endPos = new ChessPosition(endRow, endCol);
                ChessPiece pieceToMove = game.game().getBoard().getPiece(beginningPos);

                ChessMove moveToMake = new ChessMove(beginningPos, endPos, null);
                //game.game().makeMove(moveToMake);

                //server.updateGame(new UpdateGameRequest(null, this.games.get(currentGameId).gameID(), null, game), this.authData.authToken());
                ws.makeMove(authData.authToken(), currentGameId, moveToMake);

                return "Moved " + pieceToMove.getPieceType().name().toLowerCase() + " to " + params[1];
            } catch (NumberFormatException e) {
                throw new ResponseException(ResponseException.Code.ClientError, "Exception: please enter a valid row and column for piece");
            }
        }
        throw new ResponseException(ResponseException.Code.ClientError, "Exception: <POSITION>");
    }

    public String resign() throws ResponseException {
        assertInGamePlay();
        ws.resign(authData.authToken(), currentGameId);
        this.state = State.SIGNEDIN;
        return "Resigning...";
    }

    public String highlightLegalMoves(String... params) throws ResponseException {
        assertPlayingOrWatching();
        if (params.length == 1 && params[0].length() == 2) {
            try {
                int col = this.columnTable.get(Character.toLowerCase(params[0].charAt(0)));
                int row = Integer.parseInt(String.valueOf(params[0].charAt(1)));

                GameData game = server.getGameById(this.authData.authToken(), this.games.get(currentGameId).gameID());
                BoardDrawer.drawBoard(game.game(), this.currentTeamColor, true, new ChessPosition(row, col));
                return "Highlighted legal moves.";
            } catch (NumberFormatException e) {
                throw new ResponseException(ResponseException.Code.ClientError, "Exception: please enter a valid row and column");
            }
        }
        throw new ResponseException(ResponseException.Code.ClientError, "Exception: <POSITION>");
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
        if (state == State.GAMEPLAY) {
            return """
                    redraw - chess board
                    leave - game
                    move <POS₁ POS₂>
                    resign
                    highlight <POSITION> - legal moves
                    """;
        }
        return """
                create <NAME> - a game
                list - games
                join <ID> <WHITE|BLACK> - game
                observe <ID> <WHITE|BLACK> - a game (default white)
                logout - when you are done
                quit - playing chess
                help - with possible commands
                """;
    }

    private void assertSignedIn() throws ResponseException {
        if (state == State.GAMEPLAY) {
            throw new ResponseException(ResponseException.Code.ClientError, "Currently in game");
        }
        if (state == State.SIGNEDOUT) {
            throw new ResponseException(ResponseException.Code.ClientError, "You must sign in");
        }
    }

    private void assertSignedOut() throws ResponseException {
        if (state == State.SIGNEDIN) {
            throw new ResponseException(ResponseException.Code.ClientError, "Already logged in");
        }
    }

    private void assertInGamePlay() throws ResponseException {
        if (state != State.GAMEPLAY) {
            throw new ResponseException(ResponseException.Code.ClientError, "Currently not playing game");
        }
    }

    private void assertPlayingOrWatching() throws ResponseException {
        if (state != State.GAMEPLAY && state != State.OBSERVING) {
            throw new ResponseException(ResponseException.Code.ClientError, "Currently not in game");
        }
    }
}
