package chess.validPieceMoves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BishopValidMoves {

    public static Set<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition)
    {

        Set<ChessMove> moves = seeDownRight(board, myPosition);
        moves = Stream.concat(moves.stream(), seeForwardRight(board, myPosition).stream()).collect(Collectors.toSet());
        moves = Stream.concat(moves.stream(), seeDownLeft(board, myPosition).stream()).collect(Collectors.toSet());
        moves = Stream.concat(moves.stream(), seeForwardLeft(board, myPosition).stream()).collect(Collectors.toSet());

        for (ChessMove move : moves) {
            System.out.println(move);
        }
        return moves;
    }

    public static Set<ChessMove> seeForwardLeft(ChessBoard board, ChessPosition position) {
        Set<ChessMove> forwardLeftMoves = new HashSet<>();
        int row = position.getRow();
        int col = position.getColumn();
        canKeepGoing = true;

        while (row < 8 && col > 1 && canKeepGoing) {
            row++;
            col--;
            ChessMove validMove = validMoveHelper(row,col,board,position);

            if (validMove != null) {
                forwardLeftMoves.add(validMove);
            } else {
                break;
            }
        }

        return forwardLeftMoves;
    }

    public static Set<ChessMove> seeForwardRight(ChessBoard board, ChessPosition position) {
        Set<ChessMove> forwardRightMoves = new HashSet<>();
        int row = position.getRow();
        int col = position.getColumn();;
        canKeepGoing = true;

        while (row < 8 && col < 8 && canKeepGoing) {
            row++;
            col++;
            ChessMove validMove = validMoveHelper(row,col,board,position);

            if (validMove != null) {
                forwardRightMoves.add(validMove);
            } else {
                break;
            }
        }

        return forwardRightMoves;
    }

    public static Set<ChessMove> seeDownLeft(ChessBoard board, ChessPosition position) {
        Set<ChessMove> downLeftMoves = new HashSet<>();
        int row = position.getRow();
        int col = position.getColumn();;
        canKeepGoing = true;

        while (row > 1 && col > 1 && canKeepGoing) {
            row--;
            col--;
            ChessMove validMove = validMoveHelper(row,col,board,position);

            if (validMove != null) {
                downLeftMoves.add(validMove);
            } else {
                break;
            }
        }

        return downLeftMoves;
    }

    static boolean canKeepGoing = true;

    public static Set<ChessMove> seeDownRight(ChessBoard board, ChessPosition position) {
        Set<ChessMove> downRightMoves = new HashSet<>();
        int row = position.getRow();
        int col = position.getColumn();;
        canKeepGoing = true;

        while (row > 1 && col < 8 && canKeepGoing) {
            row--;
            col++;
            ChessMove validMove = validMoveHelper(row,col,board,position);

            if (validMove != null) {
                downRightMoves.add(validMove);
            } else {
                break;
            }
        }

        return downRightMoves;
    }

    public static ChessMove validMoveHelper(int row, int col, ChessBoard board, ChessPosition myPosition) {

        ChessPosition proposedPosition = new ChessPosition(row, col);

        if (board.getPiece(proposedPosition) != null) {
            ChessPiece myPiece = board.getPiece(myPosition);
            if (myPiece.getTeamColor() == board.getPiece(proposedPosition).getTeamColor()){
                return null;
            } else {
                canKeepGoing = false;
                return new ChessMove(myPosition, proposedPosition, null);
            }
        } else {
            return new ChessMove(myPosition, proposedPosition, null);
        }
    }
}
