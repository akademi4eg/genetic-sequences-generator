package gen_seq.generator;

import java.io.IOException;
import java.util.*;
import gen_seq.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author akademi4eg
 */
public class DefaultStatisticsCalculator implements StatisticsCalculator {

    public DefaultStatisticsCalculator (Chromosome c)
    {
        chr = c;
        corrs = new HashMap<Integer, Double>();
    }

    public synchronized double getAvg(boolean force) {
        if (!force && avg != null)
            return avg.doubleValue();

        avg = 0.0;
        for (int i=0; i<chr.getLength(); i++)
            avg += chr.getGeneValue(i);
        avg /= (double)chr.getLength();
        
        return avg.doubleValue();
    }

    public synchronized double getCorrelator(int r, boolean force) {
        if (r > chr.getLength())
            throw new IllegalArgumentException("Param should be less then chromosome length.");

        if (!force && corrs.containsKey(r))
            return corrs.get(new Integer(r)).doubleValue();

        double c = 0;
        for (int i=0; i<chr.getLength()-r; i++)
        {
            c += chr.getGeneValue(i)*chr.getGeneValue(i+r);
        }
        c = c / (double) (chr.getLength()-r);
        c -= Math.pow(getAvg(force), 2);

        corrs.put(r, c);
        return c;
    }

    public synchronized double getFitness(boolean force) {
        if (!force                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  && fit != null)
            return fit.doubleValue();

        fit = 0.0;
        for (int i=0; i <= StatsCollector.CORR_LEN; i++)
        {
            try {
                fit += Math.pow(getCorrelator(i, force) - EtalonCorrelator.calculate(i), 2);
            } catch (IOException ex) {
                Logger.getLogger(DefaultStatisticsCalculator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        fit /= StatsCollector.CORR_LEN;
        fit *= 1000;
        
        return fit;
    }

    public synchronized void reset ()
    {
        avg = null;
        fit = null;
        corrs.clear();
    }

    private Chromosome chr;
    private Double avg = null;
    private Double fit = null;
    private HashMap<Integer, Double> corrs;
}
