package chess;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard board;
    private TeamColor teamTurn;
    private boolean gameOver;

    public ChessGame() {
        this.board = new ChessBoard();
        board.resetBoard();
        this.setTeamTurn(TeamColor.WHITE);
        this.gameOver = false;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return this.teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamTurn = team;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);
        Collection<ChessMove> potentialMoves = piece.pieceMoves(board, startPosition);

        potentialMoves.removeIf(potentialMove -> !this.board.setNewPosition(startPosition, potentialMove.getEndPosition(), this, false));
        return potentialMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        // is the current move in the list of piece moves?
        // does the current move put your own king in check?
        // alright, make the move
        ChessPiece pieceToMove = board.getPiece(move.getStartPosition());

        if (pieceToMove == null) {
            throw new InvalidMoveException("Piece not found!");
        }

        Collection<ChessMove> potentialMoves = pieceToMove.pieceMoves(this.board, move.getStartPosition());

        if (pieceToMove.getTeamColor() != getTeamTurn()) {
            throw new InvalidMoveException("Not this team's turn!");
        }
        if (!potentialMoves.contains(move)) {
            throw new InvalidMoveException("Piece is unable to move in that way!");
        }

        ChessPiece.PieceType potentialPromotionPiece = move.getPromotionPiece();
        if (potentialPromotionPiece != null) {
            pieceToMove.setPieceType(potentialPromotionPiece);
        }

        boolean canSetNewPosWithoutKingBeingInDanger = this.board.setNewPosition(move.getStartPosition(), move.getEndPosition(), this, true);
        if (!canSetNewPosWithoutKingBeingInDanger) {
            throw new InvalidMoveException("Current team king in danger, unable to move");
        }

        // SET TEAM TURN
        if (isInCheckmate(TeamColor.BLACK) || isInCheckmate(TeamColor.WHITE) || isInStalemate(TeamColor.BLACK) || isInStalemate(TeamColor.WHITE)) {
            this.gameOver = true;
        }

        if (pieceToMove.getTeamColor() == TeamColor.BLACK) {
            this.setTeamTurn(TeamColor.WHITE);
        } else {
            this.setTeamTurn(TeamColor.BLACK);
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        // does any opposing piece have a potential move to where our current king is?
        // 1. get all the other color pieces
        // 2. get where our king is
        // 3. See if any of the color pieces potential moves = our king position

        return this.kingIsInDangerWith(this.board, teamColor);
    }

    public Collection<ChessMove> potentialMovesForTeam(ChessBoard board, TeamColor teamColor, boolean oppositeTeam) {
        Set<ChessMove> potentialMoves = new HashSet<>();
        Collection<ChessPiece> allPieces = board.getAllPiecesFromBoard();
        if (oppositeTeam) {
            if (teamColor == TeamColor.BLACK) {
                teamColor = TeamColor.WHITE;
            } else {
                teamColor = TeamColor.BLACK;
            }
        }

        for (ChessPiece piece : allPieces) {
            if (piece.getTeamColor() == teamColor) {
                Stream<ChessMove> pieceMovesStream = piece.pieceMoves(board, piece.getCurrentPosition()).stream();
                potentialMoves = Stream.concat(potentialMoves.stream(), pieceMovesStream).collect(Collectors.toSet());
            }
        }

        return potentialMoves;
    }

    public boolean kingIsInDangerWith(ChessBoard board, TeamColor teamColor) {

        Collection<ChessPiece> allPieces = board.getAllPiecesFromBoard();
        Collection<ChessMove> potentialOpponentMoves = potentialMovesForTeam(board, teamColor, true);

        ChessPiece myKing = null;

        for (ChessPiece piece : allPieces) {
            if (piece.getTeamColor() == teamColor && piece.getPieceType() == ChessPiece.PieceType.KING) {
                myKing = piece;
            }
        }

        if (myKing == null) { return false; }
        for (ChessMove opponentMove : potentialOpponentMoves) {
            boolean kingAndOpponentSamePos = myKing.getCurrentPosition().getRow() == opponentMove.getEndPosition().getRow();
            if (kingAndOpponentSamePos && myKing.getCurrentPosition().getColumn() == opponentMove.getEndPosition().getColumn()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        // 1. Is in check
        // 2. All king's valid moves will put him in check

        return isInCheck(teamColor) && !canMove(teamColor);
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        // 1. For the current team's turn, there is no valid moves -
        // (King is not in check but will be if he moves, and no valid moves from other pieces of our color)

        return !isInCheck(teamColor) && !canMove(teamColor);
    }

    private boolean canMove(TeamColor teamColor) {
        Collection<ChessMove> potentialMovesForTeam = potentialMovesForTeam(this.board, teamColor, false);
        for (ChessMove potentialMove : potentialMovesForTeam) {
            ChessPosition potMoveStart = potentialMove.getStartPosition();
            ChessPosition potMoveEnd = potentialMove.getEndPosition();
            boolean canSetNewPosWithoutKingBeingInDanger = this.board.setNewPosition(potMoveStart, potMoveEnd, this, false);
            if (canSetNewPosWithoutKingBeingInDanger) {
                return true;
            }
        }

        return false;
    }
    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.board;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(board, chessGame.board) && teamTurn == chessGame.teamTurn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, teamTurn);
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "board=" + board +
                '}';
    }
}
