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

}
