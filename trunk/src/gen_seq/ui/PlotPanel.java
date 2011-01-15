package gen_seq.ui;

import gen_seq.generator.EtalonCorrelator;
import java.awt.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import java.util.*;
import java.awt.geom.*;
import java.text.DecimalFormat;
/**
 *
 * @author akademi4eg
 */
public class PlotPanel extends JPanel {

    public PlotPanel ()
    {
        super();
        points = new HashMap<Color, LinkedList<CorrPoint>>();
        formater = new DecimalFormat("0.0E0");
    }

    @Override
    protected synchronized void paintComponent (Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        g2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, FONT_SIZE));
        g2.setColor(Color.black);
        g2.drawString("Correlators", getWidth()/2-25, 15);

        g2.drawRect(MARGIN_LEFT, MARGIN_TOP, getWidth()-MARGIN_LEFT-MARGIN_RIGHT, getHeight()-MARGIN_TOP-MARGIN_BOTTOM);

        g2.drawString(formater.format(minY), 0, getHeight()-MARGIN_BOTTOM + 3);
        g2.drawString(formater.format(maxY), 0, MARGIN_TOP + 5);
        g2.drawString(Integer.toString(minX), MARGIN_LEFT - 3, getHeight()-MARGIN_BOTTOM+FONT_SIZE);
        g2.drawString(Integer.toString(maxX), getWidth()-MARGIN_RIGHT-5*Math.round(Math.log10(maxX)), getHeight()-MARGIN_BOTTOM+FONT_SIZE);
        if (Math.signum(minY)*Math.signum(maxY) < 0)
        {
            g2.drawLine(mapXCoord(minX), mapYCoord(0), mapXCoord(maxX), mapYCoord(0));
        }
        for (int i=minX; i <= maxX; i++)
        {
            try {
                g2.fill(new Ellipse2D.Double(mapXCoord(i) - POINT_SIZE, mapYCoord(EtalonCorrelator.calculate(i)) - POINT_SIZE, 2 * POINT_SIZE, 2 * POINT_SIZE));
            } catch (IOException ex) {
                Logger.getLogger(PlotPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (points.size() > 0)
        {
            for (Color i : points.keySet())
            {
                g2.setColor(i);
                for (CorrPoint p : points.get(i))
                {
                    g2.draw(new Ellipse2D.Double(mapXCoord(p.x)-POINT_SIZE/2, mapYCoord(p.y)-POINT_SIZE/2, POINT_SIZE, POINT_SIZE));
                }
            }
        }
    }

    private int mapXCoord (int x)
    {
        x -= minX;
        return MARGIN_LEFT + (int) Math.round((double)x / (double)(maxX-minX) * (getWidth()-MARGIN_LEFT-MARGIN_RIGHT));
    }

    private int mapYCoord (double y)
    {
        y -= minY;
        return getHeight() - (MARGIN_BOTTOM + (int) Math.round(y / (double)(maxY-minY) * (getHeight()-MARGIN_TOP-MARGIN_BOTTOM)));
    }

    public void setAxis (double aminY, double amaxY, int aminX, int amaxX)
    {
        minX = aminX; minY = Math.floor(aminY*10)/10.0; maxX = amaxX; maxY = Math.ceil(amaxY*10)/10.0;
    }

    public synchronized void setCurve (Color col, LinkedList<CorrPoint> pts)
    {
        points.put(col, pts);
    }

    private HashMap<Color, LinkedList<CorrPoint>> points;
    private DecimalFormat formater;
    private double minY = 0.0;
    private double maxY = 1.0;
    private int minX = 0;
    private int maxX = 10;
    private static int POINT_SIZE = 4;
    private static int FONT_SIZE = 10;
    private static int MARGIN_LEFT = 40;
    private static int MARGIN_RIGHT = 10;
    private static int MARGIN_TOP = 25;
    private static int MARGIN_BOTTOM = 10;
}