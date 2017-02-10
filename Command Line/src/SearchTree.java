import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

/*
 *  This class executes the board solver using:
 *  HashMap and PriorityQueue
 */
public class SearchTree {
    private Board initialState;
    private HashMap<String, Node> hmap;
    private PriorityQueue<Node> queue;

    public SearchTree(Board board) {
        this.initialState = board;
        hmap = new HashMap<>();
        queue = new PriorityQueue<>(5, new Comparator<Node>() {
            @Override
            public int compare(Node n1, Node n2) {
                return n1.board.calculateHeuristic() - n2.board.calculateHeuristic();
            }
        });
    }

    // A* implementation
    // hashmap is used to check if the board state is already checked or not
    // priority queue is used to get the parent pointer
    public void solve() {
        System.out.println("Solving puzzle automatically...........................");
        initialState.resetMoves();
        int bestHeuristic = initialState.calculateHeuristic();
        Board bestBoard = new Board();
        Node initialNode = new Node(initialState);
        queue.add(initialNode);
        hmap.put(initialNode.toString(), initialNode);
        int numAllMoves = 1;
        while(!queue.isEmpty()) {
            Node currentNode = queue.remove();
            numAllMoves++;
            if (currentNode.board.calculateHeuristic() < bestHeuristic) {
                bestHeuristic = currentNode.board.calculateHeuristic();
                bestBoard = currentNode.board;
            }
            // heuristic value of 0 means we have reached a solution
            if (currentNode.board.calculateHeuristic() == 0) {
                printSolution(currentNode, getNumMoves(currentNode));
                System.out.println("Done.");
                System.exit(0);
            }
            // stores all the possible moves of the current board in hmap and priority queue
            int[] possibleMoves = currentNode.board.possibleMoves();
            for (int i=possibleMoves.length-1; i>=0; i--) {
                Board childBoard = new Board(currentNode.board.getString());
                childBoard.move(possibleMoves[i]);
                if (!hmap.containsKey(childBoard.getString())) {
                    Node childNode = new Node(childBoard);
                    childNode.parent = currentNode;
                    queue.add(childNode);
                    hmap.put(childNode.board.getString(), childNode);
                }
            }
        }
        // if we reach here, no solution was found
        System.out.println();
        System.out.println();
        System.out.println("All " + numAllMoves + " moves have been tried.");
        System.out.println("That puzzle is impossible to solve.  Best board found was: ");
        bestBoard.displayBoardWithHeuristic();
        System.out.println("Exiting program.");
    }

    /*
     *  this is a helper function used to number the boards when it's solved
     */
    int getNumMoves(Node n) {
        int numMoves = 0;
        while (n != null) {
            numMoves++;
            n = n.parent;
        }
        return numMoves;
    }

    /*
     *  this function is used to print every move executed to solve the board
     */
    void printSolution(Node n, int moveNumber) {
        if (n == null) return;
        printSolution(n.parent, moveNumber-1);
        System.out.println(moveNumber + ".");
        n.board.displayBoardWithHeuristic();
    }
}