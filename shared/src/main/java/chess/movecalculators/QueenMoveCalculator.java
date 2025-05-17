package chess.movecalculators;

import chess.*;

import java.util.ArrayList;

public class QueenMoveCalculator extends MoveCalculator {
    @Override
    public ArrayList<ChessMove> availableMoves(ChessBoard board, ChessPiece self, ChessPosition position) {

        BishopMoveCalculator bishopMoveCalculator = new BishopMoveCalculator();
        ArrayList<ChessMove> moveList = bishopMoveCalculator.availableMoves(board, self, position);
        RookMoveCalculator rookMoveCalculator = new RookMoveCalculator();
        moveList.addAll(rookMoveCalculator.availableMoves(board, self, position));

        return moveList;
    }
}
