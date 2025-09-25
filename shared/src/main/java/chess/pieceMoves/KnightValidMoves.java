package chess.pieceMoves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Set;

public class KnightValidMoves extends ValidMoves {

    public KnightValidMoves(ChessBoard board, ChessPosition myPos) {
        super(board, myPos);
    }

    public Set<ChessMove> getValidMoves() {
        int curRow = myPos.getRow();
        int curCol = myPos.getColumn();

        ChessPosition forwardLeft = new ChessPosition(curRow + 2, curCol - 1);
        addIfValid(forwardLeft);

        super.canKeepGoing = true;
        ChessPosition forwardRight = new ChessPosition(curRow + 2, curCol + 1);
        addIfValid(forwardRight);

        super.canKeepGoing = true;
        ChessPosition rightUp = new ChessPosition(curRow + 1, curCol + 2);
        addIfValid(rightUp);
        super.canKeepGoing = true;
        ChessPosition rightDown = new ChessPosition(curRow - 1, curCol + 2);
        addIfValid(rightDown);

        super.canKeepGoing = true;
        ChessPosition downRight = new ChessPosition(curRow - 2, curCol + 1);
        addIfValid(downRight);
        super.canKeepGoing = true;
        ChessPosition downLeft = new ChessPosition(curRow - 2, curCol - 1);
        addIfValid(downLeft);

        super.canKeepGoing = true;
        ChessPosition leftDown = new ChessPosition(curRow - 1, curCol - 2);
        addIfValid(leftDown);
        super.canKeepGoing = true;
        ChessPosition leftUp = new ChessPosition(curRow + 1, curCol - 2);
        addIfValid(leftUp);

        return super.moves;
    }
}
