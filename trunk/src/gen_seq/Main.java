package gen_seq;

import java.awt.*;
import javax.swing.*;

/**
 * Main class
 * @author akademi4eg
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable()
        {
            public void run ()
            {
                JFrame gframe = new GenFrame();
                gframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                gframe.setVisible(true);
            }
        });
    }

    public static final String version = "1.1.0";
}