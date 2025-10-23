package chess.piecemoves;

import chess.*;

import java.util.Set;

public class PawnValidMoves extends ValidMoves {

    public PawnValidMoves(ChessBoard board, ChessPosition myPos) {
        super(board, myPos);
    }

    public Set<ChessMove> getValidMoves() {
        int curRow = myPos.getRow();
        int curCol = myPos.getColumn();
        ChessGame.TeamColor curPieceColor = board.getPiece(myPos).getTeamColor();

        if (curPieceColor == ChessGame.TeamColor.WHITE) {
            ChessPosition forwardOne = new ChessPosition(curRow + 1, curCol);
            addIfValid(forwardOne);

            if (curRow == 2 && addIfValid(forwardOne)) {
                ChessPosition forwardTwo = new ChessPosition(curRow + 2, curCol);
                addIfValid(forwardTwo);
            }

            ChessPosition takeLeft = new ChessPosition(curRow + 1, curCol - 1);
            addIfCanTake(takeLeft);

            ChessPosition takeRight = new ChessPosition(curRow + 1, curCol + 1);
            addIfCanTake(takeRight);
        } else {
            ChessPosition backOne = new ChessPosition(curRow - 1, curCol);
            addIfValid(backOne);

            if (curRow == 7 && addIfValid(backOne)) {
                ChessPosition backTwo = new ChessPosition(curRow - 2, curCol);
                addIfValid(backTwo);
            }

            ChessPosition takeLeft = new ChessPosition(curRow - 1, curCol - 1);
            addIfCanTake(takeLeft);

            ChessPosition takeRight = new ChessPosition(curRow - 1, curCol + 1);
            addIfCanTake(takeRight);
        }

        return super.moves;
    }

    private void addIfCanTake(ChessPosition propPos) {

        int propRow = propPos.getRow();
        int propCol = propPos.getColumn();

        if (!inBounds(propRow) || !inBounds(propCol)) { return; }

        ChessPiece pieceInPropPos = board.getPiece(propPos);
        ChessPiece piece = board.getPiece(this.myPos);

        if (pieceInPropPos != null) {
            if (pieceInPropPos.getTeamColor() != piece.getTeamColor()) {
                boolean whiteColor = piece.getTeamColor() == ChessGame.TeamColor.WHITE;
                boolean blackColor = piece.getTeamColor() == ChessGame.TeamColor.BLACK;
                if ((whiteColor && propRow == 8) || (blackColor && propRow == 1)) {
                    super.moves.add(new ChessMove(myPos, propPos, ChessPiece.PieceType.BISHOP));
                    super.moves.add(new ChessMove(myPos, propPos, ChessPiece.PieceType.ROOK));
                    super.moves.add(new ChessMove(myPos, propPos, ChessPiece.PieceType.KNIGHT));
                    super.moves.add(new ChessMove(myPos, propPos, ChessPiece.PieceType.QUEEN));
                    return;
                }
                super.moves.add(new ChessMove(myPos, propPos, null));
            }
        }
    }

    @Override
    public boolean addIfValid(ChessPosition propPos) {

        int propRow = propPos.getRow();
        int propCol = propPos.getColumn();

        if (!inBounds(propRow) || !inBounds(propCol)) { return false; }

        ChessPiece pieceInPropPos = board.getPiece(propPos);
        ChessPiece piece = board.getPiece(this.myPos);

        if (pieceInPropPos != null) {
            return false;
        }


        boolean whiteColor = piece.getTeamColor() == ChessGame.TeamColor.WHITE;
        boolean blackColor = piece.getTeamColor() == ChessGame.TeamColor.BLACK;
        if ((whiteColor && propRow == 8) || (blackColor && propRow == 1)) {
            moves.add(new ChessMove(myPos, propPos, ChessPiece.PieceType.BISHOP));
            moves.add(new ChessMove(myPos, propPos, ChessPiece.PieceType.ROOK));
            moves.add(new ChessMove(myPos, propPos, ChessPiece.PieceType.KNIGHT));
            moves.add(new ChessMove(myPos, propPos, ChessPiece.PieceType.QUEEN));
            return true;
        }

        moves.add(new ChessMove(myPos, propPos, null));
        return true;
    }
}
