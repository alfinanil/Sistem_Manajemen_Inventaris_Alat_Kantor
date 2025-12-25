package org.example.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Model class untuk Log History aktivitas
 */
public class LogHistory {

    private LocalDateTime timestamp;
    private String username;
    private String aksi; // TAMBAH, EDIT, HAPUS, LOGIN, LOGOUT
    private String detail;

    // Constructor
    public LogHistory() {
    }

    public LogHistory(LocalDateTime timestamp, String username, String aksi, String detail) {
        this.timestamp = timestamp;
        this.username = username;
        this.aksi = aksi;
        this.detail = detail;
    }

    public LogHistory(String username, String aksi, String detail) {
        this.timestamp = LocalDateTime.now();
        this.username = username;
        this.aksi = aksi;
        this.detail = detail;
    }

    // Getters and Setters
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAksi() {
        return aksi;
    }

    public void setAksi(String aksi) {
        this.aksi = aksi;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    /**
     * Convert ke format string untuk file
     */
    public String toFileFormat() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return String.format("%s|%s|%s|%s",
                timestamp.format(formatter), username, aksi, detail);
    }

    /**
     * Parse dari string file
     */
    public static LogHistory fromFileFormat(String line) {
        String[] parts = line.split("\\|", 4);
        if (parts.length < 4) {
            return null;
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime timestamp = LocalDateTime.parse(parts[0].trim(), formatter);
            String username = parts[1].trim();
            String aksi = parts[2].trim();
            String detail = parts[3].trim();

            return new LogHistory(timestamp, username, aksi, detail);

        } catch (Exception e) {
            System.err.println("Error parsing LogHistory: " + e.getMessage());
            return null;
        }
    }

    /**
     * Get formatted timestamp untuk display
     */
    public String getFormattedTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return timestamp.format(formatter);
    }

    @Override
    public String toString() {
        return String.format("%s - %s - %s: %s",
                getFormattedTimestamp(), username, aksi, detail);
    }
}