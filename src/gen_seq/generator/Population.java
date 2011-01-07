package gen_seq.generator;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.DataFormatException;

/**
 * Population of chromosomes
 * @author akademi4eg
 */
public class Population {

    private Population() {
    }

    public void init ()
    {
        try {
            chromosomes = new Chromosome[length];
            for (int i=0; i<length; i++) {
                chromosomes[i] = new Chromosome(chr_length, Chromosome.BINARY_GENE);
            }
            best_achievement = chromosomes[0].clone();
            age = 1;
        } catch (DataFormatException ex) {
            Logger.getLogger(Population.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Chromosome getBestAchievement ()
    {
        return best_achievement;
    }
    
    public void setLength (int l) throws DataFormatException
    {
        if (l <=0)
            throw new DataFormatException("Length should be positive");

        length = l;
    }

    public int getLength ()
    {
        return length;
    }

    public void setChromosomeLength (int l) throws DataFormatException
    {
        if (l <=0)
            throw new DataFormatException("Length should be positive");

        chr_length = l;
    }

    public int getChromosomeLength ()
    {
        return chr_length;
    }

    public Chromosome getChromosome (int i)
    {
        return chromosomes[i];
    }

    public void setActive (boolean a)
    {
        is_active = a;
    }

    public boolean isActive ()
    {
        return is_active;
    }

    public void setRunning (boolean a)
    {
        is_running = a;
    }

    public boolean isRunning ()
    {
        return is_running;
    }

    public void evolve ()
    {
        double f;
        for (Chromosome chr : chromosomes)
        {
            f = chr.getFitnessForceCalc();
            chr.getEvolutionOperator().apply();
            if (chr.getFitnessForceCalc() < f)
                chr.getEvolutionOperator().updateRate(-1.0);
            else
                chr.getEvolutionOperator().updateRate(1.0);

            if (chr.getFitness() < best_achievement.getFitness())
            {
                best_achievement = chr.clone();
            }
        }
        age++;
    }

    public int getAge ()
    {
        return age;
    }

    public void fill (Chromosome chr)
    {
        for (int i=0; i<length; i++)
            chromosomes[i] = chr.clone();
    }

    public Chromosome getBest ()
    {
        int best = 0;
        double fit = getChromosome(0).getFitnessForceCalc();
        for (int i=1; i<getLength(); i++)
        {
            if (getChromosome(i).getFitnessForceCalc() < fit)
            {
                fit = getChromosome(i).getFitnessForceCalc();
                best = i;
            }
        }

        return getChromosome(best);
    }

    public double getAvgRate ()
    {
        double rate = 0;
        for (Chromosome chr : chromosomes)
        {
            rate += chr.getEvolutionOperator().getRate();
        }

        return rate / getLength();
    }

    public static Population getInstance() {
        return PopulationHolder.INSTANCE;
    }

    private static class PopulationHolder {
        private static final Population INSTANCE = new Population();
    }

    private boolean is_active = false;
    private boolean is_running = false;
    private int length = 10;
    private int chr_length = 1000;
    private int age = 1;
    private Chromosome[] chromosomes;
    private Chromosome best_achievement;
 }