package org.example.dao;

import org.example.model.LogHistory;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object untuk manage log history
 */
public class HistoryDAO {

    private static final String FILE_PATH = "data/history.txt";

    /**
     * Baca semua log history dari file
     */
    public List<LogHistory> getAllHistory() {
        List<LogHistory> historyList = new ArrayList<>();
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            createNewFile();
            return historyList;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                LogHistory log = LogHistory.fromFileFormat(line);
                if (log != null) {
                    historyList.add(log);
                }
            }

        } catch (IOException e) {
            System.err.println("Error reading history file: " + e.getMessage());
        }

        return historyList;
    }

    /**
     * Tambah log baru (append ke file)
     */
    public boolean addLog(LogHistory log) {
        File directory = new File("data");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH, true))) {
            writer.println(log.toFileFormat());
            return true;

        } catch (IOException e) {
            System.err.println("Error writing to history file: " + e.getMessage());
            return false;
        }
    }

    /**
     * Tambah log dengan parameter langsung
     */
    public boolean addLog(String username, String aksi, String detail) {
        LogHistory log = new LogHistory(username, aksi, detail);
        return addLog(log);
    }

    /**
     * Get history berdasarkan username
     */
    public List<LogHistory> getHistoryByUsername(String username) {
        List<LogHistory> allHistory = getAllHistory();
        List<LogHistory> filtered = new ArrayList<>();

        for (LogHistory log : allHistory) {
            if (log.getUsername().equalsIgnoreCase(username)) {
                filtered.add(log);
            }
        }

        return filtered;
    }

    /**
     * Get history berdasarkan aksi
     */
    public List<LogHistory> getHistoryByAksi(String aksi) {
        List<LogHistory> allHistory = getAllHistory();
        List<LogHistory> filtered = new ArrayList<>();

        for (LogHistory log : allHistory) {
            if (log.getAksi().equalsIgnoreCase(aksi)) {
                filtered.add(log);
            }
        }

        return filtered;
    }

    /**
     * Get history berdasarkan tanggal
     */
    public List<LogHistory> getHistoryByDate(LocalDate date) {
        List<LogHistory> allHistory = getAllHistory();
        List<LogHistory> filtered = new ArrayList<>();

        for (LogHistory log : allHistory) {
            if (log.getTimestamp().toLocalDate().equals(date)) {
                filtered.add(log);
            }
        }

        return filtered;
    }

    /**
     * Get history dalam range tanggal
     */
    public List<LogHistory> getHistoryByDateRange(LocalDate startDate, LocalDate endDate) {
        List<LogHistory> allHistory = getAllHistory();
        List<LogHistory> filtered = new ArrayList<>();

        for (LogHistory log : allHistory) {
            LocalDate logDate = log.getTimestamp().toLocalDate();
            if (!logDate.isBefore(startDate) && !logDate.isAfter(endDate)) {
                filtered.add(log);
            }
        }

        return filtered;
    }

    /**
     * Get N history terakhir
     */
    public List<LogHistory> getRecentHistory(int limit) {
        List<LogHistory> allHistory = getAllHistory();

        // Reverse list untuk get yang terbaru
        List<LogHistory> reversed = new ArrayList<>();
        for (int i = allHistory.size() - 1; i >= 0 && reversed.size() < limit; i--) {
            reversed.add(allHistory.get(i));
        }

        return reversed;
    }

    /**
     * Clear semua history (hati-hati!)
     */
    public boolean clearHistory() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            // File dikosongkan
            return true;

        } catch (IOException e) {
            System.err.println("Error clearing history: " + e.getMessage());
            return false;
        }
    }

    /**
     * Buat file baru jika belum ada
     */
    private void createNewFile() {
        File directory = new File("data");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        try {
            new File(FILE_PATH).createNewFile();
        } catch (IOException e) {
            System.err.println("Error creating history file: " + e.getMessage());
        }
    }
}