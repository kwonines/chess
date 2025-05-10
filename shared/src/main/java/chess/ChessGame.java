package chess;

import java.util.Collection;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a gameBoard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor turn = TeamColor.WHITE;
    ChessBoard gameBoard = new ChessBoard();

    public ChessGame() {
        gameBoard.resetBoard();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return turn == chessGame.turn && Objects.equals(gameBoard, chessGame.gameBoard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(turn, gameBoard);
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        turn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = gameBoard.getPiece(startPosition);
        if (piece == null) {
            return null;
        } else {
            Collection<ChessMove> validMoveList = piece.pieceMoves(gameBoard, startPosition);
            validMoveList.removeIf(this::moveWillCheck);


            return validMoveList;
        }
    }

    //when given a move, checks if that move would put that piece's team in check. Leaves the board unchanged
    private boolean moveWillCheck(ChessMove move) {
        ChessPiece originalPiece = gameBoard.getPiece(move.getStartPosition());
        ChessPiece capturePiece = gameBoard.getPiece(move.getEndPosition());
        gameBoard.addPiece(move.getEndPosition(), originalPiece);
        gameBoard.addPiece(move.getStartPosition(), null);
        boolean willMoveCheck = isInCheck(originalPiece.getTeamColor());
        gameBoard.addPiece(move.getStartPosition(), originalPiece);
        gameBoard.addPiece(move.getEndPosition(), capturePiece);
        return willMoveCheck;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        Collection<ChessMove> validMoveList = validMoves(move.getStartPosition());
        if (validMoveList == null || gameBoard.getPiece(move.getStartPosition()).getTeamColor() != turn||!validMoveList.contains(move)) {
            throw new InvalidMoveException("Move is not valid");
        } else if (move.getPromotionPiece() == null) {
            gameBoard.addPiece(move.getEndPosition(), gameBoard.getPiece(move.getStartPosition()));
            gameBoard.addPiece(move.getStartPosition(), null);
        } else {
            gameBoard.addPiece(move.getEndPosition(),
                    new ChessPiece(gameBoard.getPiece(move.getStartPosition()).getTeamColor(), move.getPromotionPiece()));
            gameBoard.addPiece(move.getStartPosition(), null);
        }

        if (turn == TeamColor.WHITE) {
            turn = TeamColor.BLACK;
        } else {
            turn = TeamColor.WHITE;
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        for (int row = 1; row < 9; row++) {
            for (int col = 1; col < 9; col++) {
                ChessPosition square = new ChessPosition(row, col);
                ChessPiece piece = gameBoard.getPiece(square);
                if (piece != null) {
                    if (piece.getTeamColor() != teamColor) {
                        Collection<ChessMove> moveList = piece.pieceMoves(gameBoard, square);
                        for (ChessMove chessMove : moveList) {
                            ChessPosition position = chessMove.getEndPosition();
                            if (gameBoard.getPiece(position) != null) {
                                if (gameBoard.getPiece(position).getPieceType() == ChessPiece.PieceType.KING) {
                                    return true;
                                }
                            }
                        }
                    }
                }
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
        return isInCheck(teamColor) && hasNoValidMoves(teamColor);
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(teamColor)) {
            return false;
        } else return hasNoValidMoves(teamColor);
    }

    //Checks to see if the given team has any moves that would not leave them in check
    private boolean hasNoValidMoves(TeamColor teamColor) {
        for (int row = 1; row < 9; row++) {
            for (int col = 1; col < 9; col++) {
                ChessPosition square = new ChessPosition(row, col);
                if (gameBoard.getPiece(square) != null && gameBoard.getPiece(square).getTeamColor() == teamColor) {
                    if (!validMoves(square).isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Sets this game's chessboard with a given gameBoard
     *
     * @param board the new gameBoard to use
     */
    public void setBoard(ChessBoard board) {
        this.gameBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return gameBoard;
    }
}
