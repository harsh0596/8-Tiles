import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

/*
 *  This class executes the board solver using:
 *  HashMap and PriorityQueue
 */
class SearchTree {
    private Board initialState;
    private HashMap<String, Node> hmap;
    private PriorityQueue<Node> queue;

    SearchTree(Board board) {
        this.initialState = board;
        hmap = new HashMap<>();
        queue = new PriorityQueue<>(5, Comparator.comparingInt(n -> n.getBoard().calculateHeuristic()));
    }

    /**
     *  A* implementation
     *  hashmap is used to check if the board state is already checked or not
     *  priority queue is used to get the parent pointer
     */
    void solve() {
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
            if (currentNode.getBoard().calculateHeuristic() < bestHeuristic) {
                bestHeuristic = currentNode.getBoard().calculateHeuristic();
                bestBoard = currentNode.getBoard();
            }
            // heuristic value of 0 means we have reached a solution
            if (currentNode.getBoard().calculateHeuristic() == 0) {
                printSolution(currentNode, getNumMoves(currentNode));
                System.out.println("Done.");
                System.exit(0);
            }
            // stores all the possible moves of the current board in hmap and priority queue
            int[] possibleMoves = currentNode.getBoard().possibleMoves();
            for (int i=possibleMoves.length-1; i>=0; i--) {
                Board childBoard = new Board(currentNode.getBoard().getString());
                childBoard.move(possibleMoves[i]);
                if (!hmap.containsKey(childBoard.getString())) {
                    Node childNode = new Node(childBoard);
                    childNode.setParent(currentNode);
                    queue.add(childNode);
                    hmap.put(childNode.getBoard().getString(), childNode);
                }
            }
        }
        // if we reach here, no solution was found
        outputImpossibleBoardInfo(bestBoard, numAllMoves);
    }

    /**
     * @param bestBoard best board found
     * @param numAllMoves total number of moves made
     */
    private void outputImpossibleBoardInfo(Board bestBoard, int numAllMoves) {
        System.out.println();
        System.out.println();
        System.out.println("All " + numAllMoves + " moves have been tried.");
        System.out.println("That puzzle is impossible to solve.  Best board found was: ");
        bestBoard.displayBoardWithHeuristic();
        System.out.println("Exiting program.");
    }

    /**
     *  helper function used to number the boards when it's solved
     *  @param node deepest Node
     */
    private int getNumMoves(Node node) {
        int numMoves = 0;
        while (node != null) {
            numMoves++;
            node = node.getParent();
        }
        return numMoves;
    }

    /**
     *  Output every move executed to solve the board
     */
    private void printSolution(Node n, int moveNumber) {
        if (n == null) return;
        printSolution(n.getParent(), moveNumber-1);
        System.out.println(moveNumber + ".");
        n.getBoard().displayBoardWithHeuristic();
    }
}