package nva;

public class TheveninDevice extends Component {
    private double theveninResistance;
    private double theveninVoltage;

    private static final double EPSILON = 1e-25;

    protected TheveninDevice(double theveninVoltage, double theveninResistance) {
        this.theveninResistance = theveninResistance;
        this.theveninVoltage = theveninVoltage;
    }

    @Override
    public String toString() {
        return super.toString("Thevenin Device");
    }

    @Override
    public boolean isIndependentCurrentComp() {
        return Math.abs(this.theveninResistance) < TheveninDevice.EPSILON;
    }

    @Override
    protected void updateCurrentThrough() {
        if (this.isIndependentCurrentComp()) return;
        this.setCurrentThrough((this.getVoltageByNodeVoltage() - this.theveninVoltage) / this.theveninResistance);
    }

    public double getTheveninResistance() {
        return this.theveninResistance;
    }

    public double getTheveninVoltage() {
        return this.theveninVoltage;
    }

    public boolean hasResistance() {
        return Math.abs(this.theveninResistance) < TheveninDevice.EPSILON;
    }

    /** i = v1/RT - v2/RT - vT/RT */
    @Override
    protected LinearEquation getCurrentLinearEquationCoefficients() {
        if (this.isIndependentCurrentComp()) return null;

        int nodeIndex1 = this.getNode1().getNodeIndex();
        int nodeIndex2 = this.getNode2().getNodeIndex();

        LinearEquation eq = new LinearEquation(this.getBelongCircuit());

        eq.addNodeVoltageCoeff(nodeIndex1, 1.0 / this.theveninResistance);
        eq.addNodeVoltageCoeff(nodeIndex2, -1.0 / this.theveninResistance);
        eq.addEqualityValue(this.theveninVoltage / this.theveninResistance);

        return eq;
    }

    /** v1 - v2 = vT */
    @Override
    protected LinearEquation getKVLOfIndependentCurrentComponent() {
        if (!this.isIndependentCurrentComp()) return null;

        int nodeIndex1 = this.getNode1().getNodeIndex();
        int nodeIndex2 = this.getNode2().getNodeIndex();

        LinearEquation eq = new LinearEquation(this.getBelongCircuit());

        eq.addNodeVoltageCoeff(nodeIndex1, 1.0);
        eq.addNodeVoltageCoeff(nodeIndex2, -1.0);
        eq.addEqualityValue(this.theveninVoltage);

        return eq;

    }
}
