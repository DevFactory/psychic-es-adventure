package de.exit13;

/**
 * Created by frank.vogel on 20.02.2015.
 */
public class Utils {
    public static void timer(long then) {
        long now = System.nanoTime();

        System.out.println("\n--------------------------------------------------------------------------------");
        System.out.println("Runtime about " + (now - then) / 1000000 + "ms." );
        System.out.println("--------------------------------------------------------------------------------");
    }
}
