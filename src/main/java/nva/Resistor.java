package nva;

public class Resistor extends Component {
    private double resistance;
    private static final double DEFAULT_RESISTANCE = 1.0;
    private static final double EPSILON = 1e-25;

    /** set the resistance to default value for invalid input
     * 0 is acceptable value */
    protected Resistor(double resistance) {
        if (Math.abs(Math.signum(resistance) - 1.0) < Resistor.EPSILON &&
            Math.abs(resistance) > Resistor.EPSILON)
            this.resistance = resistance;
        else this.resistance = Resistor.DEFAULT_RESISTANCE;
    }

    @Override
    public String toString() {
        return super.toString("Resistor");
    }

    public double getResistance() {
        return this.resistance;
    }

    @Override
    public boolean isIndependentCurrentComp() {
        return Math.abs(this.resistance) < Resistor.EPSILON;
    }

    @Override
    protected void updateCurrentThrough() {
        if (this.isIndependentCurrentComp()) return;
        this.setCurrentThrough(this.getVoltageByNodeVoltage() / this.resistance);
    }

    /** i = v1/R - v2/R, i in PSC */
    @Override
    protected LinearEquation getCurrentLinearEquationCoefficients() {
        if (this.isIndependentCurrentComp()) return null;

        int nodeIndex1 = this.getNode1().getNodeIndex();
        int nodeIndex2 = this.getNode2().getNodeIndex();

        LinearEquation eq = new LinearEquation(this.getBelongCircuit());

        eq.addNodeVoltageCoeff(nodeIndex1, 1.0 / this.resistance);
        eq.addNodeVoltageCoeff(nodeIndex2, -1.0 / this.resistance);

        return eq;
    }

    /** v1 - v2 = 0, for R = 0 */
    @Override
    protected LinearEquation getKVLOfIndependentCurrentComponent() {
        if (!this.isIndependentCurrentComp()) return null;

        int nodeIndex1 = this.getNode1().getNodeIndex();
        int nodeIndex2 = this.getNode2().getNodeIndex();

        LinearEquation eq = new LinearEquation(this.getBelongCircuit());

        eq.addNodeVoltageCoeff(nodeIndex1, 1.0);
        eq.addNodeVoltageCoeff(nodeIndex2, -1.0);

        return eq;
    }
}
