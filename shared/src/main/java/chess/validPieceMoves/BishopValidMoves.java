package chess.validPieceMoves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.Collections;

public class BishopValidMoves {

    private final Collection<ChessMove> moves;

    private int currentRow;
    private int currentCol;

    private int currentlyProposedRow;
    private int currentlyProposedCol;
    private boolean notAPieceInProposedPos;

    public BishopValidMoves(Collection<ChessMove> moves) {
        this.moves = moves;
    }

    public Collection<ChessMove> validMoves(ChessBoard board, ChessPosition myPosition) {

        this.currentRow = myPosition.getRow();
        this.currentCol = myPosition.getColumn();

        resetProposedPos();

        while ((currentlyProposedRow < 8 & currentlyProposedCol < 8) && notAPieceInProposedPos) {
            validMoveHelper(1, 1, board, myPosition);
        }

        resetProposedPos();

        while ((currentlyProposedRow > 0 & currentlyProposedCol > 0) && notAPieceInProposedPos) {
            validMoveHelper(-1, -1, board, myPosition);
        }

        resetProposedPos();

        while ((currentlyProposedRow < 8 & currentlyProposedCol > 0) && notAPieceInProposedPos) {
            validMoveHelper(1, -1, board, myPosition);
        }

        resetProposedPos();

        while ((currentlyProposedRow > 0 & currentlyProposedCol < 8) && notAPieceInProposedPos) {
            validMoveHelper(-1, 1, board, myPosition);
        }

        return this.moves;
    }

    private void resetProposedPos() {
        this.currentlyProposedRow = this.currentRow;
        this.currentlyProposedCol = this.currentCol;
        this.notAPieceInProposedPos = true;
    }

    private void validMoveHelper(int incrementRowBy, int incrementColBy, ChessBoard board, ChessPosition myPosition) {
        this.currentlyProposedRow += incrementRowBy;
        this.currentlyProposedCol += incrementColBy;
        ChessPosition proposedPosition = new ChessPosition(currentlyProposedRow, currentlyProposedCol);

        if (board.getPiece(proposedPosition) == null) {
            this.notAPieceInProposedPos = false;
            return;
        }

        ChessMove proposedMove = new ChessMove(myPosition, proposedPosition, null);
        this.moves.add(proposedMove);
    }
}
