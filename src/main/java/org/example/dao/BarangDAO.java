package org.example.dao;

import org.example.model.Barang;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Data Access Object untuk manage data Barang
 * Handle CRUD operations dan file persistence (CSV & Excel)
 */
public class BarangDAO {

    private static final String FILE_PATH = "data/inventaris.csv";
    private static final String EXCEL_PATH = "data/inventaris.xlsx";
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

            // Auto save to Excel juga
            saveToExcel(barangList);

            return true;

        } catch (IOException e) {
            System.err.println("Error saving file: " + e.getMessage());
            return false;
        }
    }

    /**
     * Export data ke Excel (.xlsx)
     */
    public boolean saveToExcel(List<Barang> barangList) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Inventaris");

        // Create header style
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 12);
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);

        // Create data style
        CellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setBorderBottom(BorderStyle.THIN);
        dataStyle.setBorderTop(BorderStyle.THIN);
        dataStyle.setBorderLeft(BorderStyle.THIN);
        dataStyle.setBorderRight(BorderStyle.THIN);

        // Create header row
        Row headerRow = sheet.createRow(0);
        String[] columns = {"Kode Barang", "Nama Alat", "Kategori", "Jumlah", "Kondisi", "Tanggal Input", "Keterangan"};

        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerStyle);
        }

        // Fill data rows
        int rowNum = 1;
        for (Barang barang : barangList) {
            Row row = sheet.createRow(rowNum++);

            Cell cell0 = row.createCell(0);
            cell0.setCellValue(barang.getKodeBarang());
            cell0.setCellStyle(dataStyle);

            Cell cell1 = row.createCell(1);
            cell1.setCellValue(barang.getNamaAlat());
            cell1.setCellStyle(dataStyle);

            Cell cell2 = row.createCell(2);
            cell2.setCellValue(barang.getKategori());
            cell2.setCellStyle(dataStyle);

            Cell cell3 = row.createCell(3);
            cell3.setCellValue(barang.getJumlah());
            cell3.setCellStyle(dataStyle);

            Cell cell4 = row.createCell(4);
            cell4.setCellValue(barang.getKondisi());
            cell4.setCellStyle(dataStyle);

            Cell cell5 = row.createCell(5);
            cell5.setCellValue(barang.getTanggalInput().toString());
            cell5.setCellStyle(dataStyle);

            Cell cell6 = row.createCell(6);
            cell6.setCellValue(barang.getKeterangan());
            cell6.setCellStyle(dataStyle);
        }

        // Auto-size columns
        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write to file
        try {
            File directory = new File("data");
            if (!directory.exists()) {
                directory.mkdirs();
            }

            FileOutputStream fileOut = new FileOutputStream(EXCEL_PATH);
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();

            System.out.println("✓ Data berhasil disimpan ke Excel: " + EXCEL_PATH);
            return true;

        } catch (IOException e) {
            System.err.println("Error saving to Excel: " + e.getMessage());
            return false;
        }
    }

    /**
     * Import data dari Excel (.xlsx)
     */
    public List<Barang> loadFromExcel(String filePath) {
        List<Barang> barangList = new ArrayList<>();

        try {
            FileInputStream file = new FileInputStream(filePath);
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(0);

            // Skip header row (row 0)
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                try {
                    String kodeBarang = getCellValueAsString(row.getCell(0));
                    String namaAlat = getCellValueAsString(row.getCell(1));
                    String kategori = getCellValueAsString(row.getCell(2));
                    int jumlah = (int) getCellValueAsNumeric(row.getCell(3));
                    String kondisi = getCellValueAsString(row.getCell(4));
                    String tanggalStr = getCellValueAsString(row.getCell(5));
                    String keterangan = getCellValueAsString(row.getCell(6));

                    LocalDate tanggal = LocalDate.parse(tanggalStr);

                    Barang barang = new Barang(kodeBarang, namaAlat, kategori, jumlah, kondisi, tanggal, keterangan);
                    barangList.add(barang);

                } catch (Exception e) {
                    System.err.println("Error parsing row " + i + ": " + e.getMessage());
                }
            }

            workbook.close();
            file.close();

            System.out.println("✓ Data berhasil diimport dari Excel: " + barangList.size() + " items");

        } catch (IOException e) {
            System.err.println("Error loading from Excel: " + e.getMessage());
        }

        return barangList;
    }

    /**
     * Helper method untuk ambil nilai cell sebagai String
     */
    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    Date date = cell.getDateCellValue();
                    LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    return localDate.toString();
                } else {
                    return String.valueOf((int) cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }

    /**
     * Helper method untuk ambil nilai cell sebagai angka
     */
    private double getCellValueAsNumeric(Cell cell) {
        if (cell == null) return 0;

        switch (cell.getCellType()) {
            case NUMERIC:
                return cell.getNumericCellValue();
            case STRING:
                try {
                    return Double.parseDouble(cell.getStringCellValue());
                } catch (NumberFormatException e) {
                    return 0;
                }
            default:
                return 0;
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