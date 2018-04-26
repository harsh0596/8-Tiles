/*
 *  Node class containing a board and a pointer to a parent
 */
class Node {
    private Board board;
    private Node parent;

    Node (Board board) {
        this.board = board;
        parent = null;
    }

    Board getBoard() {
        return board;
    }

    Node getParent() {
        return parent;
    }

    void setParent(Node parent) {
        this.parent = parent;
    }
}