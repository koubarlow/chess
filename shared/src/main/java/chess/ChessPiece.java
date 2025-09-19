package chess;

import chess.validPieceMoves.BishopValidMoves;
import chess.validPieceMoves.KingValidMoves;
import chess.validPieceMoves.KnightValidMoves;
import chess.validPieceMoves.RookValidMoves;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece piece = (ChessPiece) o;
        return pieceColor == piece.pieceColor && type == piece.type && Objects.equals(currentPosition, piece.currentPosition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type, currentPosition);
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

        Set<ChessMove> possibleMoves = new HashSet<ChessMove>();
//        MoveCalculator calculator = MoveCalculator.create(piece.getType());
//        Set<ChessPosition> candidateMoves = calculator.pieceMoves(piece, board, position)
        switch (type) {
            case BISHOP:
                BishopValidMoves bishopVM = new BishopValidMoves();
                return bishopVM.pieceMoves(board, myPosition);
            case QUEEN:
                BishopValidMoves queenDiagonalVM = new BishopValidMoves();
                RookValidMoves queenRookVm = new RookValidMoves();
                return Stream.concat(queenDiagonalVM.pieceMoves(board, myPosition).stream(), queenRookVm.pieceMoves(board, myPosition).stream()).collect(Collectors.toSet());
            case PAWN:
                break;
            case ROOK:
                RookValidMoves rookVM = new RookValidMoves();
                return rookVM.pieceMoves(board, myPosition);
            case KNIGHT:
                KnightValidMoves knightVM = new KnightValidMoves();
                return knightVM.pieceMoves(board, myPosition);
            case KING:
                KingValidMoves kingVM = new KingValidMoves();
                return kingVM.pieceMoves(board, myPosition);
        }
        // Case bishop
        // case rook
        // case etc.
        // make helper functions to make it clean
        return possibleMoves;
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", type=" + type +
                ", currentPosition=" + currentPosition +
                '}';
    }
}
