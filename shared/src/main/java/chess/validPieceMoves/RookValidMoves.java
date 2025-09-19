package chess.validPieceMoves;

import chess.*;

import java.util.*;

public class RookValidMoves extends ValidMoves {

    private int currentRow;
    private int currentCol;

    private int currentlyProposedRow;
    private int currentlyProposedCol;
    private boolean notAPieceInProposedPos;

    public Set<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        this.currentRow = myPosition.getRow();
        this.currentCol = myPosition.getColumn();

        this.notAPieceInProposedPos = true;
        this.currentlyProposedRow = this.currentRow;
        this.currentlyProposedCol = this.currentCol;

        while ((currentlyProposedCol < 8) && notAPieceInProposedPos) {
            validMoveHelper(0, 1, board, myPosition);
        }

        this.notAPieceInProposedPos = true;
        this.currentlyProposedRow = this.currentRow;
        this.currentlyProposedCol = this.currentCol;

        while ((currentlyProposedRow < 8) && notAPieceInProposedPos) {
            validMoveHelper(1, 0, board, myPosition);
        }

        this.notAPieceInProposedPos = true;
        this.currentlyProposedRow = this.currentRow;
        this.currentlyProposedCol = this.currentCol;

        while ((currentlyProposedRow > 1) && notAPieceInProposedPos) {
            validMoveHelper(-1, 0, board, myPosition);
        }

        this.notAPieceInProposedPos = true;
        this.currentlyProposedRow = this.currentRow;
        this.currentlyProposedCol = this.currentCol;

        while ((currentlyProposedCol > 1) && notAPieceInProposedPos) {
            validMoveHelper(0, -1, board, myPosition);
        }

        return this.moves;
    }

    private void validMoveHelper(int incrementRowBy, int incrementColBy, ChessBoard board, ChessPosition myPosition) {

        this.currentlyProposedRow += incrementRowBy;
        this.currentlyProposedCol += incrementColBy;

        if (currentlyProposedRow > 8 || currentlyProposedCol > 8 || currentlyProposedRow < 1 || currentlyProposedCol < 1) { return; }

        ChessPosition proposedPosition = new ChessPosition(currentlyProposedRow, currentlyProposedCol);
        ChessMove proposedMove = new ChessMove(myPosition, proposedPosition, null);

        ChessPiece pieceInProposedMove = board.getPiece(proposedPosition);

        if (pieceInProposedMove != null) {
            this.notAPieceInProposedPos = false;
            ChessGame.TeamColor currentPieceColor = board.getPiece(myPosition).getTeamColor();
            ChessGame.TeamColor otherChessPieceColor = pieceInProposedMove.getTeamColor();

            if (currentPieceColor == otherChessPieceColor) {
                return;
            }
        }

        this.moves.add(proposedMove);
    }
}
