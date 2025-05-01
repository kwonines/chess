package chess;

import java.util.ArrayList;

public interface MoveCalculator {
    public ArrayList<ChessMove> availableMoves(ChessBoard board, ChessPiece self, ChessPosition position);
}
