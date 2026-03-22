package nva;

import javax.sound.sampled.Line;

public class NodeVoltageAnalyser {

    public void analyse(Circuit circuit) {
        circuit.completeCircuit();
        circuit.clearAnalysisRecords();
        circuit.updateIndependentCurrentComponents();

        int currentNumberOfLinearEquations = 0;
        LinearEquation[] linearEquations = new LinearEquation[circuit.getNumberOfNodes() + circuit.getNumberOfIndependentCurrentComponents() + 1];

        /* add linear equations for each independent current component using KVL */
        for (int id = 0; id < circuit.getNumberOfIndependentCurrentComponents(); id++) {

            Component comp = circuit.getIndependentCurrentComponent(id);
            LinearEquation eq = comp.getKVLOfIndependentCurrentComponent();
            linearEquations[currentNumberOfLinearEquations++] = eq;
        }

        /* apply KCL to obtain equation for each node */
        for (int id = 0; id < circuit.getNumberOfNodes(); id++) {
            LinearEquation eq = new LinearEquation(circuit);
            Node node = circuit.getNode(id);

            /* add the coefficients for each component connected to the node
            * using the pointing away as positive */
            for (int i = 0; i < node.getNumOfComponents(); i++) {
                Component comp = node.getComponents()[i];

                if (comp.isIndependentCurrentComp()) {
                    eq.addIndependentCurrentCoeff(comp.getIndependentCurrentCompIndex(),
                            comp.getReferenceCurrentDirectionByNode(node));
                } else {
                    LinearEquation currentEq = comp.getCurrentLinearEquationCoefficients();
                    currentEq.factor(comp.getReferenceCurrentDirectionByNode(node));
                    eq.combine(currentEq);
                }
            }

            linearEquations[currentNumberOfLinearEquations++] = eq;
        }

        /* add linear equation for the reference node being the zeroth node */
        LinearEquation eq = new LinearEquation(circuit);
        eq.addNodeVoltageCoeff(0, 1.0);
        linearEquations[currentNumberOfLinearEquations++] = eq;

       Solution solution = LinearEquation.solve(linearEquations);

       if (solution == null) return;

       solution.logSolutionOnCircuit();

       System.out.println(circuit.toString());

    }

    /** @deprecated this method fucking sucks */
    @Deprecated
    public void runNodeVoltageAnalysis(Circuit circuit) {

        /* first reset all previous branch data */
        for (int i = 0; i < circuit.getNumberOfNodes(); i++) {
            circuit.getNode(i).resetBranchData();
        }

        /* obtain all nodes in the circuit that has more than three connections
        * or just one connection */
        int numOfMainNodes = 0;
        Node[] mainNodes = new Node[circuit.getNumberOfNodes()];

        for (int i = 0; i < circuit.getNumberOfNodes(); i++) {
            Node node = circuit.getNode(i);
            if (node.isMainNode()) {
                mainNodes[numOfMainNodes++] = node;
            }
        }

        /* compute and connect all branches in the circuit */
        int numOfBranches = 0;
        Branch[] branches = new Branch[circuit.getNumberOfComponents()];

        /* for each main node, send an agent to compute the branch
        * of which the branch has positive polarity at such node
        * and negative polarity at the end node */
        for (int i = 0; i < numOfMainNodes; i++) {
            Node startNode = mainNodes[i];

            for (int j = 0; j < startNode.getNumOfComponents(); j++) {
                double branchRes = 0.0;
                double branchVoltage = 0.0;

                /* travel through the direction of this component until
                * a main or lonely node is reached */
                Node nextNode = startNode;
                Component nextComponent = startNode.getComponents()[j];
                do {
                    if (nextComponent instanceof Resistor) {
                        branchRes += ((Resistor) nextComponent).getResistance();
                    }
                    else if (nextComponent instanceof DCVoltageSource) {
                        double relativePolarity = nextNode == nextComponent.getNode1() ? 1.0 : -1.0;
                        branchVoltage += relativePolarity * ((DCVoltageSource) nextComponent).getDCVoltage();
                    } else if (nextComponent instanceof TheveninDevice) {
                        branchRes += ((TheveninDevice) nextComponent).getTheveninResistance();
                    }

                    nextNode = nextComponent.getNode1() == nextNode ? nextComponent.getNode2() : nextComponent.getNode1();
                    if (nextNode.getNumOfComponents() == 2) {
                        nextComponent = nextNode.getComponents()[0] == nextComponent ?
                                    nextNode.getComponents()[1] : nextNode.getComponents()[0];
                    }
                } while (!nextNode.isMainNode() || !nextNode.isLonelyNode());

                /* reaches main or lonely node */


                Branch branch = new Branch(branchVoltage, branchRes, numOfBranches);
                branches[numOfBranches] = branch;
                numOfBranches++;

                branch.connectToNode(startNode, 1, i);
                branch.connectToNode(nextNode, 2, linearSearch(nextNode, mainNodes, numOfMainNodes));

            }
        }


    }

    private Integer linearSearch(Object object, Object[] arr, int arrSize) {
        for (int i = 0; i < arrSize && i < arr.length; i++) {
            if (arr[i] == object) return i;
        }

        return null;
    }

}
