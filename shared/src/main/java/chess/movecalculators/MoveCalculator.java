package chess.movecalculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;

public abstract class MoveCalculator {
    abstract ArrayList<ChessMove> availableMoves(ChessBoard board, ChessPiece self, ChessPosition position);
    protected enum SpaceType {EMPTY, ENEMY, ALLY}

    //given a row and column, checks to see if the space is occupied.
    //If the space is occupied, checks to see if the occupying piece is the same color as the given piece or not.
    protected SpaceType findSpaceType(ChessBoard board, ChessPiece self, ChessPosition space) {
        if(board.getPiece(space) == null) {
            return SpaceType.EMPTY;
        } else if (board.getPiece(space).getTeamColor() != self.getTeamColor()) {
            return SpaceType.ENEMY;
        } else {
            return SpaceType.ALLY;
        }
    }

    //Checks to see if a piece can move to a given space on a gameBoard
    protected boolean isMoveValid(ChessPosition space, ChessBoard board, ChessPiece self) {
        if (space.getRow() < 9 && space.getColumn() < 9 && space.getRow() > 0 && space.getColumn() > 0) {
            return findSpaceType(board, self, space) != SpaceType.ALLY;
        } else {
            return false;
        }
    }
}
