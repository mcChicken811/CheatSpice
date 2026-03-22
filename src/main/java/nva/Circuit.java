package nva;

/** a circuit is meant to be what hold all the nodes and components
 * reference in say a circuit
 */
public class Circuit {
    private Node[] allNodes;
    private Component[] allComponents;
    private Integer numberOfNodes;
    private Integer numberOfComponents;

    private static final int MAX_COMPONENTS = 10000;
    private static final int MAX_NODES = 10000;

    /** analysis records */
    private Component[] independentCurrentComponents;
    private int numberOfIndependentCurrentComponents;

    public Circuit() {
        this.allNodes = new Node[Circuit.MAX_NODES];
        this.allComponents = new Component[Circuit.MAX_COMPONENTS];

        this.numberOfComponents = 0;
        this.numberOfNodes = 0;
    }

    @Override
    public String toString() {
        String circuitDescription = "Components:\n";

        for (int i = 0; i < this.numberOfComponents; i++) {
            circuitDescription += "    ";
            circuitDescription += this.getComponent(i).toString();
            circuitDescription += "\n";
        }

        return circuitDescription;
    }

    /** return null if no component is in the circuit */
    public Component getNewestComponent() {
        if (this.numberOfComponents == 0) return null;
        return this.allComponents[this.numberOfComponents - 1];
    }

    /** the id of a component is its index in a circuit
    * returns null if index out of range */
    public Component getComponent(int id) {
        if (id < 0 || id >= this.numberOfComponents) return null;

        return this.allComponents[id];
    }

    /** returns the node in the circuit of the given id
     *
     * @param id the id of the node in the circuit
     * @return the node
     */
    public Node getNode(int id) {
        if (id < 0 || id >= this.numberOfNodes) throw new IndexOutOfBoundsException(
            String.format("Cannot access node of id: %d, in the circuit as it is not" +
                    "in the circuit", id)
        );

        return this.allNodes[id];
    }

    /** return the ith independent current component that is available in the list
     *
     * @param id the index of the independent current component
     * @return returns null when component of such id or the array is unavailable
     */
    protected Component getIndependentCurrentComponent(int id) {
        if (!this.isIndepentCurrentComponentProvided() ||
                id < 0 || id >= this.numberOfIndependentCurrentComponents) return null;

        return this.independentCurrentComponents[id];
    }

    protected Node[] getAllNodes() {
        return this.allNodes;
    }

    protected Component[] getAllComponents() {
        return this.allComponents;
    }

    protected Integer getNumberOfNodes() {
        return this.numberOfNodes;
    }

    protected Integer getNumberOfComponents() {
        return this.numberOfComponents;
    }

    /** return 0 if indpendent current component list is not initialized */
    protected int getNumberOfIndependentCurrentComponents() {
        if (!this.isIndepentCurrentComponentProvided()) return 0;
        return this.numberOfIndependentCurrentComponents;
    }

    /** add a node to the circuit
     * if there is any data space left */
    public Node addNode() {
        if (this.numberOfNodes >= Circuit.MAX_NODES) return null;

        Node node = new Node();
        node.setBelongCircuit(this);
        node.setNodeIndex(this.numberOfNodes);
        this.allNodes[this.numberOfNodes++] = node;

        return node;
    }

    /** add a node to the circuit that is able to take max number of components of maxComponents
     * if there is any data space left */
    public Node addNode(int maxComponents) {
        if (this.numberOfNodes >= Circuit.MAX_NODES) return null;

        Node node = new Node(maxComponents);
        node.setBelongCircuit(this);
        node.setNodeIndex(this.numberOfNodes);
        this.allNodes[this.numberOfNodes++] = node;

        return node;
    }

    /** add a component to the circuit
     * if there is any data space left */
    public Resistor addResistor(double resistance) {
        if (this.numberOfComponents >= Circuit.MAX_COMPONENTS) return null;

        Resistor res = new Resistor(resistance);
        res.setBelongCircuit(this);
        this.allComponents[this.numberOfComponents++] = res;

        return res;
    }

    public DCVoltageSource addDCVoltageSource(double voltage) {
        if (this.numberOfComponents >= Circuit.MAX_COMPONENTS) return null;

        DCVoltageSource vsrc = new DCVoltageSource(voltage);
        vsrc.setBelongCircuit(this);
        this.allComponents[this.numberOfComponents++] = vsrc;

        return vsrc;
    }

    public DCCurrentSource addDCCurrentSource(double current) {
        if (this.numberOfComponents >= Circuit.MAX_COMPONENTS) return null;

        DCCurrentSource csrc = new DCCurrentSource(current);
        csrc.setBelongCircuit(this);
        this.allComponents[this.numberOfComponents++] = csrc;

        return csrc;
    }

    public DCVoltageControlVoltageSource addDCVCVS(double coeff, int posNodeID, int negNodeID) {
        if (this.numberOfComponents >= Circuit.MAX_COMPONENTS) throw new RuntimeException(
                "Cannot add more components to the circuit, maximum number of components had been reached"
        );

        Node pos = this.getNode(posNodeID);
        Node neg = this.getNode(negNodeID);

        DCVoltageControlVoltageSource vcvs = new DCVoltageControlVoltageSource(coeff, pos, neg);
        vcvs.setBelongCircuit(this);
        this.allComponents[this.numberOfComponents++] = vcvs;
    }

    public Wire addWire() {
        if (this.numberOfComponents >= Circuit.MAX_COMPONENTS) return null;

        Wire wire = new Wire();
        wire.setBelongCircuit(this);
        this.allComponents[this.numberOfComponents++] = wire;

        return wire;
    }

    public TheveninDevice addTheveninDevice(double theveninVoltage, double theveninResistance) {
        if (this.numberOfComponents >= Circuit.MAX_COMPONENTS) return null;

        TheveninDevice theveninDevice = new TheveninDevice(theveninVoltage, theveninResistance);
        this.allComponents[this.numberOfComponents++] = theveninDevice;

        return theveninDevice;
    }

    /** create nodes at all endpoint of the components that are not connected to any */
    protected void completeCircuit() {
        for (int i = 0; i < this.numberOfComponents; i++) {
            Component component = this.getComponent(i);

            if (component.getNode1() == null) {
                Node node1 = this.addNode(1);
                component.connectToNode(node1, 1);
            }

            if (component.getNode2() == null) {
                Node node2 = this.addNode(1);
                component.connectToNode(node2, 2);
            }
        }
    }

    /** remove all node voltage and component voltage, current through values
     * that were previously logged by analysis */
    protected void clearAnalysisRecords() {
        for (int i = 0; i < this.getNumberOfNodes(); i++) {
            this.getNode(i).cleanAnalysisRecord();
        }

        for (int i = 0; i < this.getNumberOfComponents(); i++) {
            this.getComponent(i).cleanAnalysisRecord();
        }

        this.independentCurrentComponents = null;
        this.numberOfIndependentCurrentComponents = 0;
    }

    /** update the array of all independent current components in the circuit
     * and log the index into each component */
    protected void updateIndependentCurrentComponents() {
        this.numberOfIndependentCurrentComponents = 0;
        this.independentCurrentComponents = new Component[this.getNumberOfComponents()];
        for (int i = 0; i < this.getNumberOfComponents(); i++) {
            if (this.getComponent(i).isIndependentCurrentComp()) {
                this.getComponent(i).setIndependentCurrentCompIndex(this.numberOfIndependentCurrentComponents);
                this.independentCurrentComponents[this.numberOfIndependentCurrentComponents++] = this.getComponent(i);
            }
        }
    }

    protected boolean isIndepentCurrentComponentProvided() {
        return this.independentCurrentComponents != null;
    }

}
