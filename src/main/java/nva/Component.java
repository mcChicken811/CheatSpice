package nva;

public class Component {
    private Node node1;
    private Node node2;
    private Circuit belongCircuit;

    /** analysis records */
    /** by PSC convention */
    private Double currentThrough;
    /** reference polarity is positive at node1 and negative at node2 */
    private Double voltageAcross;
    private Integer independentCurrentCompIndex;

    protected Component() {
        this.node1 = null;
        this.node2 = null;
        this.belongCircuit = null;
    }

    @Override
    public String toString() {
        if (this.voltageAcross == null || this.currentThrough == null)
            return "Generic Component: (N/A)";

        return String.format("Generic Component: (Voltage: %.2f, Current: %.2f)",
                this.voltageAcross, this.currentThrough);
    }

    /** use this template to override toString() of each component */
    public String toString(String componentName) {
        if (this.voltageAcross == null || this.currentThrough == null)
            return String.format("%s: (N/A)", componentName);

        return String.format("%s: (Voltage: %.2f, Current: %.2f)",
                componentName,
                this.voltageAcross, this.currentThrough);
    }

    /** shall only be used by Circuit when adding the component in it */
    protected void setBelongCircuit(Circuit circuit) {
        this.belongCircuit = circuit;
    }

    protected Integer getIndependentCurrentCompIndex() {
        return this.independentCurrentCompIndex;
    }

    protected void updateVoltageAcross() {
        this.voltageAcross = this.getVoltageByNodeVoltage();
    }

    /** override this function for each component that are current dependent
     * to update its current through by its voltage across
     */
    protected void updateCurrentThrough() {
        return;
    }

    /** shall only be used when logging a solution onto the circuit */
    protected void setCurrentThrough(double current) {
        this.currentThrough = current;
    }

    /** return the direction of reference current direction relative to the node given
     *
     * @return returns 1.0 if the PSC reference current direction point away from node
     * negative otherwise, returns null if the component is not connected to the node provided
     */
    public Double getReferenceCurrentDirectionByNode(Node node) {
        if (node == null ||
                (this.node1 != node && this.node2 != node)) return null;

        return this.node1 == node ? 1.0 : -1.0;
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

    /** attempt to force the component to connect to positive node
     * and negative node
     * @param positiveNode the positive node of the component
     * @param negativeNode the negative node of the component
     */
    public void connectTo(Node positiveNode, Node negativeNode) {
        if (positiveNode == null || negativeNode == null) return;
        this.disconnectFromNode(1);
        this.disconnectFromNode(2);
        this.connectToNode(positiveNode, 1);
        this.connectToNode(negativeNode, 2);
    }

    public Double getVoltageByNodeVoltage() {
        if (this.node1 == null ||
            this.node2 == null ||
            this.node1.getNodeVoltage() == null ||
            this.node2.getNodeVoltage() == null) return 0.0;

        return this.node1.getNodeVoltage() - this.node2.getNodeVoltage();
    }

    /** for non-independent current sources,...
     * combine this equation with other current coefficient linear equations
     * of components connected to the same node, multiply by the current direction
     * to obtain the full KCL equation
     *
     * for constant current components, the current shall be the value of the linear equation
     *
     * Override this function for each non-independent current sources
     *
     * @return returns a linear equation
     *      of only coefficients of node voltage and independent currents
     *      that describe the current through this source from positive to negative node
     *
     *      returns null if called by non-independent current source.
     */
    protected LinearEquation getCurrentLinearEquationCoefficients() {
        return null;
    }

    /** for independent current sources
     *
     * override this method for each independent current components
     *
     * @return returns the KVL applying on this component.
     */
    protected LinearEquation getKVLOfIndependentCurrentComponent() {
        return null;
    }


}
