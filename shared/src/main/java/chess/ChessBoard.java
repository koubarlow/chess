package chess;

import java.util.Collection;
import java.util.HashMap;

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
        int col = position.getRow() - 1;
        this.board[row][col] = piece;
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
        int col = position.getRow() - 1;
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
        addPiece(newPos(rowForSpecialPieces,1), newPiece(teamColor, ChessPiece.PieceType.ROOK));
        addPiece(newPos(rowForSpecialPieces,2), newPiece(teamColor, ChessPiece.PieceType.KNIGHT));
        addPiece(newPos(rowForSpecialPieces,3), newPiece(teamColor, ChessPiece.PieceType.BISHOP));
        addPiece(newPos(rowForSpecialPieces,4), newPiece(teamColor, ChessPiece.PieceType.QUEEN));
        addPiece(newPos(rowForSpecialPieces,5), newPiece(teamColor, ChessPiece.PieceType.KING));
        addPiece(newPos(rowForSpecialPieces,6), newPiece(teamColor, ChessPiece.PieceType.BISHOP));
        addPiece(newPos(rowForSpecialPieces,7), newPiece(teamColor, ChessPiece.PieceType.KNIGHT));
        addPiece(newPos(rowForSpecialPieces,8), newPiece(teamColor, ChessPiece.PieceType.ROOK));

        for (int i = 1; i < 9; i++) {
            addPiece(newPos(rowForPawns,i), newPiece(teamColor, ChessPiece.PieceType.PAWN));
        }
    }

    private ChessPosition newPos(int row, int col) {
        return new ChessPosition(row, col);
    }

    private ChessPiece newPiece(ChessGame.TeamColor color, ChessPiece.PieceType pieceType) {
        return new ChessPiece(color, pieceType);
    }
}
