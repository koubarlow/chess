package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;
import static ui.EscapeSequences.BLACK_BISHOP;
import static ui.EscapeSequences.BLACK_KING;
import static ui.EscapeSequences.BLACK_KNIGHT;
import static ui.EscapeSequences.BLACK_PAWN;
import static ui.EscapeSequences.BLACK_QUEEN;
import static ui.EscapeSequences.BLACK_ROOK;
import static ui.EscapeSequences.EMPTY;
import static ui.EscapeSequences.RESET_BG_COLOR;
import static ui.EscapeSequences.RESET_TEXT_COLOR;
import static ui.EscapeSequences.SET_BG_COLOR_BLACK;
import static ui.EscapeSequences.SET_BG_COLOR_BLUE;
import static ui.EscapeSequences.SET_BG_COLOR_DARK_GREY;
import static ui.EscapeSequences.SET_BG_COLOR_LIGHT_GREY;
import static ui.EscapeSequences.SET_BG_COLOR_RED;
import static ui.EscapeSequences.SET_BG_COLOR_WHITE;
import static ui.EscapeSequences.SET_TEXT_COLOR_BLACK;
import static ui.EscapeSequences.SET_TEXT_COLOR_BLUE;
import static ui.EscapeSequences.SET_TEXT_COLOR_RED;
import static ui.EscapeSequences.SET_TEXT_COLOR_WHITE;
import static ui.EscapeSequences.SPACE;
import static ui.EscapeSequences.TAB;
import static ui.EscapeSequences.WHITE_BISHOP;
import static ui.EscapeSequences.WHITE_KING;
import static ui.EscapeSequences.WHITE_KNIGHT;
import static ui.EscapeSequences.WHITE_PAWN;
import static ui.EscapeSequences.WHITE_QUEEN;
import static ui.EscapeSequences.WHITE_ROOK;

public class BoardDrawer {

    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final String[] BOARD_ROWS = {" 8 ", " 7 ", " 6 ", " 5 ", " 4 ", " 3 ", " 2 ", " 1 " };

    public static void drawBoard(ChessGame chessGame, ChessGame.TeamColor teamColor) {
        ChessBoard board = chessGame.getBoard();
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);
        drawHeaders(out, teamColor);
        drawChessBoard(out, board, teamColor);
        drawHeaders(out, teamColor);
        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
    }

    private static void drawHeaders(PrintStream out, ChessGame.TeamColor color) {
        setBlue(out);
        String[] headers = {"a", "b", "c", "d", "e", "f", "g", "h"};

        if (color == ChessGame.TeamColor.BLACK) {
            headers = new String[]{"h", "g", "f", "e", "d", "c", "b", "a"};
        }
        out.print(TAB);
        for  (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
            drawHeader(out, headers[boardCol]);
        }
        out.print(TAB);
        resetColor(out);
        out.println();
    }

    private static void drawHeader(PrintStream out, String headerText) {
        out.print(SPACE);
        printHeaderText(out, headerText);
        out.print(SPACE);
    }

    private static void printHeaderText(PrintStream out, String player) {
        out.print(SET_BG_COLOR_BLUE);
        out.print(SET_TEXT_COLOR_WHITE);

        out.print(player);

        setBlue(out);
    }

    private static void printRow(PrintStream out, String row) {
        out.print(SET_BG_COLOR_BLUE);
        out.print(SET_TEXT_COLOR_WHITE);

        out.print(row);
    }

    private static void drawChessBoard(PrintStream out, ChessBoard board, ChessGame.TeamColor color) {

        if (color == ChessGame.TeamColor.WHITE) {
            for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; ++boardRow) {
                drawRowOfSquares(out, board, boardRow, color);

                if (boardRow < BOARD_SIZE_IN_SQUARES - 1) {
                    resetColor(out);
                }
            }
        } else {
            for (int boardRow = BOARD_SIZE_IN_SQUARES - 1; boardRow >= 0; --boardRow) {
                drawRowOfSquares(out, board, boardRow, color);

                if (boardRow > 1) {
                    resetColor(out);
                }
            }
        }
    }

    private static void drawRowOfSquares(PrintStream out, ChessBoard board, int boardRow, ChessGame.TeamColor color) {
        printRow(out, BOARD_ROWS[boardRow]);

        if (color == ChessGame.TeamColor.WHITE) {
            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {

                determineCheckerColor(boardRow, boardCol, out);
                ChessPosition posOfPiece = new ChessPosition(boardRow + 1, boardCol + 1);
                printPiece(out, board.getPiece(posOfPiece));
            }
        } else {
            for (int boardCol = BOARD_SIZE_IN_SQUARES - 1; boardCol >= 0; --boardCol) {
                determineCheckerColor(boardRow, boardCol, out);
                ChessPosition posOfPiece = new ChessPosition(boardRow + 1, boardCol + 1);
                printPiece(out, board.getPiece(posOfPiece));
            }
        }

        printRow(out, BOARD_ROWS[boardRow]);
        resetColor(out);
        out.println();
    }

    private static void determineCheckerColor(int boardRow, int boardCol, PrintStream out) {
        if (boardCol % 2 == 0) {
            if (boardRow % 2 == 0) {
                out.print(SET_BG_COLOR_LIGHT_GREY);
            } else {
                out.print(SET_BG_COLOR_DARK_GREY);
            }
        } else {
            if (boardRow % 2 == 0) {
                out.print(SET_BG_COLOR_DARK_GREY);
            } else {
                out.print(SET_BG_COLOR_LIGHT_GREY);
            }
        }
    }

    private static void printPiece(PrintStream out, ChessPiece piece) {
        out.print(SET_TEXT_COLOR_WHITE);

        if (piece == null) {
            out.print(EMPTY);
            resetColor(out);
            return;
        }
        switch (piece.getPieceType()) {
            case KING:
                if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                    out.print(WHITE_KING);
                } else {
                    out.print(BLACK_KING);
                }
                break;
            case QUEEN:
                if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                    out.print(WHITE_QUEEN);
                } else {
                    out.print(BLACK_QUEEN);
                }
                break;
            case BISHOP:
                if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                    out.print(WHITE_BISHOP);
                } else {
                    out.print(BLACK_BISHOP);
                }
                break;
            case KNIGHT:
                if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                    out.print(WHITE_KNIGHT);
                } else {
                    out.print(BLACK_KNIGHT);
                }
                break;
            case ROOK:
                if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                    out.print(WHITE_ROOK);
                } else {
                    out.print(BLACK_ROOK);
                }
                break;
            case PAWN:
                if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                    out.print(WHITE_PAWN);
                } else {
                    out.print(BLACK_PAWN);
                }
                break;
        }

        resetColor(out);
    }

    private static void setBlue(PrintStream out) {
        out.print(SET_BG_COLOR_BLUE);
        out.print(SET_TEXT_COLOR_BLUE);
    }
    private static void resetColor(PrintStream out) {
        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
    }
}
