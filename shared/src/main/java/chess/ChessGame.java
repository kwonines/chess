package chess;

import java.util.Collection;
import java.util.Iterator;

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
            Iterator<ChessMove> it = validMoveList.iterator();
            while (it.hasNext()) {
                ChessMove chessMove = it.next();
                if (moveWillCheck(chessMove, gameBoard, piece)) {
                    it.remove();
                }
            }


            return validMoveList;
        }
    }

    private boolean moveWillCheck(ChessMove move, ChessBoard simBoard, ChessPiece piece) {
        ChessPiece originalPiece = simBoard.getPiece(move.getStartPosition());
        ChessPiece capturePiece = simBoard.getPiece(move.getEndPosition());
        simBoard.addPiece(move.getEndPosition(), originalPiece);
        simBoard.addPiece(move.getStartPosition(), null);
        boolean willMoveCheck = isInCheckOnBoard(piece.getTeamColor(), simBoard);
        simBoard.addPiece(move.getStartPosition(), originalPiece);
        simBoard.addPiece(move.getEndPosition(), capturePiece);
        return willMoveCheck;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        //if move is not in the validmove list, throw exception
        //if promotion piece is null, set the endposition to the piece at start position, set the startposition piece to null;
        //if the promotion piece != null, set the start position to null, set the end position to the promotion piece of same color
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        return isInCheckOnBoard(teamColor, gameBoard);
    }

    private boolean isInCheckOnBoard(TeamColor teamColor, ChessBoard board) {
        for (int row = 1; row < 9; row++) {
            for (int col = 1; col < 9; col++) {
                ChessPosition square = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(square);
                if (piece != null) {
                    if (piece.getTeamColor() != teamColor) {
                        Collection<ChessMove> moveList = piece.pieceMoves(board, square);
                        for (ChessMove chessMove : moveList) {
                            ChessPosition position = chessMove.getEndPosition();
                            if (board.getPiece(position) != null) {
                                if (board.getPiece(position).getPieceType() == ChessPiece.PieceType.KING) {
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
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
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
