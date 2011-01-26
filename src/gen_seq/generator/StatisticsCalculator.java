package gen_seq.generator;

/**
 *
 * @author akademi4eg
 */
public interface StatisticsCalculator {
    double getAvg (boolean force);
    double getVariance (boolean force);
    double getCorrelator (int r, boolean force);
    double getFitness (boolean force);
    void reset ();
}
