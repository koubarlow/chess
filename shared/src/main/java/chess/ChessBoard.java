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

    HashMap<ChessPosition, ChessPiece> board;

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
        this.board.put(position, piece);
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return this.board.get(position);
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        board.clear();
        resetWhiteTeam();
        resetBlackTeam();
    }

    private void resetWhiteTeam() {
        addPiece(newPos(1,1), newPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        addPiece(newPos(1,2), newPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        addPiece(newPos(1,3), newPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        addPiece(newPos(1,4), newPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN));
        addPiece(newPos(1,5), newPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));
        addPiece(newPos(1,6), newPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        addPiece(newPos(1,7), newPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        addPiece(newPos(1,8), newPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));

        for (int i = 1; i < 9; i++) {
            addPiece(newPos(2,i), newPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        }
    }

    private void resetBlackTeam() {
        addPiece(newPos(8,1), newPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        addPiece(newPos(8,2), newPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        addPiece(newPos(8,3), newPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        addPiece(newPos(8,4), newPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN));
        addPiece(newPos(8,5), newPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));
        addPiece(newPos(8,6), newPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        addPiece(newPos(8,7), newPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        addPiece(newPos(8,8), newPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));

        for (int i = 1; i < 9; i++) {
            addPiece(newPos(7,i), newPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        }
    }

    private ChessPosition newPos(int row, int col) {
        return new ChessPosition(row, col);
    }

    private ChessPiece newPiece(ChessGame.TeamColor color, ChessPiece.PieceType pieceType) {
        return new ChessPiece(color, pieceType);
    }
}
