package org.example.view;

import org.example.model.Barang;
import org.example.dao.BarangDAO;
import org.example.dao.HistoryDAO;
import org.example.util.Session;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class DaftarInventarisFrame extends JFrame {

    private BarangDAO barangDAO;
    private HistoryDAO historyDAO;

    private JTable table;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;
    private JTextField searchField;

    public DaftarInventarisFrame() {
        barangDAO = new BarangDAO();
        historyDAO = new HistoryDAO();

        initComponents();
        loadTableData();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Daftar Inventaris");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 650);
        setLayout(new BorderLayout());

        JPanel background = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                        RenderingHints.VALUE_RENDER_QUALITY);

                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(0, 29, 57),
                        0, getHeight(), new Color(189, 216, 233)
                );

                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        add(background);

        background.add(createTopBar(), BorderLayout.NORTH);
        background.add(createCardPanel(), BorderLayout.CENTER);
    }

    // ================= TOP BAR =================
    private JPanel createTopBar() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(0, 90));

        JLabel title = new JLabel("Kelola Data Inventaris");
        title.setFont(new Font("Segoe UI Semibold", Font.BOLD, 22));
        title.setForeground(Color.WHITE);

        panel.add(title);
        return panel;
    }

    // ================= CARD =================
    private JPanel createCardPanel() {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);

        JPanel card = new JPanel(new BorderLayout(15, 15)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(20, 20, 20, 20));
        card.setPreferredSize(new Dimension(950, 480));

        card.add(createSearchBar(), BorderLayout.NORTH);
        card.add(createTablePanel(), BorderLayout.CENTER);
        card.add(createActionPanel(), BorderLayout.SOUTH);

        wrapper.add(card);
        return wrapper;
    }

    // ================= SEARCH =================
    private JPanel createSearchBar() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        searchField = new JTextField("Cari barang...");
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        searchField.setForeground(Color.GRAY);
        searchField.setPreferredSize(new Dimension(200, 38));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                new EmptyBorder(8, 12, 8, 12)
        ));

        searchField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (searchField.getText().equals("Cari barang...")) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK);
                }
            }

            public void focusLost(java.awt.event.FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Cari barang...");
                    searchField.setForeground(Color.GRAY);
                }
            }
        });

        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchTable();
            }
        });

        panel.add(searchField, BorderLayout.CENTER);
        return panel;
    }

    // ================= TABLE =================
    private JScrollPane createTablePanel() {
        String[] columns = {
                "Kode Barang", "Nama Alat", "Kategori",
                "Jumlah", "Kondisi", "Tanggal Input", "Keterangan"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(32);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        // 🔥 FULL GRID
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(true);
        table.setGridColor(new Color(220, 220, 220));

        table.setSelectionBackground(new Color(200, 230, 255));
        table.setFillsViewportHeight(true);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 13));
        header.setBackground(new Color(220, 235, 245));
        header.setForeground(new Color(30, 30, 30));
        header.setPreferredSize(new Dimension(0, 40));
        header.setReorderingAllowed(false);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {

                super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                setHorizontalAlignment(JLabel.CENTER);

                if (isSelected) {
                    setBackground(new Color(200, 230, 255));
                } else {
                    setBackground(row % 2 == 0
                            ? Color.WHITE
                            : new Color(245, 248, 250));
                }
                return this;
            }
        });

        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Color.WHITE);

        return scroll;
    }

    // ================= ACTION =================
    private JPanel createActionPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(15, 0, 0, 0));

        JButton btnBack = createRoundedButton(
                "Kembali", new Color(120, 120, 120), loadIcon("/icons/back.png")
        );
        btnBack.addActionListener(e -> {
            new DashboardFrame().setVisible(true);
            dispose();
        });

        JButton btnEdit = createRoundedButton(
                "Edit", new Color(2, 136, 209), loadIcon("/icons/edit.png")
        );
        btnEdit.addActionListener(e -> handleEdit());

        JButton btnDelete = createRoundedButton(
                "Hapus", new Color(211, 47, 47), loadIcon("/icons/trash.png")
        );
        btnDelete.addActionListener(e -> handleDelete());

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT));
        left.setOpaque(false);
        left.add(btnBack);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        right.setOpaque(false);
        right.add(btnEdit);
        right.add(btnDelete);

        panel.add(left, BorderLayout.WEST);
        panel.add(right, BorderLayout.EAST);
        return panel;
    }

    // ================= UTIL =================
    private JButton createRoundedButton(String text, Color bg, ImageIcon icon) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                super.paintComponent(g);
            }
        };

        button.setIcon(icon);
        button.setHorizontalTextPosition(SwingConstants.RIGHT);
        button.setIconTextGap(8);
        button.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 13));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(120, 38));

        return button;
    }

    private ImageIcon loadIcon(String path) {
        Image img = new ImageIcon(getClass().getResource(path))
                .getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    private void loadTableData() {
        tableModel.setRowCount(0);
        List<Barang> list = barangDAO.getAllBarang();

        for (Barang b : list) {
            tableModel.addRow(new Object[]{
                    b.getKodeBarang(),
                    b.getNamaAlat(),
                    b.getKategori(),
                    b.getJumlah(),
                    b.getKondisi(),
                    b.getTanggalInput(),
                    b.getKeterangan()
            });
        }
    }

    private void searchTable() {
        String text = searchField.getText();
        if (text.trim().isEmpty() || text.equals("Cari barang...")) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
        }
    }

    private void handleEdit() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data dulu ya ✨");
            return;
        }

        int modelRow = table.convertRowIndexToModel(row);
        String kode = tableModel.getValueAt(modelRow, 0).toString();
        Barang barang = barangDAO.getBarangByKode(kode);

        if (barang != null) {
            new FormBarangFrame(barang).setVisible(true); // 🔥 EDIT MODE
            dispose();
        }
    }


    private void handleDelete() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data dulu ya ✨");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Yakin mau hapus barang ini?",
                "Konfirmasi",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            int modelRow = table.convertRowIndexToModel(row);
            String kode = tableModel.getValueAt(modelRow, 0).toString();
            String nama = tableModel.getValueAt(modelRow, 1).toString();

            if (barangDAO.deleteBarang(kode)) {
                historyDAO.addLog(
                        Session.getCurrentUsername(),
                        "HAPUS",
                        "Menghapus barang: " + nama + " (" + kode + ")"
                );
                loadTableData();
                JOptionPane.showMessageDialog(this, "Barang berhasil dihapus ✅");
            }
        }
    }
}
