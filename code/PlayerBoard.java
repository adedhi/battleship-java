// By: Adeshvir Dhillon
// Date: Jun 20-26, 2023

// Imports
import java.util.Scanner; // For reading player input

/**
 * A class that extends the Board class to represent the player's board, a specialized board unique to the player that
 * holds and represents where their ships are placed. This class both generates the player's board and ships, and also
 * updates and returns them when needed. This class also extends the PlayerPosition interface, which ensures that it
 * implements the .getPlayerPosition() method.
 */
public class PlayerBoard extends Board implements PlayerPosition {
    // Variables
    private Board positionBoard; // Used to generate the player's board by showing the player the valid and invalid
    // positions, represented by ValidPositions and InvalidPositions respectively; it is a
    // copy of the player's board
    private DirectionBoard directionBoard; // Used to generate the player's board by showing the player the valid and
    // invalid directions, represented by ValidPositions and InvalidPositions
    // respectively; it is a copy of the player's board, but it replaces empty
    // cells with InvalidPositions
    private Ship[] ships; // An array of Ship objects, holds all 5 of the player's ships

    // Constructors

    /**
     * --Default Constructor for PlayerBoard--
     * Creates a completely new and empty board using Board's constructor, a new position board that is a copy of the
     * player's board and is used to represent valid positions, a new DirectionBoard that is a copy of the player's
     * board and is used to represent valid directions, and a new Ship[] array to hold the board's 5 ships and their
     * lengths {Carrier [5], Battleship [4], Destroyer [3], Submarine [3], Patrol Board [2]}, (Note: unlike other Board
     * object constructors, this constructor doesn't directly call .generatePlayerBoard(), because in this class, that
     * method is much more complicated).
     */
    public PlayerBoard() {
        super(); // Uses Board's constructor (board is not yet entirely generated)
        positionBoard = new Board(this);
        directionBoard = new DirectionBoard(this);
        this.ships = new Ship[5];
        this.ships[0] = new Ship("Carrier", 5);
        this.ships[1] = new Ship("Battleship", 4);
        this.ships[2] = new Ship("Destroyer", 3);
        this.ships[3] = new Ship("Submarine", 3);
        this.ships[4] = new Ship("Patrol Boat", 2);
    }

    // Methods

    /**
     * Generates the player's board by receiving and parsing user input. Iterates for every ship in the Ship[] array.
     * First, determines all valid positions on the board and allows the player to choose the first position. Then,
     * determines all valid directions using the player's choice of position and allows the player to choose the
     * direction. Finally, places the ship onto the board and updates the ship object's position array.
     */
    public void generatePlayerBoard() {
        String shipName; // Name of the current ship
        int shipLength; // Length of the current ship; determines how many positions the player must input and which positions are valid
        int[][] shipPositions; // Holds the indices of every position of the current ship; inner list length is always 2 (2 indices), outer list length varies with ship length
        int[] playerPosition; // An int[] array of length 2 that holds the indices of the player's chosen position {i,j}
        int playerIndexI; // The indexI of the player's position
        int playerIndexJ; // The indexJ of the player's position
        String playerDirection; // The string representation of the player's chosen direction

        for (Ship ship : ships) { // For every ship [5] in the Ship[] array
            this.resetPositionBoard(); // Set positionBoard to a new Board that is a copy of the current main board
            this.resetDirectionBoard(); // Set directionBoard to a new DirectionBoard that is a copy of the current main board
            shipName = ship.getName(); // Get the name of the ship
            shipLength = ship.getLength(); // Get the length of the ship

            System.out.println(); // Output the name and length of the ship, using the Colours class to get coloured text
            System.out.println("Placing the " + Colours.getPurple() + shipName + Colours.getReset() + " [length: " + Colours.getCyan() + shipLength + Colours.getReset() + "]");

            for (int i = 0; i < positionBoard.getLength(); i++) { // For every row in the position board
                for (int j = 0; j < positionBoard.getLength(); j++) { // For every position in the position board
                    if (positionBoard.checkPositionValidity(i, j, shipLength)) { // If the position is valid
                        positionBoard.addValidPosition(i, j); // Represent the position as a ValidPosition
                    } else {
                        if (!positionBoard.isShip(i, j)) { // If the position is not a ship
                            positionBoard.addInvalidPosition(i, j); // Represent the position as an InvalidPosition
                        }
                    }
                }
            }

            System.out.println();
            positionBoard.printBoard(); // Print the positionBoard with the possible valid positions for the player to choose
            playerPosition = this.getPlayerPosition(); // Get the player's choice of position
            playerIndexI = playerPosition[0]; // The indexI of the player's choice of position
            playerIndexJ = playerPosition[1]; // The indexJ of the player's choice of position
            positionBoard.addShip(playerIndexI, playerIndexJ); // Adds the ship to this object's position board
            directionBoard.addShip(playerIndexI, playerIndexJ); // Adds the ship to this object's direction board

            directionBoard.updateValidPositionDirections(playerIndexI, playerIndexJ, shipLength); // Calculate and update the valid directions based on the player's choice of position and ship length
            System.out.println();
            directionBoard.printBoard(); // Print the directionBoard with the possible valid directions for the player to choose
            playerDirection = this.getPlayerDirection(); // Get the player's choice of direction
            shipPositions = this.placeShipOnBoard(playerIndexI, playerIndexJ, playerDirection, shipLength); // Place the ship on the main board and receive the ship positions
            ship.addPositions(shipPositions); // Add the received ship positions to the current ship object's positions array
        }
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
                System.out.print("Please choose a position [Letter][Number]: ");
                if (playerScanner.hasNextLine()) { // If the player inputted something
                    playerInput = playerScanner.nextLine(); // Receive the input
                    playerPosition = Board.positionStringToIndices(playerInput); // Parse the position string to the corresponding indices
                    if ((playerPosition[0] != -1) && (playerPosition[1] != -1)) { // If the position was valid
                        if (positionBoard.isShip(playerPosition[0], playerPosition[1])) { // Occupied Position
                            System.out.println("That position is already occupied, please try again");
                            continue;
                        } else if (positionBoard.isInvalidPosition(playerPosition[0], playerPosition[1])) { // Invalid Position
                            System.out.println("That position is invalid, please try again");
                            continue;
                        } else if (positionBoard.isValidPosition(playerPosition[0], playerPosition[1])) { // Valid Position
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
     * Receives player input for their choice of direction and returns it. If the inputted direction is invalid, it
     * keeps on looping until a valid position is entered.
     *
     * @return playerDirection: a valid direction string that is either "D", "U", "R", or "L"
     */
    public String getPlayerDirection() {
        String playerDirection; // The player's string input for the direction
        Scanner playerScanner = new Scanner(System.in); // Scanner to read the player's input

        while (true) { // Loops until a valid direction is received
            try {
                System.out.println();
                System.out.println("Possible directions: " + directionBoard.getPossibleDirectionsString()); // Print out the possible directions to the player
                System.out.print("Please choose a direction [Letter]: ");
                if (playerScanner.hasNextLine()) { // If the player inputted something
                    playerDirection = playerScanner.nextLine(); // Receive the input
                    if (directionBoard.possibleDirectionsContains(playerDirection)) { // If the player inputted direction is in the possible directions ArrayList, and is thus valid
                        return playerDirection.toUpperCase(); // Return the valid direction
                    } else { // If the direction is not in the possible directions ArrayList
                        if (directionBoard.validDirection(playerDirection)) { // If the direction would otherwise be valid ("D", "U", "R", "L"), but is not in the possible directions for the current position
                            System.out.println("That direction is not available, please try again");
                        } else { // If the direction is simply invalid
                            System.out.println("That input is invalid, please try again");
                        }
                        continue;
                    }
                }
                System.out.println("That input is invalid, please try again"); // If, at any point, a condition was not met; continue is unnecessary as this is the last element in the loop
            } catch (Exception e) {
                System.out.println("That input is invalid, please try again"); // If, at any point, an exception was raised; continue is unnecessary as this is the last element in the loop
            }
        }
    }

    /**
     * Resets the positionBoard back to its original state by setting it to a new Board that is a copy
     * of the current main board.
     */
    public void resetPositionBoard() {
        positionBoard = new Board(this);
    }

    /**
     * Resets the directionBoard back to its original state by setting it to a new DirectionBoard that is a copy
     * of the current main board.
     */
    public void resetDirectionBoard() {
        directionBoard = new DirectionBoard(this);
    }

    /**
     * Receives the computer's move, which has been determined to be a hit, and updates the corresponding position in
     * one of the ships in the player's board's array of ships. (Note: this method is only called in the Player class,
     * in which an instance of PlayerBoard exists, because, although player/computer moves are made in the
     * Player/Computer classes respectively, their ships are stored within their Board classes
     * (PlayerBoard/ComputerBoard), so there is a need to access the board classes).
     *
     * @param computerMove: an int[] array of length 2 that contains the two indices of the computer's move {i,j}
     */
    public void updateHitShipPositions(int[] computerMove) {
        for (Ship ship : ships) { // For every ship [5] in the Ship[] array; there will be only one position, in one ship,
            // that is in the same position as the computer move
            ship.updateHitPositions(computerMove); // If the computer's move position is in the current ship's positions
            // it will update the position to {-1,-1} to simulate removal,
            // otherwise, it does nothing.
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
        return true; // All the player's ships are sunk -> game is over
    }

    // [Getter Methods]

    /**
     * @return the PlayerBoard's position board
     */
    public Board getPositionBoard() {
        return positionBoard;
    }

    /**
     * @return the PlayerBoard's direction board
     */
    public Board getDirectionBoard() {
        return directionBoard;
    }

    /**
     * @return the PlayerBoard's ship array
     */
    public Ship[] getShips() {
        return ships;
    }
}