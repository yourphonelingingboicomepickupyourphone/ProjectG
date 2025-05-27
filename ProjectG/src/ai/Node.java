package ai;

public class Node {

    Node parent;
    public int col;
    public int row;
    public int gCost; // Cost from start to this node
    public int hCost; // Heuristic cost from this node to end
    public int fCost; // Total cost (gCost + hCost)

    boolean solid;
    boolean open;
    boolean checked;

    public Node(int col, int row) {
        this.col = col;
        this.row = row;
        
    }
}
