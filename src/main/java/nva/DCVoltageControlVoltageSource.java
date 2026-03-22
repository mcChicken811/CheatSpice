package nva;

public class DCVoltageControlVoltageSource extends Component {
    private Node controlNodePositive;
    private Node controlNodeNegative;
    private double coeff;

    protected DCVoltageControlVoltageSource(double coeff, Node controlNodePositive,
                                            Node controlNodeNegative) {
        this.coeff = coeff;
        this.controlNodePositive = controlNodePositive;
        this.controlNodeNegative = controlNodeNegative;
    }

    public Node getControlNodePositive() {
        return this.controlNodePositive;
    }

    public Node getControlNodeNegative() {
        return this.controlNodeNegative;
    }

    public double getCoeff() {
        return this.coeff;
    }

    @Override
    public String toString() {
        return super.toString("DC Voltage Control Voltage Source (VCVS)");
    }

    @Override
    public boolean isIndependentCurrentComp() {
        return true;
    }

    public void setControlNodePositive(Node node) {
        if (node == null) throw new IllegalArgumentException(
                "Attempting to set positive node of DCVCVS to null," +
                        "Cannot set this node to nothing."
        );

        if (node.getBelongCircuit() != this.getBelongCircuit()) throw new IllegalArgumentException(
                "Attempting to make a DCVCVS depends on node outside the circuit"
        );

        this.controlNodePositive = node;
    }

    public void setControlNodeNegative(Node node) {
        if (node == null) throw new IllegalArgumentException(
                "Attempting to set negative node of DCVCVS to null," +
                        "Cannot set this node to nothing."
        );

        if (node.getBelongCircuit() != this.getBelongCircuit()) throw new IllegalArgumentException(
                "Attempting to make a DCVCVS depends on node outside the circuit"
        );

        this.controlNodeNegative = node;
    }

    @Override
    protected LinearEquation getKVLOfIndependentCurrentComponent() {
        int nodeIndex1 = this.getNode1().getNodeIndex();
        int nodeIndex2 = this.getNode2().getNodeIndex();

        int positiveNodeIndex = this.controlNodePositive.getNodeIndex();
        int negativeNodeIndex = this.controlNodeNegative.getNodeIndex();

        LinearEquation eq = new LinearEquation(this.getBelongCircuit());

        eq.addNodeVoltageCoeff(nodeIndex1, 1.0);
        eq.addNodeVoltageCoeff(nodeIndex2, -1.0);
        eq.addNodeVoltageCoeff(positiveNodeIndex, -1.0 * this.coeff);
        eq.addNodeVoltageCoeff(negativeNodeIndex, this.coeff);

        return eq;
    }
}
