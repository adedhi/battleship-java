// By: Adeshvir Dhillon
// Date: Jun 23-26, 2023

/**
 * A static class that functions as a collection of static ANSI colour code methods. Contains getter methods that allow
 * access to these colour codes, to be used for coloured text output. (Note: originally, each class that required
 * coloured text had its own 'private static final String' colour codes, but it's better and easier to have those
 * classes call 'Colours.getColour()' instead)
 */
public final class Colours {
    // Variables
    private static final String ANSI_RESET = "\u001B[0m"; // Resets the text output back to normal
    private static final String ANSI_RED = "\033[0;31m"; // Sets text output to red
    private static final String ANSI_RED_BOLD_BRIGHT = "\033[1;91m"; // Sets text output to red (bolded and bright)
    private static final String ANSI_YELLOW = "\u001B[33m"; // Sets text output to yellow
    private static final String ANSI_GREEN = "\033[0;32m"; // Sets text output to green
    private static final String ANSI_CYAN = "\033[0;36m"; // Sets text output to cyan
    private static final String ANSI_PURPLE = "\033[0;35m"; // Sets text output to purple

    // Constructors

    /**
     * --Constructor for Colours--
     * (inaccessible, because there are not meant to be any instances of this class)
     */
    private Colours() {
    }

    // Static Methods
    // [Getter Methods]

    /**
     * @return ANSI code that resets coloured text output
     */
    public static String getReset() {
        return ANSI_RESET;
    }

    /**
     * @return ANSI code that colours text red
     */
    public static String getRed() {
        return ANSI_RED;
    }

    /**
     * @return ANSI code that colours text red (bolded and bright)
     */
    public static String getRedBoldBright() {
        return ANSI_RED_BOLD_BRIGHT;
    }

    /**
     * @return ANSI code that colours text yellow
     */
    public static String getYellow() {
        return ANSI_YELLOW;
    }

    /**
     * @return ANSI code that colours text green
     */
    public static String getGreen() {
        return ANSI_GREEN;
    }

    /**
     * @return ANSI code that colours text cyan
     */
    public static String getCyan() {
        return ANSI_CYAN;
    }

    /**
     * @return ANSI code that colours text purple
     */
    public static String getPurple() {
        return ANSI_PURPLE;
    }
}