package chess.validPieceMoves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class KingValidMoves {

    public static Set<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition)
    {
        Set<ChessMove> moves = new HashSet<>();
        moves.add(seeLeft(board, myPosition));
        moves.add(seeForwardLeft(board, myPosition));
        moves.add(seeForward(board, myPosition));
        moves.add(seeForwardRight(board, myPosition));
        moves.add(seeRight(board, myPosition));
        moves.add(seeDownRight(board, myPosition));
        moves.add(seeDown(board, myPosition));
        moves.add(seeDownLeft(board, myPosition));

        for (ChessMove move : moves) {
            System.out.println(move);
        }
        return moves;
    }

    public static ChessMove seeForward(ChessBoard board, ChessPosition position) {
        int row = position.getRow();
        int col = position.getColumn();;
        row++;

        return validMoveHelper(row,col,board,position);
    }

    public static ChessMove seeLeft(ChessBoard board, ChessPosition position) {
        int row = position.getRow();
        int col = position.getColumn();;
        col--;

        return validMoveHelper(row,col,board,position);
    }

    public static ChessMove seeDown(ChessBoard board, ChessPosition position) {
        int row = position.getRow();
        int col = position.getColumn();;
        row--;

        return validMoveHelper(row,col,board,position);
    }

    public static ChessMove seeRight(ChessBoard board, ChessPosition position) {
        int row = position.getRow();
        int col = position.getColumn();;
        col++;

        return validMoveHelper(row,col,board,position);
    }

    public static ChessMove seeForwardLeft(ChessBoard board, ChessPosition position) {

        int row = position.getRow();
        int col = position.getColumn();;
        row++;
        col--;

        return validMoveHelper(row,col,board,position);
    }

    public static ChessMove seeForwardRight(ChessBoard board, ChessPosition position) {
        int row = position.getRow();
        int col = position.getColumn();;
        row++;
        col++;

        return validMoveHelper(row,col,board,position);
    }

    public static ChessMove seeDownLeft(ChessBoard board, ChessPosition position) {
        int row = position.getRow();
        int col = position.getColumn();;
        row--;
        col--;

        return validMoveHelper(row,col,board,position);
    }

    public static ChessMove seeDownRight(ChessBoard board, ChessPosition position) {
        int row = position.getRow();
        int col = position.getColumn();;
        row--;
        col++;

        return validMoveHelper(row,col,board,position);
    }

    public static ChessMove validMoveHelper(int row, int col, ChessBoard board, ChessPosition myPosition) {

        ChessPosition proposedPosition = new ChessPosition(row, col);
        if (board.getPiece(proposedPosition) != null) {
            ChessPiece myPiece = board.getPiece(myPosition);
            if (myPiece.getTeamColor() == board.getPiece(proposedPosition).getTeamColor()){
                return null;
            } else {
                return new ChessMove(myPosition, proposedPosition, null);
            }
        } else {
            return new ChessMove(myPosition, proposedPosition, null);
        }
    }
}
