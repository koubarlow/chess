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

    private HashMap<ChessPosition, ChessPiece> chessboard;

    public ChessBoard() {
        resetBoard();
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        this.chessboard.put(position, piece);
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return this.chessboard.get(position);
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        if (this.chessboard != null) { this.chessboard.clear(); }
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
            addPiece(newPos(rowForPawns,i), newPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        }
    }

    private ChessPosition newPos(int row, int col) {
        return new ChessPosition(row, col);
    }

    private ChessPiece newPiece(ChessGame.TeamColor color, ChessPiece.PieceType pieceType) {
        return new ChessPiece(color, pieceType);
    }
}
