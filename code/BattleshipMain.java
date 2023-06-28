// By: Adeshvir Dhillon
// Date: Jun 20-28, 2023

public class BattleshipMain {
    public static void main(String[] args){
        GameManager gameManager; // GameManager object that holds all the necessary objects and code to run the Battleship game

        System.out.println();
        System.out.println("~Beginning of Program~");

        System.out.println(); // Introduction
        System.out.println("...Welcome to Battleship");
        System.out.println("   this is a player vs. computer game");
        System.out.println("   both you and the computer will place 5 ships, of varying lengths, on a 10x10 grid");
        System.out.println("   then, you will take turns guessing and firing at each others' boards, trying to sink the enemy ships");
        System.out.println("   the first one to sink all of their enemy's ships wins");
        System.out.println("   good luck...");

        while(true){ // Loops until the player chooses to end the game
            gameManager = new GameManager();
            gameManager.enterToContinue(); // Allows the player to pause before initializing the game

            System.out.println();
            System.out.println("INITIALIZING GAME");

            gameManager.generateBoards(); // Generates both the player's and the computer's boards
            gameManager.enterToContinue(); // Allows the player to pause before starting the game

            if(!gameManager.runGame()){ // Runs the main loop of the game and will eventually return true or false
                // depending on if the player wants to play again or end the game
                break; // If the player decides to end the game, break out of the loop
            } // If the player decides to play again, continue the loop
        }

        System.out.println();
        System.out.println("Thank you for playing!"); // End of program
        System.out.println("~End of Program~");
    }
}