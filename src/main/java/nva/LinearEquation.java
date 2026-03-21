package nva;

/** shall only used when running analysis */
public class LinearEquation {

    private int numberOfNodeVoltages;
    private int numberOfIndependentCurrents;

    private double[] data;

    protected LinearEquation(Circuit circuit) {
        this.numberOfNodeVoltages = circuit.getNumberOfNodes();
        this.numberOfIndependentCurrents = circuit.getNumberOfIndependentCurrentComponents();

        /* coefficient for nodevoltages, independent currents and the value of equality */
        this.data = new double[this.numberOfNodeVoltages + this.numberOfIndependentCurrents + 1];

        /* set all coefficients and value of equality to 0 */
        for (int i = 0; i < this.data.length; i++) {
            this.data[i] = 0.0;
        }
    }

    /** add the ith node voltage coefficient by the given value
     * will not do anything for index out of range */
    protected void addNodeVoltageCoeff(int i, double value) {
        if (i < 0 || i >= this.numberOfNodeVoltages) return;

        this.data[i] += value;
    }

    /** add the ith independent current coefficient by the given value
     * will not do anything for index out of range
     */
    protected void addIndependentCurrentCoeff(int i, double value) {
        if (i < 0 || i >= this.numberOfIndependentCurrents) return;

        this.data[i + this.numberOfNodeVoltages] += value;
    }

    /** add the equality value by the given value */
    protected void addEqualityValue(double value) {
        this.data[data.length - 1] += value;
    }
}
