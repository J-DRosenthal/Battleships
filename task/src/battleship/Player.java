package battleship;

public class Player {
    int playerNumber;
    char[][] gameBoard;
    char[][] fogOfWar;
    Ship[] ships = new Ship[]{
            new Ship("Aircraft Carrier", 5),
            new Ship("Battleship", 4),
            new Ship("Submarine", 3),
            new Ship("Cruiser", 3),
            new Ship("Destroyer", 2)};

    public Player(int playerNumber, char[][] board, char[][] fogOfWar) {
        this.playerNumber = playerNumber;
        this.gameBoard = board;
        this.fogOfWar = fogOfWar;
    }


}
