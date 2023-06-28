// By: Adeshvir Dhillon
// Date: Jun 23-26, 2023

// Imports
import java.util.ArrayList; // To maintain a dynamic ArrayList of possible valid directions

/**
 * A class that extends the Board class to represent a direction board: a specialized board that determines and
 * maintains the possible valid directions of ship placement from a particular position. Primarily used when either the
 * player or the computer needs to place a ship.
 */
public class DirectionBoard extends Board {
    // Variables
    private ArrayList<String> possibleDirections; // Holds the valid directions for the current ship; dynamic because size varies from 1-4
    private static final String[] directions = {"D", "U", "R", "L"}; // Possible valid directions; "Down", "Up", "Right", "Left"

    // Constructors

    /**
     * --Default Constructor for DirectionBoard--
     * Creates a completely new board with InvalidPositions (5) instead of empty cells (0) and initializes
     * possibleDirections as an empty ArrayList. Calls super(); to generate the board (with empty cells), then calls
     * .generateDirectionBoard() to replace every empty cell in the board with an InvalidPosition.
     */
    public DirectionBoard() {
        super(); // Board's .generateBoard()
        this.generateDirectionBoard();
        possibleDirections = new ArrayList<>();
    }

    /**
     * --Copy Constructor for DirectionBoard--
     * Creates a new board that is a copy of the passed board and replaces every empty cell (0) with an
     * InvalidPosition (5), but doesn't replace any other type of piece, and initializes possibleDirections as an empty
     * ArrayList. Calls super(Board oldBoard); to generate the copy board, then calls .generateDirectionBoard() to
     * replace every empty cell in the board with an InvalidPosition, while leaving non-empty cells the same.
     *
     * @param oldBoard: the old board to be copied
     */
    public DirectionBoard(Board oldBoard) {
        super(oldBoard); // Board's .copyBoard(Board oldBoard)
        this.generateDirectionBoard();
        possibleDirections = new ArrayList<>();
    }

    // Methods

    /**
     * Generates a new board from the superclass' (Board's) board. Calls Board's .getBoard() method and replaces every
     * empty cell (0) in the returned board with an InvalidPosition (5), but leaves every non-empty cell the same.
     * (Note: this method is private so that it can only be called from inside the constructor, otherwise the board
     * risks getting reset).
     */
    private void generateDirectionBoard() {
        int[][] superBoard = this.getBoard();
        for (int i = 0; i < superBoard.length; i++) {
            for (int j = 0; j < superBoard[i].length; j++) {
                if (this.isEmpty(i, j)) {
                    this.addInvalidPosition(i, j);
                }
            }
        }
    }

    /**
     * Calculates and updates the valid directions to place a ship of the passed shipLength from the passed position.
     * Performs checks for all 4 directions (up, down, right, left) to determine if a ship (of the passed shipLength)
     * can be placed at that position. If the direction is valid, it changes all the positions in which the ship would
     * be placed if that direction were chosen to ValidPositions. Otherwise, it leaves the positions as
     * InvalidPositions. (Note: this method is similar to the Board class' .checkPositionValidity(int i, int j, int
     * shipLength) method, but where that method returns true after simply finding even a single valid direction, this
     * method checks all valid directions and also updates the valid positions).
     *
     * @param i:          indexI of the position
     * @param j:          indexJ of the position
     * @param shipLength: length of the ship being placed
     */
    public void updateValidPositionDirections(int i, int j, int shipLength) {
        if (i + shipLength <= 10) { // Down Check [i + k]
            for (int k = 1; k < shipLength; k++) { // Checks every position except the already placed passed position
                if ((k == shipLength - 1) && (!this.isShip(i + k, j))) { // If this is both the last position and not a ship
                    possibleDirections.add(DirectionBoard.directions[0]); // "D"
                    for (int l = 1; l < shipLength; l++) { // The same loop as the one using k
                        this.addValidPosition(i + l, j); // Update all the positions to ValidPositions
                    }
                } else if (this.isShip(i + k, j)) { // If this is not the last position, but a ship
                    break;
                }
            }
        }
        if (i - shipLength >= -1) { // Up Check [i - k]
            for (int k = 1; k < shipLength; k++) { // Checks every position except the already placed passed position
                if ((k == shipLength - 1) && (!this.isShip(i - k, j))) { // If this is both the last position and not a ship
                    possibleDirections.add(DirectionBoard.directions[1]); // "U"
                    for (int l = 1; l < shipLength; l++) { // The same loop as the one using k
                        this.addValidPosition(i - l, j); // Update all the positions to ValidPositions
                    }
                } else if (this.isShip(i - k, j)) { // If this is not the last position, but a ship
                    break;
                }
            }
        }
        if (j + shipLength <= 10) { // Right Check [j + k]
            for (int k = 1; k < shipLength; k++) { // Checks every position except the already placed passed position
                if ((k == shipLength - 1) && (!this.isShip(i, j + k))) { // If this is both the last position and not a ship
                    possibleDirections.add(DirectionBoard.directions[2]); // "R"
                    for (int l = 1; l < shipLength; l++) { // The same loop as the one using k
                        this.addValidPosition(i, j + l); // Update all the positions to ValidPositions
                    }
                } else if (this.isShip(i, j + k)) { // If this is not the last position, but a ship
                    break;
                }
            }
        }
        if (j - shipLength >= -1) { // Left Check [j - k]
            for (int k = 1; k < shipLength; k++) { // Checks every position except the already placed passed position
                if ((k == shipLength - 1) && (!this.isShip(i, j - k))) { // If this is both the last position and not a ship
                    possibleDirections.add(DirectionBoard.directions[3]); // "L"
                    for (int l = 1; l < shipLength; l++) { // The same loop as the one using k
                        this.addValidPosition(i, j - l); // Update all the positions to ValidPositions
                    }
                } else if (this.isShip(i, j - k)) { // If this is not the last position, but a ship
                    break;
                }
            }
        }
    }

    /**
     * Determines if the passed direction is a valid direction by comparing it to every valid direction string in the
     * static directions array. Returns true if the passed direction is a valid direction. Otherwise, returns false.
     * This method is case-insensitive, so the passed direction can be lower-case or upper-case.
     *
     * @param direction: the direction to be compared with the valid directions
     * @return boolean: true if the direction is a valid direction, false otherwise
     */
    public boolean validDirection(String direction) {
        for (String validDirection : DirectionBoard.directions) { // For every valid direction {"D", "U", "R", "L"}
            if (validDirection.equals(direction.toUpperCase())) { // .toUpperCase() to make it case-insensitive
                return true; // Valid direction
            }
        }
        return false; // Not a valid direction
    }

    /**
     * Checks and returns true if the passed direction is a possible direction, meaning that it is within the
     * possibleDirections ArrayList. Otherwise, returns false. This method is case-insensitive, so the passed direction
     * can be lower-case or upper-case.
     *
     * @param direction: the direction to be compared with the possible directions
     * @return boolean: true if the direction is a possible direction, false otherwise
     */
    public boolean possibleDirectionsContains(String direction) {
        return possibleDirections.contains(direction.toUpperCase()); // .toUpperCase() to make it case-insensitive
    }

    // [Getter Methods]

    /**
     * Creates and returns a String representation of the possibleDirections ArrayList. Will return "[None]" if the
     * possibleDirections ArrayList is of an invalid size (!= 1-4), which should not be possible and implies a bug in
     * the code.
     *
     * @return possibleDirectionsString: a String that is either "[None]" or the valid string representation of the
     * possibleDirections array
     */
    public String getPossibleDirectionsString() {
        String returnString = "[None]"; // Will return this if possibleDirections is not a valid size (1-4)

        if ((1 <= possibleDirections.size()) && (possibleDirections.size() <= 4)) { // If possibleDirections is a valid size (1-4)
            returnString = "[";
            for (int i = 0; i < possibleDirections.size(); i++) {
                returnString += possibleDirections.get(i);
                if (i < possibleDirections.size() - 1) { // If the direction is not the last possible direction
                    returnString += ", ";
                } else { // If the direction is the last possible direction
                    returnString += "]";
                }
            }
        }
        return returnString; // Return either "[None]" or the valid possibleDirections string
    }

    /**
     * Checks to see if the passed index is valid in the possibleDirections ArrayList and returns the direction at that
     * index if it is. Otherwise, returns null.
     *
     * @param index: the index of the direction in the possibleDirections ArrayList
     * @return direction: a String direction if the index is valid, or null if the index is invalid
     */
    public String getPossibleDirectionsElement(int index) {
        if ((0 <= index) && (index < possibleDirections.size())) {
            return possibleDirections.get(index);
        } else {
            return null;
        }
    }

    /**
     * @return the length (.size()) of the possibleDirections ArrayList
     */
    public int getPossibleDirectionsLength() {
        return possibleDirections.size();
    }

    /**
     * @return the possibleDirections ArrayList
     */
    public ArrayList<String> getPossibleDirections() {
        return possibleDirections;
    }
}