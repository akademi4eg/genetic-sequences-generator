package gen_seq.generator;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.DataFormatException;

/**
 *
 * @author akademi4eg
 */
public class Chromosome implements Cloneable {
    public Chromosome (int len, int gene_type) throws DataFormatException
    {
        if (len <= 0)
            throw new DataFormatException("Length should be positive");
        length = len;
        switch (gene_type)
        {
            case BINARY_GENE:
                genes = new BinaryGene[len];
                for (int i=0; i<len; i++)
                    genes[i] = new BinaryGene();
                break;
            default:
                throw new IllegalArgumentException("Unknown gene type.");
        }

        stats = new DefaultStatisticsCalculator(this);
        mutator = new MutationOperator(INITIAL_MUTATION_RATE, this);
    }

    public Chromosome (Gene[] gs, double a_rate)
    {
        length = gs.length;
        genes = gs;
        stats = new DefaultStatisticsCalculator(this);
        mutator = new MutationOperator(a_rate, this);
    }

    public int getLength ()
    {
        return length;
    }

    public int getGeneValue (int i)
    {
        return genes[i].getValue();
    }

    public Gene[] getGenes ()
    {
        return genes;
    }

    public synchronized double getAvg ()
    {
        return stats.getAvg(false);
    }

    public synchronized double getAvgForceCalc ()
    {
        return stats.getAvg(true);
    }

    public synchronized double getFitness ()
    {
        return stats.getFitness(false);
    }

    public synchronized double getFitnessForceCalc ()
    {
        return stats.getFitness(true);
    }

    public synchronized double getCorrelator (int r)
    {
        return stats.getCorrelator(r, false);
    }

    public synchronized double getCorrelatorForceCalc (int r)
    {
        return stats.getCorrelator(r, true);
    }

    public UnaryEvolutionOperator getEvolutionOperator ()
    {
        return mutator;
    }

    public void markDirty ()
    {
        stats.reset();
    }

    @Override
    public synchronized Chromosome clone ()
    {
        Gene[] new_genes = new Gene[length];
        for (int i=0; i<length; i++)
            try {
            new_genes[i] = genes[i].clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(Chromosome.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new Chromosome(new_genes, mutator.getRate());
    }

    private int length = 100;
    private Gene[] genes;
    private StatisticsCalculator stats;
    private UnaryEvolutionOperator mutator;

    public final static int BINARY_GENE = 1;
    public static double INITIAL_MUTATION_RATE = 0.05;
    public static int RATE_UPDATE_PERIOD = 1;
}