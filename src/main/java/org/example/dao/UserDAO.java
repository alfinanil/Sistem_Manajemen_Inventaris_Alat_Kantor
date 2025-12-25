package org.example.dao;

import org.example.model.User;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    private static final String FILE_PATH = "data/users.csv";
    private static final String EXCEL_PATH = "data/users.xlsx";
    private static final String HEADER =
            "username,password,nama_lengkap,role,tanggal_daftar";

    // ================= GET ALL USERS =================
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            createDefaultUser();
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            reader.readLine(); // skip header
            String line;

            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    User user = User.fromCSV(line);
                    if (user != null) {
                        users.add(user);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error read users: " + e.getMessage());
        }

        return users;
    }

    // ================= SAVE USERS =================
    public boolean saveAllUsers(List<User> users) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            writer.println(HEADER);
            for (User u : users) {
                if (u != null) {
                    writer.println(u.toCSV());
                }
            }
            saveToExcel(users);
            return true;
        } catch (IOException e) {
            System.err.println("Gagal simpan users: " + e.getMessage());
            return false;
        }
    }

    // ================= SAVE TO EXCEL =================
    private void saveToExcel(List<User> users) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Users");

            Row header = sheet.createRow(0);
            String[] cols = {"Username", "Nama Lengkap", "Role", "Tanggal Daftar"};

            for (int i = 0; i < cols.length; i++) {
                header.createCell(i).setCellValue(cols[i]);
            }

            int rowNum = 1;
            for (User u : users) {
                if (u == null) continue;

                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(u.getUsername());
                row.createCell(1).setCellValue(u.getNamaLengkap());
                row.createCell(2).setCellValue(u.getRole());
                row.createCell(3).setCellValue(u.getTanggalDaftar().toString());
            }

            File dir = new File("data");
            if (!dir.exists()) dir.mkdirs();

            try (FileOutputStream out = new FileOutputStream(EXCEL_PATH)) {
                workbook.write(out);
            }

        } catch (IOException e) {
            System.err.println("Excel users gagal: " + e.getMessage());
        }
    }

    // ================= LOGIN =================
    public User validateLogin(String username, String password) {
        if (username == null || password == null) return null;

        for (User u : getAllUsers()) {
            if (u != null &&
                    u.getUsername().equalsIgnoreCase(username) &&
                    u.getPassword().equals(password)) {
                return u;
            }
        }
        return null;
    }

    // ================= REGISTER =================
    public boolean registerUser(User user) {
        if (user == null || user.getUsername() == null) {
            return false;
        }

        if (getUserByUsername(user.getUsername()) != null) {
            return false;
        }

        List<User> users = getAllUsers();
        users.add(user);
        return saveAllUsers(users);
    }

    // ================= FIND USER =================
    public User getUserByUsername(String username) {
        if (username == null) return null;

        for (User u : getAllUsers()) {
            if (u != null && u.getUsername().equalsIgnoreCase(username)) {
                return u;
            }
        }
        return null;
    }

    // ================= DEFAULT ADMIN =================
    private void createDefaultUser() {
        new File("data").mkdirs();
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            writer.println(HEADER);
            User admin = new User(
                    "admin",
                    "admin123",
                    "Administrator",
                    "Admin",
                    LocalDate.now()
            );
            writer.println(admin.toCSV());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
