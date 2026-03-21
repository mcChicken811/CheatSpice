package nva;

import org.ejml.data.DMatrixRMaj;

public class Solution {
    private Circuit belongCircuit;

    private int numberOfNodeVoltageSolutions;
    private int numberOfIndependentCurrentSolutions;
    private double[] data;
    private boolean isSolved;

    /** this object can only be made after the independent current components of the circuit
     * has been updated
     * @param circuit the circuit which this solution belongs to
     */
    protected Solution(Circuit circuit) {
        this.belongCircuit = circuit;
        this.numberOfNodeVoltageSolutions = circuit.getNumberOfNodes();
        this.numberOfIndependentCurrentSolutions = circuit.getNumberOfIndependentCurrentComponents();
        this.data = new double[this.numberOfNodeVoltageSolutions + this.numberOfIndependentCurrentSolutions];
        this.isSolved = false;

        /* initialize data to all 0.0 */
        for (int i = 0; i < this.data.length; i++)
            this.data[i] = 0.0;
    }

    protected boolean isSolved() {
        return this.isSolved;
    }

    /** should only be used when the solution represent one that is solved result */
    protected void setSolved(boolean isSolved) {
        this.isSolved = isSolved;
    }

    /** returns null if the solution is unsolved
     * or if the nodeID is out of range */
    protected Double getNodeVoltageSolution(int nodeID) {
        if (nodeID < 0 || nodeID >= this.numberOfNodeVoltageSolutions ||
            !this.isSolved) return null;

        return this.data[nodeID];
    }

    /** returns null if the solution is unsolved
     * or if the given id is out of range */
    protected Double getIndependentCurrentSolution(int independentCurrentComponentID) {
        if (independentCurrentComponentID < 0 ||
            independentCurrentComponentID >= this.numberOfIndependentCurrentSolutions ||
            !this.isSolved)  return null;

        return this.data[this.numberOfNodeVoltageSolutions + independentCurrentComponentID];
    }

    /** logs the solution from EJML matrix form to this data structure
     *
     * @param x the solution in EJML matrix form
     */
    protected void logSolution(DMatrixRMaj x) {
        if (x == null ||
            x.numRows != this.data.length ||
            x.numCols != 1) return;

        for (int i = 0; i < x.numRows; i++) {
            this.data[i] = x.get(i, 0);
        }

        this.isSolved = true;
    }

    /** logs the solution if there is any onto the circuit of belonging
     *
     */
    protected void logSolutionOnCircuit() {
        if (this.belongCircuit == null ||
            !this.isSolved ||
            this.belongCircuit.getNumberOfIndependentCurrentComponents() !=
                    this.numberOfIndependentCurrentSolutions ||
            this.belongCircuit.getNumberOfNodes() !=
                    this.numberOfNodeVoltageSolutions) return;

        /* log the node voltages */
        for (int i = 0; i < this.numberOfNodeVoltageSolutions; i++) {
            Node node = this.belongCircuit.getNode(i);

            node.setNodeVoltage(this.getNodeVoltageSolution(i));
        }

        /* log the voltage across each component */
        for (int i = 0; i < this.belongCircuit.getNumberOfComponents(); i++) {
            Component component = this.belongCircuit.getComponent(i);

            component.updateVoltageAcross();
        }

        /* log the current through onto each independent current components */
        for (int i = 0; i < this.numberOfIndependentCurrentSolutions; i++) {
            Component component = this.belongCircuit.getIndependentCurrentComponent(i);

            component.setCurrentThrough(this.getIndependentCurrentSolution(i));
        }

        /* update the current through of each device */
        for (int i = 0; i < this.belongCircuit.getNumberOfComponents(); i++) {
            Component component = this.belongCircuit.getComponent(i);

            component.updateCurrentThrough();
        }

    }

}
