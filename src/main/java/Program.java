import nva.*;

public class Program {
    public static void main(String[] args) {
        /* setting up the circuit */
        Circuit circuit = new Circuit();

        Node n0 = circuit.addNode();
        Node n1 = circuit.addNode();
        Node n2 = circuit.addNode();
        Node n3 = circuit.addNode();

        Resistor r1 = circuit.addResistor(10);
        r1.connectTo(n1, n0);

        Resistor r2 = circuit.addResistor(5);
        r2.connectTo(n0, n3);

        Resistor r3 = circuit.addResistor(15);
        r3.connectTo(n0, n2);

        Resistor r4 = circuit.addResistor(10);
        r4.connectTo(n2, n1);

        Resistor r5 = circuit.addResistor(5);
        r5.connectTo(n1, n3);

        DCCurrentSource c1 = circuit.addDCCurrentSource(1);
        c1.connectTo(n3, n0);

        DCCurrentSource c2 = circuit.addDCCurrentSource(0);
        c2.connectTo(n3, n1);

        DCVoltageControlVoltageSource v1 = circuit.addDCVCVS(2, 1, 0);
        v1.connectTo(n2, n3);

        NodeVoltageAnalyser analyser = new NodeVoltageAnalyser();
        analyser.analyse(circuit);

        System.out.println(circuit.toString());
    }
}
