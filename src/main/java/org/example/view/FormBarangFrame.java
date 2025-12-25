package org.example.view;

import org.example.dao.BarangDAO;
import org.example.model.Barang;
import org.example.util.GradientPanel;
import org.example.util.RoundedBorder;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;

public class FormBarangFrame extends JFrame {

    private JTextField txtKode, txtNama, txtTanggal;
    private JComboBox<String> cbKategori, cbKondisi;
    private JSpinner spJumlah;
    private JTextArea txtKeterangan;
    private JButton btnSave, btnCancel;

    private final BarangDAO barangDAO = new BarangDAO();

    private boolean isEdit = false;
    private Barang barangEdit;

    // ================= TAMBAH =================
    public FormBarangFrame() {
        this.isEdit = false;
        initUI();
        initDataTambah();
    }

    // ================= EDIT =================
    public FormBarangFrame(Barang barang) {
        this.isEdit = true;
        this.barangEdit = barang;
        initUI();
        initDataEdit();
    }

    // ================= UI =================
    private void initUI() {
        setTitle(isEdit ? "Edit Barang" : "Tambah Barang");
        setSize(820, 680);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        GradientPanel background = new GradientPanel();
        background.setLayout(new BorderLayout());
        setContentPane(background);

        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.add(createRoundedCard());

        background.add(centerWrapper, BorderLayout.CENTER);
    }

    private JComponent createRoundedCard() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Color.WHITE);

        content.add(Box.createVerticalStrut(25));

        JLabel title = new JLabel(isEdit ? "EDIT BARANG" : "TAMBAH BARANG BARU");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(title);

        content.add(Box.createVerticalStrut(20));

        txtKode = createField();
        txtNama = createField();
        txtTanggal = createField();
        txtTanggal.setEnabled(false);

        cbKategori = createCombo("Elektronik", "ATK", "Furniture", "Lainnya");
        cbKondisi = createCombo("Baik", "Rusak");

        spJumlah = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));
        spJumlah.setMaximumSize(new Dimension(300, 42));
        spJumlah.setBorder(new RoundedBorder(20));

        txtKeterangan = new JTextArea();
        txtKeterangan.setLineWrap(true);
        txtKeterangan.setWrapStyleWord(true);
        txtKeterangan.setBorder(new RoundedBorder(20));

        JScrollPane ketScroll = new JScrollPane(txtKeterangan);
        ketScroll.setBorder(null);
        ketScroll.setMaximumSize(new Dimension(300, 90));

        content.add(field("Kode Barang", txtKode));
        content.add(field("Nama Barang", txtNama));
        content.add(field("Kategori", cbKategori));
        content.add(field("Jumlah", spJumlah));
        content.add(field("Kondisi", cbKondisi));
        content.add(field("Tanggal", txtTanggal));
        content.add(field("Keterangan", ketScroll));

        content.add(Box.createVerticalStrut(25));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        btnPanel.setOpaque(false);

        btnSave = createButton(isEdit ? "UPDATE" : "SIMPAN", new Color(0, 29, 57));
        btnSave.addActionListener(e -> handleSave());

        btnCancel = createButton("BATAL", new Color(223, 240, 248));
        btnCancel.addActionListener(e -> {
            new DaftarInventarisFrame().setVisible(true);
            dispose();
        });

        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);
        content.add(btnPanel);

        content.add(Box.createVerticalStrut(25));

        // ===== scroll + rounded card =====
        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        return new JPanel(new BorderLayout()) {
            {
                setOpaque(false);
                setPreferredSize(new Dimension(440, 580));
                setBorder(new EmptyBorder(12, 12, 12, 12));
                add(scroll);
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
                g2.dispose();
            }
        };
    }

    // ================= INIT DATA =================
    private void initDataTambah() {
        txtKode.setText(barangDAO.generateKodeBarang());
        txtTanggal.setText(LocalDate.now().toString());
    }

    private void initDataEdit() {
        txtKode.setText(barangEdit.getKodeBarang());
        txtKode.setEnabled(false);
        txtNama.setText(barangEdit.getNamaAlat());
        cbKategori.setSelectedItem(barangEdit.getKategori());
        spJumlah.setValue(barangEdit.getJumlah());
        cbKondisi.setSelectedItem(barangEdit.getKondisi());
        txtTanggal.setText(barangEdit.getTanggalInput().toString());
        txtKeterangan.setText(barangEdit.getKeterangan());
    }

    // ================= ACTION =================
    private void handleSave() {
        Barang b = new Barang(
                txtKode.getText(),
                txtNama.getText(),
                (String) cbKategori.getSelectedItem(),
                (int) spJumlah.getValue(),
                (String) cbKondisi.getSelectedItem(),
                LocalDate.parse(txtTanggal.getText()),
                txtKeterangan.getText()
        );

        if (isEdit) {
            barangDAO.updateBarang(b.getKodeBarang(), b);
            JOptionPane.showMessageDialog(this, "Barang berhasil diupdate ✅");
        } else {
            barangDAO.tambahBarang(b);
            JOptionPane.showMessageDialog(this, "Barang berhasil ditambahkan ✅");
        }

        new DaftarInventarisFrame().setVisible(true);
        dispose();
    }

    // ================= HELPER =================
    private JPanel field(String label, JComponent comp) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(6, 40, 6, 40));

        JLabel l = new JLabel(label);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        l.setAlignmentX(Component.CENTER_ALIGNMENT);

        comp.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(l);
        p.add(Box.createVerticalStrut(6));
        p.add(comp);
        return p;
    }

    private JTextField createField() {
        JTextField f = new JTextField();
        f.setMaximumSize(new Dimension(300, 42));
        f.setBorder(new RoundedBorder(20));
        f.setHorizontalAlignment(JTextField.CENTER);
        return f;
    }

    private JComboBox<String> createCombo(String... items) {
        JComboBox<String> cb = new JComboBox<>(items);
        cb.setMaximumSize(new Dimension(300, 42));
        cb.setBorder(new RoundedBorder(20));
        return cb;
    }

    private JButton createButton(String text, Color color) {
        JButton b = new JButton(text);
        b.setPreferredSize(new Dimension(140, 45));
        b.setBackground(color);
        b.setForeground(color.getRed() + color.getGreen() + color.getBlue() > 600
                ? new Color(0, 29, 57) : Color.WHITE);
        b.setFocusPainted(false);
        b.setBorder(new RoundedBorder(25));
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        return b;
    }
}
