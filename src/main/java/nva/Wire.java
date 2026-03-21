package nva;

/** literally just a resistor with 0 resistance */
public class Wire extends Resistor {
    protected Wire() {
        super(0.0);
    }

    @Override
    public boolean isIndependentCurrentComp() {
        return true;
    }
}
