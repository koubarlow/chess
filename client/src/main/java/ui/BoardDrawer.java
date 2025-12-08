package ui;

import chess.*;
import exception.ResponseException;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

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
import static ui.EscapeSequences.SET_BG_COLOR_BLUE;
import static ui.EscapeSequences.SET_BG_COLOR_DARK_GREY;
import static ui.EscapeSequences.SET_BG_COLOR_LIGHT_GREY;
import static ui.EscapeSequences.SET_TEXT_COLOR_BLUE;
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
    private static final String[] BOARD_ROWS = {" 1 ", " 2 ", " 3 ", " 4 ", " 5 ", " 6 ", " 7 ", " 8 " };

    public static void drawBoard(ChessGame c, ChessGame.TeamColor t, boolean h, ChessPosition p) throws ResponseException {
        ChessBoard board = c.getBoard();

        Set<ChessMove> positionsToHighlight = new HashSet<>();
        if (h) {
            ChessPiece pieceToHighlight = c.getBoard().getPiece(p);
            positionsToHighlight = collectValidMoves(c, p);
            if (pieceToHighlight == null) {
                throw new ResponseException(ResponseException.Code.ClientError, "No piece on selected square!");
            }
        }

        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);
        drawHeaders(out, t);
        drawChessBoard(out, board, t, positionsToHighlight, p);
        drawHeaders(out, t);
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

    private static Set<ChessMove> collectValidMoves(ChessGame game, ChessPosition position) {
        return new HashSet<>(game.validMoves(position));
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

    private static void drawChessBoard(PrintStream out, ChessBoard b, ChessGame.TeamColor c, Set<ChessMove> m, ChessPosition p) {

        if (c == ChessGame.TeamColor.BLACK) {
            for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; ++boardRow) {
                drawRowOfSquares(out, b, boardRow, c, m, p);

                if (boardRow < BOARD_SIZE_IN_SQUARES - 1) {
                    resetColor(out);
                }
            }
        } else {
            for (int boardRow = BOARD_SIZE_IN_SQUARES - 1; boardRow >= 0; --boardRow) {
                drawRowOfSquares(out, b, boardRow, c, m, p);

                if (boardRow > 1) {
                    resetColor(out);
                }
            }
        }
    }

    private static void drawRowOfSquares(PrintStream out, ChessBoard b, int r, ChessGame.TeamColor c, Set<ChessMove> m, ChessPosition p) {
        printRow(out, BOARD_ROWS[r]);

        if (c == ChessGame.TeamColor.WHITE) {
            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
                printSquare(r, boardCol, out, m, p, b);
            }
        } else {
            for (int boardCol = BOARD_SIZE_IN_SQUARES - 1; boardCol >= 0; --boardCol) {
                printSquare(r, boardCol, out, m, p, b);
            }
        }

        printRow(out, BOARD_ROWS[r]);
        resetColor(out);
        out.println();
    }

    private static void printSquare(int r, int c, PrintStream out, Set<ChessMove> m, ChessPosition p, ChessBoard b) {
        determineCheckerColor(r, c, out, m, p);
        ChessPosition posOfPiece = new ChessPosition(r + 1, c + 1);
        printPiece(out, b.getPiece(posOfPiece));
    }

    private static void determineCheckerColor(int r, int c, PrintStream out, Set<ChessMove> m, ChessPosition p) {
        boolean needToHighlight = false;
        boolean specialHighlight = false;

        for (ChessMove move: m) {
            int columnToPossiblyHighlight = move.getEndPosition().getColumn();
            int rowToPossiblyHighlight = move.getEndPosition().getRow();

            if (columnToPossiblyHighlight == c + 1 && rowToPossiblyHighlight == r + 1) {
                needToHighlight = true;
                break;
            }
        }

        if (p != null && c + 1 == p.getColumn() && r + 1 == p.getRow()) {
            specialHighlight = true;
        }

        setSquareColor(r, c, out, needToHighlight, specialHighlight);
    }

    private static void setSquareColor(int boardRow, int boardCol, PrintStream out, boolean needToHighlight, boolean specialHighlight) {
        if (specialHighlight) {
            out.print(SET_BG_COLOR_MAGENTA);
            return;
        }

        boolean colEven = boardCol % 2 == 0;
        boolean rowEven = boardRow % 2 == 0;

        if (needToHighlight) {
            if (rowEven && colEven || !rowEven && !colEven) {
                out.print(SET_BG_COLOR_DARK_GREEN);
            } else {
                out.print(SET_BG_COLOR_GREEN);
            }
        } else {
            if (rowEven && colEven || !rowEven && !colEven) {
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
