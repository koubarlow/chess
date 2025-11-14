package client;

import chess.ChessBoard;
import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import model.*;
import server.ServerFacade;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static ui.EscapeSequences.*;
import static ui.EscapeSequences.SET_TEXT_COLOR_GREEN;

public class ChessClient {

    private String username = null;
    private AuthData authData = null;
    private final ServerFacade server;
    private State state = State.SIGNEDOUT;

    public ChessClient(String serverUrl) throws Exception {
        server = new ServerFacade(serverUrl);
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
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    public String register(String... params) throws Exception {
        if (params.length >= 3) {
            state = State.SIGNEDIN;
            this.username = params[0];
            String password = params[1];
            String email = params[2];
            this.authData = server.register(new RegisterRequest(username, password, email));
            return String.format("You registered and signed in as %s.", username);
        }
        throw new Exception("Expected: <USERNAME> <PASSWORD> <EMAIL>");
    }

    public String login(String... params) throws Exception {
        if (params.length >= 2) {
            state = State.SIGNEDIN;
            this.username = params[0];
            String password = params[1];
            this.authData = server.login(new LoginRequest(username, password));
            return String.format("You signed in as %s.", username);
        }
        throw new Exception("Expected: <USERNAME> <PASSWORD>");
    }

    public String createGame(String... params) throws Exception {
        assertSignedIn();
        if (params.length >= 1) {
            String gameName = params[0];
            server.createGame(new CreateGameRequest(gameName), this.authData.authToken());
            return String.format("You created a game named: %s.", gameName);
        }
        throw new Exception("Expected: <NAME>");
    }

    public String listGames() throws Exception {
        assertSignedIn();
        GamesWrapper gameList = server.listGames(this.authData.authToken());
        var result = new StringBuilder();
        var gson = new Gson();
        for (GameData game : gameList.games()) {
            result.append(gson.toJson(game)).append('\n');
        }
        return result.toString();
    }

    public String joinGame(String... params) throws Exception {
        assertSignedIn();
        if (params.length >= 2) {
            ChessGame.TeamColor teamColor = null;
            
            int gameId = Integer.parseInt(params[0]);
            if (Objects.equals(params[1], "white")) {
                teamColor = ChessGame.TeamColor.WHITE;
            } else if (Objects.equals(params[1], "black")) {
                teamColor = ChessGame.TeamColor.BLACK;
            }
            server.joinGame(new JoinGameRequest(teamColor, gameId, username), this.authData.authToken());
            drawBoard(new ChessGame(), teamColor);
            return String.format("You joined game %s as %s.", gameId, teamColor);
        }
        throw new Exception("Exception: <ID> <WHITE|BLACK>");
    }

    public String observeGame(String... params) throws ResponseException {
        assertSignedIn();
        if (params.length >= 1) {
            int gameId = Integer.parseInt(params[0]);
            // Observe game
            return String.format("You're observing game %s as %s.", gameId, ChessGame.TeamColor.WHITE);
        }
        throw new ResponseException(ResponseException.Code.ClientError, "Exception: <ID> <WHITE|BLACK>");
    }

    public String logout() throws Exception {
        assertSignedIn();
        state = State.SIGNEDOUT;
        server.logout(this.authData.authToken());
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
                observe <ID> - a game
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

    private static final int BOARD_SIZE_IN_SQUARES = 10;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 3;
    private static final int LINE_WIDTH_IN_PADDED_CHARS = 1;
    private static Random rand = new Random();

    private void drawBoard(ChessGame chessGame, ChessGame.TeamColor teamColor) {
        ChessBoard board = chessGame.getBoard();
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);
        drawHeaders(out, teamColor);
//        drawChessBoard(out, teamColor);
//        drawFooters(out, teamColor);
        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
    }

    private static void drawHeaders(PrintStream out, ChessGame.TeamColor color) {
        setBlue(out);
        String[] headers = {" ", "a", "b", "c", "d", "e", "f", "g", "h", " "};

        if (color == ChessGame.TeamColor.BLACK) {
            headers = new String[]{" ", "h", "g", "f", "e", "d", "c", "b", "a", " "};
        }
        for  (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
            drawHeader(out, headers[boardCol]);

            if (boardCol < BOARD_SIZE_IN_SQUARES - 1) {
                out.print(EMPTY.repeat(LINE_WIDTH_IN_PADDED_CHARS));
            }
        }
        out.println();

    }

    private static void drawHeader(PrintStream out, String headerText) {
        int prefixLength = SQUARE_SIZE_IN_PADDED_CHARS / 2;
        int suffixLength = SQUARE_SIZE_IN_PADDED_CHARS - prefixLength - 1;

        out.print(EMPTY.repeat(prefixLength));
        printHeaderText(out, headerText);
        out.print(EMPTY.repeat(suffixLength));
    }

    private static void printHeaderText(PrintStream out, String player) {
        out.print(SET_BG_COLOR_BLUE);
        out.print(SET_TEXT_COLOR_WHITE);

        out.print(player);

        setBlue(out);
    }

    private static void setBlue(PrintStream out) {
        out.print(SET_BG_COLOR_BLUE);
        out.print(SET_TEXT_COLOR_BLUE);
    }
}
