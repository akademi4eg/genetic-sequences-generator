/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gen_seq.export;

import gen_seq.generator.*;
import gen_seq.ui.StatsCollector;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.*;

/**
 *
 * @author akademi4eg
 */
public class ChromosomeToFileExporter implements Exporter<Chromosome> {

    public void run()
    {
        FileWriter fstream = null;
        BufferedWriter out = null;
        try {
            fstream = new FileWriter(getName()+".chr");
            out = new BufferedWriter(fstream);
            Date date = new Date();
            DateFormat dateFormat = new SimpleDateFormat(getSettings().getProperty("file.date_format"));
            String header = getSettings().getProperty("file.header")
                                         .replace("{EXPORT_DATE}", dateFormat.format(date))
                                         .replace("{LENGTH}", Integer.toString(getData().getLength()))
                                         .replace("{FIT}", Double.toString(getData().getFitness()))
                                         .replace("{CORRELATOR}", EtalonCorrelator.expression)
                                         .replace("{CORR_LEN}", Integer.toString(StatsCollector.CORR_LEN));
            out.write(header);
            // TODO fill with data
        } catch (IOException ex)
        {
            
        }
        finally {
            // closing streams
            if (out != null)
            {
                try {
                    out.close();
                    fstream.close();
                } catch (IOException ex) {
                    Logger.getLogger(ChromosomeToFileExporter.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public Chromosome getData() {
        return data;
    }

    public void setData(Chromosome data) {
        this.data = data;
    }

    public Properties getSettings() {
        if (settings == null)
        {
            FileInputStream is = null;
            settings = new Properties();
            try {
                is = new FileInputStream(PROPERTIES_FILE);
                settings.load(is);
            }
            catch (IOException ex)
            {
                // something happened. Let's use default parameters.
                Logger.getLogger(StatsCollector.class.getName()).log(Level.SEVERE, null, ex);
                setDefaultSettings();
            }
            finally
            {
                // closing a stream.
                if ( null != is )
                    try { is.close(); } catch( IOException e ) {
                        Logger.getLogger(StatsCollector.class.getName()).log(Level.SEVERE, null, e);
                    }
            }
        }
        return settings;
    }

    protected void setDefaultSettings ()
    {
        if (settings == null) settings = new Properties();
        settings.setProperty("file.date_format", "yyyy/MM/dd HH:mm:ss");
        settings.setProperty("line.length", "100");
        settings.setProperty("file.header",
                "----------------------------------------------------------------------------------------------------" + System.getProperty("line.separator")
              + "--- Sequence generated using genetic algorythm." + System.getProperty("line.separator")
              + "--- Export date: {EXPORT_DATE}" + System.getProperty("line.separator")
              + "--- Length: {LENGTH}" + System.getProperty("line.separator")
              + "--- Desired correlator: C(r) = {CORRELATOR}" + System.getProperty("line.separator")
              + "--- Inspected points: {CORR_LEN}" + System.getProperty("line.separator")
              + "--- Fitness: {FIT}" + System.getProperty("line.separator")
              + "----------------------------------------------------------------------------------------------------" + System.getProperty("line.separator")
              );

        setSettings(settings);
    }

    public void setSettings(Properties settings) {
        this.settings = settings;
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(PROPERTIES_FILE);
            settings.store(os, null);
        } catch (IOException ex) {
            Logger.getLogger(StatsCollector.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            // closing a stream.
            if ( null != os )
                try { os.close(); } catch( IOException e ) {
                    Logger.getLogger(StatsCollector.class.getName()).log(Level.SEVERE, null, e);
                }
        }
    }

    public String getName() {
        return filename;
    }

    public void setName(String name) {
        filename = name;
    }

    protected String filename;
    protected Properties settings;
    protected Chromosome data;
    public static final String PROPERTIES_FILE = "chr2file_exporter.properties";
}
