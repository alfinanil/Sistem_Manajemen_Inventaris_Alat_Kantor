package org.example.util;

import java.io.File;

/**
 * Konfigurasi untuk "Database" berbasis File
 * Menangani pembuatan direktori data jika belum ada
 */
public class DatabaseConfig {

    public static final String DATA_DIR = "data";

    static {
        ensureDirectoryExists();
    }

    /**
     * Pastikan folder data ada, jika tidak buat baru
     */
    public static void ensureDirectoryExists() {
        File directory = new File(DATA_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
            System.out.println("Directory 'data' created.");
        }
    }

    public static String getBarangFilePath() {
        return DATA_DIR + "/inventaris.csv";
    }

    public static String getUserFilePath() {
        return DATA_DIR + "/users.csv";
    }

    public static String getHistoryFilePath() {
        return DATA_DIR + "/history.txt";
    }

    public static String getSessionFilePath() {
        return DATA_DIR + "/session.txt";
    }
}