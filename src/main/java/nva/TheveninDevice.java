package nva;

public class TheveninDevice extends Component {
    private double theveninResistance;
    private double theveninVoltage;

    private static final double EPSILON = 1e-25;

    protected TheveninDevice(double theveninVoltage, double theveninResistance) {
        this.theveninResistance = theveninResistance;
        this.theveninVoltage = theveninVoltage;
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
