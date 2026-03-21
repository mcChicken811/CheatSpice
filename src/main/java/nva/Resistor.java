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
}
