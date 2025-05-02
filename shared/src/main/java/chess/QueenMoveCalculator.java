package chess;

import java.util.ArrayList;

public class QueenMoveCalculator implements MoveCalculator {
    @Override
    public ArrayList<ChessMove> availableMoves(ChessBoard board, ChessPiece self, ChessPosition position) {
        ArrayList<ChessMove> moveList = new ArrayList<>();

        for (int row = position.getRow() + 1, col = position.getColumn() + 1; row < 9 && col < 9; row++, col++) {
            if (insertValidMove(board, self, position, moveList, row, col))
                break;
        }

        for (int row = position.getRow() - 1, col = position.getColumn() + 1; row > 0 && col < 9; row--, col++) {
            if (insertValidMove(board, self, position, moveList, row, col))
                break;
        }

        for (int row = position.getRow() - 1, col = position.getColumn() - 1; row > 0 && col > 0; row--, col--) {
            if (insertValidMove(board, self, position, moveList, row, col))
                break;
        }
        for (int row = position.getRow() + 1, col = position.getColumn() - 1; row < 9 && col > 0; row++, col--)
            if (insertValidMove(board, self, position, moveList, row, col)) {
                break;
        }

        for (int row = position.getRow() + 1; row < 9; row++) {
            if (insertValidMove(board, self, position, moveList, row, position.getColumn()))
                break;
        }

        for (int col = position.getColumn() + 1; col < 9; col++) {
            if (insertValidMove(board, self, position, moveList, position.getRow(), col))
                break;
        }

        for (int row = position.getRow() - 1; row > 0; row--) {
            if (insertValidMove(board, self, position, moveList, row, position.getColumn()))
                break;
        }

        for (int col = position.getColumn() - 1; col > 0; col--) {
            if (insertValidMove(board, self, position, moveList, position.getRow(), col))
                break;
        }



        return moveList;
    }

    //given a row and column to move to, will insert that as a move if the space is empty or occupied as an enemy.
    //If the space is an enemy, it will also return true. If the space is an ally, it will just return true.
    private boolean insertValidMove(ChessBoard board, ChessPiece self, ChessPosition position, ArrayList<ChessMove> moveList, int row, int col) {
        ChessPosition moveSpace;
        spaceType moveSpaceType;
        moveSpace = new ChessPosition(row, col);
        moveSpaceType = findSpaceType(board, self, moveSpace);
        if (moveSpaceType == spaceType.EMPTY) {
            moveList.add(new ChessMove(position, moveSpace));
        } else if (moveSpaceType == spaceType.ENEMY) {
            moveList.add(new ChessMove(position, moveSpace));
            return true;
        } else return true;
        return false;
    }

    //given a row and column, checks to see if the space is occupied.
    //If the space is occupied, checks to see if the occupying piece is the same color as the given piece or not.
    private MoveCalculator.spaceType findSpaceType(ChessBoard board, ChessPiece self, ChessPosition space) {
        if(board.getPiece(space) == null) {
            return MoveCalculator.spaceType.EMPTY;
        } else if (board.getPiece(space).getTeamColor() != self.getTeamColor()) {
            return MoveCalculator.spaceType.ENEMY;
        } else return MoveCalculator.spaceType.ALLY;
    }
}
