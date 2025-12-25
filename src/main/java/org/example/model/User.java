package org.example.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Model class untuk data User
 */
public class User {

    private String username;
    private String password;
    private String namaLengkap;
    private String role; // "Admin" atau "User"
    private LocalDate tanggalDaftar;

    // Constructor
    public User() {
    }

    public User(String username, String password, String namaLengkap, String role, LocalDate tanggalDaftar) {
        this.username = username;
        this.password = password;
        this.namaLengkap = namaLengkap;
        this.role = role;
        this.tanggalDaftar = tanggalDaftar;
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNamaLengkap() {
        return namaLengkap;
    }

    public void setNamaLengkap(String namaLengkap) {
        this.namaLengkap = namaLengkap;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public LocalDate getTanggalDaftar() {
        return tanggalDaftar;
    }

    public void setTanggalDaftar(LocalDate tanggalDaftar) {
        this.tanggalDaftar = tanggalDaftar;
    }

    /**
     * Convert ke format CSV
     */
    public String toCSV() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return String.format("%s,%s,%s,%s,%s",
                username, password, namaLengkap, role, tanggalDaftar.format(formatter));
    }

    /**
     * Parse dari string CSV
     */
    public static User fromCSV(String csvLine) {
        String[] parts = csvLine.split(",", 5);
        if (parts.length < 5) {
            return null;
        }

        try {
            String username = parts[0].trim();
            String password = parts[1].trim();
            String namaLengkap = parts[2].trim();
            String role = parts[3].trim();
            LocalDate tanggalDaftar = LocalDate.parse(parts[4].trim());

            return new User(username, password, namaLengkap, role, tanggalDaftar);

        } catch (Exception e) {
            System.err.println("Error parsing User from CSV: " + e.getMessage());
            return null;
        }
    }

    @Override
    public String toString() {
        return String.format("User{username=%s, nama=%s, role=%s}", username, namaLengkap, role);
    }
}