package nva;

public class DCCurrentSource extends Component {
    private double dcCurrent;

    protected DCCurrentSource(double dcCurrent) {this.dcCurrent = dcCurrent;}

    @Override
    public String toString() {
        return super.toString("DC Current Source");
    }

    public double getDcCurrent() {
        return this.dcCurrent;
    }

    @Override
    public boolean isIndependentCurrentComp() {
        return false;
    }

    @Override
    protected LinearEquation getCurrentLinearEquationCoefficients() {
        LinearEquation eq = new LinearEquation(this.getBelongCircuit());

        eq.addEqualityValue(this.dcCurrent);

        return eq;
    }
}
