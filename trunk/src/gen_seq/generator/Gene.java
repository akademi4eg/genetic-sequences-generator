package gen_seq.generator;

/**
 *
 * @author akademi4eg
 */
public interface Gene {
    int getValue ();
    void setValue (int value);
    void setRandom ();
    void mutate ();
    public Gene clone() throws CloneNotSupportedException;
}
