package chess;

import java.util.ArrayList;

public class BishopMoveCalculator implements MoveCalculator {
    @Override
    public ArrayList<ChessMove> availableMoves(ChessBoard board, ChessPiece self, ChessPosition position) {
        ArrayList<ChessMove> moveList = new ArrayList<>();
        for (int row = position.getRow() + 1, col = position.getColumn() + 1; row < 9 && col < 9; row++, col++) {
            if (insertMove(board, self, position, moveList, row, col)) {
                break;
            }
        }
        for (int row = position.getRow() + 1, col = position.getColumn() - 1; row < 9 && col > 0; row++, col--) {
            if (insertMove(board, self, position, moveList, row, col)) {
                break;
            }
        }
        for (int row = position.getRow() - 1, col = position.getColumn() + 1; row > 0 && col < 9; row--, col++) {
            if (insertMove(board, self, position, moveList, row, col)) {
                break;
            }
        }
        for (int row = position.getRow() - 1, col = position.getColumn() - 1; row > 0 && col > 0; row--, col--) {
            if (insertMove(board, self, position, moveList, row, col)) {
                break;
            }
        }

        return moveList;
    }

    private boolean insertMove(ChessBoard board, ChessPiece self, ChessPosition position, ArrayList<ChessMove> moveList, int row, int col) {
        ChessPosition space;
        space = new ChessPosition(row, col);
        if (findSpaceType(board, self, space) == spaceType.EMPTY) {
            moveList.add(new ChessMove(position, space));
        } else if (findSpaceType(board, self, space) == spaceType.ENEMY) {
            moveList.add(new ChessMove(position, space));
            return true;
        } else return true;
        return false;
    }

    //given a row and column, checks to see if the space is occupied.
    //If the space is occupied, checks to see if the occupying piece is the same color as the given piece or not.
    private BishopMoveCalculator.spaceType findSpaceType(ChessBoard board, ChessPiece self, ChessPosition space) {
        if(board.getPiece(space) == null) {
            return BishopMoveCalculator.spaceType.EMPTY;
        } else if (board.getPiece(space).getTeamColor() != self.getTeamColor()) {
            return BishopMoveCalculator.spaceType.ENEMY;
        } else return BishopMoveCalculator.spaceType.ALLY;
    }
}
