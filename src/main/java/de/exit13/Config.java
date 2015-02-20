package de.exit13;

/**
 * Created by frank.vogel on 18.02.2015.
 */
public class Config {
    // server settings
    public static final int SERVER_PORT = 9300;
    public static final String SERVER_ADDRESS = "localhost";


    // indexing settings
    public static final String INDEX_NAME = "awi";
    public static final String INDEX_TYPE = "impression"; // each doc is an impression
    public static final int BULK_ACTION_COUNT = 2222;

    //static final String FILTER_EXPRESSION = "^impressionlog2014010[1-7].*";
    public static final String FILTER_EXPRESSION = "^impressionlog20140101.*";
    public static final String SRC_DIRECTORY = "d:\\ESTEST";
    public static final int MAX_LINES_TO_PROCESS = 0; // set > 0 to break after XX processed lines

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
}