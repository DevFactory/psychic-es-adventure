package de.exit13;

import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

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


    public static ArrayList<File> readFilesFromDirectory(String directory, String filterExpression) {
        Boolean filtered = true;
        File file = new File(directory);
        ArrayList<File> fileList = new ArrayList<File>();
        // TODO - catch files == null
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            // filter the file names and then add
            if (null != filterExpression) {
                if (files[i].getName().matches(filterExpression)) {
                    fileList.add(files[i]);
                }
            }
        }
        System.out.println(Config.ANSI_RED + fileList.size() + Config.ANSI_RESET + " files to process.");
        return fileList;
    }

    public void processFile(File file, int limit, String indexName, String indexType) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        int i = 0;
        while ((line = br.readLine()) != null) {
            String json = "{}";
            if(i > 1) {
                try {
                    json = convertLineToJson(line).toJSONString();
                    //System.out.println(i);
                } catch (Exception e) {
                    // e.printStackTrace();
                }
                //System.out.println(json);
                if (limit > 0) {
                    if (i == limit) {
                        break;
                    }
                }
            }
            i++;
        }
        br.close();
    }

    public JSONObject convertLineToJson(String line) {
            String[] pieces = line.replace(" ", "").split(";");
            JSONObject jsonObject = new JSONObject();
            // Stations_ID; Mess_Datum; Qualitaets_Niveau; LUFTTEMPERATUR;DAMPFDRUCK;BEDECKUNGSGRAD;LUFTDRUCK_STATIONSHOEHE;REL_FEUCHTE; WINDGESCHWINDIGKEIT; LUFTTEMPERATUR_MAXIMUM;LUFTTEMPERATUR_MINIMUM;LUFTTEMP_AM_ERDB_MINIMUM; WINDSPITZE_MAXIMUM; NIEDERSCHLAGSHOEHE;NIEDERSCHLAGSHOEHE_IND;SONNENSCHEINDAUER; SCHNEEHOEHE;eor

            int stationsId = parseInt(pieces[0]);
            jsonObject.put("stations_id", stationsId);

            long messDatum = parseLong(pieces[1]);
            jsonObject.put("mess_datum", messDatum);

            int qualitaetsNiveau = parseInt(pieces[2]);
            jsonObject.put("qualitaets_niveau", qualitaetsNiveau);

            float lufttemperatur = parseFloat(pieces[3]);
            jsonObject.put("lufttemperatur", lufttemperatur);

            float dampfdruck = parseFloat(pieces[4]);
            jsonObject.put("dampfdruck", dampfdruck);

            float bedeckungsGrad = parseFloat(pieces[5]);
            jsonObject.put("bedeckungsgrad", bedeckungsGrad);

            float luftdruckStationshoehe = parseFloat(pieces[6]);
            jsonObject.put("luftdruck_stationshoehe", luftdruckStationshoehe);

            float relFeuchte = parseFloat(pieces[7]);
            jsonObject.put("rel_feuchte", relFeuchte);

            float windgeschwindigkeit = parseFloat(pieces[8]);
            jsonObject.put("windgeschwindigkeit", windgeschwindigkeit);

            float lufttemperaturMaximum = parseFloat(pieces[9]);
            jsonObject.put("lufttemperatur_maximum", lufttemperaturMaximum);

            float lufttemperaturMinimum = parseFloat(pieces[10]);
            jsonObject.put("lufttemperatur_minimum", lufttemperaturMinimum);

            float luttemperaturAmErdbMinimum = parseFloat(pieces[11]);
            jsonObject.put("lufttemperatur_am_erdb_minimum", luttemperaturAmErdbMinimum);

            float windspitzeMaximum = parseFloat(pieces[12]);
            jsonObject.put("windspitze_maximum", windspitzeMaximum);

            float niederschlagshoehe = parseFloat(pieces[13]);
            jsonObject.put("niederschlagshoehe", niederschlagshoehe);

            float niederschlagshoeheInd = parseFloat(pieces[14]);
            jsonObject.put("niederschlagshoehe_ind", niederschlagshoeheInd);

            float sonnenscheindauer = parseFloat(pieces[15]);
            jsonObject.put("sonnenscheindauer", sonnenscheindauer);

            return jsonObject;
    }
}
