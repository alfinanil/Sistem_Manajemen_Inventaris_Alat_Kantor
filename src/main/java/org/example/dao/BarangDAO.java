package org.example.dao;

import org.example.model.Barang;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object untuk manage data Barang
 * Handle CRUD operations dan file persistence
 */
public class BarangDAO {

    private static final String FILE_PATH = "data/inventaris.csv";
    private static final String HEADER = "kode_barang,nama_alat,kategori,jumlah,kondisi,tanggal_input,keterangan";

    /**
     * Baca semua data barang dari file
     */
    public List<Barang> getAllBarang() {
        List<Barang> barangList = new ArrayList<>();
        File file = new File(FILE_PATH);

        // Jika file tidak ada, buat file baru dengan header
        if (!file.exists()) {
            createNewFile();
            return barangList;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line = reader.readLine(); // Skip header

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                Barang barang = Barang.fromCSV(line);
                if (barang != null) {
                    barangList.add(barang);
                }
            }

        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }

        return barangList;
    }

    /**
     * Simpan semua data barang ke file (overwrite)
     */
    public boolean saveAllBarang(List<Barang> barangList) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            // Write header
            writer.println(HEADER);

            // Write data
            for (Barang barang : barangList) {
                writer.println(barang.toCSV());
            }

            return true;

        } catch (IOException e) {
            System.err.println("Error saving file: " + e.getMessage());
            return false;
        }
    }

    /**
     * Tambah barang baru
     */
    public boolean tambahBarang(Barang barang) {
        List<Barang> barangList = getAllBarang();

        // Cek duplikat kode barang
        for (Barang b : barangList) {
            if (b.getKodeBarang().equalsIgnoreCase(barang.getKodeBarang())) {
                return false; // Kode barang sudah ada
            }
        }

        barangList.add(barang);
        return saveAllBarang(barangList);
    }

    /**
     * Update barang berdasarkan kode barang
     */
    public boolean updateBarang(String kodeBarang, Barang barangBaru) {
        List<Barang> barangList = getAllBarang();
        boolean found = false;

        for (int i = 0; i < barangList.size(); i++) {
            if (barangList.get(i).getKodeBarang().equalsIgnoreCase(kodeBarang)) {
                barangList.set(i, barangBaru);
                found = true;
                break;
            }
        }

        if (found) {
            return saveAllBarang(barangList);
        }

        return false;
    }

    /**
     * Hapus barang berdasarkan kode barang
     */
    public boolean deleteBarang(String kodeBarang) {
        List<Barang> barangList = getAllBarang();
        boolean removed = barangList.removeIf(b -> b.getKodeBarang().equalsIgnoreCase(kodeBarang));

        if (removed) {
            return saveAllBarang(barangList);
        }

        return false;
    }

    /**
     * Cari barang berdasarkan kode barang
     */
    public Barang getBarangByKode(String kodeBarang) {
        List<Barang> barangList = getAllBarang();

        for (Barang barang : barangList) {
            if (barang.getKodeBarang().equalsIgnoreCase(kodeBarang)) {
                return barang;
            }
        }

        return null;
    }

    /**
     * Generate kode barang otomatis (BRG + nomor urut)
     */
    public String generateKodeBarang() {
        List<Barang> barangList = getAllBarang();

        if (barangList.isEmpty()) {
            return "BRG001";
        }

        int maxNumber = 0;
        for (Barang barang : barangList) {
            String kode = barang.getKodeBarang();
            if (kode.startsWith("BRG")) {
                try {
                    int number = Integer.parseInt(kode.substring(3));
                    if (number > maxNumber) {
                        maxNumber = number;
                    }
                } catch (NumberFormatException e) {
                    // Skip kode yang tidak sesuai format
                }
            }
        }

        return String.format("BRG%03d", maxNumber + 1);
    }

    /**
     * Get total barang
     */
    public int getTotalBarang() {
        return getAllBarang().size();
    }

    /**
     * Get total barang berdasarkan kondisi
     */
    public int getTotalByKondisi(String kondisi) {
        List<Barang> barangList = getAllBarang();
        return (int) barangList.stream()
                .filter(b -> b.getKondisi().equalsIgnoreCase(kondisi))
                .count();
    }

    /**
     * Get barang berdasarkan kategori
     */
    public List<Barang> getBarangByKategori(String kategori) {
        List<Barang> barangList = getAllBarang();
        List<Barang> filtered = new ArrayList<>();

        for (Barang barang : barangList) {
            if (barang.getKategori().equalsIgnoreCase(kategori)) {
                filtered.add(barang);
            }
        }

        return filtered;
    }

    /**
     * Get barang dengan stok rendah (< 5)
     */
    public List<Barang> getBarangStokRendah() {
        List<Barang> barangList = getAllBarang();
        List<Barang> stokRendah = new ArrayList<>();

        for (Barang barang : barangList) {
            if (barang.getJumlah() < 5) {
                stokRendah.add(barang);
            }
        }

        return stokRendah;
    }

    /**
     * Buat file baru dengan header
     */
    private void createNewFile() {
        File directory = new File("data");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            writer.println(HEADER);
        } catch (IOException e) {
            System.err.println("Error creating file: " + e.getMessage());
        }
    }
}