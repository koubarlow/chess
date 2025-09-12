package chess;

import chess.validPieceMoves.BishopValidMoves;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final ChessPiece.PieceType type;
    private ChessPosition currentPosition;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.type;
    }

    /**
    * @return the position of the piece
    **/
    public ChessPosition getCurrentPosition() {
        return this.currentPosition;
    }

    public void setPosition(ChessPosition position) {
        this.currentPosition = position;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> possibleMoves = new ArrayList<>();

        switch (type) {
            case BISHOP:
                BishopValidMoves bishopValidMoves = new BishopValidMoves(possibleMoves);
                return bishopValidMoves.validMoves(board, myPosition);
            case QUEEN:
                break;
        }
        // Case bishop
        // case rook
        // case etc.
        // make helper functions to make it clean
        throw new RuntimeException("Not implemented");
    }
}
