// By: Adeshvir Dhillon
// Date: Jun 23-25, 2023

/**
 * An interface that requires the implementation of a getPlayerPosition() method. This interface was created to ensure
 * that Player and PlayerBoard, which both implement the interface and contain methods that require player inputted
 * positions, were standardised.
 * @see Player
 * @see PlayerBoard
 */
public interface PlayerPosition {
    /**
     *
     * @return an int[] array of length 2 that contains the inputted player's position's indices (i,j)
     */
    public int[] getPlayerPosition();
}