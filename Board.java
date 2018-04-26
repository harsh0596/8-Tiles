import java.util.Arrays;
import java.util.Random;

/**
 *  This class holds the 2D array of a board
 *  as well as basic functionality such as swap tiles,
 *  generate board, calculate heuristic value etc.
 */
class Board {
    private int[][] board;
    private int[][] solvedBoard;
    private int moveNumber;

    /**
     *  Constructor (Default)
     */
    Board() {
        moveNumber = 1;
        initializeSolvedBoard();
        initializeRandomBoard();
    }

    /**
     *  Constructor (Overload)
     *  @param initialBoard board state as a String
     */
    Board(String initialBoard) {
        moveNumber = 1;
        initializeSolvedBoard();
        initializeBoardWithValues(initialBoard);
    }


    /**
     * Initializes solved board state
     */
    private void initializeSolvedBoard() {
        solvedBoard = new int[Constants.NUMROWS][Constants.NUMCOLS];
        int k = 1;
        for (int i = 0; i < Constants.NUMROWS; i++) {
            for (int j = 0; j < Constants.NUMCOLS; j++) {
                solvedBoard[i][j] = k;
                k++;
            }
        }
        solvedBoard[Constants.NUMROWS - 1][Constants.NUMCOLS - 1] = 0;
    }

    /**
     * Initializes a board with random values
     */
    private void initializeRandomBoard() {
        board = new int[Constants.NUMROWS][Constants.NUMCOLS];
        boolean duplicate[] = new boolean[Constants.NUMBOXES];

        Random randomGenerator = new Random();
        randomGenerator.setSeed( System.currentTimeMillis());
        int randomNumber;

        for (int i=0; i<Constants.NUMROWS; i++) {
            for (int j=0; j<Constants.NUMCOLS; j++) {
                while(true) {
                    randomNumber = randomGenerator.nextInt(Constants.NUMBOXES);
                    if (!duplicate[randomNumber]) {
                        board[i][j] = randomNumber;
                        duplicate[randomNumber] = true;
                        break;
                    }
                }
            }
        }
    }

    /**
     * @param input board state as a String
     *  Generates a board based on the input of the user
     */
    private void initializeBoardWithValues(String input) {
        this.board = new int[Constants.NUMROWS][Constants.NUMCOLS];
        int elements[] = new int[Constants.NUMBOXES];
        boolean duplicate[] = new boolean[Constants.NUMBOXES];

        if (input.length() == Constants.NUMBOXES) {
            for (int i = 0; i < Constants.NUMBOXES; i++) {
                elements[i] = Character.getNumericValue(input.charAt(i));
            }

            int k = 0;
            for (int i = 0; i < Constants.NUMROWS; i++) {
                for (int j = 0; j < Constants.NUMCOLS; j++) {
                    if (!duplicate[elements[k]]) {
                        board[i][j] = elements[k];
                        duplicate[elements[k]] = true;
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
    }

    /**
     *  outputs the board to console
     */
    private void displayBoard() {
        for (int i = 0; i < Constants.NUMROWS; i++) {
            System.out.print("   ");
            for (int j = 0; j< Constants.NUMCOLS; j++) {
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

    /**
     *  @return number of moves possible based on empty space
     */
    private int numMoves() {
        int x = getXCoord(0, board);
        int y = getYCoord(0, board);

        // 4 Corners
        if ((x == 0 && y == 0) || (x == Constants.NUMROWS - 1 && y == 0)
                || (x == 0 && y == Constants.NUMCOLS - 1) || (x == Constants.NUMROWS && y == Constants.NUMCOLS))
            return 2;

            // Non-corner Edges
        else if (x == 0 || y == 0 || x == Constants.NUMROWS - 1 || y == Constants.NUMCOLS - 1)
            return 3;

        else
            return 4;
    }

    /**
     *  @param value (range 1 to NUMBOXES - 1) and a 2D array of ints representing the board
     *  @param board Board to find the value from
     *  @return returns the X coordinate of the value in the board
     */
    private int getXCoord(int value, int board[][]) {
        if (value > Constants.NUMBOXES - 1)
            return -1;

        int xCoord = 0;
        for (int i = 0; i < Constants.NUMROWS; i++) {
            for (int j = 0; j < Constants.NUMCOLS; j++) {
                if (board[i][j] == value) {
                    xCoord = i;
                    break;
                }
            }
        }
        return xCoord;
    }

    /**
     *  @param value (range 1 to NUMBOXES - 1) and a 2D array of ints representing the board
     *  @param board Board to find the value from
     *  @return returns the Y coordinate of the value in the board
     */
    private int getYCoord(int value, int board[][]) {
        if (value > Constants.NUMBOXES - 1)
            return -1;

        int yCoord = 0;
        for (int i = 0; i < Constants.NUMROWS; i++) {
            for (int j = 0; j < Constants.NUMCOLS; j++) {
                if (board[i][j] == value) {
                    yCoord = j;
                    break;
                }
            }
        }
        return yCoord;
    }

    /**
     *  Outputs the board along with move number and heuristic
     */
    void displayBoardWithInfo() {
        if (moveNumber == 1) {
            System.out.println("Initial board is:");
        }
        System.out.println(moveNumber + ".");
        displayBoard();
        System.out.println("Heuristic value: " + calculateHeuristic());
        System.out.println();
    }

    /**
     *  Outputs the board with heuristic
     */
    void displayBoardWithHeuristic() {
        displayBoard();
        System.out.println("Heuristic value: " + calculateHeuristic());
        System.out.println();
    }

    /**
     *  @param value int value ranging from 1 to NUMBOXES - 1
     *  @return true if move is valid false if invalid
     */
    boolean isValidMove(int value) {
        if (value > Constants.NUMBOXES - 1)
            return false;

        for (int i = 0; i < Constants.NUMCOLS; i++) {
            for (int j = 0; j < Constants.NUMROWS; j++) {
                if (board[i][j] == value) {
                    if (i + 1 <= Constants.NUMROWS - 1 && board[i + 1][j] == 0)
                        return true;
                    else if (i - 1 >= 0 && board[i - 1][j] == 0)
                        return true;
                    else if (j + 1 <= Constants.NUMCOLS - 1 && board[i][j + 1] == 0)
                        return true;
                    else if (j - 1 >= 0 && board[i][j - 1] == 0)
                        return true;
                }
            }
        }
        return false;
    }

    /**
     *  @param value ranging from 1 to NUMBOXES - 1
     *  performs the swap based on the integer entered
     */
    void move(int value) {
        if (value > Constants.NUMBOXES - 1)
            return;

        this.moveNumber++;
        for (int i = 0; i < Constants.NUMROWS; i++) {
            for (int j = 0; j < Constants.NUMCOLS; j++) {
                if (board[i][j] == value) {
                    if (i + 1 <= Constants.NUMROWS - 1 && board[i + 1][j] == 0) {
                        board[i+1][j] = board[i][j];
                        board[i][j] = 0;
                        return;
                    }
                    else if (i - 1 >= 0 && board[i - 1][j] == 0) {
                        board[i-1][j] = board[i][j];
                        board[i][j] = 0;
                        return;
                    }
                    else if (j + 1 <= Constants.NUMCOLS - 1 && board[i][j + 1] == 0) {
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

    /**
     *  @return integer value representing the heuristic
     *  calculates the difference in x and difference in y and adds them together
     */
    int calculateHeuristic() {
        int heuristicValue = 0;
        int differenceX;
        int differenceY;
        for (int i = 0; i < Constants.NUMROWS; i++) {
            for (int j = 0; j < Constants.NUMCOLS; j++) {
                differenceX = Math.abs(getXCoord(this.board[i][j], solvedBoard) - i);
                differenceY = Math.abs(getYCoord(this.board[i][j], solvedBoard) - j);
                heuristicValue += differenceX;
                heuristicValue += differenceY;
            }
        }
        return heuristicValue;
    }



    /**
     *  @return an array containing numbers (range 1 to NUMBOXES - 1)
     *  array size is based off of numMoves()
     */
    int[] possibleMoves() {
        int[] arrayPossibleMoves = new int[numMoves()];
        int j = 0;
        for (int i = 1; i < Constants.NUMBOXES; i++) {
            if (isValidMove(i)) {
                arrayPossibleMoves[j] = i;
                j++;
            }
        }
        return arrayPossibleMoves;
    }

    /**
     *  @return string that will be used for hashmap
     *  ex: this following board returns the string: "123456780"
     *    1 2 3
     *    4 5 6
     *    7 8
     *  avoids collisions
     */
    String getString() {
        int[] retArray = new int[Constants.NUMBOXES];
        int k = 0;
        for (int i = 0; i < Constants.NUMROWS; i++) {
            for (int j = 0; j < Constants.NUMCOLS; j++) {
                retArray[k] = board[i][j];
                k++;
            }
        }
        return Arrays.toString(retArray).replaceAll("[,\\[\\] ]", "");
    }

    /**
     *  makes the move number 1
     */
    void resetMoves() {
        moveNumber = 1;
    }
}