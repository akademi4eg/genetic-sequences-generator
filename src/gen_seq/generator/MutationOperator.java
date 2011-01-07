package gen_seq.generator;

import java.util.*;
/**
 *
 * @author akademi4eg
 */
public class MutationOperator implements UnaryEvolutionOperator {

    public MutationOperator (double r, Chromosome achr)
    {
        chr = achr;
        rate = r;
    }

    public void apply() {
        Gene[] genes = chr.getGenes();
        for (Gene g : genes)
        {
            if (chaos.nextDouble() < rate)
                g.mutate();
        }
        chr.markDirty();
    }

    public double getRate ()
    {
        return rate;
    }

    public void updateRate (double multiplier)
    {
        double new_rate = rate + multiplier*RATE_STEP;
        if (new_rate < 0)
            rate = RATE_STEP;
        else if ( new_rate > 1.0 )
            rate = 1.0 - RATE_STEP;
        else
            rate = new_rate;
    }

    private double rate;
    private Random chaos = new Random();
    private Chromosome chr;
    public final static double RATE_STEP = 0.0005;
}
