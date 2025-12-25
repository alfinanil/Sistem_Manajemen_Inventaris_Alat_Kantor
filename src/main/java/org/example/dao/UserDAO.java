package org.example.dao;

import org.example.model.User;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object untuk manage data User
 */
public class UserDAO {

    private static final String FILE_PATH = "data/users.csv";
    private static final String HEADER = "username,password,nama_lengkap,role,tanggal_daftar";

    /**
     * Baca semua data user dari file
     */
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        File file = new File(FILE_PATH);

        // Jika file tidak ada, buat file dengan user default (admin)
        if (!file.exists()) {
            createDefaultUser();
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line = reader.readLine(); // Skip header

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                User user = User.fromCSV(line);
                if (user != null) {
                    userList.add(user);
                }
            }

        } catch (IOException e) {
            System.err.println("Error reading users file: " + e.getMessage());
        }

        return userList;
    }

    /**
     * Simpan semua data user ke file
     */
    public boolean saveAllUsers(List<User> userList) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            // Write header
            writer.println(HEADER);

            // Write data
            for (User user : userList) {
                writer.println(user.toCSV());
            }

            return true;

        } catch (IOException e) {
            System.err.println("Error saving users file: " + e.getMessage());
            return false;
        }
    }

    /**
     * Validasi login
     * @return User jika berhasil, null jika gagal
     */
    public User validateLogin(String username, String password) {
        List<User> userList = getAllUsers();

        for (User user : userList) {
            if (user.getUsername().equalsIgnoreCase(username) &&
                    user.getPassword().equals(password)) {
                return user;
            }
        }

        return null; // Login gagal
    }

    /**
     * Cek apakah username sudah ada
     */
    public boolean isUsernameExists(String username) {
        List<User> userList = getAllUsers();

        for (User user : userList) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Register user baru
     */
    public boolean registerUser(User newUser) {
        // Cek duplikat username
        if (isUsernameExists(newUser.getUsername())) {
            return false;
        }

        List<User> userList = getAllUsers();
        userList.add(newUser);

        return saveAllUsers(userList);
    }

    /**
     * Get user berdasarkan username
     */
    public User getUserByUsername(String username) {
        List<User> userList = getAllUsers();

        for (User user : userList) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return user;
            }
        }

        return null;
    }

    /**
     * Update user
     */
    public boolean updateUser(String username, User updatedUser) {
        List<User> userList = getAllUsers();
        boolean found = false;

        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getUsername().equalsIgnoreCase(username)) {
                userList.set(i, updatedUser);
                found = true;
                break;
            }
        }

        if (found) {
            return saveAllUsers(userList);
        }

        return false;
    }

    /**
     * Delete user
     */
    public boolean deleteUser(String username) {
        List<User> userList = getAllUsers();
        boolean removed = userList.removeIf(u -> u.getUsername().equalsIgnoreCase(username));

        if (removed) {
            return saveAllUsers(userList);
        }

        return false;
    }

    /**
     * Buat user default (admin) saat pertama kali
     */
    private void createDefaultUser() {
        File directory = new File("data");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            writer.println(HEADER);

            // User default: admin
            User admin = new User("admin", "admin123", "Administrator", "Admin", LocalDate.now());
            writer.println(admin.toCSV());

            System.out.println("Default user created - Username: admin, Password: admin123");

        } catch (IOException e) {
            System.err.println("Error creating users file: " + e.getMessage());
        }
    }
}