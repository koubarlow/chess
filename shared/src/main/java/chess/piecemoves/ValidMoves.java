package chess.piecemoves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.HashSet;
import java.util.Set;

public class ValidMoves {

    Set<ChessMove> moves;
    ChessBoard board;
    ChessPosition myPos;
    boolean canKeepGoing;

    public ValidMoves(ChessBoard board, ChessPosition myPos) {
        this.moves = new HashSet<>();
        this.board = board;
        this.myPos = myPos;
        this.canKeepGoing = true;
    }

    public boolean addIfValid(ChessPosition propPos) {

        int propRow = propPos.getRow();
        int propCol = propPos.getColumn();

        if (!inBounds(propRow) || !inBounds(propCol)) { return false; }

        ChessPiece pieceInPropPos = board.getPiece(propPos);
        ChessPiece piece = board.getPiece(this.myPos);

        if (pieceInPropPos != null) {
            if (pieceInPropPos.getTeamColor() == piece.getTeamColor()) {
                canKeepGoing = false;
                return false;
            }
        }

        if (!canKeepGoing) { return false; }

        if (pieceInPropPos != null) {
            canKeepGoing = false;
            moves.add(new ChessMove(myPos, propPos, null));
            return true;
        }

        moves.add(new ChessMove(myPos, propPos, null));
        return true;
    }

    public boolean inBounds(int proposedPos) {
        return proposedPos >= 1 && proposedPos <= 8;
    }

    public void checkIfCanSlideInDirection(int rowInc, int colInc) {
        int row = myPos.getRow();
        int col = myPos.getColumn();
        this.canKeepGoing = true;

        while (inBounds(row) && inBounds(col) && this.canKeepGoing) {
            row += rowInc;
            col += colInc;

            ChessPosition propPos = new ChessPosition(row, col);
            addIfValid(propPos);
        }
    }
}
