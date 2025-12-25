package org.example;

import org.example.view.LoginFrame;
import org.example.util.DatabaseConfig;

import javax.swing.*;

/**
 * Kelas Main untuk menjalankan aplikasi
 */
public class Main {
    public static void main(String[] args) {
        // 1. Setup Database Config (Pastikan folder data ada)
        DatabaseConfig.ensureDirectoryExists();

        // 2. Set Look and Feel ke System Default (biar enak dilihat di Windows/Mac/Linux)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 3. Jalankan aplikasi di thread Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            // Buka Login Frame
            new LoginFrame().setVisible(true);
        });
    }
}