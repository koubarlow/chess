package chess.validPieceMoves;

import chess.*;

import javax.sound.midi.SysexMessage;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BishopValidMoves {

    private final Set<ChessMove> moves;

    private int currentRow;
    private int currentCol;

    private int currentlyProposedRow;
    private int currentlyProposedCol;
    private boolean notAPieceInProposedPos;

    public BishopValidMoves() {
        this.moves = new HashSet<>();
    }

    public Set<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        this.currentRow = myPosition.getRow();
        this.currentCol = myPosition.getColumn();

        this.notAPieceInProposedPos = true;
        this.currentlyProposedRow = this.currentRow;
        this.currentlyProposedCol = this.currentCol;

        while ((currentlyProposedRow < 8 & currentlyProposedCol < 8) && notAPieceInProposedPos) {
            validMoveHelper(1, 1, board, myPosition);
        }

        this.notAPieceInProposedPos = true;
        this.currentlyProposedRow = this.currentRow;
        this.currentlyProposedCol = this.currentCol;

        while ((currentlyProposedRow > 1 & currentlyProposedCol < 8) && notAPieceInProposedPos) {
            validMoveHelper(-1, 1, board, myPosition);
        }

        this.notAPieceInProposedPos = true;
        this.currentlyProposedRow = this.currentRow;
        this.currentlyProposedCol = this.currentCol;

        while ((currentlyProposedRow > 1 & currentlyProposedCol > 1) && notAPieceInProposedPos) {
            validMoveHelper(-1, -1, board, myPosition);
        }

        this.notAPieceInProposedPos = true;
        this.currentlyProposedRow = this.currentRow;
        this.currentlyProposedCol = this.currentCol;

        while ((currentlyProposedRow < 8 & currentlyProposedCol > 1) && notAPieceInProposedPos) {
            validMoveHelper(1, -1, board, myPosition);
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
