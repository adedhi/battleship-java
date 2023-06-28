// By: Adeshvir Dhillon
// Date: Jun 20-26, 2023

/**
 * A class that represents a 10x10 playing board. Holds the board's positions (in an int[10][10] nested array) and
 * length. Also contains static variables that represent the board's letter headers (like a real Battleship board)
 * and the two types of board pieces (□ [Ships, ValidPositions, InvalidPositions] and x [Hit, Miss]). Contains methods
 * that allow for generating and printing the board, as well as checking position states, adding pieces/ships, and
 * static methods to parse Position <-> Indices. (Note: used and extended extensively throughout the Battleship program,
 * thus it is an extremely important class).
 */
public class Board {
    // Variables
    private int[][] board; // A nested int[][] array representing the board and holding its positions and their states
    // Note: although traditional battleship positions are [Letter][Number], this board stores
    //       positions as [Number][Letter] (much easier to print), so extra care must be taken
    //       to parse positions <-> indices
    private final int length = 10; // The board's length; which, regardless of whether the outer or inner loop is being
    // considered, is always 10
    // Note: this could have been a static variable with a static getter, but I found it made
    //       calls to .getLength() confusing because they were called on the class Board
    //       instead of the current Board object
    private final static char[] letterHeaders = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'}; // A char[] array containing the board's letter headers
    private final static String boardPiece = "□"; // Piece representing Ships (Purple), ValidPositions (Green), and Invalid Positions (Red)
    private final static String hitPiece = "x"; // Piece representing Hits (Bolded and Bright Red) and Misses (Cyan)

    // Constructors

    /**
     * --Default Constructor for Board--
     * Creates a completely new and empty board.
     */
    public Board() { // New Board
        this.generateBoard();
    }

    /**
     * --Copy Constructor for Board--
     * Creates a new board that is a copy of the passed board.
     *
     * @param oldBoard: the old board to be copied
     */
    public Board(Board oldBoard) { // Copy Board
        this.copyBoard(oldBoard);
    }

    // Methods

    /**
     * Sets the Board's board variable to an int[10][10] array, with each position set to 0 (empty). Used in Board's
     * default constructor. (Note: this method is private so that it can only be called from inside the constructor,
     * otherwise the board risks getting reset).
     */
    private void generateBoard() {
        board = new int[10][10];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = 0;
            }
        }
    }

    /**
     * Sets the Board's board variable to an int[10][10] array, with each position set to the passed old board's state
     * for the same position. Used in Board's copy constructor. (Note: this method is private so that it can only be
     * called from inside the constructor, otherwise the board risks getting reset).
     *
     * @param oldBoard: the old board to be copied
     */
    private void copyBoard(Board oldBoard) {
        board = new int[10][10];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = oldBoard.getPosition(i, j);
            }
        }
    }

    /**
     * Prints the board in the Battleship board format (letter headers -> each row of the board, preceded by a number
     * 1-10). Converts the board's int elements to their corresponding String representations. Makes extensive use of
     * ANSI colours (via the Colours class) to make the board easier to read and differentiate between pieces.
     * // Board Symbols
     * // Letters/Numbers -> Yellow
     * // 0 = Empty -> No Colour -> No Symbol
     * // 1 = Ship -> Purple -> □
     * // 2 = Hit -> Red (Bold and Bright) -> x
     * // 3 = Miss -> Cyan -> x
     * // 4 = ValidPosition -> Green -> □
     * // 5 = InvalidPosition -> Red -> □
     */
    public void printBoard() {
        System.out.print("  |"); // Letter Headers
        for (char letterHeader : letterHeaders) {
            System.out.print(" " + Colours.getYellow() + letterHeader + Colours.getReset() + " |");
        }
        for (int i = 0; i < board.length; i++) { // Row Numbers
            System.out.println(); // Newline
            if (i == 9) { // Prints 10; being 2 digits, 10 takes up the extra space
                System.out.print(Colours.getYellow() + (i + 1) + Colours.getReset() + "|");
            } else { // Prints 1-9; there is a space before each 1-9 digit to be inline with 10 when it is printed
                System.out.print(" " + Colours.getYellow() + (i + 1) + Colours.getReset() + "|");
            }
            for (int j = 0; j < board[i].length; j++) { // Rows
                switch (board[i][j]) {
                    case 0 -> System.out.print("   |"); // Empty
                    case 1 -> System.out.print(" " + Colours.getPurple() + Board.boardPiece + Colours.getReset() + " |"); // Ship
                    case 2 -> System.out.print(" " + Colours.getRedBoldBright() + Board.hitPiece + Colours.getReset() + " |"); // Hit
                    case 3 -> System.out.print(" " + Colours.getCyan() + Board.hitPiece + Colours.getReset() + " |"); // Miss
                    case 4 -> System.out.print(" " + Colours.getGreen() + Board.boardPiece + Colours.getReset() + " |"); // ValidPosition
                    case 5 -> System.out.print(" " + Colours.getRed() + Board.boardPiece + Colours.getReset() + " |"); // InvalidPosition
                }
            }
        }
    }

    /**
     * Receives a board position (i,j) and the length of the ship being placed, performs checks for all 4 directions
     * (up, down, right, left) to determine if a ship (of the passed shipLength) can be placed at that position. If
     * even one direction is valid for that position, the position is deemed valid and true is returned. Otherwise,
     * the position is deemed invalid and false is returned. (Note: Because the board is printed with row 0 at the top
     * and row 9 at the bottom, going down in position means increasing i and going up means decreasing i).
     *
     * @param i:          indexI of the position
     * @param j:          indexJ of the position
     * @param shipLength: length of the ship being placed
     * @return boolean: true if valid, false otherwise
     */
    public boolean checkPositionValidity(int i, int j, int shipLength) {
        if (i + shipLength <= 10) { // Down check [i + k]
            for (int k = 0; k < shipLength; k++) {
                if ((k == shipLength - 1) && (!this.isShip(i + k, j))) { // If this is both the last position and not a ship
                    return true; // If even one check is valid, the position is valid
                } else if (this.isShip(i + k, j)) { // If this is not the last position, but a ship
                    break; // Next check
                }
            }
        }
        if (i - shipLength >= -1) { // Up check [i - k]
            for (int k = 0; k < shipLength; k++) {
                if ((k == shipLength - 1) && (!this.isShip(i - k, j))) { // If this is both the last position and not a ship
                    return true; // If even one check is valid, the position is valid
                } else if (this.isShip(i - k, j)) { // If this is not the last position, but a ship
                    break; // Next check
                }
            }
        }
        if (j + shipLength <= 10) { // Right check [j + k]
            for (int k = 0; k < shipLength; k++) {
                if ((k == shipLength - 1) && (!this.isShip(i, j + k))) { // If this is both the last position and not a ship
                    return true; // If even one check is valid, the position is valid
                } else if (this.isShip(i, j + k)) { // If this is not the last position, but a ship
                    break; // Next check
                }
            }
        }
        if (j - shipLength >= -1) { // Left check [j - k]
            for (int k = 0; k < shipLength; k++) {
                if ((k == shipLength - 1) && (!this.isShip(i, j - k))) { // If this is both the last position and not a ship
                    return true; // If even one check is valid, the position is valid
                } else if (this.isShip(i, j - k)) { // If this is not the last position, but a ship
                    break; // Next check
                }
            }
        }
        return false; // No checks were valid, so the position is invalid
    }

    /**
     * Places a ship on the board based on the starting position (i,j), the direction, and the length of the ship.
     * Returns an int[][] array containing all the positions of the placed ship. Assumes that there is enough space on
     * the board to place a ship of the passed length in the passed direction.
     *
     * @param i:          indexI of the position
     * @param j:          indexJ of the position
     * @param direction:  the direction ["D", "U", "R", "L"]
     * @param shipLength: the length of the ship being placed
     * @return shipPositions: the array containing the positions of the placed ship
     */
    public int[][] placeShipOnBoard(int i, int j, String direction, int shipLength) {
        this.addShip(i, j); // Adds the first position of the ship to this object's board
        int[][] shipPositions = new int[shipLength][2]; // Creates a new nested int[][] array of shipLength to hold and
        // return the positions of the placed ship
        shipPositions[0][0] = i; // Updates the I position of the first part of the current ship
        shipPositions[0][1] = j; // Updates the J position of the first part of the current ship

        switch (direction) {
            case "D" -> {  // Down Direction [i + k]
                for (int k = 1; k < shipLength; k++) { // Ignores the first position of the ship; it's already placed
                    this.addShip(i + k, j);
                    shipPositions[k][0] = i + k;
                    shipPositions[k][1] = j;
                }
            }
            case "U" -> {  // Up Direction [i - k]
                for (int k = 1; k < shipLength; k++) { // Ignores the first position of the ship; it's already placed
                    this.addShip(i - k, j);
                    shipPositions[k][0] = i - k;
                    shipPositions[k][1] = j;
                }
            }
            case "R" -> {  // Right Direction [j + k]
                for (int k = 1; k < shipLength; k++) { // Ignores the first position of the ship; it's already placed
                    this.addShip(i, j + k);
                    shipPositions[k][0] = i;
                    shipPositions[k][1] = j + k;
                }
            }
            case "L" -> {  // Left Direction [j - k]
                for (int k = 1; k < shipLength; k++) { // Ignores the first position of the ship; it's already placed
                    this.addShip(i, j - k);
                    shipPositions[k][0] = i;
                    shipPositions[k][1] = j - k;
                }
            }
        }
        return shipPositions;
    }

    /**
     * Checks and returns true if the passed position is within the board (regardless of the position's state),
     * otherwise, returns false.
     *
     * @param i: indexI of the position
     * @param j: indexJ of the position
     * @return boolean: true if contains, false otherwise
     */
    public boolean containsPosition(int i, int j) {
        return ((0 <= i && i < board.length) && (0 <= j && j < board[0].length));
    }

    // [Setter Methods]

    /**
     * Updates the passed position with the passed symbol. Checks to see if the position is contained within the board
     * first. (Note: this is a private method, meaning that it is only called by the 6 .add____(int i, int j) methods
     * within the Board class; this is to prevent unknown symbols from being entered into the board, which would cause
     * errors in printBoard() and other functions).
     *
     * @param i:      indexI of the position
     * @param j:      indexJ of the position
     * @param symbol: the symbol to be added to the board at the passed position [0,1,2,3,4,5]
     */
    private void updatePosition(int i, int j, int symbol) { // Never directly called outside of class
        if (!this.containsPosition(i, j)) {
            return;
        }
        board[i][j] = symbol;
    }

    /**
     * Calls .updatePosition() to add an empty cell (0) to the passed position.
     *
     * @param i: indexI of the position
     * @param j: indexJ of the position
     */
    public void addEmpty(int i, int j) {
        this.updatePosition(i, j, 0);
    }

    /**
     * Calls .updatePosition() to add a Ship (1) to the passed position.
     *
     * @param i: indexI of the position
     * @param j: indexJ of the position
     */
    public void addShip(int i, int j) {
        this.updatePosition(i, j, 1);
    }

    /**
     * Calls .updatePosition() to add a Hit (2) to the passed position.
     *
     * @param i: indexI of the position
     * @param j: indexJ of the position
     */
    public void addHit(int i, int j) {
        this.updatePosition(i, j, 2);
    }

    /**
     * Calls .updatePosition() to add a Miss (3) to the passed position.
     *
     * @param i: indexI of the position
     * @param j: indexJ of the position
     */
    public void addMiss(int i, int j) {
        this.updatePosition(i, j, 3);
    }

    /**
     * Calls .updatePosition() to add a ValidPosition (4) to the passed position.
     *
     * @param i: indexI of the position
     * @param j: indexJ of the position
     */
    public void addValidPosition(int i, int j) {
        this.updatePosition(i, j, 4);
    }

    /**
     * Calls .updatePosition() to add an InvalidPosition (5) to the passed position.
     *
     * @param i: indexI of the position
     * @param j: indexJ of the position
     */
    public void addInvalidPosition(int i, int j) {
        this.updatePosition(i, j, 5);
    }

    // [Getter Methods]

    /**
     * Returns the piece at the passed position. Checks to see if the position is contained within the board first.
     * (Note: unlike .updatePosition(), this method is not private because getting the element at a specific position
     * in the board is a valid request in and of itself, however this method is still called by other methods within
     * the Board class, specifically those that check to see if a position holds a certain piece; .is__(int i, int j)).
     *
     * @param i: indexI of the position
     * @param j: indexJ of the position
     * @return board[i][j]: an int value from 0-5 that represents a specific board piece
     */
    public int getPosition(int i, int j) {
        if (!this.containsPosition(i, j)) {
            return -1;
        }
        return board[i][j];
    }

    /**
     * Calls .getPosition() to see if the passed position is an empty cell (0); returns true if it is, false otherwise.
     *
     * @param i: indexI of the position
     * @param j: indexJ of the position
     * @return boolean: true if the position is an empty cell, false otherwise
     */
    public boolean isEmpty(int i, int j) {
        return this.getPosition(i, j) == 0;
    }

    /**
     * Calls .getPosition() to see if the passed position is a Ship (1); returns true if it is, false otherwise.
     *
     * @param i: indexI of the position
     * @param j: indexJ of the position
     * @return boolean: true if the position is a Ship, false otherwise
     */
    public boolean isShip(int i, int j) {
        return this.getPosition(i, j) == 1;
    }

    /**
     * Calls .getPosition() to see if the passed position is a Hit (2); returns true if it is, false otherwise.
     *
     * @param i: indexI of the position
     * @param j: indexJ of the position
     * @return boolean: true if the position is a Hit, false otherwise
     */
    public boolean isHit(int i, int j) {
        return this.getPosition(i, j) == 2;
    }

    /**
     * Calls .getPosition() to see if the passed position is a Miss (3); returns true if it is, false otherwise.
     *
     * @param i: indexI of the position
     * @param j: indexJ of the position
     * @return boolean: true if the position is a Miss, false otherwise
     */
    public boolean isMiss(int i, int j) {
        return this.getPosition(i, j) == 3;
    }

    /**
     * Calls .getPosition() to see if the passed position is a ValidPosition (4); returns true if it is, false otherwise.
     *
     * @param i: indexI of the position
     * @param j: indexJ of the position
     * @return boolean: true if the position is a ValidPosition, false otherwise
     */
    public boolean isValidPosition(int i, int j) {
        return this.getPosition(i, j) == 4;
    }

    /**
     * Calls .getPosition() to see if the passed position is an InvalidPosition (5); returns true if it is, false otherwise.
     *
     * @param i: indexI of the position
     * @param j: indexJ of the position
     * @return boolean: true if the position is a InvalidPosition, false otherwise
     */
    public boolean isInvalidPosition(int i, int j) {
        return this.getPosition(i, j) == 5;
    }

    /**
     * Returns a copy of the board, using the Board classes' copy constructor.
     *
     * @return Board: a new Board object that is a copy of the current Board
     */
    public Board getBoardCopy() { // Returns a copy of the board
        return new Board(this);
    }

    /**
     * @return board: the current object's int[][] board
     */
    public int[][] getBoard() { // Returns the int[][] board itself
        return board;
    }

    /**
     * @return length: the current object's length (which is always 10)
     */
    public int getLength() {
        return length;
    }

    // Static Methods

    /**
     * Checks to see if the passed character is contained within the letterHeaders array, and returns the index of the
     * character in the letterHeaders array if it is within it. Otherwise, returns -1 to signify that the character is
     * not contained within the letterHeaders array.
     *
     * @param character
     * @return
     */
    public static int letterIndexOf(char character) {
        for (int i = 0; i < Board.letterHeaders.length; i++) {
            if (letterHeaders[i] == Character.toUpperCase(character)) {// For case-insensitive comparison, make the passed character uppercase
                return i;
            }
        }
        return -1; // No matches found; returns -1 to show that the character is not contained in letterHeaders
    }

    /**
     * Receives a String position, expected to be in the form [Letter][Number], and parses it into an int[] of length 2
     * containing the corresponding positions on the int[][] board. If the passed position is invalid, it returns
     * {-1,-1}. Otherwise, if the passed position is valid, it returns the true indices of the position. (Note: this
     * method is static, because the result is the same independent of which Board object it is called upon)
     *
     * @param position: a user-inputted position string; valid positions are either of length 2 or 3, and are in the
     *                  format [Letter {A-J}][Number {1-10}]
     * @return positionIndices[]: an int[] array of length 2 that holds either the valid indices, or {-1,-1} if invalid
     */
    public static int[] positionStringToIndices(String position) {
        int[] positionIndices = new int[2];
        int indexI; // Holds the index of the number value
        int indexJ; // Holds the index of the letter value

        if (position.length() == 2) { // [Letter {A-J}][Number {1-9}]
            indexJ = Board.letterIndexOf(position.charAt(0)); // Get the letter's index
            if (indexJ != -1) { // If the letter is valid
                if (Character.isDigit(position.charAt(1))) { // If the second digit is a number
                    indexI = Character.getNumericValue(position.charAt(1)) - 1; // Given position is 1-indexed, to convert it to 0-indexed, subtract 1
                    if ((0 <= indexI) && (indexI <= 9)) { // If the number is valid
                        positionIndices[0] = indexI;
                        positionIndices[1] = indexJ;
                        return positionIndices;
                    }
                }
            }
        } else if (position.length() == 3) { // [Letter {A-J}][Number {10}] -> 10 is the only valid input for the last 2 characters
            indexJ = Board.letterIndexOf(position.charAt(0)); // Get the letter's index
            if (indexJ != -1) { // If the letter is valid
                if ((position.charAt(1) == '1') && (position.charAt(2) == '0')) { // If the number input is 10
                    indexI = 9; // Given position is 1-indexed, to convert it to 0-indexed, subtract 1, in this case: 10-1 = 9
                    positionIndices[0] = indexI;
                    positionIndices[1] = indexJ;
                    return positionIndices;
                }
            }
        }
        positionIndices[0] = -1; // If the position was invalid, the program would eventually reach this code
        positionIndices[1] = -1; // Returning indices of -1 means that the position to be parsed was invalid
        return positionIndices;
    }

    /**
     * Receives an int[] array of length 2 containing the two positions indices of a board position and parses them
     * into a position string. If the indices are valid, it returns a String in the format [Letter {A-J}][Number {1-10}].
     * Otherwise, it returns null. (Note: this method is static, because the result is the same independent of which
     * Board object it is called upon)
     *
     * @param indices: an int[] array of length 2 that contains the two position indices (i,j) to be parsed
     * @return returnString: either the valid position string in the format [Letter {A-J}][Number {1-10}], or null if
     * the indices are invalid
     */
    public static String indicesToPositionString(int[] indices) {
        String returnString = null;

        if (((0 <= indices[0]) && (indices[0] <= 9)) && ((0 <= indices[1]) && (indices[1] <= 9))) { // If the indices are valid -> (0 <= i <= 9) and (0 <= j <= 9)
            returnString = "";
            returnString += Board.letterHeaders[indices[1]];
            returnString += (indices[0] + 1); // The position string is 1-indexed, so add 1 to the 0-indexed i to convert it to the proper format
        }
        return returnString; // Returns the position string if the indices are valid, returns null otherwise
    }
}