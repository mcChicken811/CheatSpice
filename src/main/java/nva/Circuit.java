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
    public void addNode() {
        if (this.numberOfNodes >= Circuit.MAX_NODES) return;

        Node node = new Node();
        this.allNodes[this.numberOfNodes++] = node;
    }

    /** add a node to the circuit that is able to take max number of components of maxComponents
     * if there is any data space left */
    public void addNode(int maxComponents) {
        if (this.numberOfNodes >= Circuit.MAX_NODES) return;

        Node node = new Node(maxComponents);
        this.allNodes[this.numberOfNodes++] = node;
    }

    /** add a component to the circuit
     * if there is any data space left */
    public void addResistor(double resistance) {
        if (this.numberOfComponents >= Circuit.MAX_COMPONENTS) return;

        Resistor res = new Resistor(resistance);
        this.allComponents[this.numberOfComponents++] = res;
    }

    public void addDCVoltageSource(double voltage) {
        if (this.numberOfComponents >= Circuit.MAX_COMPONENTS) return;

        DCVoltageSource vsrc = new DCVoltageSource(voltage);
        this.allComponents[this.numberOfComponents++] = vsrc;
    }

    public void addWire() {
        if (this.numberOfComponents >= Circuit.MAX_COMPONENTS) return;

        Wire wire = new Wire();
        this.allComponents[this.numberOfComponents++] = wire;
    }

    public void addTheveninDevice(double theveninVoltage, double theveninResistance) {
        if (this.numberOfComponents >= Circuit.MAX_COMPONENTS) return;

        TheveninDevice theveninDevice = new TheveninDevice(theveninVoltage, theveninResistance);
        this.allComponents[this.numberOfComponents++] = theveninDevice;
    }


}
