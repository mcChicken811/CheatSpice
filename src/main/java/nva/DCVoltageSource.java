package nva;

public class DCVoltageSource extends Component {
    private double dcVoltage;

    protected DCVoltageSource(double dcVoltage) {
        this.dcVoltage = dcVoltage;
    }

    @Override
    public String toString() {
        return super.toString("DC Voltage Sourece");
    }

    public double getDCVoltage() {
        return this.dcVoltage;
    }

    @Override
    public boolean isIndependentCurrentComp() {
        return true;
    }

    @Override
    protected LinearEquation getKVLOfIndependentCurrentComponent() {
        int nodeIndex1 = this.getNode1().getNodeIndex();
        int nodeIndex2 = this.getNode2().getNodeIndex();

        LinearEquation eq = new LinearEquation(this.getBelongCircuit());

        eq.addNodeVoltageCoeff(nodeIndex1, 1.0);
        eq.addNodeVoltageCoeff(nodeIndex2, -1.0);
        eq.addEqualityValue(this.dcVoltage);

        return eq;
    }
}
