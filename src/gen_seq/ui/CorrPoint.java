package gen_seq.ui;

/**
 *
 * @author akademi4eg
 */
public class CorrPoint implements Cloneable {
    public CorrPoint (int ax, double ay)
    {
        x = ax; y = ay;
    }

    public CorrPoint clone () throws CloneNotSupportedException
    {
        return (CorrPoint) super.clone();
    }

    public double y;
    public int x;
}
