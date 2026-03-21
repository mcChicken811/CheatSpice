import nva.*;

public class Program {
    public static void main(String[] args) {
        /* setting up the circuit */
        Circuit circuit = new Circuit();

        for (int i = 0; i < 4; i++) {
            circuit.addNode();
        }

        circuit.addResistor(6.0);
        circuit.getNewestComponent().connectToNode(circuit.getNode(0), 1);
        circuit.getNewestComponent().connectToNode(circuit.getNode(3), 2);

        circuit.addResistor(12.0);
        circuit.getNewestComponent().connectToNode(circuit.getNode(0), 1);
        circuit.getNewestComponent().connectToNode(circuit.getNode(1), 2);

        circuit.addResistor(12.0);
        circuit.getNewestComponent().connectToNode(circuit.getNode(3), 1);
        circuit.getNewestComponent().connectToNode(circuit.getNode(2), 2);

        circuit.addResistor(6.0);
        circuit.getNewestComponent().connectToNode(circuit.getNode(1), 1);
        circuit.getNewestComponent().connectToNode(circuit.getNode(2), 2);

        circuit.addDCVoltageSource(20.0);
        circuit.getNewestComponent().connectToNode(circuit.getNode(0), 1);
        circuit.getNewestComponent().connectToNode(circuit.getNode(2), 2);

        circuit.addWire();
        circuit.getNewestComponent().connectToNode(circuit.getNode(3), 1);
        circuit.getNewestComponent().connectToNode(circuit.getNode(1), 2);

    }
}
