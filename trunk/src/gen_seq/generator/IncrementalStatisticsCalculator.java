package gen_seq.generator;

import java.util.HashMap;
import java.util.Map.Entry;

/**
 *
 * @author akademi4eg
 */
public class IncrementalStatisticsCalculator implements StatisticsCalculator {

    public IncrementalStatisticsCalculator (Chromosome c)
    {
        chr = c;
        corrs = new HashMap<Integer, Double>();
        corrs_updated = new HashMap<Integer, Boolean>();
        directCalculator = new DefaultStatisticsCalculator(c);
    }

    public synchronized HashMap<Integer, GenesPair> getDeltas() {
        if (deltas == null) throw new IllegalStateException();
        return deltas;
    }

    /**
     * Make sure you call this before applying changes!
     * @param changes
     */
    public synchronized void setDeltas(HashMap<Integer, GenesPair> changes) {
        deltas = (HashMap<Integer, GenesPair>) changes.clone();
        old_chr = chr.clone();
    }

    public synchronized double getAvg(boolean force) {
        if (avg == null) {
            avg = directCalculator.getAvg(force);
        } else {
            if (!force && avg_updated) return avg;
            for (GenesPair value : getDeltas().values()) {
                avg += (value.getNew().getValue() - value.getOld().getValue()) / (double) chr.getLength();
            }
        }

        avg_updated = true;
        return avg;
    }

    public double getVariance(boolean force) {
        if (var == null) {
            var = directCalculator.getVariance(force);
        } else {
            if (!force && var_updated) return var;
            for (GenesPair value : getDeltas().values()) {
                var += (Math.pow(value.getNew().getValue(), 2) - Math.pow(value.getOld().getValue(), 2)) / (double) chr.getLength();
            }
        }

        var_updated = true;
        return var;
    }

    public synchronized double getCorrelator(int r, boolean force) {
        if (!corrs.containsKey(r)) {
            corrs.put(r, directCalculator.getCorrelator(r, force));
        } else {
            if (!force && corrs_updated.containsKey(r)) return corrs.get(r);
            double new_corr = corrs.get(r);
            for (Entry<Integer, GenesPair> entry : getDeltas().entrySet()) {
                if (entry.getKey() - r >= 0)
                    new_corr += (old_chr.getGeneValue(entry.getKey() - r)*(entry.getValue().getNew().getValue() - entry.getValue().getOld().getValue())) / (double) (chr.getLength() - r);
                if (entry.getKey() + r < chr.getLength())
                    new_corr += (old_chr.getGeneValue(entry.getKey() + r)*(entry.getValue().getNew().getValue() - entry.getValue().getOld().getValue())) / (double) (chr.getLength() - r);
            }
            corrs.put(r, new_corr);
        }

        corrs_updated.put(r, true);
        return corrs.get(r);
    }

    public synchronized double getFitness(boolean force) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public synchronized void reset() {
        avg_updated = false;
        var_updated = false;
        fit_updated = false;
        corrs_updated.clear();
    }

    private Chromosome chr;
    private Chromosome old_chr;
    private Double avg = null;
    private Double var = null;
    private Double fit = null;
    private boolean avg_updated = false;
    private boolean var_updated = false;
    private boolean fit_updated = false;
    private HashMap<Integer, Boolean> corrs_updated;
    private HashMap<Integer, Double> corrs;
    private HashMap<Integer, GenesPair> deltas;// maps index of changed gene to growth of its value
    StatisticsCalculator directCalculator;
}