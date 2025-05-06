package chess;

import java.util.ArrayList;

public class KingMoveCalculator implements MoveCalculator {
    @Override
    public ArrayList<ChessMove> availableMoves(ChessBoard board, ChessPiece self, ChessPosition position) {
        ArrayList<ChessMove> moveList = new ArrayList<>();
        // ↑↑↑
        ChessPosition moveSpace = new ChessPosition(position.getRow() + 1, position.getColumn());
        if (isMoveValid(moveSpace, board, self)) {
            moveList.add(new ChessMove(position, moveSpace));
        }
        // ↗↗↗
        moveSpace = new ChessPosition(position.getRow() + 1, position.getColumn() + 1);
        if (isMoveValid(moveSpace, board, self)) {
            moveList.add(new ChessMove(position, moveSpace));
        }
        // →→→
        moveSpace = new ChessPosition(position.getRow(), position.getColumn() + 1);
        if (isMoveValid(moveSpace, board, self)) {
            moveList.add(new ChessMove(position, moveSpace));
        }
        // ↘↘↘
        moveSpace = new ChessPosition(position.getRow() - 1, position.getColumn() + 1);
        if (isMoveValid(moveSpace, board, self)) {
            moveList.add(new ChessMove(position, moveSpace));
        }
        // ↓↓↓
        moveSpace = new ChessPosition(position.getRow() - 1, position.getColumn());
        if (isMoveValid(moveSpace, board, self)) {
            moveList.add(new ChessMove(position, moveSpace));
        }
        // ↙↙↙
        moveSpace = new ChessPosition(position.getRow() - 1, position.getColumn() - 1);
        if (isMoveValid(moveSpace, board, self)) {
            moveList.add(new ChessMove(position, moveSpace));
        }
        // ←←←
        moveSpace = new ChessPosition(position.getRow(), position.getColumn() - 1);
        if (isMoveValid(moveSpace, board, self)) {
            moveList.add(new ChessMove(position, moveSpace));
        }
        // ↖↖↖
        moveSpace = new ChessPosition(position.getRow() + 1, position.getColumn() - 1);
        if (isMoveValid(moveSpace, board, self)) {
            moveList.add(new ChessMove(position, moveSpace));
        }
        return moveList;
    }

    //given a row and column, checks to see if the space is occupied.
    //If the space is occupied, checks to see if the occupying piece is the same color as the given piece or not.
    private SpaceType findSpaceType(ChessBoard board, ChessPiece self, ChessPosition space) {
        if(board.getPiece(space) == null) {
            return SpaceType.EMPTY;
        } else if (board.getPiece(space).getTeamColor() != self.getTeamColor()) {
            return SpaceType.ENEMY;
        } else {
            return SpaceType.ALLY;
        }
    }

    //Checks to see if the king can move to a given space on a board
    private boolean isMoveValid(ChessPosition space, ChessBoard board, ChessPiece self) {
        if (space.getRow() < 9 && space.getColumn() < 9 && space.getRow() > 0 && space.getColumn() > 0) {
            return findSpaceType(board, self, space) != SpaceType.ALLY;
        } else {
            return false;
        }
    }
}
