package chess;

import java.util.ArrayList;

public class PawnMoveCalculator implements MoveCalculator {
    @Override
    public ArrayList<ChessMove> availableMoves(ChessBoard board, ChessPiece self, ChessPosition position) {
        ArrayList<ChessMove> moveList = new ArrayList<>();
        ChessPosition moveSpace;
        if (self.getTeamColor() == ChessGame.TeamColor.WHITE) {
            // ↖↖↖
            moveSpace = new ChessPosition(position.getRow() + 1, position.getColumn() - 1);
            makeWhiteDiagonalMove(board, self, position, moveList, moveSpace);
            // ↗↗↗
            moveSpace = new ChessPosition(position.getRow() + 1, position.getColumn() + 1);
            makeWhiteDiagonalMove(board, self, position, moveList, moveSpace);
            // ↑↑↑
            moveSpace = new ChessPosition(position.getRow() + 1, position.getColumn());
            boolean isForwardBlocked = true;
            if (isForwardValid(moveSpace, board, self) && moveSpace.getRow() == 8) {
                addPromotionMoves(position, moveList, moveSpace);
                isForwardBlocked = false;
            } else if (isForwardValid(moveSpace, board, self)) {
                moveList.add(new ChessMove(position, moveSpace));
                isForwardBlocked = false;
            }
            // double ↑↑↑
            moveSpace = new ChessPosition(position.getRow() + 2, position.getColumn());
            if (isForwardValid(moveSpace, board, self) && position.getRow() == 2 && !isForwardBlocked) {
                moveList.add(new ChessMove(position, moveSpace));
            }

        } else if (self.getTeamColor() == ChessGame.TeamColor.BLACK) {
            moveSpace = new ChessPosition(position.getRow() - 1, position.getColumn() - 1);
            makeBlackDiagonalMove(board, self, position, moveList, moveSpace);
            // ↗↗↗
            moveSpace = new ChessPosition(position.getRow() - 1, position.getColumn() + 1);
            makeBlackDiagonalMove(board, self, position, moveList, moveSpace);
            // ↑↑↑
            moveSpace = new ChessPosition(position.getRow() - 1, position.getColumn());
            boolean isForwardBlocked = true;
            if (isForwardValid(moveSpace, board, self) && moveSpace.getRow() == 1) {
                addPromotionMoves(position, moveList, moveSpace);
                isForwardBlocked = false;
            } else if (isForwardValid(moveSpace, board, self)) {
                moveList.add(new ChessMove(position, moveSpace));
                isForwardBlocked = false;
            }
            // double ↑↑↑
            moveSpace = new ChessPosition(position.getRow() - 2, position.getColumn());
            if (isForwardValid(moveSpace, board, self) && position.getRow() == 7 && !isForwardBlocked) {
                moveList.add(new ChessMove(position, moveSpace));
            }
        }
        return moveList;
    }

    private void addPromotionMoves(ChessPosition position, ArrayList<ChessMove> moveList, ChessPosition moveSpace) {
        moveList.add(new ChessMove(position, moveSpace, ChessPiece.PieceType.QUEEN));
        moveList.add(new ChessMove(position, moveSpace, ChessPiece.PieceType.ROOK));
        moveList.add(new ChessMove(position, moveSpace, ChessPiece.PieceType.BISHOP));
        moveList.add(new ChessMove(position, moveSpace, ChessPiece.PieceType.KNIGHT));
    }

    private void makeBlackDiagonalMove(ChessBoard board, ChessPiece self, ChessPosition position,
                                       ArrayList<ChessMove> moveList, ChessPosition moveSpace) {
        if (isDiagonalValid(moveSpace, board, self) && moveSpace.getRow() == 1) {
            addPromotionMoves(position, moveList, moveSpace);
        } else if (isDiagonalValid(moveSpace, board, self)) {
            moveList.add(new ChessMove(position, moveSpace));
        }
    }

    private void makeWhiteDiagonalMove(ChessBoard board, ChessPiece self, ChessPosition position,
                                       ArrayList<ChessMove> moveList, ChessPosition moveSpace) {
        if (isDiagonalValid(moveSpace, board, self) && moveSpace.getRow() == 8) {
            addPromotionMoves(position, moveList, moveSpace);
        } else if (isDiagonalValid(moveSpace, board, self)) {
            moveList.add(new ChessMove(position, moveSpace));
        }
    }

    //given a row and column, checks to see if the space is occupied.
    //If the space is occupied, checks to see if the occupying piece is the same color as the given piece or not.
    private SpaceType findSpaceType(ChessBoard board, ChessPiece self, ChessPosition space) {
        if(board.getPiece(space) == null) {
            return SpaceType.EMPTY;
        } else if (board.getPiece(space).getTeamColor() != self.getTeamColor()) {
            return SpaceType.ENEMY;
        } else {
            return SpaceType.ALLY;
        }
    }

    private boolean isDiagonalValid(ChessPosition space, ChessBoard board, ChessPiece self) {
        if (space.getRow() < 9 && space.getColumn() < 9 && space.getRow() > 0 && space.getColumn() > 0) {
            return findSpaceType(board, self, space) == SpaceType.ENEMY;
        } else {
            return false;
        }
    }

    private boolean isForwardValid(ChessPosition space, ChessBoard board, ChessPiece self) {
        if (space.getRow() < 9 && space.getColumn() < 9 && space.getRow() > 0 && space.getColumn() > 0) {
            return findSpaceType(board, self, space) == SpaceType.EMPTY;
        } else {
            return false;
        }
    }
}
