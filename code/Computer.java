// By: Adeshvir Dhillon
// Date: Jun 23-26, 2023

// Imports
import java.util.Random; // To simulate a random number generator (mainly uses .nextInt() method)
import java.util.ArrayList; // To maintain a dynamic ArrayList of possible valid moves

/**
 * A class to represent the computer and all its objects/variables. Holds the computer's ComputerBoard, enemyBoard,
 * random number generator, and possibleMoves. Allows the computer to make a move. (Note: this class was made to collect
 * all the computer's objects/variables in one class to allow for easier access and updating; it's used mainly in the
 * GameManager class).
 */
public class Computer {
    // Variables
    private ComputerBoard board; // A ComputerBoard object that represents the computer's board
    private Board enemyBoard; // A Board object that represents what the computer sees of the player's board (where it
    // makes moves); holds hits and misses
    private Random randomNumberGenerator; // A random number generator; because the computer must follow an algorithm
    // to make decisions, a random number generator makes the game fair and dynamic
    // by generating random numbers for the indices of moves
    private ArrayList<int[]> possibleMoves; // An ArrayList of possible moves (each an int[] array of length 2, containing
    // two indices {i,j}) that remain to be made; the number of possible moves is
    // dynamic (0-4)

    // Constructor

    /**
     * --Default Constructor for Computer--
     * Creates a new ComputerBoard (but doesn't generate it yet), an enemy board, a Random object to serve as a random
     * number generator (using .nextInt()) for the ComputerBoard's methods, and a possible moves ArrayList.
     */
    public Computer() {
        board = new ComputerBoard();
        enemyBoard = new Board();
        randomNumberGenerator = new Random();
        possibleMoves = new ArrayList<>();
    }

    // Methods

    /**
     * Generates the computer's ComputerBoard (Note: made to allow the GameManager class to access the ComputerBoard's
     * .generateComputerBoard() method).
     *
     * @see GameManager
     */
    public void generateBoard() {
        board.generateComputerBoard();
    }

    /**
     * Prints the computer's ComputerBoard (Note: made to allow the GameManager class to access the ComputerBoard's
     * .printBoard() method).
     *
     * @see GameManager
     */
    public void printBoard() {
        board.printBoard();
    }

    /**
     * Updates the ship position that has been hit by the player move in the ComputerBoard's ships (Note: made to allow
     * the GameManager class to access the ComputerBoard's .updateHitShipPositions() method).
     *
     * @param playerMove: an int[] array of length 2 containing the two indices of the player's move {i,j}
     * @see GameManager
     */
    public void updateHitShipPositions(int[] playerMove) {
        board.updateHitShipPositions(playerMove);
    }

    /**
     * Checks if all the computer's ships in the ComputerBoard have been sunk. Returns true if they have, implying that
     * the game is over and the player has won. Otherwise, returns false. (Note: made to allow the GameManager class
     * to access the ComputerBoard's .checkWin() method).
     *
     * @return boolean: true if the player has won, false otherwise
     * @see GameManager
     */
    public boolean checkWin() {
        return board.checkWin();
    }

    /**
     * Generates a computer move by randomly picking a position on the computer's enemy board. If the move is a hit, the
     * method will generate an ArrayList of possible moves, from which the computer will keep on randomly choosing moves
     * until the ArrayList is depleted (if, at any point, one of the possible moves is a hit as well, the method will
     * recalculate the ArrayList of possible moves using the position of that possible move). Otherwise, if the hit is a
     * miss, the computer will either continue to randomly choose moves from the ArrayList of possible moves or, if
     * there are no more possible moves, start randomly picking positions again. (Note: this method requires the
     * player's board, which contains their ships, to determine if a move was a hit or a miss, however the passed board
     * will always be a copy of the player's board, created in the GameManager class using the Board class'
     * .getBoardCopy() method, to ensure that the player's board is not changed).
     *
     * @param playerBoard: a copy of the player's board, used to determine if the move made was a hit or a miss
     * @return computerMove: an int[] array of length 2 containing the two indices of the computer's move {i,j}
     */
    public int[] makeMove(Board playerBoard) {
        int[] computerMove = new int[2]; // Computer's move, to be returned regardless of hit or miss
        int computerIndexI; // The indexI of the computer's move
        int computerIndexJ; // the indexJ of the computer's move
        int possibleMovesIndex; // Randomly chosen index of a move in the possible moves ArrayList, if it is not empty

        if (possibleMoves.size() == 0) { // If there are no possible moves; possibleMoves ArrayList is empty
            do { // Loops until a valid position (not a hit nor a miss) is found
                computerIndexI = randomNumberGenerator.nextInt(0, 10); // Random indexI
                computerIndexJ = randomNumberGenerator.nextInt(0, 10); // Random indexJ
            }
            while ((enemyBoard.isHit(computerIndexI, computerIndexJ)) || (enemyBoard.isMiss(computerIndexI, computerIndexJ))); // While the position is a hit or a miss

            computerMove[0] = computerIndexI; // Update the indexI of the computerMove
            computerMove[1] = computerIndexJ; // Update the indexJ of the computerMove
        } else { // If there are possible moves; possibleMoves ArrayList is not empty
            possibleMovesIndex = randomNumberGenerator.nextInt(0, this.getPossibleMovesLength()); // Randomly choose a possible move index
            computerMove = this.getPossibleMovesElement(possibleMovesIndex); // Get the randomly chosen possible move using the index
            this.removePossibleMovesElement(possibleMovesIndex); // Remove the randomly chosen move from the Arraylist of possible moves (it cannot be chosen more than once)
        }
        this.checkComputerMove(computerMove, playerBoard); // Checks and outputs whether the computer move was a hit or a miss and updates the computer's enemy board
        return computerMove; // Return the computer move to the calling function (Note: this method is primarily called in the class GameManager, and the returned move is used to update the player's board)
    }

    /**
     * Determines if the passed computer move was a hit or a miss by checking the same position in the player's board,
     * then outputs the state of the move using the Colours class and the position string representation of the
     * computer's move indices. In the case of a hit, it calls the .updatePossibleMoves() method to recalculate and
     * update the ArrayList of possible moves. Also updates the computer's enemy board.
     *
     * @param computerMove: an int[] array of length 2 containing the indices of the computer's move {i,j}
     * @param playerBoard:  the player's board, used to determine if the computer's move was a hit (ship) or a miss (no ship)
     */
    private void checkComputerMove(int[] computerMove, Board playerBoard) {
        String computerMoveString = Board.indicesToPositionString(computerMove); // Parse the computer move indices into their position string representation (format [Letter][Number])

        System.out.println();
        if (playerBoard.isShip(computerMove[0], computerMove[1])) { // Hit (computer move position in the player's board was a ship, thus it hit the ship)
            System.out.println("Computer Move [" + Colours.getYellow() + computerMoveString + Colours.getReset() + "] was a " + Colours.getRedBoldBright() + "HIT" + Colours.getReset()); // Output the state of the move
            enemyBoard.addHit(computerMove[0], computerMove[1]); // Update the computer's enemyBoard with a hit
            this.updatePossibleMoves(computerMove[0], computerMove[1]); // Because the computer made a hit, recalculate and update the ArrayList of possible moves
        } else { // Miss (computer move position in the player's board was not a ship)
            System.out.println("Computer Move [" + Colours.getYellow() + computerMoveString + Colours.getReset() + "] was a " + Colours.getCyan() + "MISS" + Colours.getReset()); // Output the state of the move
            enemyBoard.addMiss(computerMove[0], computerMove[1]); // Update the computer's enemyBoard with a miss
        }
    }

    /**
     * Calculates and updates the ArrayList of possible moves using the passed computer move indices. Performs 4 checks
     * (down, up, right, left) to see if the next immediate position in each direction is valid. Appends any valid
     * positions to the ArrayList of possible moves. (Note: this method is private because it requires that the passed
     * move be a hit, thus it is safer to only have it be called from inside the .checkComputerMove() method that does
     * just that).
     *
     * @param i: indexI of the computer's move
     * @param j: indexJ of the computer's move
     */
    private void updatePossibleMoves(int i, int j) {
        this.resetPossibleMoves();

        if (i < 9) { // Down Check
            if (enemyBoard.isEmpty(i + 1, j)) {
                possibleMoves.add(new int[]{i + 1, j});
            }
        }
        if (i > 0) { // Up Check
            if (enemyBoard.isEmpty(i - 1, j)) {
                possibleMoves.add(new int[]{i - 1, j});
            }
        }
        if (j < 9) { // Right Check
            if (enemyBoard.isEmpty(i, j + 1)) {
                possibleMoves.add(new int[]{i, j + 1});
            }
        }
        if (j > 0) { // Left Check
            if (enemyBoard.isEmpty(i, j - 1)) {
                possibleMoves.add(new int[]{i, j - 1});
            }
        }
    }

    /**
     * Checks to see if the passed index is valid for the length (size) of the ArrayList of possible moves, then
     * removes the element at that index if the index is valid. Otherwise, doesn't remove anything.
     *
     * @param index: the index of the element to be removed in the ArrayList of possible moves
     */
    public void removePossibleMovesElement(int index) {
        if ((0 <= index) && (index < possibleMoves.size())) { // If the index is contained within the ArrayList of possible moves
            possibleMoves.remove(index); // Remove the element at the index
        }
    }

    /**
     * Resets the ArrayList of possible moves to a new, empty ArrayList
     */
    public void resetPossibleMoves() {
        possibleMoves = new ArrayList<>();
    }

    // [Setter Methods]

    /**
     * Adds a hit on the computer's board at the position of the passed player's move. (Note: made to allow the
     * GameManager class to access the ComputerBoard's .addHit() method).
     *
     * @param playerMove: an int[] array containing the 2 indices of the player's move {i,j}
     * @see GameManager
     */
    public void addHit(int[] playerMove) {
        board.addHit(playerMove[0], playerMove[1]);
    }

    /**
     * Adds a miss on the computer's board at the position of the passed player's move. (Note: made to allow the
     * GameManager class to access the ComputerBoard's .addMiss() method).
     *
     * @param playerMove: an int[] array containing the 2 indices of the player's move {i,j}
     * @see GameManager
     */
    public void addMiss(int[] playerMove) {
        board.addMiss(playerMove[0], playerMove[1]);
    }

    // [Getter Methods]

    /**
     * Checks to see if the passed index is valid for the length (size) of the ArrayList of possible moves, then returns
     * the element at that index if the index is valid. Otherwise, returns null.
     *
     * @param index: the index of the element to be removed in the ArrayList of possible moves
     * @return element: the element in possibleMoves at the passed index if the index is valid / null otherwise
     */
    public int[] getPossibleMovesElement(int index) {
        if ((0 <= index) && (index < possibleMoves.size())) { // If the index is contained within the ArrayList of possible moves
            return possibleMoves.get(index); // Return the element at that index
        } else { // Index is not contained within the ArrayList of possible moves
            return null;
        }
    }

    /**
     * @return the computer's board
     */
    public ComputerBoard getBoard() {
        return board;
    }

    /**
     * @return a copy of the computer's board
     */
    public Board getBoardCopy() {
        return board.getBoardCopy();
    }

    /**
     * @return the computer's board's ships
     */
    public Ship[] getShips() {
        return board.getShips();
    }

    /**
     * @return the length of the ArrayList of possible moves
     */
    public int getPossibleMovesLength() {
        return possibleMoves.size();
    }
}