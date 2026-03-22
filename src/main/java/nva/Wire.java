package nva;

/** literally just a resistor with 0 resistance */
public class Wire extends Resistor {
    protected Wire() {
        super(0.0);
    }

    @Override
    public boolean isIndependentCurrentComp() {
        return true;
    }

    @Override
    public String toString() {
        return super.toString("Wire");
    }

    /** v1 - v2 = 0 */
    @Override
    protected LinearEquation getKVLOfIndependentCurrentComponent() {
        int nodeIndex1 = this.getNode1().getNodeIndex();
        int nodeIndex2 = this.getNode2().getNodeIndex();

        LinearEquation eq = new LinearEquation(this.getBelongCircuit());

        eq.addNodeVoltageCoeff(nodeIndex1, 1.0);
        eq.addNodeVoltageCoeff(nodeIndex2, -1.0);

        return eq;
    }
}
