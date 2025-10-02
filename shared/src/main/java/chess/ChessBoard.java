package chess;

import java.util.*;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private final ChessPiece[][] board;

    public ChessBoard() {
        this.board = new ChessPiece[8][8];
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        if (piece == null) { return; }
        int row = position.getRow() - 1;
        int col = position.getColumn() - 1;
        this.board[row][col] = piece;
        piece.setPosition(position);
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        int row = position.getRow() - 1;
        int col = position.getColumn() - 1;
        return this.board[row][col];
    }

    public boolean setNewPosition(ChessPosition curPos, ChessPosition newPos, ChessGame game, boolean actuallyMakeMove) {
        ChessPiece pieceInCurPos = getPiece(curPos);
        ChessPiece ogPiece = getPiece(newPos);

        this.board[curPos.getRow() - 1][curPos.getColumn() - 1] = null;
        this.board[newPos.getRow() - 1][newPos.getColumn() - 1] = pieceInCurPos;

        if (game.isInCheck(pieceInCurPos.getTeamColor())) {
            this.board[curPos.getRow() - 1][curPos.getColumn() - 1] = pieceInCurPos;
            this.board[newPos.getRow() - 1][newPos.getColumn() - 1] = ogPiece;

            return false;
        }

        if (!actuallyMakeMove) {
            this.board[curPos.getRow() - 1][curPos.getColumn() - 1] = pieceInCurPos;
            this.board[newPos.getRow() - 1][newPos.getColumn() - 1] = ogPiece;
        } else {
            pieceInCurPos.setPosition(newPos);
        }

        return true;
    }

    public Collection<ChessPiece> getAllPiecesFromBoard() {
        ArrayList<ChessPiece> pieces = new ArrayList<>();
        for (ChessPiece[] row : board) {
            for (ChessPiece piece : row) {
                if (piece != null) {
                    pieces.add(piece);
                }
            }
        }
        return pieces;
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        addPawns(2, ChessGame.TeamColor.WHITE);
        addPawns(7, ChessGame.TeamColor.BLACK);
        addSpecialPieces(1, ChessGame.TeamColor.WHITE);
        addSpecialPieces(8, ChessGame.TeamColor.BLACK);
    }

    private void addSpecialPieces(int row, ChessGame.TeamColor team) {
        ChessPiece.PieceType[] pieces = {ChessPiece.PieceType.ROOK, ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.KING, ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.ROOK};

        for (int col = 1; col < 9; col++) {
            ChessPiece newSpecialPiece = new ChessPiece(team, pieces[col - 1]);
            ChessPosition newPos = new ChessPosition(row, col);
            addPiece(newPos, newSpecialPiece);
        }
    }

    private void addPawns(int row, ChessGame.TeamColor team) {
        for (int col = 1; col < 9; col++) {
            ChessPiece newPawn = new ChessPiece(team, ChessPiece.PieceType.PAWN);
            ChessPosition newPos = new ChessPosition(row, col);
            addPiece(newPos, newPawn);
        }
    }

    @Override
    public String toString() {
        return "ChessBoard{" +
                "board=" + Arrays.toString(board) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }
}
