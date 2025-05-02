package chess;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {


    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        switch (this.type) {
            case BISHOP:
                BishopMoveCalculator bCalc = new BishopMoveCalculator();
                return bCalc.availableMoves(board, this, myPosition);
            case KING:
                KingMoveCalculator kCalc = new KingMoveCalculator();
                return kCalc.availableMoves(board, this, myPosition);
            case KNIGHT:
                KnightMoveCalculator nCalc = new KnightMoveCalculator();
                return nCalc.availableMoves(board, this, myPosition);
            case PAWN:
                PawnMoveCalculator pCalc = new PawnMoveCalculator();
                return pCalc.availableMoves(board, this, myPosition);
            case QUEEN:
                QueenMoveCalculator qCalc = new QueenMoveCalculator();
                return qCalc.availableMoves(board, this, myPosition);
            case ROOK:
                RookMoveCalculator rCalc = new RookMoveCalculator();
                return rCalc.availableMoves(board, this, myPosition);
            default: return null;
        }
    }
}
