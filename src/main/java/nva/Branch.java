package nva;

@Deprecated
/** This resembles any generic branch of our scope of analysis
 * @deprecated please don't use this piece of shit */
public class Branch {
    /* these index refers to its index in the scope of NVA
     * nodeIndex refers to the index of the connected main node in NVA
     * if the connected node is not main but lonely, then the index is null
     */
    private Integer node1Index;
    private Integer node2Index;
    private int index;

    private Node node1;
    private Node node2;

    private double theveninVoltage;
    private double theveninResistance;

    protected Branch(double theveninVoltage, double theveninResistance,
                     int index) {
        this.theveninResistance = theveninResistance;
        this.theveninVoltage = theveninVoltage;

        this.node1Index = null;
        this.node2Index = null;
        this.index = index;

        this.node1 = null;
        this.node2 = null;
    }


    /** connect the node(asNode) of the branch to the given node
     * will not connect if no node is provided
     * asNode shall be 1 or 2
     * the index of the node in NVA scope shall also be provided,
     * please provide null if node is a lonely node
     * a branch should only be connected to a main node or a lonely node
     * will also fail to connect if the branch node(asNode) is already connected to some other nodes
     * will also fail to connect if node data is at its limit */
    protected void connectToNode(Node node, int asNode, Integer nodeIndex) {
        if (node == null ||
                (asNode != 1 && asNode != 2) ||
        (!node.isMainNode() && !node.isLonelyNode()) ||
        node.isConnectedBranchesAtMaximumCapacity()) return;

        if (asNode == 1) {
            if (this.node1 != null) return;

            if (node.isLonelyNode()) this.node1Index = null;
            else this.node1Index = nodeIndex;

            this.node1 = node;
        } else if (asNode == 2) {
            if (this.node2 != null) return;

            if (node.isLonelyNode()) this.node2Index = null;
            else this.node2Index = null;

            this.node2 = node;
        }

        node.addBranchToBranchArray(this);
    }
}
