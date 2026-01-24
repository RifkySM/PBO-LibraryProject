package com.example.pbolibraryproject.util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CSVUtil {
    private static final String DATA_DIR = "data";

    static {
        // Create data directory if it doesn't exist
        try {
            Path dataPath = Paths.get(DATA_DIR);
            if (!Files.exists(dataPath)) {
                Files.createDirectories(dataPath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String[]> readCSV(String filename) {
        List<String[]> data = new ArrayList<>();
        String filePath = DATA_DIR + File.separator + filename;

        File file = new File(filePath);
        if (!file.exists()) {
            return data; // Return empty list if file doesn't exist
        }

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                data.add(values);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    public static void writeCSV(String filename, List<String[]> data) {
        String filePath = DATA_DIR + File.separator + filename;

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (String[] row : data) {
                bw.write(String.join(",", row));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void appendCSV(String filename, String[] row) {
        String filePath = DATA_DIR + File.separator + filename;

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            bw.write(String.join(",", row));
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean fileExists(String filename) {
        String filePath = DATA_DIR + File.separator + filename;
        return new File(filePath).exists();
    }
}
