package de.exit13.utils;

import de.exit13.search.ElasticImpl;
import de.exit13.utils.configuration.Config;
import de.exit13.utils.configuration.ElasticConfig;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

/**
 * Created by dude on 13.03.15.
 */
public class ElasticImportUtils {
    ElasticConfig elasticConfig = new ElasticConfig();
    ElasticImpl elastic = new ElasticImpl();

    public void elasticImport()  throws Exception{
        System.out.println(Config.ANSI_RED_FG + "elastic" + Config.ANSI_RESET +" import...");
        FileUtils fu = new FileUtils();

        String directory = "/data/rawdata/CLIMAT";
        String filter = "CLIMAT_RAW_200303.txt";
        ArrayList<String> fileList = fu.readDirectory(directory, filter);
        System.out.println("Read " + fileList.size() + " files from " + directory + ".");

        for(String file : fileList) {
            ArrayList<String> fileContent = fu.readFileContent(file);
            processFileContent(fileContent);

        }

        System.out.println(Config.ANSI_GREEN_FG + "Done!" + Config.ANSI_RESET);
    }

    private void processFileContent(ArrayList<String> fileContent) {
        int i = 0;
        for(String line : fileContent ){
            //if(i++ > 0 && i == 1006) {
                convertLineToJson(line);
            //}
        }
    }


    private JSONObject convertLineToJson(String line) {
        String[] pieces = line.split(";",-1);
        Map<String, Object> mapping = new HashMap<String, Object>();
        for(int i=0; i< pieces.length; i++) {
            // set all the zero value pieces to -9999
            if (pieces[i].equals("")) {
                pieces[i] = "-9999";
            }
            // for a reference where the values come from -> see /resources/datafile_formta.txt
            mapping.put("year", parseInt(pieces[0]));
            mapping.put("month", parseInt(pieces[1]));
            mapping.put("station_id", pieces[2]);
            System.out.println(i + " --- " + parseInt(pieces[i]));
        }
        JSONObject jsonObject = new JSONObject();
        return jsonObject;
    }


}
