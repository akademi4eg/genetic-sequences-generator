package gen_seq.generator;

/**
 *
 * @author akademi4eg
 */
public interface UnaryEvolutionOperator {
    void apply ();
    void updateRate (double multiplier);
    double getRate ();
    void setRate (double rate);
}
