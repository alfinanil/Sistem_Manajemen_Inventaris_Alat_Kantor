package org.example.view;

import org.example.model.LogHistory;
import org.example.dao.HistoryDAO;
import org.example.util.ColorPalette;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Frame untuk melihat Laporan / Riwayat Aktivitas
 */
public class LaporanFrame extends JFrame {

    private HistoryDAO historyDAO;

    // SIMPAN REFERENSI (INI KUNCI)
    private JTable table;
    private DefaultTableModel tableModel;

    public LaporanFrame() {
        historyDAO = new HistoryDAO();
        initComponents();
        loadHistory();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Laporan Aktivitas - Sistem Inventaris Kantor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setResizable(true);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(ColorPalette.BG_LIGHT);

        mainPanel.add(createTopBar(), BorderLayout.NORTH);
        mainPanel.add(createContentPanel(), BorderLayout.CENTER);

        setContentPane(mainPanel);
    }

    /* ================= TOP BAR ================= */

    private JPanel createTopBar() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ColorPalette.PRIMARY_COLOR);
        panel.setBorder(new EmptyBorder(15, 25, 15, 25));
        panel.setPreferredSize(new Dimension(0, 70));

        // Panel kiri untuk judul
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setBackground(ColorPalette.PRIMARY_COLOR);

        JLabel titleLabel = new JLabel("  LAPORAN AKTIVITAS");
        titleLabel.setFont(ColorPalette.FONT_HEADER);
        titleLabel.setForeground(ColorPalette.TEXT_WHITE);

        leftPanel.add(titleLabel);
        panel.add(leftPanel, BorderLayout.WEST);

        // Panel kanan untuk button kembali
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setBackground(ColorPalette.PRIMARY_COLOR);

        JButton btnBack = new JButton("⬅ Kembali");
        btnBack.setFont(ColorPalette.FONT_REGULAR);
        btnBack.setForeground(ColorPalette.TEXT_WHITE);
        btnBack.setBackground(new Color(0, 0, 0, 0));
        btnBack.setFocusPainted(false);
        btnBack.setBorderPainted(false);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBack.addActionListener(e -> {
            new DashboardFrame().setVisible(true);
            dispose();
        });

        rightPanel.add(btnBack);
        panel.add(rightPanel, BorderLayout.EAST);

        return panel;
    }

    /* ================= CONTENT ================= */

    private JPanel createContentPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ColorPalette.BG_LIGHT);
        panel.setBorder(new EmptyBorder(20, 25, 20, 25));

        String[] columnNames = {"Waktu", "Username", "Aksi", "Detail"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setFont(ColorPalette.FONT_REGULAR);
        table.setRowHeight(35);
        table.setShowGrid(true);
        table.setGridColor(new Color(220, 220, 220));
        table.setBackground(Color.WHITE);
        table.setSelectionBackground(new Color(230, 240, 255));
        table.setSelectionForeground(Color.BLACK);

        // Header styling
        table.getTableHeader().setFont(ColorPalette.FONT_SUBHEADER);
        table.getTableHeader().setBackground(Color.WHITE);
        table.getTableHeader().setForeground(Color.BLACK);
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(220, 220, 220)));
        table.getTableHeader().setReorderingAllowed(false);

        // Center alignment untuk semua kolom
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        centerRenderer.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, new Color(220, 220, 220)));

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Set column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(150); // Waktu
        table.getColumnModel().getColumn(1).setPreferredWidth(120); // Username
        table.getColumnModel().getColumn(2).setPreferredWidth(100); // Aksi
        table.getColumnModel().getColumn(3).setPreferredWidth(250); // Detail

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        scrollPane.getViewport().setBackground(Color.WHITE);

        panel.add(scrollPane, BorderLayout.CENTER);

        JLabel infoLabel = new JLabel(" Menampilkan log aktivitas pengguna dalam sistem.");
        infoLabel.setFont(ColorPalette.FONT_SMALL);
        infoLabel.setForeground(ColorPalette.TEXT_SECONDARY);
        infoLabel.setBorder(new EmptyBorder(10, 0, 0, 0));
        panel.add(infoLabel, BorderLayout.SOUTH);

        return panel;
    }

    /* ================= DATA ================= */

    private void loadHistory() {
        tableModel.setRowCount(0); // clear table

        List<LogHistory> logs = historyDAO.getAllHistory();

        for (LogHistory log : logs) {
            tableModel.addRow(new Object[]{
                    log.getFormattedTimestamp(),
                    log.getUsername(),
                    log.getAksi(),
                    log.getDetail()
            });
        }
    }
}
