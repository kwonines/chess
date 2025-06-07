package ui;

import chess.ChessGame;

public class PlayerColor {
    private static ChessGame.TeamColor playerColor = null;


    public static ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }

    public static void setPlayerColor(ChessGame.TeamColor playerColor) {
        PlayerColor.playerColor = playerColor;
    }
}
