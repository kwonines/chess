package chess;

import java.util.ArrayList;

public interface MoveCalculator {
    ArrayList<ChessMove> availableMoves(ChessBoard board, ChessPiece self, ChessPosition position);
    enum spaceType {EMPTY, ENEMY, ALLY}
}
