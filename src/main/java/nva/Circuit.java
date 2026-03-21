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

    public Circuit() {
        this.allNodes = new Node[Circuit.MAX_NODES];
        this.allComponents = new Component[Circuit.MAX_COMPONENTS];

        this.numberOfComponents = 0;
        this.numberOfNodes = 0;
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

    /** the id of a node is its index in a circuit
    * returns null if index out of range */
    public Node getNode(int id) {
        if (id < 0 || id >= this.numberOfNodes) return null;

        return this.allNodes[id];
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

    /** add a node to the circuit
     * if there is any data space left */
    public Node addNode() {
        if (this.numberOfNodes >= Circuit.MAX_NODES) return null;

        Node node = new Node();
        this.allNodes[this.numberOfNodes++] = node;

        return node;
    }

    /** add a node to the circuit that is able to take max number of components of maxComponents
     * if there is any data space left */
    public Node addNode(int maxComponents) {
        if (this.numberOfNodes >= Circuit.MAX_NODES) return null;

        Node node = new Node(maxComponents);
        this.allNodes[this.numberOfNodes++] = node;

        return node;
    }

    /** add a component to the circuit
     * if there is any data space left */
    public Resistor addResistor(double resistance) {
        if (this.numberOfComponents >= Circuit.MAX_COMPONENTS) return null;

        Resistor res = new Resistor(resistance);
        this.allComponents[this.numberOfComponents++] = res;

        return res;
    }

    public DCVoltageSource addDCVoltageSource(double voltage) {
        if (this.numberOfComponents >= Circuit.MAX_COMPONENTS) return null;

        DCVoltageSource vsrc = new DCVoltageSource(voltage);
        this.allComponents[this.numberOfComponents++] = vsrc;

        return vsrc;
    }

    public Wire addWire() {
        if (this.numberOfComponents >= Circuit.MAX_COMPONENTS) return null;

        Wire wire = new Wire();
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


}
