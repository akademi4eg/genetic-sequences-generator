package gen_seq.export;

import java.util.Properties;

/**
 * Generic exporting interface.
 * @author akademi4eg
 */
public interface Exporter<T> {
    public void run ();
    public T getData ();
    public void setData (T data);
    public Properties getSettings ();
    public void setSettings (Properties settings);
    public String getName ();
    public void setName (String name);
}