package org.example.dao;

import org.example.model.LogHistory;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HistoryDAO {

    private static final String FILE_PATH = "data/history.txt";
    private static final String EXCEL_PATH = "data/history.xlsx";

    public List<LogHistory> getAllHistory() {
        List<LogHistory> list = new ArrayList<>();
        File file = new File(FILE_PATH);

        if (!file.exists()) createFile();

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    list.add(LogHistory.fromFileFormat(line));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean addLog(LogHistory log) {
        new File("data").mkdirs();
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_PATH, true))) {
            pw.println(log.toFileFormat());
            saveToExcel(getAllHistory());
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean addLog(String username, String aksi, String detail) {
        return addLog(new LogHistory(username, aksi, detail));
    }

    private void saveToExcel(List<LogHistory> logs) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("History");

            Row header = sheet.createRow(0);
            String[] cols = {"Tanggal", "Username", "Aksi", "Detail"};

            for (int i = 0; i < cols.length; i++) {
                header.createCell(i).setCellValue(cols[i]);
            }

            int rowNum = 1;
            for (LogHistory h : logs) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(h.getTimestamp().toString());
                row.createCell(1).setCellValue(h.getUsername());
                row.createCell(2).setCellValue(h.getAksi());
                row.createCell(3).setCellValue(h.getDetail());
            }

            try (FileOutputStream out = new FileOutputStream(EXCEL_PATH)) {
                workbook.write(out);
            }

        } catch (IOException e) {
            System.err.println("Excel history gagal: " + e.getMessage());
        }
    }

    private void createFile() {
        new File("data").mkdirs();
        try {
            new File(FILE_PATH).createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<LogHistory> getHistoryByDate(LocalDate date) {
        List<LogHistory> result = new ArrayList<>();
        for (LogHistory h : getAllHistory()) {
            if (h.getTimestamp().toLocalDate().equals(date)) {
                result.add(h);
            }
        }
        return result;
    }
}
