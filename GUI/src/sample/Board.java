package sample;
import java.util.Arrays;
import java.util.Random;

/*
 *  This class holds the 2D array of a board
 *  as well as basic functionality such as swap tiles,
 *  generate board, calculate heuristic value etc.
 */
public class Board {
    private int[][] board;
    private int[][] solvedBoard;
    private int moveNumber;

    /*
     *  Type: Constructor (Default)
     *  Generates a board with random values
     *  Makes sure there are no repetitions
     *  Generates the solved state of the board
     */
    public Board() {
        moveNumber = 1;
        this.board = new int[3][3];
        int duplicate[] = new int[9];
        for (int i=0; i<9; i++) {
            duplicate[i] = 0;
        }
        Random randomGenerator = new Random();
        randomGenerator.setSeed( System.currentTimeMillis());
        int randomNumber;

        for (int i=0; i<3; i++) {
            for (int j=0; j<3; j++) {
                while(true) {
                    randomNumber = randomGenerator.nextInt(9);
                    if (duplicate[randomNumber] == 0) {
                        board[i][j] = randomNumber;
                        duplicate[randomNumber] = 1;
                        break;
                    }
                }
            }
        }

        solvedBoard = new int[3][3];
        int k = 1;
        for (int i=0; i<3; i++) {
            for (int j=0; j<3; j++) {
                solvedBoard[i][j] = k;
                k++;
            }
        }
        solvedBoard[2][2] = 0;
    }

    /*
     *  Type: Constructor (Overload)
     *  @param input string
     *  Generates a board based on the input of the user
     *  Does error checking (no duplicates etc.)
     *  Generates the solved state of the board
     */
    public Board(String initialBoard) {
        moveNumber = 1;
        this.board = new int[3][3];
        int elements[] = new int[9];
        int duplicate[] = new int[9];
        for (int i=0; i<9; i++) {
            duplicate[i] = 0;
        }
        if (initialBoard.length() == 9) {
            for (int i=0; i<9; i++) {
                elements[i] = Character.getNumericValue(initialBoard.charAt(i));
            }

            int k = 0;
            for (int i=0; i<3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (duplicate[elements[k]] == 0) {
                        board[i][j] = elements[k];
                        duplicate[elements[k]] = 1;
                        k++;
                    }
                    else {
                        System.out.println("Duplicate Values!");
                        System.exit(0);
                    }
                }
            }
        }
        else {
            System.out.println("Invalid Values!");
            System.exit(0);
        }

        solvedBoard = new int[3][3];
        int k = 1;
        for (int i=0; i<3; i++) {
            for (int j=0; j<3; j++) {
                solvedBoard[i][j] = k;
                k++;
            }
        }
        solvedBoard[2][2] = 0;
    }

    public int[][] getBoard() {
        return board;
    }

    /*
     *  Type: Function (Helper)
     *  Prints out the board
     */
    private void displayBoard() {
        for (int i=0; i<3; i++) {
            System.out.print("   ");
            for (int j=0; j<3; j++) {
                if (this.board[i][j] == 0) {
                    System.out.print("  ");
                }
                else {
                    System.out.print(this.board[i][j] + " ");
                }
            }
            System.out.println();
        }
    }

    /*
     *  Prints out the board along with move number and heuristic
     */
    public void displayBoardWithInfo() {
        if (moveNumber == 1) {
            System.out.println("Initial board is:");
        }
        System.out.println(moveNumber + ".");
        displayBoard();
        System.out.println("Heuristic value: " + calculateHeuristic());
        System.out.println();
    }

    /*
     *  Prints out the board with heuristic
     */
    public void displayBoardWithHeuristic() {
        displayBoard();
        System.out.println("Heuristic value: " + calculateHeuristic());
        System.out.println();
    }

    /*
     *  @param integer value ranging from 1-8
     *  @return true if move is valid false if invalid
     */
    public boolean isValidMove(int value) {
        for (int i=0; i<3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == value) {
                    if (i + 1 <= 2 && board[i + 1][j] == 0)
                        return true;
                    else if (i - 1 >= 0 && board[i - 1][j] == 0)
                        return true;
                    else if (j + 1 <= 2 && board[i][j + 1] == 0)
                        return true;
                    else if (j - 1 >= 0 && board[i][j - 1] == 0)
                        return true;
                }
            }
        }
        return false;
    }

    /*
     *  @param integer ranging from 1-8
     *  performs the swap based on the integer entered
     */
    public void move(int value) {
        this.moveNumber++;
        for (int i=0; i<3; i++) {
            for (int j=0; j<3; j++) {
                if (board[i][j] == value) {
                    if (i + 1 <= 2 && board[i + 1][j] == 0) {
                        board[i+1][j] = board[i][j];
                        board[i][j] = 0;
                        return;
                    }
                    else if (i - 1 >= 0 && board[i - 1][j] == 0) {
                        board[i-1][j] = board[i][j];
                        board[i][j] = 0;
                        return;
                    }
                    else if (j + 1 <= 2 && board[i][j + 1] == 0) {
                        board[i][j+1] = board[i][j];
                        board[i][j] = 0;
                        return;
                    }
                    else if (j - 1 >= 0 && board[i][j - 1] == 0) {
                        board[i][j-1] = board[i][j];
                        board[i][j] = 0;
                        return;
                    }
                }
            }
        }
    }

    /*
     *  @return integer value representing the heuristic
     *  calculates the difference in x and difference in y and adds them together
     */
    public int calculateHeuristic() {
        int heuristicValue = 0;
        int differenceX;
        int differenceY;
        for (int i=0; i<3; i++) {
            for (int j=0; j<3; j++) {
                differenceX = Math.abs(getXCoord(this.board[i][j], solvedBoard) - i);
                differenceY = Math.abs(getYCoord(this.board[i][j], solvedBoard) - j);
                heuristicValue += differenceX; heuristicValue += differenceY;
            }
        }
        return heuristicValue;
    }

    /*
     *  @return number of moves possible based on empty space
     */
    private int numMoves() {
        int x = getXCoord(0, board);
        int y = getYCoord(0, board);
        if (x == 1 && y == 1)
            return 4;
        else if ((x == 0 && y == 1) || (x == 1 && y == 0)
                || (x == 1 && y == 2) || (x == 2 && y == 1))
            return 3;
        else if ((x == 0 && y == 0) || (x == 2 && y == 0)
                || (x == 0 && y == 2) || (x == 2 && y == 2))
            return 2;
        return 0;
    }

    /*
     *  @return an array containing numbers (range 1-8)
     *  array size is based off of numMoves()
     */
    public int[] possibleMoves() {
        int[] arrayPossibleMoves = new int[numMoves()];
        int j = 0;
        for (int i=1; i<=8; i++) {
            if (isValidMove(i)) {
                arrayPossibleMoves[j] = i;
                j++;
            }
        }
        return arrayPossibleMoves;
    }

    /*
     *  @return string that will be used for hashmap
     *  ex: this following board returns the string: "123456780"
     *    1 2 3
     *    4 5 6
     *    7 8
     *  avoids collisions
     */
    public String getString() {
        int[] retArray = new int[9];
        int k=0;
        for (int i=0; i<3; i++) {
            for (int j=0; j<3; j++) {
                retArray[k] = board[i][j];
                k++;
            }
        }
        return Arrays.toString(retArray).replaceAll("[\\,\\[\\]\\ ]", "");
    }

    /*
     *  makes the move number 1
     *  this will be used for the auto solver
     */
    public void resetMoves() {
        moveNumber = 1;
    }

    /*
     *  @param integer (range 1-8) and a 2D array of ints representing the board
     *  @return returns the X coordinate of the value in the board
     */
    private int getXCoord(int value, int board[][]) {
        int xCoord = 0;
        for (int i=0; i<3; i++) {
            for (int j=0; j<3; j++) {
                if (board[i][j] == value) {
                    xCoord = i;
                    break;
                }
            }
        }
        return xCoord;
    }

    /*
     *  @param integer (range 1-8) and a 2D array of ints representing the board
     *  @return returns the Y coordinate of the value in the board
     */
    private int getYCoord(int value, int board[][]) {
        int yCoord = 0;
        for (int i=0; i<3; i++) {
            for (int j=0; j<3; j++) {
                if (board[i][j] == value) {
                    yCoord = j;
                    break;
                }
            }
        }
        return yCoord;
    }
}