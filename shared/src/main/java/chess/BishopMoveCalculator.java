package chess;

import java.util.ArrayList;

public class BishopMoveCalculator implements MoveCalculator {
    @Override
    public ArrayList<ChessMove> availableMoves(ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        int row = position.getRow();
        int colUp = position.getColumn();
        int colDown = position.getColumn();
        while (row < 9) {
            row++;
            colUp++;
            colDown--;
            if (isValidMove(board, row, colUp))
                possibleMoves.add(makeValidMove(board, position, row, colUp));
            if (isValidMove(board, row, colDown))
                possibleMoves.add(makeValidMove(board, position, row, colDown));

        }
        row = position.getRow();
        colUp = position.getColumn();
        colDown = position.getColumn();
        while (row > 0) {
            row--;
            colUp++;
            colDown--;
            if (isValidMove(board, row, colUp))
                possibleMoves.add(makeValidMove(board, position, row, colUp));
            if (isValidMove(board, row, colDown))
                possibleMoves.add(makeValidMove(board, position, row, colDown));

        }



        return possibleMoves;
    }

    private boolean isValidMove(ChessBoard board, int row, int col) {
        return row > 0 && row < 9 && col > 0 && col < 9;
    }

    private ChessMove makeValidMove(ChessBoard board, ChessPosition startPosition, int row, int col) {
        ChessPosition newPosition = new ChessPosition(row, col);
        return new ChessMove(startPosition, newPosition);
    }
}
