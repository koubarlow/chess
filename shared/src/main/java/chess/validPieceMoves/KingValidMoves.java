package chess.validPieceMoves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class KingValidMoves {

    Set<ChessMove> moves;

    public KingValidMoves() {
        this.moves = new HashSet<>();
    }

    public Set<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition)
    {
        ChessPosition upRight = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
        addIfValid(board, myPosition, upRight);

        ChessPosition right = new ChessPosition(myPosition.getRow(), myPosition.getColumn() + 1);
        addIfValid(board, myPosition, right);

        ChessPosition downRight = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1);
        addIfValid(board, myPosition, downRight);

        ChessPosition down = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
        addIfValid(board, myPosition, down);

        ChessPosition downLeft = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1);
        addIfValid(board, myPosition, downLeft);

        ChessPosition left = new ChessPosition(myPosition.getRow(), myPosition.getColumn() - 1);
        addIfValid(board, myPosition, left);

        ChessPosition upLeft = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
        addIfValid(board, myPosition, upLeft);

        ChessPosition up = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
        addIfValid(board, myPosition, up);

//        for (ChessMove move : moves) {
//            System.out.println(move);
//        }

        return moves;
    }

    public void addIfValid(ChessBoard board, ChessPosition myPos, ChessPosition propPos) {

        int propRow = propPos.getRow();
        int propCol = propPos.getColumn();
        if (propRow > 8 || propCol > 8 || propRow < 1 || propCol < 1) { return; }

        ChessMove propMove = new ChessMove(myPos, propPos,null);
        ChessPiece pieceInPropMove = board.getPiece(propPos);

        if (pieceInPropMove != null) {
            ChessPiece myPiece = board.getPiece(myPos);
            if (myPiece.getTeamColor() == pieceInPropMove.getTeamColor()){
                return;
            }
        }

        this.moves.add(propMove);
    }
}
