package nva;

public class DCVoltageSource extends Component {
    private double dcVoltage;

    protected DCVoltageSource(double dcVoltage) {
        this.dcVoltage = dcVoltage;
    }

    public double getDCVoltage() {
        return this.dcVoltage;
    }

    @Override
    public boolean isIndependentCurrentComp() {
        return true;
    }
}
