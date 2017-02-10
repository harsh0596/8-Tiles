package sample;

/*
 *  Node class containing a board and a pointer to a parent
 */
public class Node {
    public Board board;
    public Node parent;

    // constructor to put a board inside this node
    // parent is set to null
    public Node (Board board) {
        this.board = board;
        parent = null;
    }
}