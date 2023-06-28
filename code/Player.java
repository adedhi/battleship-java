// By: Adeshvir Dhillon
// Date: Jun 20-26, 2023

// Imports
import java.util.Scanner; // For reading player input

/**
 * A class to represent the player and all their objects/variables. Holds the player's PlayerBoard, enemyBoard, and
 * playerMoveCounter. Allows the player to make a move. (Note: this class was made to collect all the player's
 * objects/variables in one class to allow for easier access and updating; it's used mainly in the GameManager class).
 * This class also extends the PlayerPosition interface, which ensures that it implements the
 * .getPlayerPosition() method.
 */
public class Player implements PlayerPosition {
    // Variables
    private PlayerBoard board; // A PlayerBoard object that represents the player's board
    private Board enemyBoard;// A Board object that represents what the computer sees of the player's board (where it
    // makes moves); holds hits and misses
    private int playerMoveCounter; // Holds the number of moves that the player has made, outputted to them at the end of the game (regardless of win or loss)

    // Constructors

    /**
     * --Default Constructor for Player--
     * Creates a new PlayerBoard (but doesn't generate it yet), an enemy board, and a player move counter.
     */
    public Player() {
        this.board = new PlayerBoard();
        this.enemyBoard = new Board();
        playerMoveCounter = 0;
    }

    // Methods

    /**
     * Generates the player's PlayerBoard (Note: made to allow the GameManager class to access the PlayerBoard's
     * .generatePlayerBoard() method).
     *
     * @see GameManager
     */
    public void generateBoard() {
        board.generatePlayerBoard();
    }

    /**
     * Prints the player's PlayerBoard (Note: made to allow the GameManager class to access the PlayerBoard's
     * .printBoard() method).
     *
     * @see GameManager
     */
    public void printBoard() {
        board.printBoard();
    }

    /**
     * Updates the ship position that has been hit by the computer move in the PlayerBoard's ships (Note: made to allow
     * the GameManager class to access the PlayerBoard's .updateHitShipPositions() method).
     *
     * @param computerMove: an int[] array of length 2 containing the two indices of the computer's move {i,j}
     * @see GameManager
     */
    public void updateHitShipPositions(int[] computerMove) {
        board.updateHitShipPositions(computerMove);
    }

    /**
     * Checks if all the player's ships in the PlayerBoard have been sunk. Returns true if they have, implying that
     * the game is over and the computer has won. Otherwise, returns false. (Note: made to allow the GameManager class
     * to access the PlayerBoard's .checkWin() method).
     *
     * @return boolean: true if the computer has won, false otherwise
     * @see GameManager
     */
    public boolean checkWin() {
        return board.checkWin();
    }

    /**
     * Prints the player's enemy board and allows them to make a move. Receives player input for their choice of
     * position, then outputs whether their move was a hit or a miss. Updates the player's enemy board and prints it.
     * Returns the player's move so that the computer's board can be updated. (Note: this method requires the computer's
     * board, which contains their ships, to determine if a move was a hit or a miss, however the passed board will
     * always be a copy of the computer's board, created in the GameManager class using the Board class' .getBoardCopy()
     * method, to ensure that the computer's board is not changed).
     *
     * @param computerBoard: a copy of the computer's board, used to determine if the move made was a hit or a miss
     * @return playerMove: an int[] array of length 2 containing the two indices of the player's move {i,j}
     */
    public int[] makeMove(Board computerBoard) {
        int[] playerMove; // Player's move, to be returned regardless of hit or miss

        System.out.println();
        System.out.println("Enemy Board: "); // Print the player's enemy board
        enemyBoard.printBoard();

        playerMove = getPlayerPosition(); // Get the player's choice of position
        this.incrementPlayerMovesCounter(); // Increment the player's move counter (because they have made one move)

        System.out.println();
        this.checkPlayerMove(playerMove, computerBoard); // Checks and outputs whether the player move was a hit or a miss and updates the player's enemy board

        System.out.println();
        System.out.println("Enemy Board: "); // Print the updated player's enemy board
        enemyBoard.printBoard();

        return playerMove; // Return the player move to the calling function (Note: this method is primarily called in the class GameManager, and the returned move is used to update the computer's board)
    }

    /**
     * Receives player input for their choice of position, parses it into the corresponding indices and returns it. If
     * the inputted position is invalid, it keeps on looping until a valid position is entered. This method's
     * implementation is required by the PlayerPosition interface.
     *
     * @return playerPosition: an int[] array of length 2 that contains the indices of the player's position {i,j}
     * @see PlayerPosition
     */
    public int[] getPlayerPosition() {
        String playerInput; // The player's string input
        int[] playerPosition; // The player's choice of position
        Scanner playerScanner = new Scanner(System.in); // Scanner to read the player's input

        while (true) { // Loops until a valid position is received
            try {
                System.out.println();
                System.out.print("Please make a move [Letter][Number]: ");
                if (playerScanner.hasNextLine()) { // If the player inputted something
                    playerInput = playerScanner.nextLine(); // Receive the input
                    playerPosition = Board.positionStringToIndices(playerInput); // Parse the position string to the corresponding indices
                    if ((playerPosition[0] != -1) && (playerPosition[1] != -1)) { // If the position was valid
                        if ((enemyBoard.isHit(playerPosition[0], playerPosition[1])) || enemyBoard.isMiss(playerPosition[0], playerPosition[1])) { // Occupied Position
                            System.out.println("That move was already made, please try again");
                            continue;
                        } else { // Valid Position
                            return playerPosition; // Return the valid position
                        }
                    }
                }
                System.out.println("That input is invalid, please try again"); // If, at any point, a condition was not met; continue is unnecessary as this is the last line in the loop
            } catch (Exception e) {
                System.out.println("That input is invalid, please try again"); // If, at any point, an exception was raised; continue is unnecessary as this is the last line in the loop
            }
        }
    }

    /**
     * Determines if the passed player move was a hit or a miss by checking the same position in the computer's board,
     * then outputs the state of the move using the Colours class and the position string representation of the
     * player's move indices. Also updates the player's enemy board.
     *
     * @param playerMove:    an int[] array of length 2 containing the indices of the player's move {i,j}
     * @param computerBoard: the computer's board, used to determine if the player's move was a hit (ship) or a miss (no ship)
     */
    private void checkPlayerMove(int[] playerMove, Board computerBoard) {
        String playerMoveString = Board.indicesToPositionString(playerMove); // Parse the player move indices into their position string representation (format [Letter][Number])

        if (computerBoard.isShip(playerMove[0], playerMove[1])) { // If the move was a hit
            System.out.println("Player Move [" + Colours.getYellow() + playerMoveString + Colours.getReset() + "] was a " + Colours.getRedBoldBright() + "HIT" + Colours.getReset()); // Output the state of the move
            enemyBoard.addHit(playerMove[0], playerMove[1]); // Update the player's enemyBoard with a hit
        } else { // If the move was a miss
            System.out.println("Player Move [" + Colours.getYellow() + playerMoveString + Colours.getReset() + "] was a " + Colours.getCyan() + "MISS" + Colours.getReset()); // Output the state of the move
            enemyBoard.addMiss(playerMove[0], playerMove[1]); // Update the player's enemyBoard with a hit
        }
    }

    // [Setter Methods]

    /**
     * Adds a hit on the player's board at the position of the passed computer's move. (Note: made to allow the
     * GameManager class to access the PlayerBoard's .addHit() method).
     *
     * @param computerMove: an int[] array containing the 2 indices of the computer's move {i,j}
     * @see GameManager
     */
    public void addHit(int[] computerMove) {
        board.addHit(computerMove[0], computerMove[1]);
    }

    /**
     * Adds a miss on the player's board at the position of the passed computer's move. (Note: made to allow the
     * GameManager class to access the PlayerBoard's .addMiss() method).
     *
     * @param computerMove: an int[] array containing the 2 indices of the computer's move {i,j}
     * @see GameManager
     */
    public void addMiss(int[] computerMove) {
        board.addMiss(computerMove[0], computerMove[1]);
    }

    /**
     * Increments the player move counter by 1.
     */
    public void incrementPlayerMovesCounter() {
        playerMoveCounter++;
    }

    // [Getter Methods]

    /**
     * @return the player move counter
     */
    public int getPlayerMoveCounter() {
        return playerMoveCounter;
    }

    /**
     * @return the player's board
     */
    public PlayerBoard getBoard() {
        return board;
    }

    /**
     * @return a copy of the player's board
     */
    public Board getBoardCopy() {
        return board.getBoardCopy();
    }

    /**
     * @return the player's board's ships
     */
    public Ship[] getPlayerShips() {
        return board.getShips();
    }
}