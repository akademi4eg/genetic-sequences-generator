package gen_seq.generator;

/**
 *
 * @author akademi4eg
 */
public final class BinaryGene implements Gene, Cloneable {

    public BinaryGene ()
    {
        setRandom();
    }

    public int getValue() {
        return value;
    }

    public void setValue(int v) {
        value = v;
    }

    private int value;

    public void setRandom() {
        value = new java.util.Random().nextInt(2);
    }

    public void mutate ()
    {
        value = 1 - value;
    }

    @Override
    public String toString ()
    {
        return Integer.toString(value);
    }

    @Override
    public Gene clone () throws CloneNotSupportedException
    {
        return (Gene) super.clone();
    }
}
