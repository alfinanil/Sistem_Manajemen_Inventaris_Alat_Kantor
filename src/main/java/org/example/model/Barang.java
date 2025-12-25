package org.example.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Model class untuk data Barang/Inventaris
 */
public class Barang {

    private String kodeBarang;
    private String namaAlat;
    private String kategori;
    private int jumlah;
    private String kondisi; // "Baik" atau "Rusak"
    private LocalDate tanggalInput;
    private String keterangan;

    // Constructor
    public Barang() {
    }

    public Barang(String kodeBarang, String namaAlat, String kategori,
                  int jumlah, String kondisi, LocalDate tanggalInput, String keterangan) {
        this.kodeBarang = kodeBarang;
        this.namaAlat = namaAlat;
        this.kategori = kategori;
        this.jumlah = jumlah;
        this.kondisi = kondisi;
        this.tanggalInput = tanggalInput;
        this.keterangan = keterangan;
    }

    // Getters and Setters
    public String getKodeBarang() {
        return kodeBarang;
    }

    public void setKodeBarang(String kodeBarang) {
        this.kodeBarang = kodeBarang;
    }

    public String getNamaAlat() {
        return namaAlat;
    }

    public void setNamaAlat(String namaAlat) {
        this.namaAlat = namaAlat;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public String getKondisi() {
        return kondisi;
    }

    public void setKondisi(String kondisi) {
        this.kondisi = kondisi;
    }

    public LocalDate getTanggalInput() {
        return tanggalInput;
    }

    public void setTanggalInput(LocalDate tanggalInput) {
        this.tanggalInput = tanggalInput;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    /**
     * Convert ke format CSV
     */
    public String toCSV() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return String.format("%s,%s,%s,%d,%s,%s,%s",
                kodeBarang, namaAlat, kategori, jumlah, kondisi,
                tanggalInput.format(formatter),
                keterangan.replace(",", ";")); // Replace comma untuk avoid conflict
    }

    /**
     * Parse dari string CSV
     */
    public static Barang fromCSV(String csvLine) {
        String[] parts = csvLine.split(",", 7);
        if (parts.length < 7) {
            return null;
        }

        try {
            String kodeBarang = parts[0].trim();
            String namaAlat = parts[1].trim();
            String kategori = parts[2].trim();
            int jumlah = Integer.parseInt(parts[3].trim());
            String kondisi = parts[4].trim();
            LocalDate tanggal = LocalDate.parse(parts[5].trim());
            String keterangan = parts[6].trim().replace(";", ",");

            return new Barang(kodeBarang, namaAlat, kategori, jumlah, kondisi, tanggal, keterangan);

        } catch (Exception e) {
            System.err.println("Error parsing Barang from CSV: " + e.getMessage());
            return null;
        }
    }

    @Override
    public String toString() {
        return String.format("Barang{kode=%s, nama=%s, kategori=%s, jumlah=%d, kondisi=%s}",
                kodeBarang, namaAlat, kategori, jumlah, kondisi);
    }
}