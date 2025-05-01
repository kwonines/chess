package chess;

import java.util.ArrayList;
import java.util.Collection;

public interface MoveCalculator {
    public ArrayList<ChessMove> availableMoves(ChessBoard board, ChessPosition position);
}
