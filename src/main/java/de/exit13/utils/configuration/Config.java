package de.exit13.utils.configuration;

/**
 * Created by elshotodore on 18.02.2015.
 */
public class Config {
    // server settings
    public static final int SERVER_PORT = 9300;
    public static final String SERVER_ADDRESS = "localhost";
    public static final String CLUSTER_NAME = "elasticsearch";

    // indexing settings
    public static final String INDEX_NAME = "climatedata";
    public static final String INDEX_TYPE = "r"; // each doc is a record
    public static final int BULK_ACTION_COUNT = 50;

    public static final String FILTER_EXPRESSION = "^CLIMAT_RAW_2003.*.txt";
    public static final String SRC_DATA_DIR = "/data/rawdata/";
    public static final int MAX_LINES_TO_PROCESS = 0; // set > 0 to break after XX processed lines

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK_FG = "\u001B[30m";
    public static final String ANSI_RED_FG = "\u001B[31m";
    public static final String ANSI_GREEN_FG = "\u001B[32m";
    public static final String ANSI_YELLOW_FG = "\u001B[33m";
    public static final String ANSI_BLUE_FG = "\u001B[34m";
    public static final String ANSI_PURPLE_FG = "\u001B[35m";
    public static final String ANSI_CYAN_FG = "\u001B[36m";
    public static final String ANSI_WHITE_FG = "\u001B[37m";
}
