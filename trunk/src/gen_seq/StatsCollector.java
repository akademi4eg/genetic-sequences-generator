package gen_seq;

import gen_seq.generator.*;
import java.io.IOException;
import java.util.*;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author akademi4eg
 */
public class StatsCollector extends java.util.TimerTask {
    public StatsCollector (GenFrame gf)
    {
        statsHolder = gf;
    }

    public void run() {
        minY = 0; maxY = 0;
        Population pop = Population.getInstance();
        if (!pop.isActive())
            return;
        Color col;
        for (int i=0; i<pop.getLength(); i++)
        {
            col = null;
            if (i < 5)
            {
                switch (i)
                {
                    case 0:
                        col = Color.red;
                        break;
                    case 1:
                        col = Color.blue;
                        break;
                    case 2:
                        col = Color.green;
                        break;
                    case 3:
                        col = Color.orange;
                        break;
                    case 4:
                        col = Color.magenta;
                        break;
                }
                statsHolder.drawCorrCurve(col, buildCurve(pop.getChromosome(i)));
            }
            statsHolder.updateStatsTableRow(i, pop.getChromosome(i).getAvg(),
                                               pop.getChromosome(i).getFitness(), col);
        }
        double cr;
        for (int r=1; r<CORR_LEN + 1; r++)
        {
            try {
                cr = EtalonCorrelator.calculate(r);
                if (cr > maxY) maxY = cr;
                else if (cr < minY) minY = cr;
            } catch (IOException ex) {
                Logger.getLogger(StatsCollector.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        statsHolder.drawCorrsPlotAxis(minY, maxY, 1, CORR_LEN);
        statsHolder.updateStats(POPULATION_AGE, Integer.toString(pop.getAge()));
        statsHolder.updateStats(AVG_RATE, Double.toString(Math.round(pop.getAvgRate()*100000)/100000.0));
        statsHolder.updateStats(BEST_ACHIEVEMENT, Double.toString(Math.round(pop.getBestAchievement().getFitness()*10000)/10000.0));
    }

    private LinkedList<CorrPoint> buildCurve (Chromosome chr)
    {
        LinkedList<CorrPoint> pts = new LinkedList<CorrPoint>();
        CorrPoint temp = new CorrPoint(0, 0);
        for (int r=1; r<CORR_LEN + 1; r++)
        {
            temp.x = r;
            temp.y = chr.getCorrelator(r);
            try {
                pts.add(temp.clone());
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(StatsCollector.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (temp.y > maxY) maxY = temp.y;
            else if (temp.y < minY) minY = temp.y;
        }

        return pts;
    }

    public final static int POPULATION_LENGTH = 1;
    public final static int CHROMOSOME_LENGTH = 2;
    public final static int POPULATION_AGE = 3;
    public final static int AVG_RATE = 4;
    public final static int BEST_ACHIEVEMENT = 5;
    public final static int DELAY = 500;
    private double minY = 0;
    private double maxY = 0;
    public static int CORR_LEN = 10;
    private GenFrame statsHolder;
}