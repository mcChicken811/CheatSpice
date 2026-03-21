package nva;

public class Component {
    private Node node1;
    private Node node2;
    private Circuit belongCircuit;

    /** analysis records */
    private Double currentThrough;
    private Double voltageAcross;
    private Integer independentCurrentCompIndex;

    protected Component() {
        this.node1 = null;
        this.node2 = null;
        this.belongCircuit = null;
    }

    /** shall only be used by Circuit when adding the component in it */
    protected void setBelongCircuit(Circuit circuit) {
        this.belongCircuit = circuit;
    }

    public Circuit getBelongCircuit() {
        return this.belongCircuit;
    }

    public boolean isIndependentCurrentComp() {
        return false;
    }

    public Node getNode1() {
        return this.node1;
    }

    public Node getNode2() {
        return this.node2;
    }

    protected void setIndependentCurrentCompIndex(int index) {
        this.independentCurrentCompIndex = index;
    }

    protected void setNode1(Node node) {
        this.node1 = node;
    }

    protected void setNode2(Node node) {
        this.node2 = node;
    }

    /** remove all analysis logged values in the component */
    protected void cleanAnalysisRecord() {
        this.currentThrough = null;
        this.voltageAcross = null;
        this.independentCurrentCompIndex = null;
    }

    /** nodeIndex: 1 or 2, meaning to disconnect from node 1 or node 2*/
    public void disconnectFromNode(int nodeIndex) {
        if (nodeIndex != 1 && nodeIndex != 2) return;

        Node nodeToDisconnectFrom = nodeIndex == 1 ? this.node1 : this.node2;
        if (nodeToDisconnectFrom == null) return;

        nodeToDisconnectFrom.removeComponentFromList(this);
        if (nodeIndex == 1) this.setNode1(null);
        else this.setNode2(null);
    }

    /** connect the node(asNode) of this component to the given node
     * will not do anything if such connection is impossible,
     * will not do anything if node of other circuit is provided
     * */
    public void connectToNode(Node node, int asNode) {
        if (node == null) return;
        if (asNode != 1 && asNode != 2) return;
        if (node.getBelongCircuit() != this.belongCircuit) return;

        node.connectComponent(this, asNode);
    }

    public Double getVoltageByNodeVoltage() {
        if (this.node1 == null ||
            this.node2 == null ||
            this.node1.getNodeVoltage() == null ||
            this.node2.getNodeVoltage() == null) return 0.0;

        return this.node1.getNodeVoltage() - this.node2.getNodeVoltage();
    }
}
