package chess.piecemoves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Set;

public class BishopValidMoves extends ValidMoves {

    public BishopValidMoves(ChessBoard board, ChessPosition myPos) {
        super(board, myPos);
    }

    public Set<ChessMove> getValidMoves() {
        checkIfCanSlideInDirection(1 , 1);
        checkIfCanSlideInDirection(-1 , -1);
        checkIfCanSlideInDirection(-1 , 1);
        checkIfCanSlideInDirection(1 , -1);
        return super.moves;
    }
}
