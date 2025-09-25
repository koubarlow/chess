package chess;

import chess.pieceMoves.*;

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

    public ChessPosition getCurrentPosition() {
        return this.currentPosition;
    }

    public void setPosition(ChessPosition pos) {
        this.currentPosition = pos;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Set<ChessMove> moves = new HashSet<>();
        switch (type) {
            case BISHOP:
                BishopValidMoves bishopVM = new BishopValidMoves(board, myPosition);
                return bishopVM.getValidMoves();
            case ROOK:
                RookValidMoves rookVM = new RookValidMoves(board, myPosition);
                return rookVM.getValidMoves();
            case QUEEN:
                BishopValidMoves diagonalVM = new BishopValidMoves(board, myPosition);
                RookValidMoves lateralVM = new RookValidMoves(board, myPosition);
                return Stream.concat(diagonalVM.getValidMoves().stream(), lateralVM.getValidMoves().stream()).collect(Collectors.toSet());
            case KNIGHT:
                KnightValidMoves knightVM = new KnightValidMoves(board, myPosition);
                return knightVM.getValidMoves();
            case PAWN:
                PawnValidMoves pawnVM = new PawnValidMoves(board, myPosition);
                return pawnVM.getValidMoves();
            case KING:
                KingValidMoves kingVM = new KingValidMoves(board, myPosition);
                return kingVM.getValidMoves();
        }
        return moves;
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", type=" + type +
                ", currentPosition=" + currentPosition +
                '}';
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
}
