import chess.*;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import server.Server;

public class Main {
    public static void main(String[] args) throws DataAccessException {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Server: " + piece);

        DatabaseManager.createDatabase();

        Server server = new Server();
        server.run(8080);


    }
}