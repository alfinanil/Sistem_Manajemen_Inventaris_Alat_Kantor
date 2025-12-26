package org.example;

import org.example.view.LoginFrame;
import org.example.util.DatabaseConfig;

import javax.swing.*;

/**
 * Kelas Main untuk menjalankan aplikasi
 */
public class Main {
    public static void main(String[] args) {
        DatabaseConfig.ensureDirectoryExists();

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}