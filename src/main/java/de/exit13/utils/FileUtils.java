package de.exit13.utils;

import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

/**
 * Created by elshotodore on 20.02.2015.
 */
public class FileUtils {

    public ArrayList<String> readFileContent(String fileName) throws IOException {
        ArrayList<String> fileContent = new ArrayList<String>();

        String fileExtension = fileName.substring(fileName.lastIndexOf("."), fileName.length());
        //System.out.println("FILE EXTENSION: " +  fileExtension);

        BufferedReader br = new BufferedReader(new FileReader(fileName));

        String line;
        int i = 0;
        while ((line = br.readLine()) != null) {
            // skip empty lines and lines with #
            if(!line.startsWith("#") && line.trim().length() != 0) {

                fileContent.add(line);
            }
            i++;
        }
        br.close();
        return fileContent;

    }

    public ArrayList<String> readDirectory(String directory, String filter) throws IOException {
        ArrayList<String> directoryContent = new ArrayList<String>();

        File folder = new File(directory);
        File[] listOfFiles  = folder.listFiles();

        for (File file : listOfFiles) {
            if (file.isFile()) {
                System.out.println(file.getName());
            }
        }
        return directoryContent;
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
