package chess;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    ChessPiece[][] board;

    public ChessBoard() {
        this.board = new ChessPiece[8][8];
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        int row = position.getRow() - 1;
        int col = position.getColumn() - 1;
        this.board[row][col] = piece;
        piece.setPosition(position);
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        int row = position.getRow() - 1;
        int col = position.getColumn() - 1;
        return this.board[row][col];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        this.board = new ChessPiece[8][8];
        resetPieces(1, 2, ChessGame.TeamColor.WHITE);
        resetPieces(8, 7, ChessGame.TeamColor.BLACK);
    }

    private void resetPieces(int rowForSpecialPieces, int rowForPawns, ChessGame.TeamColor teamColor) {
        addPiece(new ChessPosition(rowForSpecialPieces,1), new ChessPiece(teamColor, ChessPiece.PieceType.ROOK));
        addPiece(new ChessPosition(rowForSpecialPieces,2), new ChessPiece(teamColor, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(rowForSpecialPieces,3), new ChessPiece(teamColor, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(rowForSpecialPieces,4), new ChessPiece(teamColor, ChessPiece.PieceType.QUEEN));
        addPiece(new ChessPosition(rowForSpecialPieces,5), new ChessPiece(teamColor, ChessPiece.PieceType.KING));
        addPiece(new ChessPosition(rowForSpecialPieces,6), new ChessPiece(teamColor, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(rowForSpecialPieces,7), new ChessPiece(teamColor, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(rowForSpecialPieces,8), new ChessPiece(teamColor, ChessPiece.PieceType.ROOK));

        for (int i = 1; i < 9; i++) {
            addPiece(new ChessPosition(rowForPawns,i), new ChessPiece(teamColor, ChessPiece.PieceType.PAWN));
        }
    }

    @Override
    public String toString() {
        return "ChessBoard{" +
                "board=" + Arrays.toString(board) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }
}
