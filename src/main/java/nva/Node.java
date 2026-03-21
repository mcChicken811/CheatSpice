package nva;

public class Node {
    private Double nodeVoltage;
    private boolean isReferenceNode;
    private Component[] components;
    private int maxComponents;
    private int numOfComponents;

    /* for node voltage analysis */
    @Deprecated
    private int numOfBranches;
    @Deprecated
    private Branch[] connectedBranches;

    private static final int DEFAULT_MAX_COMPONENTS = 10;

    public int getNumOfComponents() {
        return this.numOfComponents;
    }

    protected boolean isLonelyNode() { return this.numOfComponents <= 1; }

    protected boolean isMainNode() { return this.numOfComponents >= 3; }

    protected boolean isConnectedBranchesAtMaximumCapacity() {
        return this.numOfBranches >= this.maxComponents;
    }

    /** make the node with the default maximum components */
    protected Node() {
        this(Node.DEFAULT_MAX_COMPONENTS);
    }

    /** auto set maxComponents to default value if an invalid value is entered
     * for example, less than or equal to 0 */
    protected Node(int maxComponents) {
        if (maxComponents <= 0) this.maxComponents = Node.DEFAULT_MAX_COMPONENTS;
        this.components = new Component[this.maxComponents];
        this.numOfComponents = 0;

        this.nodeVoltage = null;
        this.isReferenceNode = false;

    }

    /** connect the given component to this node as its node(asNode) (1 or 2)
     * will not add component if nothing is passed (null is passed)
     * will not add component if the node already hold its maximum number of components
     * will not add component if the node to be connected as is not 1 or 2
     * will not add component if the node to be connected is already connected to some other node */
    public void connectComponent(Component component, int asNode) {
        if (component == null) return;
        if (this.numOfComponents >= this.maxComponents) return;
        if (asNode != 1 && asNode != 2) return;

        if (asNode == 1) {
            if (component.getNode1() != null) return;
            component.setNode1(this);
        } else if (asNode == 2) {
            if (component.getNode2() != null) return;
            component.setNode2(this);
        }

        this.components[this.numOfComponents++] = component;
    }

    /** disconnect the component from this node if it is connected of course
     * will not do anything if the component is found to be not connected to this node
     * */
    public void disconnectComponent(Component component) {
        if (component == null) return;

        this.removeComponentFromList(component);
        if (component.getNode1() == this) component.setNode1(null);
        else if (component.getNode2() == this) component.setNode2(null);
    }

    /** remove the given component from the components list
     * if it was in that list, else do nothing */
    protected void removeComponentFromList(Component component) {
        /* search for this component index in the components array */
        int i;
        for (i = 0; i < this.numOfComponents; i++) {
            if (this.components[i] == component) break;
        }

        /* component is not found on this list */
        if (i == this.numOfComponents) return;

        /* move all the components behind the component forward by one index
        * by swapping ahead one by one */
        Component tmp;
        for (i = i + 1; i < this.numOfComponents; i++) {
            /* swap */
            tmp = this.components[i - 1];
            this.components[i - 1] = this.components[i];
            this.components[i] = tmp;
        }
        if (this.numOfComponents > 0) this.numOfComponents--;
    }

    public void connectComponent(Component component) {
        this.connectComponent(component, 1);
    }

    public Double getNodeVoltage() {
        return this.nodeVoltage;
    }

    public boolean isReferenceNode() {
        return this.isReferenceNode;
    }

    public Component[] getComponents() {
        return this.components;
    }

    protected void setReferenceNode(boolean to) { this.isReferenceNode = to; }

    @Deprecated
    protected void addBranchToBranchArray(Branch branch) {
        if (branch == null ||
            this.numOfBranches >= this.maxComponents) return;

        this.connectedBranches[this.numOfBranches++] = branch;
    }

    @Deprecated
    protected void resetBranchData() {
        this.numOfBranches = 0;
        this.connectedBranches = new Branch[this.maxComponents];
    }
}
