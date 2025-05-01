package chess;

import java.util.ArrayList;

public class BishopMoveCalculator implements MoveCalculator {
    @Override
    public ArrayList<ChessMove> availableMoves(ChessBoard board, ChessPiece self, ChessPosition position) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        int row = position.getRow();
        int colUp = position.getColumn();
        int colDown = position.getColumn();
        while (row < 9) {
            row++;
            colUp++;
            colDown--;
            if (isValidMove(board, self, row, colUp))
                possibleMoves.add(makeMove(position, row, colUp));
            if (isValidMove(board, self, row, colDown))
                possibleMoves.add(makeMove(position, row, colDown));

        }
        row = position.getRow();
        colUp = position.getColumn();
        colDown = position.getColumn();
        while (row > 0) {
            row--;
            colUp++;
            colDown--;
            if (isValidMove(board, self, row, colUp))
                possibleMoves.add(makeMove(position, row, colUp));
            if (isValidMove(board, self, row, colDown))
                possibleMoves.add(makeMove(position, row, colDown));

        }
        return possibleMoves;
    }

    /*Checks to see if the provided space can be moved to by the Bishop. If the space is either outside the board,
    or occupied by a piece of the same color it will return false, otherwise returns true.*/
    private boolean isValidMove (ChessBoard board, ChessPiece self, int row, int col) {
        if (row > 0 && row < 9 && col > 0 && col < 9) {
            ChessPosition space = new ChessPosition(row, col);
            ChessPiece piece = board.getPiece(space);
            if (piece == null) {
                return true;
            } else return piece.getTeamColor() != self.getTeamColor();
        } else return false;
    }

    private ChessMove makeMove(ChessPosition startPosition, int row, int col) {
        ChessPosition newPosition = new ChessPosition(row, col);
        return new ChessMove(startPosition, newPosition);
    }
}
