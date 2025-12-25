package org.example.view;

import org.example.model.Barang;
import org.example.dao.BarangDAO;
import org.example.dao.HistoryDAO;
import org.example.util.ColorPalette;
import org.example.util.Session;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;

/**
 * Frame untuk Form Tambah/Edit Barang
 */
public class FormBarangFrame extends JFrame {

    private BarangDAO barangDAO;
    private HistoryDAO historyDAO;
    private Barang existingBarang; // Null jika mode tambah, ada objek jika mode edit

    // Components
    private JTextField txtKode;
    private JTextField txtNama;
    private JComboBox<String> cbKategori;
    private JSpinner spJumlah;
    private JComboBox<String> cbKondisi;
    private JTextField txtTanggal;
    private JTextField txtKeterangan;

    public FormBarangFrame(Barang barang) {
        this.barangDAO = new BarangDAO();
        this.historyDAO = new HistoryDAO();
        this.existingBarang = barang;

        initComponents();

        if (barang != null) {
            fillForm(barang);
        } else {
            // Mode Tambah: Generate Kode Otomatis
            txtKode.setText(barangDAO.generateKodeBarang());
            txtKode.setEditable(false);
            txtTanggal.setText(LocalDate.now().toString());
        }

        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle(existingBarang == null ? "Tambah Barang Baru" : "Edit Barang");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(750, 700);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(ColorPalette.BG_LIGHT);

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(ColorPalette.PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(0, 70));
        headerPanel.setBorder(new EmptyBorder(0, 20, 0, 20));

        JLabel titleLabel = new JLabel(existingBarang == null ? "TAMBAH BARANG" : "EDIT BARANG");
        titleLabel.setFont(ColorPalette.FONT_HEADER);
        titleLabel.setForeground(ColorPalette.TEXT_WHITE);
        headerPanel.add(titleLabel);

        // Center wrapper untuk form
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setBackground(ColorPalette.BG_LIGHT);

        // Form Panel dengan lebar terbatas dan center alignment
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(ColorPalette.BG_WHITE);
        formPanel.setBorder(new EmptyBorder(40, 80, 40, 80));
        formPanel.setPreferredSize(new Dimension(600, 620));
        formPanel.setMaximumSize(new Dimension(600, 620));

        // Input fields
        formPanel.add(createInputRow("Kode Barang", txtKode = createTextField()));
        formPanel.add(Box.createVerticalStrut(15));

        formPanel.add(createInputRow("Nama Alat", txtNama = createTextField()));
        formPanel.add(Box.createVerticalStrut(15));

        formPanel.add(createLabelRow("Kategori"));
        cbKategori = new JComboBox<String>(new String[]{"Elektronik", "Furniture", "ATK", "Alat Kebersihan", "Lainnya"}) {
            @Override
            public void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 25, 25);
                g2.setColor(ColorPalette.SECONDARY_COLOR);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 25, 25);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        cbKategori.setFont(ColorPalette.FONT_REGULAR);
        cbKategori.setMaximumSize(new Dimension(450, 45));
        cbKategori.setPreferredSize(new Dimension(450, 45));
        cbKategori.setAlignmentX(Component.CENTER_ALIGNMENT);
        cbKategori.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        cbKategori.setBackground(Color.WHITE);
        cbKategori.setOpaque(false);
        formPanel.add(cbKategori);
        formPanel.add(Box.createVerticalStrut(15));

        formPanel.add(createLabelRow("Jumlah"));
        spJumlah = new JSpinner(new SpinnerNumberModel(1, 0, 1000, 1)) {
            @Override
            public void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 25, 25);
                g2.setColor(ColorPalette.SECONDARY_COLOR);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 25, 25);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        spJumlah.setFont(ColorPalette.FONT_REGULAR);
        spJumlah.setMaximumSize(new Dimension(450, 45));
        spJumlah.setPreferredSize(new Dimension(450, 45));
        spJumlah.setAlignmentX(Component.CENTER_ALIGNMENT);
        ((JSpinner.DefaultEditor) spJumlah.getEditor()).getTextField().setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        ((JSpinner.DefaultEditor) spJumlah.getEditor()).getTextField().setOpaque(false);
        spJumlah.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        spJumlah.setBackground(Color.WHITE);
        spJumlah.setOpaque(false);
        formPanel.add(spJumlah);
        formPanel.add(Box.createVerticalStrut(15));

        formPanel.add(createLabelRow("Kondisi"));
        cbKondisi = new JComboBox<String>(new String[]{"Baik", "Rusak"}) {
            @Override
            public void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 25, 25);
                g2.setColor(ColorPalette.SECONDARY_COLOR);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 25, 25);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        cbKondisi.setFont(ColorPalette.FONT_REGULAR);
        cbKondisi.setMaximumSize(new Dimension(450, 45));
        cbKondisi.setPreferredSize(new Dimension(450, 45));
        cbKondisi.setAlignmentX(Component.CENTER_ALIGNMENT);
        cbKondisi.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        cbKondisi.setBackground(Color.WHITE);
        cbKondisi.setOpaque(false);
        formPanel.add(cbKondisi);
        formPanel.add(Box.createVerticalStrut(15));

        formPanel.add(createInputRow("Tanggal Input (YYYY-MM-DD)", txtTanggal = createTextField()));
        txtTanggal.setEnabled(false);
        formPanel.add(Box.createVerticalStrut(15));

        formPanel.add(createInputRow("Keterangan", txtKeterangan = createTextField()));
        formPanel.add(Box.createVerticalStrut(25));

        // Buttons
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        buttonPanel.setMaximumSize(new Dimension(450, 45));
        buttonPanel.setPreferredSize(new Dimension(450, 45));
        buttonPanel.setOpaque(false);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnSave = new JButton("💾 Simpan");
        styleButton(btnSave, ColorPalette.SUCCESS_COLOR);
        btnSave.addActionListener(e -> handleSave());

        JButton btnCancel = new JButton("❌ Batal");
        styleButton(btnCancel, ColorPalette.DANGER_COLOR);
        btnCancel.addActionListener(e -> handleCancel());

        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        formPanel.add(buttonPanel);

        // Tambahkan form panel ke center wrapper
        centerWrapper.add(formPanel);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Tambahkan scroll pane untuk form
        JScrollPane scrollPane = new JScrollPane(centerWrapper);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JPanel createInputRow(String labelText, JTextField textField) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(450, 80));

        JLabel label = new JLabel(labelText);
        label.setFont(ColorPalette.FONT_REGULAR);
        label.setForeground(ColorPalette.TEXT_SECONDARY);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(label);
        panel.add(Box.createVerticalStrut(8));
        panel.add(textField);

        return panel;
    }

    private JPanel createLabelRow(String labelText) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(450, 65));

        JLabel label = new JLabel(labelText);
        label.setFont(ColorPalette.FONT_REGULAR);
        label.setForeground(ColorPalette.TEXT_SECONDARY);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(label);
        panel.add(Box.createVerticalStrut(8));
        return panel;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Background rounded
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

                // Border rounded
                g2.setColor(ColorPalette.SECONDARY_COLOR);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 25, 25);

                g2.dispose();
                super.paintComponent(g);
            }
        };
        field.setFont(ColorPalette.FONT_REGULAR);
        field.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        field.setMaximumSize(new Dimension(450, 45));
        field.setPreferredSize(new Dimension(450, 45));
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
        field.setOpaque(false);
        return field;
    }

    private void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(ColorPalette.TEXT_WHITE);
        button.setFont(ColorPalette.FONT_BUTTON);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Membuat tombol rounded
        button.setBorder(new EmptyBorder(12, 20, 12, 20));
        button.setOpaque(false);
        button.setContentAreaFilled(false);

        // Override paint untuk membuat rounded corners
        button.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                JButton btn = (JButton) c;
                g2.setColor(btn.getBackground());
                g2.fillRoundRect(0, 0, btn.getWidth(), btn.getHeight(), 30, 30);

                super.paint(g2, c);
                g2.dispose();
            }
        });
    }

    private void fillForm(Barang b) {
        txtKode.setText(b.getKodeBarang());
        txtNama.setText(b.getNamaAlat());
        cbKategori.setSelectedItem(b.getKategori());
        spJumlah.setValue(b.getJumlah());
        cbKondisi.setSelectedItem(b.getKondisi());
        txtTanggal.setText(b.getTanggalInput().toString());
        txtKeterangan.setText(b.getKeterangan());
    }

    private void handleSave() {
        String kode = txtKode.getText().trim();
        String nama = txtNama.getText().trim();
        String kategori = (String) cbKategori.getSelectedItem();
        int jumlah = (int) spJumlah.getValue();
        String kondisi = (String) cbKondisi.getSelectedItem();
        LocalDate tanggal = LocalDate.parse(txtTanggal.getText().trim());
        String ket = txtKeterangan.getText().trim();

        if (nama.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama barang tidak boleh kosong!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Barang barang = new Barang(kode, nama, kategori, jumlah, kondisi, tanggal, ket);

        boolean success;
        if (existingBarang == null) {
            success = barangDAO.tambahBarang(barang);
            if (success) historyDAO.addLog(Session.getCurrentUsername(), "TAMBAH", "Menambah barang baru: " + nama);
        } else {
            success = barangDAO.updateBarang(kode, barang);
            if (success) historyDAO.addLog(Session.getCurrentUsername(), "EDIT", "Mengupdate barang: " + nama);
        }

        if (success) {
            JOptionPane.showMessageDialog(this, "Data berhasil disimpan!");
            new DaftarInventarisFrame().setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan data. Kode barang mungkin duplikat.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleCancel() {
        new DaftarInventarisFrame().setVisible(true);
        dispose();
    }
}