package gen_seq.generator;

import java.io.IOException;
import java.util.HashMap;

/**
 *
 * @author akademi4eg
 */
public class EtalonCorrelator {

    public synchronized static double calculate (int r) throws IOException
    {
        if ( r == 0 ) return 0.25; // TODO is this a good solution?
        if (expression == null) return 0.0;
        if (cache == null) cache = new HashMap<Integer, Double>();

        if (cache.containsKey(r))
            return cache.get(r);

        getParser().addVariable("r", (double) r);

        double value;
        value = getParser().getValue();// Get the value
        if (getParser().hasError()) {
            throw new IOException("Error during evaluation. "+getParser().getErrorInfo());
        }
        cache.put(r, value);

        return value;
    }

    public static org.nfunk.jep.JEP getParser ()
    {
        if (corrParser == null)
        {
            corrParser = new org.nfunk.jep.JEP();
            corrParser.addStandardFunctions();
            corrParser.addStandardConstants();
            cache = new HashMap<Integer, Double>();
        }
        return corrParser;
    }

    public static String expression;
    private static HashMap<Integer, Double> cache;
    private static org.nfunk.jep.JEP corrParser;
 }