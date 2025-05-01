package chess;

import java.util.ArrayList;

public class RookMoveCalculator implements MoveCalculator {
    @Override
    public ArrayList<ChessMove> availableMoves(ChessBoard board, ChessPiece self, ChessPosition position) {
        ArrayList<ChessMove> moveList = new ArrayList<>();
        spaceType space;
        for (int row = position.getRow() + 1; row < 9; row++) {
            space = isSpaceAvailable(board, self, row, position.getColumn());
            if (space == spaceType.EMPTY) {
                moveList.add(makeMove(position, row, position.getColumn()));
            } else if (space == spaceType.ENEMY) {
                moveList.add(makeMove(position, row, position.getColumn()));
                break;
            } else break;
        }
        for (int row = position.getRow() - 1; row > 0; row--) {
            space = isSpaceAvailable(board, self, row, position.getColumn());
            if (space == spaceType.EMPTY) {
                moveList.add(makeMove(position, row, position.getColumn()));
            } else if (space == spaceType.ENEMY) {
                moveList.add(makeMove(position, row, position.getColumn()));
                break;
            } else break;
        }
        for (int col = position.getColumn() + 1; col < 9; col++) {
            space = isSpaceAvailable(board, self, position.getRow(), col);
            if (space == spaceType.EMPTY) {
                moveList.add(makeMove(position, position.getRow(), col));
            } else if (space == spaceType.ENEMY) {
                moveList.add(makeMove(position, position.getRow(), col));
                break;
            } else break;
        }
        for (int col = position.getColumn() - 1; col > 0; col--) {
            space = isSpaceAvailable(board, self, position.getRow(), col);
            if (space == spaceType.EMPTY) {
                moveList.add(makeMove(position, position.getRow(), col));
            } else if (space == spaceType.ENEMY) {
                moveList.add(makeMove(position, position.getRow(), col));
                break;
            } else break;
        }
        return moveList;
    }

    private spaceType isSpaceAvailable(ChessBoard board, ChessPiece self, int row, int col) {
        ChessPosition space = new ChessPosition(row, col);
        if(board.getPiece(space) == null) {
            return spaceType.EMPTY;
        } else if (board.getPiece(space).getTeamColor() != self.getTeamColor()) {
            return spaceType.ENEMY;
        } else return spaceType.ALLY;
    }

    private enum spaceType {
        EMPTY, ENEMY, ALLY
    }

    private ChessMove makeMove(ChessPosition startPosition, int row, int col) {
        ChessPosition newPosition = new ChessPosition(row, col);
        return new ChessMove(startPosition, newPosition);
    }
}
