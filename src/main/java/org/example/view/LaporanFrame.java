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

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setBackground(ColorPalette.PRIMARY_COLOR);

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

        JLabel titleLabel = new JLabel("  LAPORAN AKTIVITAS");
        titleLabel.setFont(ColorPalette.FONT_HEADER);
        titleLabel.setForeground(ColorPalette.TEXT_WHITE);

        leftPanel.add(btnBack);
        leftPanel.add(titleLabel);
        panel.add(leftPanel, BorderLayout.WEST);

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
        table.setRowHeight(30);
        table.getTableHeader().setFont(ColorPalette.FONT_SUBHEADER);
        table.getTableHeader().setBackground(ColorPalette.PRIMARY_COLOR);
        table.getTableHeader().setForeground(ColorPalette.TEXT_WHITE);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(null);

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
