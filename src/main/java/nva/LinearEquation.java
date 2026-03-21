package nva;

import org.ejml.data.DMatrixRMaj;
import org.ejml.data.DMatrixSparseCSC;
import org.ejml.interfaces.linsol.LinearSolverSparse;
import org.ejml.sparse.csc.factory.LinearSolverFactory_DSCC;
import org.ejml.sparse.FillReducing;

/** shall only used when running analysis */
public class LinearEquation {

    private int numberOfNodeVoltages;
    private int numberOfIndependentCurrents;
    private Circuit belongCircuit;

    private double[] data;

    private static final int RECOMMENDED_CAPACITY_MODIFIER_FOR_SPARSE_COEFF_MATRIX = 5;

    protected LinearEquation(Circuit circuit) {
        this.numberOfNodeVoltages = circuit.getNumberOfNodes();
        this.numberOfIndependentCurrents = circuit.getNumberOfIndependentCurrentComponents();

        /* coefficient for nodevoltages, independent currents and the value of equality */
        this.data = new double[this.numberOfNodeVoltages + this.numberOfIndependentCurrents + 1];

        /* set all coefficients and value of equality to 0 */
        for (int i = 0; i < this.data.length; i++) {
            this.data[i] = 0.0;
        }

        this.belongCircuit = circuit;
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

    /** Gives the coefficeint matrix in terms of EJML Matrix library sparse matrix
     * data structure
     *
     * @param eqs the array of linear equations
     * @param capacityModifier the initial capacity of the sparse matrix is equal to this multiplied
     *                         by the number of unknown for this system of linear equations,
     *                         recommended value is 5
     * @return returns this sparse coefficient matrix of the given set of linear equations,
     *          returns null if the linear equations are for different circuit,
     *          or if the dimension of the linear equations does not match
     */
    protected static DMatrixSparseCSC getSparseCoefficientMatrix(LinearEquation[] eqs, int capacityModifier) {
        if (eqs == null ||
            eqs.length == 0) return null;

        /* check if the equations belong to the same circuit */
        Circuit circuitOfEquations = eqs[0].belongCircuit;
        int dimOfEquations = eqs[0].data.length - 1;
        for (int i = 0; i < eqs.length; i++) {
            if (circuitOfEquations != eqs[i].belongCircuit) return null;
            if (dimOfEquations != eqs[i].data.length - 1) return null;
        }

        DMatrixSparseCSC matrix = new DMatrixSparseCSC(eqs.length,
                dimOfEquations, capacityModifier * dimOfEquations);

        /* log the coefficients into the matrix */
        for (int row = 0; row < eqs.length; row++) {
            for (int col = 0; col < dimOfEquations; col++) {
                matrix.set(row, col, eqs[row].data[col]);
            }
        }

        return matrix;
    }

    /** get the matrix with recommended capacity modifier value */
    protected static DMatrixSparseCSC getSparseCoefficientMatrix(LinearEquation[] eqs) {
        return LinearEquation.getSparseCoefficientMatrix(eqs,
                LinearEquation.RECOMMENDED_CAPACITY_MODIFIER_FOR_SPARSE_COEFF_MATRIX);
    }

    /** Gives the value matrix of the set of linear equations
     * in terms of EJML Matrix library sparse matrix
     * data structure
     *
     * @param eqs the array of linear equations
     * @return returns this value matrix of the given set of linear equations,
     *          returns null if the linear equations are for different circuit,
     *          or if the dimension of the linear equations does not match
     */
    protected static DMatrixRMaj getValueMatrix(LinearEquation[] eqs) {
        if (eqs == null ||
                eqs.length == 0) return null;

        /* check if the equations belong to the same circuit */
        Circuit circuitOfEquations = eqs[0].belongCircuit;
        int dimOfEquations = eqs[0].data.length - 1;
        for (int i = 0; i < eqs.length; i++) {
            if (circuitOfEquations != eqs[i].belongCircuit) return null;
            if (dimOfEquations != eqs[i].data.length - 1) return null;
        }

        DMatrixRMaj matrix = new DMatrixRMaj(eqs.length, 1);

        /* log data into this column matrix */
        for (int i = 0; i < eqs.length; i++) {
            matrix.set(i, 0, eqs[i].data[dimOfEquations]);
        }

        return matrix;
    }

    /** solves the given set of linear equation for some circuit
     *
     * @param eqs the array of linear equation for the circuit
     * @return returns the solution for that circuit
     *          returns null if failed to solve
     */
    protected static Solution solve(LinearEquation[] eqs) {

        /* uses the recommended capacity modifier value */
        DMatrixSparseCSC A = getSparseCoefficientMatrix(eqs);

        DMatrixRMaj b = getValueMatrix(eqs);

        if (A == null ||
            b == null) return null;

        Circuit circuit = eqs[0].belongCircuit;

        /* solve the linear equation */

        // 1. Create the QR solver with Fill-Reduction (COLAMD)
        // This reorders the matrix to keep R as sparse as possible.
        LinearSolverSparse<DMatrixSparseCSC, DMatrixRMaj> solver =
                LinearSolverFactory_DSCC.qr(FillReducing.NONE); //TODO : get FillReducing.COLAMD somehow

        // 2. Perform the decomposition (A = QR)
        // If setA returns false, the matrix might be structurally singular.
        if (!solver.setA(A)) {
            throw new RuntimeException("Matrix decomposition failed.");
        }

        // 3. Solve for x
        DMatrixRMaj x = new DMatrixRMaj(A.numCols, 1);
        solver.solve(b, x);

        /* log the solution (x) onto Solution form */
        Solution solution = new Solution(circuit);
        solution.logSolution(x);

        return solution;

    }
}
