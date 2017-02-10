package sample;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.animation.*;
import javafx.util.Duration;

/* The following class takes care of everything in the UI.
 * Takes care of button press events,
 * swapping tiles, accepting user defined board etc.
 */
public class Controller {
    private boolean settingBoard;
    private int setCounter;
    public Button setBoard;
    public Button solve;
    public Button exit;
    public Button button1, button2, button3, button4, button5,
            button6, button7, button8, button9;
    private Button[][] buttonBoard;
    private Board board;
    private int[][] userSetBoard;
    public Label label;
    private PauseTransition pauseTransArray[];

    /*
     * The initialize function is run first in JavaFX.
     * Here I initialize a 2D array of Buttons for simplicity
     * I also initialize a Board instance and it will work as a parallel array
     * with the 2D array of buttons.
     */
    public void initialize() {
        initializeButtonBoard();
        board = new Board();
        setBoard();
    }

    /*
     * A helper function that maps the buttons defined the FXML
     * to the 2D array of Buttons.
     */
    private void initializeButtonBoard() {
        buttonBoard = new Button[3][3];

        buttonBoard[0][0] = button1;
        buttonBoard[0][1] = button2;
        buttonBoard[0][2] = button3;
        buttonBoard[1][0] = button4;
        buttonBoard[1][1] = button5;
        buttonBoard[1][2] = button6;
        buttonBoard[2][0] = button7;
        buttonBoard[2][1] = button8;
        buttonBoard[2][2] = button9;

        settingBoard = false;
    }

    /*
     * The following function updates the UI board with the board
     * that is in the instance of the Board class.
     */
    private void setBoard() {
        for (int i=0; i<3; i++) {
            for (int j=0; j<3; j++) {
                if (board.getBoard()[i][j] != 0)
                    buttonBoard[i][j].setText(Integer.toString(board.getBoard()[i][j]));
                else
                    buttonBoard[i][j].setText(" ");
                label.setText("Heuristic Value: "+ Integer.toString(board.calculateHeuristic()));
            }
        }

        if (board.calculateHeuristic() == 0) {
            label.setText("Board Solved. Congratulations!");
        }
    }

    /*
     * @param Board
     * The following function updates the UI with the board
     * that is passed in as the parameter.
     */
    private void setBoard(Board b) {
        for (int i=0; i<3; i++) {
            for (int j=0; j<3; j++) {
                if (b.getBoard()[i][j] != 0)
                    buttonBoard[i][j].setText(Integer.toString(b.getBoard()[i][j]));
                else
                    buttonBoard[i][j].setText(" ");
                label.setText("Heuristic Value: "+ Integer.toString(b.calculateHeuristic()));
            }
        }

        if (b.calculateHeuristic() == 0) {
            label.setText("Board Solved. Congratulations!");
        }
    }

    /*
     * @param An event
     * The following function handles any event of a button press from the 3x3 grid
     */
    public void swap(ActionEvent event) {
        // User is manually setting up the board
        if (settingBoard) {
            userBoard(event);
        }

        // User is trying to swap a tile
        else {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (event.getSource().equals(buttonBoard[i][j])) {
                        if (!buttonBoard[i][j].getText().equals(" ") && board.isValidMove(Integer.parseInt(buttonBoard[i][j].getText()))) {
                            board.move(Integer.parseInt(buttonBoard[i][j].getText()));
                            setBoard();
                        }
                    }
                }
            }
        }
    }

    // The following function sets all the tiles' text to blank
    private void clearBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttonBoard[i][j].setText("");
            }
        }
    }

    /*
     * @param an Event
     * This is a helper function that lets the user set the board manually
     */
    private void userBoard(Event e) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (e.getSource().equals(buttonBoard[i][j])) {

                    // First click should be blank
                    if (setCounter == 0) {
                        buttonBoard[i][j].setText(" ");
                        label.setText("Set the board to your liking.");
                        setCounter++;
                    }

                    // Rest of the clicks
                    else if (userSetBoard[i][j] == 0 && setCounter < 9 && !buttonBoard[i][j].getText().equals(" ")) {
                        userSetBoard[i][j] = setCounter;
                        buttonBoard[i][j].setText(Integer.toString(setCounter));
                        label.setText("Set the board to your liking.");
                        setCounter++;
                        if (setCounter == 9) {
                            initializeCustomBoard();
                        }
                    }

                    // invalid (clicked the button where the value is already assigned.
                    else {
                        label.setText("Invalid; Try again");
                    }
                }
            }
        }
    }

    /*
     * Coverts the array made from user's manually set board to a string
     * and updates the Board instance to that.
     */
    private void initializeCustomBoard() {
        settingBoard = false;
        setCounter = 0;
        String customBoardString = "";
        for (int i=0; i<3; i++) {
            for (int j=0; j<3; j++) {
                customBoardString = customBoardString.concat(Integer.toString(userSetBoard[i][j]));
            }
        }

        board = new Board(customBoardString);
    }

    /*
     * A helper function that reverses my "linked list" but is actually just a singly linked tree.
     */
    private Node reverseNodes(Node currNode) {
        Node previousNode = null;
        Node nextNode;
        while(currNode != null) {
            nextNode = currNode.parent;
            currNode.parent = previousNode;
            previousNode = currNode;
            currNode = nextNode;
        }
        return previousNode;
    }

    /*
     * The following updates the board every 400ms when the user clicks the solve button.
     */
    private void updateBoard(Node n) {
        int i=0;
        while (n != null) {
            Board b = n.board;
            board = n.board;
            pauseTransArray[i] = new PauseTransition(Duration.millis(300));
            pauseTransArray[i].setOnFinished(event -> setBoard(b));
            n = n.parent;
            i++;
        }
    }

    /*
     * Exits the program when exit button is clicked
     */
    public void onClickExit() {
        System.exit(0);
    }

    /*
     * handles the event when the user clicks Set Board
     */
    public void onClickSetBoard() {
        board = null;
        clearBoard();
        settingBoard = true;
        setCounter = 0;
        label.setText("Set the board to your liking.");
        userSetBoard = new int[3][3];
    }

    /*
     * Handles the event when the user clicks solve.
     * Creates an instance of SearchTree which solves the board
     * A helper function updates the UI board every 400ms.
     */
    public void onClickSolve() {
        SearchTree boardSolver = new SearchTree(board);
        boardSolver.solve();
        Node solvedBoards = boardSolver.getSolvedSequence();

        if (solvedBoards == null) {
            label.setText("**********BOARD IS IMPOSSIBLE TO SOLVE!**********");
        }

        else {
            Node reversed = reverseNodes(solvedBoards);
            //boardSolver.print(reversed);

            pauseTransArray = new PauseTransition[boardSolver.getNumMoves(reversed)];
            updateBoard(reversed);
            SequentialTransition st = new SequentialTransition(pauseTransArray);
            st.play();
        }
    }
}
