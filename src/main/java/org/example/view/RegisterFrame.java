package org.example.view;

import org.example.dao.UserDAO;
import org.example.model.User;
import org.example.util.Session;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;

public class RegisterFrame extends JFrame {

    private JTextField namaLengkapField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JLabel errorLabel;

    private final UserDAO userDAO = new UserDAO();

    private static final Dimension FIELD_SIZE = new Dimension(340, 50);
    private static final Dimension BUTTON_SIZE = new Dimension(240, 42);
    private static final Color PRIMARY = new Color(109, 148, 197);

    public RegisterFrame(LoginFrame loginFrame) {
        initComponents();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Daftar Akun");
        setSize(600, 780);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new GridBagLayout()) {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(0, 29, 57, 255),
                        0, getHeight(), new Color(223, 240, 248)
                );
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        mainPanel.add(createCardPanel());
        add(mainPanel);
    }

    private JPanel createCardPanel() {
        JPanel card = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int r = 42;
                g2.setColor(new Color(0, 0, 0, 20));
                g2.fillRoundRect(8, 8, getWidth() - 16, getHeight() - 16, r, r);

                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth() - 16, getHeight() - 16, r, r);
            }
        };

        card.setOpaque(false);
        card.setLayout(new BorderLayout());
        card.setPreferredSize(new Dimension(460, 650));

        card.add(createHeaderPanel(), BorderLayout.NORTH);
        card.add(createFormPanel(), BorderLayout.CENTER);

        return card;
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int r = 42;
                g2.setColor(PRIMARY);
                g2.fillRoundRect(0, 0, getWidth(), getHeight() + r, r, r);
                g2.fillRect(0, r, getWidth(), getHeight());
            }
        };

        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(35, 30, 35, 30));
        panel.setPreferredSize(new Dimension(460, 180));

        JLabel icon = new JLabel("📝");
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 56));
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("DAFTAR AKUN BARU");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(icon);
        panel.add(Box.createVerticalStrut(12));
        panel.add(title);

        return panel;
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int r = 42;
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, -r, getWidth(), getHeight() + r, r, r);
                g2.fillRect(0, 0, getWidth(), r);
            }
        };

        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(30, 55, 25, 55));

        panel.add(label("Nama Lengkap"));
        panel.add(inputField(namaLengkapField = new JTextField()));
        panel.add(Box.createVerticalStrut(16));

        panel.add(label("Username (min. 4 karakter)"));
        panel.add(inputField(usernameField = new JTextField()));
        panel.add(Box.createVerticalStrut(16));

        panel.add(label("Password (min. 6 karakter)"));
        panel.add(inputField(passwordField = new JPasswordField()));
        panel.add(Box.createVerticalStrut(16));

        panel.add(label("Konfirmasi Password"));
        panel.add(inputField(confirmPasswordField = new JPasswordField()));
        panel.add(Box.createVerticalStrut(10));

        errorLabel = new JLabel(" ");
        errorLabel.setForeground(new Color(239, 68, 68));
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(errorLabel);

        panel.add(Box.createVerticalStrut(18));

        JButton daftarBtn = createPrimaryButton("DAFTAR");
        daftarBtn.addActionListener(e -> handleRegister());
        panel.add(daftarBtn);

        panel.add(Box.createVerticalStrut(25));

        JLabel copyright = new JLabel("© 2025 • Kelompok UAP Pemrograman Lanjut");
        copyright.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        copyright.setForeground(new Color(148, 163, 184));
        copyright.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(copyright);

        return panel;
    }

    private JPanel inputField(JComponent field) {
        JPanel wrapper = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int radius = 18;

                // background soft
                g2.setColor(new Color(239, 246, 251)); // abu sangat soft
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

                // border
                g2.setColor(new Color(239, 246, 251));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            }
        };

        wrapper.setLayout(new BorderLayout());
        wrapper.setMaximumSize(FIELD_SIZE);
        wrapper.setOpaque(false);

        field.setBorder(new EmptyBorder(8, 14, 8, 14));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setOpaque(false); // 🔥 PENTING
        field.setBackground(new Color(0, 0, 0, 0)); // transparan

        wrapper.add(field, BorderLayout.CENTER);
        return wrapper;
    }


    private JButton createPrimaryButton(String text) {
        JButton btn = new JButton(text) {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(PRIMARY);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);

                g2.setColor(Color.WHITE);
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent()) / 2 - 2;
                g2.drawString(getText(), x, y);
            }
        };

        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(BUTTON_SIZE);
        btn.setPreferredSize(BUTTON_SIZE);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);

        return btn;
    }

    private JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(new Color(0, 29, 57));
        l.setAlignmentX(Component.CENTER_ALIGNMENT);
        return l;
    }

    private void handleRegister() {
        String nama = namaLengkapField.getText().trim();
        String user = usernameField.getText().trim();
        String pass = new String(passwordField.getPassword());
        String conf = new String(confirmPasswordField.getPassword());

        if (nama.isEmpty() || user.isEmpty() || pass.isEmpty() || conf.isEmpty()) {
            errorLabel.setText("Semua field wajib diisi");
            return;
        }

        if (user.length() < 4) {
            errorLabel.setText("Username minimal 4 karakter");
            return;
        }

        if (pass.length() < 6) {
            errorLabel.setText("Password minimal 6 karakter");
            return;
        }

        if (!pass.equals(conf)) {
            errorLabel.setText("Password tidak sama");
            return;
        }

        if (userDAO.registerUser(User.fromCSV(user))) {
            errorLabel.setText("Username sudah terdaftar");
            return;
        }

        User newUser = new User(user, pass, nama, "User", LocalDate.now());
        userDAO.registerUser(newUser);
        Session.setCurrentUser(newUser);

        new DashboardFrame().setVisible(true);
        dispose();
    }
}
