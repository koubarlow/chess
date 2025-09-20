package chess.validPieceMoves;

import chess.*;

import java.util.Set;

public class PawnValidMoves extends ValidMoves {

    public Set<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition)
    {
        ChessPiece myPiece = board.getPiece(myPosition);

        if (myPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            if (myPosition.getRow() == 2) {
                ChessPosition headStart = new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn());
                addIfValid(board, myPosition, headStart);
            }

            ChessPosition upOne = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
            addIfValid(board, myPosition, upOne);

            ChessPosition upLeft = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
            addIfCanTake(board, myPosition, upLeft);

            ChessPosition upRight = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
            addIfCanTake(board, myPosition, upRight);
        } else {
            if (myPosition.getRow() == 7) {
                ChessPosition headStart = new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn());
                addIfValid(board, myPosition, headStart);
            }

            ChessPosition downOne = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
            addIfValid(board, myPosition, downOne);

            ChessPosition downLeft = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1);
            addIfCanTake(board, myPosition, downLeft);

            ChessPosition downRight = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1);
            addIfCanTake(board, myPosition, downRight);
        }
//        for (ChessMove move : moves) {
//            System.out.println(move);
//        }

        return moves;
    }

    public void addIfCanTake(ChessBoard board, ChessPosition myPos, ChessPosition propPos) {

        int propRow = propPos.getRow();
        int propCol = propPos.getColumn();
        if (propRow > 8 || propCol > 8 || propRow < 1 || propCol < 1) { return; }

        ChessPiece pieceInPropMove = board.getPiece(propPos);
        ChessPiece myPiece = board.getPiece(myPos);

        if ((propRow == 8 || propRow == 1) && pieceInPropMove != null && (myPiece.getTeamColor() != pieceInPropMove.getTeamColor())) {
            this.moves.add(new ChessMove(myPos, propPos, ChessPiece.PieceType.BISHOP));
            this.moves.add(new ChessMove(myPos, propPos, ChessPiece.PieceType.KNIGHT));
            this.moves.add(new ChessMove(myPos, propPos, ChessPiece.PieceType.ROOK));
            this.moves.add(new ChessMove(myPos, propPos, ChessPiece.PieceType.QUEEN));
            return;
        }

        ChessMove propMove = new ChessMove(myPos, propPos,null);

        if (pieceInPropMove != null) {
            if (myPiece.getTeamColor() != pieceInPropMove.getTeamColor()){
                this.moves.add(propMove);
            }
        }
    }

    @Override
    public void addIfValid(ChessBoard board, ChessPosition myPos, ChessPosition propPos) {

        int propRow = propPos.getRow();
        int propCol = propPos.getColumn();
        if (propRow > 8 || propCol > 8 || propRow < 1 || propCol < 1) { return; }

        ChessPiece pieceInPropMove = board.getPiece(propPos);

        // for double move
        if ((myPos.getRow() == 2) && (propPos.getRow() == 4)) {
            ChessPiece pieceInPlaceBeforePropMove = board.getPiece(new ChessPosition(myPos.getRow() + 1, myPos.getColumn()));
            if (pieceInPlaceBeforePropMove != null) { return; }
        }

        if ((myPos.getRow() == 7) && (propPos.getRow() == 5)) {
            ChessPiece pieceInPlaceBeforePropMove = board.getPiece(new ChessPosition(myPos.getRow() - 1, myPos.getColumn()));
            if (pieceInPlaceBeforePropMove != null) { return; }
        }


        // for promotion at the end
        if ((propRow == 8 || propRow == 1) && pieceInPropMove == null) {
            this.moves.add(new ChessMove(myPos, propPos, ChessPiece.PieceType.BISHOP));
            this.moves.add(new ChessMove(myPos, propPos, ChessPiece.PieceType.KNIGHT));
            this.moves.add(new ChessMove(myPos, propPos, ChessPiece.PieceType.ROOK));
            this.moves.add(new ChessMove(myPos, propPos, ChessPiece.PieceType.QUEEN));

            return;
        }

        if (pieceInPropMove != null) {
            return;
        }

        ChessMove propMove = new ChessMove(myPos, propPos,null);
        this.moves.add(propMove);
    }
}
