package battleship;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class BattleShipGame {
    Player player1;
    Player player2;
    final String[] columns = new String[] {" ","1","2","3","4","5","6","7","8","9","10"};
    final String[] rows = new String[] {"A","B","C","D","E","F","G","H","I","J"};

    public BattleShipGame() {
        player1 = new Player(1, generateBoard(), generateBoard());
        player2 = new Player(2, generateBoard(), generateBoard());
        gamePlayLoop();

    }

    char[][] generateBoard() {
        char[][] newBoard = new char[10][10];

        for (char[] chars : newBoard) {
            Arrays.fill(chars, '~');
        }
        return newBoard;
    }

    void printBoard(char[][] board) {
        for (String column : columns) {
            System.out.print(column + " ");
        }
        System.out.println();

        for (int i = 0; i < board.length; i++) {
            System.out.print(rows[i] + " ");
            System.out.println(Arrays.toString(board[i]).replaceAll("[\\[\\],]", ""));
        }
        System.out.println();
    }

    void printActiveGame(Player player) {
        Player opponent = player.playerNumber == 1 ? player2 : player1;
        printBoard(opponent.fogOfWar);
        System.out.printf("%s\n\n", "-".repeat(10));
        printBoard(player.gameBoard);
    }

    void placeShip(Player player, Ship ship) {
        Scanner sc = new Scanner(System.in);

        try {
            int[] coord1 = translateInput(sc.next());
            int[] coord2 = translateInput(sc.next());
            sc.nextLine();

            int dx = coord1[1] - coord2[1];
            int dy = coord1[0] - coord2[0];

            int length = Math.max(Math.abs(coord1[0] - coord2[0]), Math.abs(coord1[1] - coord2[1])) + 1;

            if (dx != 0 && dy != 0) {
                System.out.println("Error! Wrong ship location! Try again:");
                placeShip(player, ship);
            } else if (length != ship.getLength()) {
                System.out.printf("Error! Wrong length of the %s! Try again:\n", ship.getName());
                placeShip(player, ship);
            } else if (!checkValidPlacement(player, coord1, coord2)) {
                System.out.println("Error! Wrong ship location! Try again:");
                placeShip(player, ship);
            } else {

                int x = coord1[1];
                int y = coord1[0];

                while (player.gameBoard[coord2[0]][coord2[1]] != 'O') {
                    player.gameBoard[y][x] = 'O';
                    ship.addCoordinate(Arrays.asList(y, x));
                    if (dx == 0) {
                        y = dy < 0 ? ++y : --y;
                    } else {
                        x = dx < 0 ? ++x : --x;
                    }
                }
                System.out.println();
            }

        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Error: " + e + ". One or more given coordinates were out of bounds (A-J, 1-10)");
            placeShip(player, ship);
        }

    }

    void placeAllShips(Player player) {
        System.out.printf("Player %d, place your ships to the game field\n\n", player.playerNumber);
        printBoard(player.gameBoard);
        for (Ship ship : player.ships) {
            System.out.printf("Enter the coordinates of the %s (%d cells):\n\n", ship.getName(), ship.getLength());

            placeShip(player, ship);

            printBoard(player.gameBoard);
        }
    }

    boolean checkValidPlacement(Player player, int[] coord1, int[] coord2) {
        int x = coord1[1];
        int y = coord1[0];

        int dx = coord1[1] - coord2[1];
        int dy = coord1[0] - coord2[0];

        do {
            char[] checkedValues = {
                    player.gameBoard[y][x],
                    player.gameBoard[y][Math.max(x-1,0)],
                    player.gameBoard[y][Math.min(x+1, 9)],
                    player.gameBoard[Math.max(y-1,0)][x],
                    player.gameBoard[Math.min(y+1, 9)][x],
            };
            if (Arrays.toString(checkedValues).contains("O")) {
                return false;
            }
            if (dx == 0) {
                y = dy < 0 ? ++y : --y;
            } else {
                x = dx < 0 ? ++x : --x;
            }

        } while (y != coord2[0] || x != coord2[1]);
        return true;
    }

    int[] translateInput(String input) {
        return new int[] {
                Arrays.asList(rows).indexOf(input.substring(0,1)),
                Integer.parseInt(input.substring(1))-1
        };
    }

    void fire(Player player) {
        try {
            Player opponent = player.playerNumber == 1 ? player2 : player1;
            Scanner sc = new Scanner(System.in);
            int[] shotCoord = translateInput(sc.next());
            int row = shotCoord[0];
            int column = shotCoord[1];

            switch (opponent.gameBoard[row][column]) {
                case '~' -> {
                    opponent.fogOfWar[row][column] = 'M';
                    opponent.gameBoard[row][column] = 'M';
                    System.out.println("You missed!");
                }
                case 'O','X' -> {
                    opponent.fogOfWar[row][column] = 'X';
                    opponent.gameBoard[row][column] = 'X';

                    if (checkShipDestroyed(opponent)) {
                        if (checkGameWon(opponent)) {
                            System.out.println("You sank the last ship. You won. Congratulations!");
                            System.exit(0);
                        } else {
                            System.out.println("You sank a ship!");
                        }
                    } else {
                        System.out.println("You hit a ship!");
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("\nError! You entered the wrong coordinates! Try again:\n");
            fire(player);
        }
    }

    boolean checkShipDestroyed(Player player) {
        boolean shipDestroyedThisTurn = false;

        for (Ship ship : player.ships) {
            if (!ship.isDestroyed()) {
                StringBuilder sb = new StringBuilder();
                for (List<Integer> coordinate : ship.getCoordinates()) {
                    sb.append(player.fogOfWar[coordinate.get(0)][coordinate.get(1)]);
                }

                if (!sb.toString().contains("~")) {
                    ship.setDestroyed(true);
                    shipDestroyedThisTurn = true;
                }
            }
        }

        return shipDestroyedThisTurn;
    }

    boolean checkGameWon(Player player) {
        boolean isGameOver = true;
        for (Ship ship : player.ships) {
            if (!ship.isDestroyed()) {
                isGameOver = false;
                break;
            }
        }
        return isGameOver;
    }

    void gamePlayLoop() {
        Scanner sc = new Scanner(System.in);

        placeAllShips(player1);
        System.out.println("Press Enter and pass the move to another player");
        sc.nextLine();
        placeAllShips(player2);
        System.out.println("Press Enter and pass the move to another player");
        sc.nextLine();

        Player currentPlayer = player1;
        while (!checkGameWon(player1) || !checkGameWon(player2)) {
            printActiveGame(currentPlayer);
            System.out.printf("Player %d, it's your turn\n", currentPlayer.playerNumber);
            fire(currentPlayer);
            System.out.println("Press Enter and pass the move to another player");
            sc.nextLine();
            currentPlayer = currentPlayer.equals(player1) ? player2 : player1;
        }
    }
}
