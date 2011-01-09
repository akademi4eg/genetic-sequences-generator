package gen_seq.generator;

import java.util.concurrent.CountDownLatch;
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
        resetEvolutionCounter();
        chromosomes = new Chromosome[length];
        for (int i=0; i<length; i++) {
            new Thread(new ChromosomeInitializer(i, chr_length, Chromosome.BINARY_GENE)).start();
        }
        try {
            getEvolutionCounter().await();
        } catch (InterruptedException ex) {
            Logger.getLogger(Population.class.getName()).log(Level.SEVERE, null, ex);
        }
        best_achievement = chromosomes[0].clone();
        age = 1;
    }

    public CountDownLatch getEvolutionCounter ()
    {
        if (evolution_counter == null)
            evolution_counter = new CountDownLatch(length);

        return evolution_counter;
    }

    public void resetEvolutionCounter ()
    {
        evolution_counter = new CountDownLatch(length);
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

    public void setChromosome (int i, Chromosome chr)
    {
        chromosomes[i] = chr;
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
        resetEvolutionCounter();
        for (final Chromosome chr : chromosomes)
        {
            // TODO think about using task queues here
            new Thread(new Runnable() {
                public void run() {
                    chr.evolve();
                    getEvolutionCounter().countDown();
                }
            }).start();
        }
        try {
            getEvolutionCounter().await();
        } catch (InterruptedException ex) {
            Logger.getLogger(Population.class.getName()).log(Level.SEVERE, null, ex);
        }
        age++;
    }

    public int getAge ()
    {
        return age;
    }

    public void fill (Chromosome chr)
    {
        resetEvolutionCounter();
        for (int i=0; i<length; i++)
        {
            new Thread(new ChromosomeFiller(i, chr)).start();
        }
        try {
            getEvolutionCounter().await();
        } catch (InterruptedException ex) {
            Logger.getLogger(Population.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    void setBestAchievement(Chromosome chr) {
        best_achievement = chr.clone();
    }

    private static class PopulationHolder {
        private static final Population INSTANCE = new Population();
    }

    public static boolean keep_best = false;
    private boolean is_active = false;
    private CountDownLatch evolution_counter;
    private boolean is_running = false;
    private int length = 10;
    private int chr_length = 1000;
    private int age = 1;
    private Chromosome[] chromosomes;
    private Chromosome best_achievement;
 }

class ChromosomeInitializer implements Runnable
{
    public ChromosomeInitializer (int chr_index, int length, int type)
    {
        this.chr_index = chr_index;
        this.length = length;
        this.type = type;
    }

    public void run() {
        try {
            Chromosome chr = new Chromosome(length, type);
            Population.getInstance().setChromosome(chr_index, chr);
        } catch (DataFormatException ex) {
            Logger.getLogger(ChromosomeInitializer.class.getName()).log(Level.SEVERE, null, ex);
        }
        Population.getInstance().getEvolutionCounter().countDown();
    }

    private int chr_index;
    private int type;
    private int length;
}

class ChromosomeFiller implements Runnable
{
    public ChromosomeFiller (int chr_index, Chromosome filler)
    {
        this.filler = filler;
        this.chr_index = chr_index;
    }

    public void run() {
        Population.getInstance().setChromosome(chr_index, filler.clone());
        Population.getInstance().getEvolutionCounter().countDown();
    }

    private Chromosome filler;
    private int chr_index;
}