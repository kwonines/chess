package chess.movecalculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;

public class RookMoveCalculator extends MoveCalculator {
    @Override
    public ArrayList<ChessMove> availableMoves(ChessBoard board, ChessPiece self, ChessPosition position) {
        ArrayList<ChessMove> moveList = new ArrayList<>();
        for (int row = position.getRow() + 1; row < 9; row++) {
            if (insertRowMove(board, self, position, moveList, row)) {
                break;
            }
        }
        for (int row = position.getRow() - 1; row > 0; row--) {
            if (insertRowMove(board, self, position, moveList, row)) {
                break;
            }
        }
        for (int col = position.getColumn() + 1; col < 9; col++) {
            if (insertColumnMove(board, self, position, moveList, col)) {
                break;
            }
        }
        for (int col = position.getColumn() - 1; col > 0; col--) {
            if (insertColumnMove(board, self, position, moveList, col)) {
                break;
            }
        }
        return moveList;
    }

    //Used to add a move to the moveList array when given just a column to move to
    private boolean insertColumnMove(ChessBoard board, ChessPiece self, ChessPosition position, ArrayList<ChessMove> moveList, int col) {
        SpaceType space;
        space = isSpaceAvailable(board, self, position.getRow(), col);
        if (space == SpaceType.EMPTY) {
            moveList.add(makeMove(position, position.getRow(), col));
            return false;
        } else if (space == SpaceType.ENEMY) {
            moveList.add(makeMove(position, position.getRow(), col));
            return true;
        } else {
            return true;
        }
    }

    //Used to add a move to the moveList array when given just a row to move to
    private boolean insertRowMove(ChessBoard board, ChessPiece self, ChessPosition position, ArrayList<ChessMove> moveList, int row) {
        SpaceType space;
        space = isSpaceAvailable(board, self, row, position.getColumn());
        if (space == SpaceType.EMPTY) {
            moveList.add(makeMove(position, row, position.getColumn()));
            return false;
        } else if (space == SpaceType.ENEMY) {
            moveList.add(makeMove(position, row, position.getColumn()));
            return true;
        } else {
            return true;
        }
    }

    //given a row and column, checks to see if the space is occupied.
    //If the space is occupied, checks to see if the occupying piece is the same color as the given piece or not.
    private SpaceType isSpaceAvailable(ChessBoard board, ChessPiece self, int row, int col) {
        ChessPosition space = new ChessPosition(row, col);
        if(board.getPiece(space) == null) {
            return SpaceType.EMPTY;
        } else if (board.getPiece(space).getTeamColor() != self.getTeamColor()) {
            return SpaceType.ENEMY;
        } else {
            return SpaceType.ALLY;
        }
    }

    //used to simplify, returns a ChessMove given one position and one set of coordinate
    private ChessMove makeMove(ChessPosition startPosition, int row, int col) {
        ChessPosition newPosition = new ChessPosition(row, col);
        return new ChessMove(startPosition, newPosition);
    }
}
