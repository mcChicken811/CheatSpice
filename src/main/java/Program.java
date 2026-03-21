import nva.*;

public class Program {
    public static void main(String[] args) {
        /* setting up the circuit */
        Circuit circuit = new Circuit();

        for (int i = 0; i < 2; i++) {
            circuit.addNode();
        }

        circuit.addDCVoltageSource(1);
        circuit.getNewestComponent().connectToNode(circuit.getNode(0), 1);
        circuit.getNewestComponent().connectToNode(circuit.getNode(1), 2);

        circuit.addResistor(5);
        circuit.getNewestComponent().connectToNode(circuit.getNode(0), 1);
        circuit.getNewestComponent().connectToNode(circuit.getNode(1), 2);

        System.out.println(circuit.toString());

        NodeVoltageAnalyser analyser = new NodeVoltageAnalyser();

        analyser.analyse(circuit);
    }
}
