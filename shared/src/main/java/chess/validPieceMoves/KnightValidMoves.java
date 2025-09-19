package chess.validPieceMoves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.*;

public class KnightValidMoves extends ValidMoves {

    public Set<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        ChessPosition upRight = new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn() + 1);
        addIfValid(board, myPosition, upRight);

        ChessPosition right = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 2);
        addIfValid(board, myPosition, right);

        ChessPosition downRight = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 2);
        addIfValid(board, myPosition, downRight);

        ChessPosition down = new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn() + 1);
        addIfValid(board, myPosition, down);

        ChessPosition downLeft = new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn() - 1);
        addIfValid(board, myPosition, downLeft);

        ChessPosition left = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 2);
        addIfValid(board, myPosition, left);

        ChessPosition upLeft = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 2);
        addIfValid(board, myPosition, upLeft);

        ChessPosition up = new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn() - 1);
        addIfValid(board, myPosition, up);

        return super.moves;
    }
}
