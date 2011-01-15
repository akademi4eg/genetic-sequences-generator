package gen_seq.generator;

import java.util.Random;

/**
 *
 * @author akademi4eg
 */
public final class TernaryGene implements Gene, Cloneable {

    public TernaryGene ()
    {
        setRandom();
    }

    public int getValue() {
        return value;
    }

    public void setValue(int v) {
        //TODO think on implementing some validation here
        value = v;
    }

    public void setRandom() {
        value = chaos.nextInt(3);
    }

    public void mutate ()
    {
        // TODO not best implementation
        setRandom();
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

    private int value;
    private static final Random chaos = new Random();
}