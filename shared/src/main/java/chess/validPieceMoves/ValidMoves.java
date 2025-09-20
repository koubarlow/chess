package chess.validPieceMoves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.HashSet;
import java.util.Set;

public class ValidMoves {

    Set<ChessMove> moves;

    public ValidMoves() {
        this.moves = new HashSet<>();
    }

    public void addIfValid(ChessBoard board, ChessPosition myPos, ChessPosition propPos) {

        int propRow = propPos.getRow();
        int propCol = propPos.getColumn();
        if (propRow > 8 || propCol > 8 || propRow < 1 || propCol < 1) { return; }

        ChessMove propMove = new ChessMove(myPos, propPos,null);
        ChessPiece pieceInPropMove = board.getPiece(propPos);

        System.out.println(propMove);
        System.out.println(pieceInPropMove);

        if (pieceInPropMove != null) {
            ChessPiece myPiece = board.getPiece(myPos);
            if (myPiece.getTeamColor() == pieceInPropMove.getTeamColor()){
                return;
            }
        }

        this.moves.add(propMove);
    }
}
