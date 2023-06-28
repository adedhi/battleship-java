// By: Adeshvir Dhillon
// Date: Jun 20-26, 2023

/**
 * A class that represents a single ship. Holds the ship's name, length, and positions (number of positions is
 * determined by length).
 */
public class Ship {
    // Variables
    private final String name; // Name of the ship
    private final int length; // Length of the ship; used to determine the number of positions for the positions array
    private int[][] positions; // Nested array holding the positions (i,j) of the ship on a board; outer loop length
    // determined by length of the ship, inner loop length is always 2 (2 indices)

    // Constructors

    /**
     * --Constructor for Ship--
     *
     * @param name:   ship's name
     * @param length: ship's length
     */
    public Ship(String name, int length) {
        this.name = name;
        this.length = length;
        this.positions = new int[length][2]; // 2-D board, so each position will have 2 indices
    }

    // Methods

    /**
     * Updates the ship position that has been hit by the passed int[] move to {-1,-1}. Iterates through the positions
     * array, checking for a match to the passed move. Once a match is found, it sets the indices of the position to
     * {-1,-1} (to simulate removal of that position, which is otherwise difficult because positions is a static int
     * array). Assumes that the move has already been confirmed to be a hit.
     *
     * @param move: the position to look for
     */
    public void updateHitPositions(int[] move) {
        for (int i = 0; i < positions.length; i++) {
            if ((positions[i][0] == move[0]) && (positions[i][1] == move[1])) { // If position == move
                positions[i][0] = -1; // A ship's position being -1 implies that the position has been hit
                positions[i][1] = -1;
            }
        }
    }

    /**
     * Iterates through the positions array, looking for a non-hit (!= {-1,-1}) position. If there is even one
     * non-hit position, the ship is not sunk, and it returns false. Otherwise, the ship is sunk, and it returns true.
     * If all a player's board's ships are sunk, it means that the game is over and that the opponent has won.
     *
     * @return boolean: true if sunk, false otherwise
     */
    public boolean isSunk() {
        for (int[] position : positions) {
            if ((position[0] != -1) && (position[1] != -1)) { // If the position has not yet been hit
                return false; // Ship has not been sunk
            }
        }
        return true; // Ship has been sunk

//        for(int i = 0; i < positions.length; i++){
//            for(int j = 0; j < positions[i].length; j++){
//                if(positions[i][j] != -1){ // If the position has not yet been hit
//                    return false; // Ship has not been sunk
//                }
//            }
//        }
//        return true; // Ship has been sunk
    }

    // [Setter Methods]

    /**
     * Adds positions to the ship's positions array. Copies the passed int[][] array. Updates all positions in one
     * method call, instead of a method call per position, thus it requires that the shipPositions array be initialized,
     * updated, and passed in the calling function.
     *
     * @param shipPositions: int[][] array of positions to be copied
     */
    public void addPositions(int[][] shipPositions) {
        if (shipPositions.length == length) { // Ensuring that the passed array is of the correct length
            for (int i = 0; i < shipPositions.length; i++) {
                for (int j = 0; j < shipPositions[i].length; j++) {
                    positions[i][j] = shipPositions[i][j];
                }
            }
        }
    }

    // [Getter Methods]

    /**
     * @return Ship's name
     */
    public String getName() {
        return name;
    }

    /**
     * @return Ship's length
     */
    public int getLength() {
        return length;
    }

    /**
     * @return Ship's positions, a nested array containing 2 indices for each position
     */
    public int[][] getPositions() {
        return positions;
    }
}