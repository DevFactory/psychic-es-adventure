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
            // skip empty lines and lines with # (comment)
            if(!line.startsWith("#") && line.trim().length() != 0) {
                fileContent.add(line);
            }
            i++;
        }
        br.close();
        return fileContent;

    }

    public ArrayList<String> readDirectory(String directory, String filter) throws IOException {
        ArrayList<String> fileList = new ArrayList<String>();
        // add trailing slash if missing
        if(!directory.endsWith("/")) {
            directory += "/";
        }
        File folder = new File(directory);
        File[] listOfFiles  = folder.listFiles();
        for (File file : listOfFiles) {
            if (file.isFile()) {
                if(filter != null) {
                    if (file.getName().matches(filter)) {
                        fileList.add(directory + file.getName());
                    }
                }
                else {
                    fileList.add(file.getName());
                }
            }
        }
        return fileList;
    }
}
