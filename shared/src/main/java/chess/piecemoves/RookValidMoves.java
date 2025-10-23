package chess.piecemoves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Set;

public class RookValidMoves extends ValidMoves {

    public RookValidMoves(ChessBoard board, ChessPosition myPos) {
        super(board, myPos);
    }

    public Set<ChessMove> getValidMoves() {
        checkIfCanSlideInDirection(1 , 0);
        checkIfCanSlideInDirection(-1 , 0);
        checkIfCanSlideInDirection(0 , 1);
        checkIfCanSlideInDirection(0 , -1);
        return super.moves;
    }
}
