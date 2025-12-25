package org.example.util;

import org.example.model.User;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Class untuk mengelola session user yang sedang login
 */
public class Session {

    private static User currentUser = null;
    private static final String SESSION_FILE = "data/session.txt";

    /**
     * Set user yang sedang login
     */
    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    /**
     * Get user yang sedang login
     */
    public static User getCurrentUser() {
        return currentUser;
    }

    /**
     * Get username user yang sedang login
     */
    public static String getCurrentUsername() {
        return currentUser != null ? currentUser.getUsername() : "";
    }

    /**
     * Get nama lengkap user yang sedang login
     */
    public static String getCurrentNamaLengkap() {
        return currentUser != null ? currentUser.getNamaLengkap() : "";
    }

    /**
     * Get role user yang sedang login
     */
    public static String getCurrentRole() {
        return currentUser != null ? currentUser.getRole() : "";
    }

    /**
     * Cek apakah user sudah login
     */
    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    /**
     * Logout user (hapus session)
     */
    public static void logout() {
        currentUser = null;
        deleteSessionFile();
    }

    /**
     * Simpan session ke file (untuk remember me)
     */
    public static void saveSession(boolean rememberMe) {
        if (!rememberMe || currentUser == null) {
            return;
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(SESSION_FILE))) {
            writer.println(currentUser.getUsername());
            writer.println(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        } catch (IOException e) {
            System.err.println("Error saving session: " + e.getMessage());
        }
    }

    /**
     * Load session dari file
     * @return username jika session valid, null jika tidak
     */
    public static String loadSession() {
        File file = new File(SESSION_FILE);
        if (!file.exists()) {
            return null;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(SESSION_FILE))) {
            String username = reader.readLine();
            String timestampStr = reader.readLine();

            if (username == null || timestampStr == null) {
                return null;
            }

            // Cek apakah session sudah expired (7 hari)
            LocalDateTime savedTime = LocalDateTime.parse(timestampStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            LocalDateTime now = LocalDateTime.now();

            if (now.minusDays(7).isAfter(savedTime)) {
                // Session expired
                deleteSessionFile();
                return null;
            }

            return username;

        } catch (IOException e) {
            System.err.println("Error loading session: " + e.getMessage());
            return null;
        }
    }

    /**
     * Hapus file session
     */
    private static void deleteSessionFile() {
        File file = new File(SESSION_FILE);
        if (file.exists()) {
            file.delete();
        }
    }
}