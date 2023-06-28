// By: Adeshvir Dhillon
// Date: Jun 24-28, 2023

// Imports
import java.util.Scanner; // For reading player input

/**
 * A class that holds all the necessary objects and code to run the Battleship game. Manages both a Player object and a
 * Computer object and allows them to access each other (when making a move). Holds the main game loop and the play
 * again functionality.
 */
public class GameManager {
    // Variables
    private Player player; // The Player object representing the player
    private Board playerBoardCopy; // Holds a copy of the player's board; to be used when the computer is making a move
    int[] playerMove; // An int[] array of length 2 that holds the indices of the player's move {i,j}
    private Computer computer; // The Computer object representing the computer
    private Board computerBoardCopy; // Holds a copy of the computer's board; to be used when the player is making a move
    int[] computerMove; // An int[] array of length 2 that holds the indices of the computer's move {i,j}

    // Constructors

    /**
     * --Default Constructor for GameManager--
     * Creates a new Player object, a new Computer object, an int[] array of length 2 to hold the player's move and an
     * int[] array of length 2 to hold the computer's move.
     */
    public GameManager() {
        player = new Player();
        computer = new Computer();
        playerMove = new int[2];
        computerMove = new int[2];
    }

    // Methods

    /**
     * Allows the player to generate their board by placing their ships and generates the computer's board by randomly
     * placing ships. Also outputs the player's board and allows the player to continue at their own pace (via the
     * .enterToContinue() method).
     */
    public void generateBoards() {
        System.out.println();
        System.out.println("...Generating Player Board...");
        player.generateBoard(); // Allows the player to place their ships
        System.out.println();
        System.out.println("--Player Board Generated--");
        System.out.println("Player Board:"); // Outputs the player board
        player.printBoard();

        this.enterToContinue(); // Allows the player to continue at their own pace
        System.out.println();
        System.out.println("...Generating Computer Board...");
        computer.generateBoard(); // Generates the computer's board (randomly places ships)
        System.out.println("--Computer Board Generated--"); // (Note: doesn't output the computer's board to the player, for obvious reasons)
    }

    /**
     * The main loop of the game. Loops, allowing the player to make a move, then the computer, until the player or the
     * computer has won the game by sinking all of their enemy's ships. Then, asks if the player would like to play
     * again. Makes use of the .enterToContinue() method to allow the player to pause as long as they need between
     * stages of the game.
     *
     * @return boolean: true if the player would like to play again, false if they would like to end the game
     */
    public boolean runGame() {
        System.out.println();
        System.out.println("STARTING GAME");

        while (true) { // Loops until either the player or computer wins
            System.out.println(); // Player's Turn
            System.out.println("Player's Turn");
            computerBoardCopy = computer.getBoardCopy(); // Get a copy of the computer's board, to be used to check if the player's move was a hit or a miss
            playerMove = player.makeMove(computerBoardCopy); // Allow the player to make a move
            if (computerBoardCopy.isShip(playerMove[0], playerMove[1])) { // If the player move was a hit
                computer.addHit(playerMove); // Update the computer's board (not the copy)
                computer.updateHitShipPositions(playerMove); // Update the computer's ships
                this.enterToContinue(); // Pause for the player
                if (computer.checkWin()) { // Check to see if the player has won (the player can only win after making a hit, not a miss)
                    System.out.println();
                    System.out.println(Colours.getGreen() + "PLAYER WINS" + Colours.getReset()); // Player wins
                    System.out.println("You made " + Colours.getCyan() + player.getPlayerMoveCounter() + Colours.getReset() + " moves in total"); // Output the number of moves that the player made
                    return this.playAgain(); // Ask the player if they would like to play again
                }
            } else { // If the player move was a miss
                computer.addMiss(playerMove); // Update the computer's board (not the copy)
                this.enterToContinue(); // Pause for the player
            }

            System.out.println(); // Computer's Turn
            System.out.println("Computer's Turn");
            playerBoardCopy = player.getBoardCopy(); // Get a copy of the player's board, to be used to check if the computer's move was a hit or a miss
            computerMove = computer.makeMove(playerBoardCopy); // Generate a computer move
            if (playerBoardCopy.isShip(computerMove[0], computerMove[1])) { // If the computer move was a hit
                player.addHit(computerMove); // Update the player's board (not the copy)
                player.updateHitShipPositions(computerMove); // Update the player's ships
                System.out.println();
                System.out.println("Player's Board:"); // Out the player's board so that the player can see where the computer hit
                player.printBoard();
                this.enterToContinue(); // Pause for the player
                if (player.checkWin()) { // Check to see if the computer has won (the computer can only win after making a hit, not a miss)
                    System.out.println();
                    System.out.println(Colours.getRed() + "COMPUTER WINS" + Colours.getReset()); // Computer wins
                    System.out.println("You made " + Colours.getCyan() + player.getPlayerMoveCounter() + Colours.getReset() + " moves in total"); // Output the number of moves that the player made
                    return this.playAgain(); // Ask the player if they would like to play again
                }
            } else { // If the computer move was a miss
                player.addMiss(computerMove); // Update the player's board (not the copy)
                System.out.println();
                System.out.println("Player's Board:"); // Out the player's board so that the player can see where the computer hit
                player.printBoard();
                this.enterToContinue(); // Pause for the player
            }
        }
    }

    /**
     * Asks the player to press enter (or simply just input an empty string) to continue the process in the calling
     * method. Once the player presses enter, it returns (nothing) to the calling method. If they do not press enter, it
     * loops until they do. (Note: this method was created so that the player can take their time looking at the
     * output/decisions of the game before continuing to the next stage).
     */
    public void enterToContinue() {
        Scanner inputScanner = new Scanner(System.in); // Scanner to read the player's input

        while (true) { // Loops until enter is pressed
            try {
                System.out.println();
                System.out.print("---press " + Colours.getGreen() + "ENTER" + Colours.getReset() + " to continue--- ");
                if (inputScanner.hasNextLine()) { // If the player inputted something
                    if (inputScanner.nextLine().equals("")) { // If the player pressed enter (would return an empty String)
                        return; // Calling method continues to the next line
                    }
                } // If the player didn't press enter, the loop restarts; continue is unnecessary as this is the last line in the loop
            } catch (
                    Exception e) { // If, at any point, an exception was raised; continue is unnecessary as this is the last line in the loop
            }
        }
    }

    /**
     * Asks the player if they would like to play again, and to input 1 for Yes and 2 for No. Loops until the player
     * inputs either 1 or 2. If the player inputs 1, it returns true to signify choosing to play again. If the player
     * inputs 2, it returns false to signify choosing to end the game.
     *
     * @return boolean: true if play again, false if end game
     */
    public boolean playAgain() {
        Scanner inputScanner = new Scanner(System.in); // Scanner to read the player's input
        int inputInteger; // The player's choice of whether to play again (1 = Yes / 2 = No)

        while (true) { // Loops until either 1 or 2 is inputted
            try {
                System.out.println();
                System.out.println("Would you like to play again?");
                System.out.print("(1) for Yes / (2) for No: ");
                if (inputScanner.hasNextInt()) { // If the player inputted something, specifically an int
                    inputInteger = inputScanner.nextInt(); // Receive the inputted int
                    if (inputInteger == 1) { // If the player inputted 1 (Yes)
                        return true; // Play again
                    } else if (inputInteger == 2) { // If the player inputted 2 (No)
                        return false; // End game
                    }
                }
                System.out.println("That input is invalid, please try again"); // If, at any point, a condition was not met; continue is unnecessary as this is the last line in the loop
                inputScanner.next(); // Need to discard the invalid input, otherwise the scanner will keep on reading it and create an infinite loop
            } catch (Exception e) {
                System.out.println("That input is invalid, please try again"); // If, at any point, an exception was raised; continue is unnecessary as this is the last line in the loop
                inputScanner.next(); // Need to discard the invalid input, otherwise the scanner will keep on reading it and create an infinite loop
            }
        }
    }
}