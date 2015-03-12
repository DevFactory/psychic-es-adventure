package de.exit13.utils.enums;

/**
 * Created by elshotodore on 12.03.15.
 */
public enum FileTypes {
    CSV(".csv"),
    GZIP(".gz"),
    TXT(".txt");

    private String extension;

    FileTypes(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }
}