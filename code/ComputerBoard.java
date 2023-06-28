// By: Adeshvir Dhillon
// Date: Jun 23-26, 2023

//Imports
import java.util.Random; // To simulate a random number generator (mainly uses .nextInt() method)

/**
 * A class that extends the Board class to represent the computer's board, a specialized board unique to the computer
 * that holds and represents where their ships are placed. This class both generates the computer's board and ships, and
 * also updates and returns them when needed.
 */
public class ComputerBoard extends Board {
    // Variables
    private DirectionBoard directionBoard; // Used to calculate and represent valid directions for when the computer
    // board is being generated in .generateComputerBoard(); a copy of the main
    // board, where each empty cell is replaced with an InvalidPosition
    private Ship[] ships; // An array of Ship objects, holds all 5 of the computer's ships
    private final Random randomNumberGenerator; // A random number generator; because the computer must follow an algorithm
    // to make decisions, a random number generator makes the game fair and dynamic
    // by generating random numbers for the indices and directions of ships

    // Constructors

    /**
     * --Default Constructor for ComputerBoard--
     * Creates a completely new and empty board using Board's constructor, a new DirectionBoard that is a copy of the
     * previous board and is used to represent valid directions, a new Ship[] array to hold the board's 5 ships and
     * their lengths {Carrier [5], Battleship [4], Destroyer [3], Submarine [3], Patrol Board [2]}, and a Random object
     * to serve as a random number generator (using .nextInt()) for the ComputerBoard's methods. (Note: unlike other
     * Board object constructors, this constructor doesn't directly call .generateComputerBoard(), because in this
     * class, that method is much more complicated).
     */
    public ComputerBoard() {
        super(); // Uses Board's constructor (board is not yet entirely generated)
        directionBoard = new DirectionBoard(this);
        this.ships = new Ship[5];
        this.ships[0] = new Ship("Carrier", 5);
        this.ships[1] = new Ship("Battleship", 4);
        this.ships[2] = new Ship("Destroyer", 3);
        this.ships[3] = new Ship("Submarine", 3);
        this.ships[4] = new Ship("Patrol Boat", 2);
        randomNumberGenerator = new Random();
    }

    // Methods

    /**
     * Generates the computer's board by randomly placing each of the 5 ships. Extensively uses the random number
     * generator to choose the indices of the first positions and the directions of each ship.
     */
    public void generateComputerBoard() {
        int computerIndexI; // The indexI of the computer's position
        int computerIndexJ; // The indexJ of the computer's position
        int shipLength; // The length of the current ship
        int[][] shipPositions; // The positions of the current ship
        int computerDirectionIndex; // Holds a random value that corresponds to an index in possibleDirections; used to randomly choose a ship direction
        String computerDirection; // Holds the direction string itself

        for (Ship ship : ships) { // For every ship [5] in the Ship[] array
            this.resetDirectionBoard(); // Set directionBoard to a new DirectionBoard that is a copy of the current main board
            shipLength = ship.getLength();

            do { // Loops until a valid position is found (Note: if there are no valid positions, the game is already over)
                computerIndexI = randomNumberGenerator.nextInt(0, 10); // Random indexI
                computerIndexJ = randomNumberGenerator.nextInt(0, 10); // Random indexJ
            }
            while (!this.checkPositionValidity(computerIndexI, computerIndexJ, shipLength)); // While the randomly generated position is still invalid

            directionBoard.addShip(computerIndexI, computerIndexJ); // Adds the valid position, which is the first position of the ship, to the directionBoard
            directionBoard.updateValidPositionDirections(computerIndexI, computerIndexJ, shipLength); // Calculate and represent the possible valid directions using the valid position
            computerDirectionIndex = randomNumberGenerator.nextInt(0, directionBoard.getPossibleDirectionsLength()); // Randomly chose a possible valid direction
            computerDirection = directionBoard.getPossibleDirectionsElement(computerDirectionIndex); // Get the string representation of the randomly chosen direction (required for Board's .placeShipOnBoard() method)
            shipPositions = this.placeShipOnBoard(computerIndexI, computerIndexJ, computerDirection, shipLength); // Place the ship on the main board
            ship.addPositions(shipPositions); // Add the ship's positions to the current Ship object
        }
    }

    /**
     * Resets the directionBoard back to its original state by setting it to a new DirectionBoard that is a copy
     * of the current main board.
     */
    public void resetDirectionBoard() {
        directionBoard = new DirectionBoard(this);
    }

    /**
     * Receives the player's move, which has been determined to be a hit, and updates the corresponding position in one
     * of the ships in the computer board's array of ships. (Note: this method is only called in the Computer class, in
     * which an instance of ComputerBoard exists, because, although player/computer moves are made in the Player/Computer
     * classes respectively, their ships are stored within their Board classes (PlayerBoard/ComputerBoard), so there is
     * a need to access the board classes).
     *
     * @param playerMove: an int[] array of length 2 that contains the two indices of the player's move {i,j}
     */
    public void updateHitShipPositions(int[] playerMove) {
        for (Ship ship : ships) { // For every ship in the Ship[] array; there will only be one position, in one ship,
            // that is in the same position as the player move
            ship.updateHitPositions(playerMove); // If the player move's position is in the current ship's positions,
            // it will update the position to {-1,-1} to simulate removal,
            // otherwise it does nothing
        }
    }

    /**
     * Iterates through every ship in the Ship[] array and checks if they are sunk (all positions are {-1,-1}/removed).
     * If there is even one non-sunk ship, returns false. Otherwise, returns true.
     *
     * @return boolean: true if all ships are sunk, false if even one ship is not yet sunk
     */
    public boolean checkWin() {
        for (Ship ship : ships) { // For every ship [5] in the Ship[] array
            if (!ship.isSunk()) { // If the ship is not yet sunk
                return false; // No one has won -> game is not over
            }
        }
        return true; // All the computer's ships are sunk -> game is over
    }

    // [Getter Methods]

    /**
     * @return the ComputerBoard's direction board
     */
    public DirectionBoard getDirectionBoard() {
        return directionBoard;
    }

    /**
     * @return the ComputerBoard's ship array
     */
    public Ship[] getShips() {
        return ships;
    }
}