package de.exit13.utils;

import de.exit13.utils.Configuration.Config;

import java.io.IOException;

/**
 * Created by dude on 13.03.15.
 */
public class ESImportUtils {
    public void elasticImport()  throws Exception{
        System.out.println(Config.ANSI_RED_FG + "elastic" + Config.ANSI_RESET +" import...");
        FileUtils fu = new FileUtils();

        fu.readDirectory("/data/rawdata", null);

        System.out.println(Config.ANSI_GREEN_FG + "Done!" + Config.ANSI_RESET);
    }
}
