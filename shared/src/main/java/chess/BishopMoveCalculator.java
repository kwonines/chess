package chess;

import java.util.ArrayList;

public class BishopMoveCalculator extends MoveCalculator {
    @Override
    public ArrayList<ChessMove> availableMoves(ChessBoard board, ChessPiece self, ChessPosition position) {
        ArrayList<ChessMove> moveList = new ArrayList<>();
        for (int row = position.getRow() + 1, col = position.getColumn() + 1; row < 9 && col < 9; row++, col++) {
            if (insertMoveIfValid(board, self, position, moveList, row, col)) {
                break;
            }
        }
        for (int row = position.getRow() + 1, col = position.getColumn() - 1; row < 9 && col > 0; row++, col--) {
            if (insertMoveIfValid(board, self, position, moveList, row, col)) {
                break;
            }
        }
        for (int row = position.getRow() - 1, col = position.getColumn() + 1; row > 0 && col < 9; row--, col++) {
            if (insertMoveIfValid(board, self, position, moveList, row, col)) {
                break;
            }
        }
        for (int row = position.getRow() - 1, col = position.getColumn() - 1; row > 0 && col > 0; row--, col--) {
            if (insertMoveIfValid(board, self, position, moveList, row, col)) {
                break;
            }
        }

        return moveList;
    }

    private boolean insertMoveIfValid(ChessBoard board, ChessPiece self, ChessPosition position, ArrayList<ChessMove> moveList, int row, int col) {
        ChessPosition space;
        space = new ChessPosition(row, col);
        if (findSpaceType(board, self, space) == SpaceType.EMPTY) {
            moveList.add(new ChessMove(position, space));
        } else if (findSpaceType(board, self, space) == SpaceType.ENEMY) {
            moveList.add(new ChessMove(position, space));
            return true;
        } else {
            return true;
        }
        return false;
    }
}
