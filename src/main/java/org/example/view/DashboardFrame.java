package org.example.view;

import org.example.dao.BarangDAO;
import org.example.dao.HistoryDAO;
import org.example.model.Barang;
import org.example.util.Session;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class DashboardFrame extends JFrame {

    private BarangDAO barangDAO;
    private HistoryDAO historyDAO;

    private JLabel totalAlatLabel;
    private JLabel alatTersediaLabel;
    private JLabel alatRusakLabel;

    private JPanel contentPanel;

    public DashboardFrame() {
        barangDAO = new BarangDAO();
        historyDAO = new HistoryDAO();

        initComponents();
        loadDashboardContent();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Sistem Manajemen Inventaris Alat Kantor");
        setSize(1100, 750);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(true);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        mainPanel.add(createSidebar(), BorderLayout.WEST);

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(223, 240, 248));
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);
    }


    private JButton createMenuButton(String text, String iconPath) {
        JButton button = new JButton(text);

        button.setIcon(new ImageIcon(getClass().getResource(iconPath)));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setIconTextGap(12);

        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setForeground(Color.BLACK);
        button.setBackground(new Color(203, 220, 235));

        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(12, 20, 12, 20));
        button.setMaximumSize(new Dimension(200, 45));

        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(69, 127, 181));
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(203, 220, 235));
            }
        });

        return button;
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(203, 220, 235));
        sidebar.setPreferredSize(new Dimension(200, 750));
        sidebar.setBorder(new EmptyBorder(20, 0, 20, 0));

        JLabel title = new JLabel("📦 Inventaris");
        title.setFont(new Font("Segoe UI emoji", Font.BOLD, 18));
        title.setForeground(Color.WHITE);
        title.setBorder(new EmptyBorder(10, 20, 30, 20));
        sidebar.add(title);

        // ===== MENU DIMULAI DI SINI =====
        JButton dashboardBtn = createMenuButton("Dashboard", "/icons/Box.png");
        JButton dataInventarisBtn = createMenuButton("Data Inventaris", "/icons/data.png");
        JButton tambahAlatBtn = createMenuButton("Tambah Alat", "/icons/edit.png");
        JButton laporanBtn = createMenuButton("Laporan", "/icons/laporan.png");
        JButton logoutBtn = createMenuButton("Logout", "/icons/log-out.png");

        // Tambahkan ActionListener untuk navigasi
        dashboardBtn.addActionListener(e -> loadDashboardContent());
        dataInventarisBtn.addActionListener(e -> openDataInventaris());
        tambahAlatBtn.addActionListener(e -> openTambahEdit());
        laporanBtn.addActionListener(e -> openLaporan());
        logoutBtn.addActionListener(e -> handleLogout());

        // Tambahkan tombol ke sidebar
        sidebar.add(dashboardBtn);
        sidebar.add(dataInventarisBtn);
        sidebar.add(tambahAlatBtn);
        sidebar.add(laporanBtn);

        sidebar.add(Box.createVerticalGlue());
        sidebar.add(logoutBtn);

        return sidebar;
    }

    /* ================= DASHBOARD CONTENT ================= */

    private void loadDashboardContent() {
        contentPanel.removeAll();

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(new Color(240, 242, 245));
        content.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel title = new JLabel("Selamat Datang di Sistem Manajemen Inventaris");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(new Color(30, 40, 60));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        content.add(title);
        content.add(Box.createVerticalStrut(25));
        content.add(createStatsPanel());
        content.add(Box.createVerticalStrut(30));
        content.add(createRecentInventoryTable());

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        contentPanel.add(scroll, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    /* ================= STATS ================= */

    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 20, 0));
        panel.setBackground(new Color(240, 242, 245));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // buat card & langsung simpan JLabel
        totalAlatLabel = createStatCard("📋", "Total Alat", new Color(59, 130, 246), panel);
        alatTersediaLabel = createStatCard("✅", "Alat Tersedia", new Color(34, 197, 94), panel);
        alatRusakLabel = createStatCard("🔧", "Alat Rusak", new Color(239, 68, 68), panel);

        updateStats();
        return panel;
    }

    private JLabel createStatCard(String icon, String title, Color color, JPanel container) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(15, 0));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 225, 230), 1),
                new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleLabel.setForeground(new Color(100, 116, 139));

        JLabel valueLabel = new JLabel("0");
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        valueLabel.setForeground(color);

        textPanel.add(titleLabel);
        textPanel.add(Box.createVerticalStrut(5));
        textPanel.add(valueLabel);

        card.add(iconLabel, BorderLayout.WEST);
        card.add(textPanel, BorderLayout.CENTER);

        container.add(card);

        return valueLabel;
    }

    private void updateStats() {
        List<Barang> list = barangDAO.getAllBarang();

        totalAlatLabel.setText(String.valueOf(list.size()));
        alatTersediaLabel.setText(String.valueOf(
                list.stream().filter(b -> b.getKondisi().equalsIgnoreCase("Baik")).count()
        ));
        alatRusakLabel.setText(String.valueOf(
                list.stream().filter(b -> b.getKondisi().equalsIgnoreCase("Rusak")).count()
        ));
    }


    private JPanel createRecentInventoryTable() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(Color.WHITE);
        section.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 225, 230), 1),
                new EmptyBorder(20, 20, 20, 20)
        ));
        section.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400));
        section.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel headerLabel = new JLabel("Inventaris Terbaru");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        headerLabel.setForeground(new Color(30, 40, 60));

        JButton lihatSemuaBtn = new JButton("Lihat Semua");
        lihatSemuaBtn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lihatSemuaBtn.setForeground(new Color(59, 130, 246));
        lihatSemuaBtn.setBackground(Color.WHITE);
        lihatSemuaBtn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(59, 130, 246), 1),
                new EmptyBorder(8, 20, 8, 20)
        ));
        lihatSemuaBtn.setFocusPainted(false);
        lihatSemuaBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lihatSemuaBtn.addActionListener(e -> openDataInventaris());

        headerPanel.add(headerLabel, BorderLayout.WEST);
        headerPanel.add(lihatSemuaBtn, BorderLayout.EAST);

        section.add(headerPanel);
        section.add(Box.createVerticalStrut(15));

        // Table
        String[] columns = {"ID", "Nama", "Kategori", "Jumlah", "Kondisi"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(40);
        table.setShowGrid(true);
        table.setGridColor(new Color(220, 225, 230));
        table.setIntercellSpacing(new Dimension(1, 1));

        // Header style sama persis dengan Data Inventaris
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(Color.WHITE);
        table.getTableHeader().setForeground(new Color(71, 85, 105));
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(1, 1, 2, 1, new Color(220, 225, 230)));
        table.getTableHeader().setReorderingAllowed(false);

        // Cell renderer mirip Data Inventaris
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, new Color(220, 225, 230)));
                if (column == 3 || column == 4) setHorizontalAlignment(JLabel.CENTER);
                else setHorizontalAlignment(JLabel.CENTER);

                if (!isSelected) {
                    setBackground(Color.WHITE);
                    setForeground(new Color(30, 40, 60));
                }
                return c;
            }
        });

        // Load recent 5 data
        List<Barang> recentBarang = barangDAO.getAllBarang();
        int limit = Math.min(5, recentBarang.size());
        for (int i = 0; i < limit; i++) {
            Barang b = recentBarang.get(i);
            model.addRow(new Object[]{
                    b.getKodeBarang(),
                    b.getNamaAlat(),
                    b.getKategori(),
                    b.getJumlah(),
                    b.getKondisi()
            });
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 225, 230), 1));
        scrollPane.setPreferredSize(new Dimension(0, 250));

        section.add(scrollPane);
        return section;
    }


    /* ================= NAV ================= */

    private void openDataInventaris() {
        new DaftarInventarisFrame().setVisible(true);
        dispose();
    }

    private void openTambahEdit() {
        new FormBarangFrame(null).setVisible(true);
        dispose();
    }

    private void openLaporan() {
        new LaporanFrame().setVisible(true);
        dispose();
    }

    private void handleLogout() {
        Session.logout();
        new LoginFrame().setVisible(true);
        dispose();
    }

}
