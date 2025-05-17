package chess.movecalculators;

import chess.*;

import java.util.ArrayList;

public class KingMoveCalculator extends MoveCalculator {
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
}
