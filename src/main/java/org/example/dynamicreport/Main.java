package org.example.dynamicreport;

import org.example.dynamicreport.Export.DynamicReport;

public class Main {
    public static final String OUTPUT_PATH = "src/main/resources/";

    public static void main(String[] args) {
        new DynamicReport(OUTPUT_PATH);
    }
}