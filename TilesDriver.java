import java.util.Scanner;

public class TilesDriver {
    private static Board board;

    public static void main(String[] args) {
        outputInitialInfo();
        setupBoard();
        playGame();
    }

    /**
     * outputs game instructions
     */
    private static void outputInitialInfo() {
        System.out.println("Author Code: Icon");
        System.out.println("Class: CS 342, Fall 2016");
        System.out.println("Program: #3, 8 Tiles\n");

        System.out.println("Welcome to the 8-tiles puzzle."
                + "\nPlace the tiles in ascending numerical order.  For each  "
                + "\nmove enter the piece to be moved into the blank square, "
                + "\nor 0 to exit the program.\n");

        System.out.print("Choose a game option: "
                + "\n  1. Start playing "
                + "\n  2. Set the starting configuration"
                + "\nEnter your choice --> ");
    }

    /**
     * Gives the user two options:
     * 1. Generates a random board
     * 2. Asks the user to input a board
     */
    private static void setupBoard() {
        int userInput;
        String initialBoard;

        Scanner scanner = new Scanner(System.in);
        userInput = scanner.nextInt();

        if (userInput == 1) {
            System.out.println();
            board = new Board();
        }
        else if (userInput == 2) {
            System.out.print("Some boards such as 728045163 are impossible."
                    + "\nOthers such as 245386107 are possible."
                    + "\nEnter a string of 9 digits (including 0) for the board --> ");
            initialBoard = scanner.next();
            System.out.println();
            board = new Board(initialBoard);
        }
        else {
            System.out.println("Invalid input!");
        }
    }

    /**
     * Asks user to make moves or solve the puzzle
     */
    private static void playGame() {
        boolean invalidMove = false;
        char getNextMove;
        Scanner scanner = new Scanner(System.in);

        /* Start of the interactive loop */
        while(true) {
            if (!invalidMove)
                board.displayBoardWithInfo();
            if (board.calculateHeuristic() == 0) {
                System.out.println("Done.");
                System.exit(0);
            }
            System.out.print("Piece to move: ");
            getNextMove = scanner.next().charAt(0);
            if (Character.getNumericValue(getNextMove) == 0) {
                System.out.println("Exiting program.");
                System.exit(0);
            }
            else if (getNextMove == 's') {
                SearchTree searchtree = new SearchTree(board);
                searchtree.solve();
                break;
            }
            else if (board.isValidMove(Character.getNumericValue(getNextMove))) {
                int moveInt = Character.getNumericValue(getNextMove);
                board.move(moveInt);
                invalidMove = false;
            }
            else {
                System.out.println("*** Invalid move.  Please retry.");
                System.out.println();
                invalidMove = true;
            }
        }
    }
}