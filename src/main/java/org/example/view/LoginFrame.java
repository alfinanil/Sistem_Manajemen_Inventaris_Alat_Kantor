package org.example.view;

import org.example.dao.UserDAO;
import org.example.dao.HistoryDAO;
import org.example.model.User;
import org.example.util.Session;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel errorLabel;

    private UserDAO userDAO;
    private HistoryDAO historyDAO;

    private static final Dimension BUTTON_SIZE = new Dimension(240, 42);

    public LoginFrame() {
        userDAO = new UserDAO();
        historyDAO = new HistoryDAO();
        initComponents();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Login - Sistem Inventaris Kantor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 780);
        setResizable(false);

        JPanel mainPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(0, 29, 57),
                        0, getHeight(), new Color(223, 240, 248)
                );
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.add(createCardPanel());

        add(mainPanel);
    }

    private JPanel createCardPanel() {
        JPanel card = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int radius = 42;

                g2.setColor(new Color(0, 0, 0, 20));
                g2.fillRoundRect(8, 8, getWidth() - 16, getHeight() - 16, radius, radius);

                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth() - 16, getHeight() - 16, radius, radius);
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

                int radius = 42;

                g2.setColor(new Color(109, 148, 197));
                g2.fillRoundRect(0, 0, getWidth(), getHeight() + radius, radius, radius);
                g2.fillRect(0, radius, getWidth(), getHeight());
            }
        };

        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(35, 30, 35, 30));
        panel.setPreferredSize(new Dimension(460, 180));

        JLabel icon = new JLabel("📦");
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 56));
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("SISTEM INVENTARIS");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("ALAT KANTOR");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        subtitle.setForeground(new Color(200, 210, 220));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(icon);
        panel.add(Box.createVerticalStrut(12));
        panel.add(title);
        panel.add(Box.createVerticalStrut(6));
        panel.add(subtitle);

        return panel;
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int radius = 42;

                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, -radius, getWidth(), getHeight() + radius, radius, radius);
                g2.fillRect(0, 0, getWidth(), radius);
            }
        };

        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(30, 55, 25, 55));

        JLabel welcome = new JLabel("Selamat Datang!");
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 22));
        welcome.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel slogan = new JLabel("Kelola inventaris alat kantor dengan mudah dan terorganisir.");
        slogan.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        slogan.setForeground(new Color(100, 116, 139));
        slogan.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(welcome);
        panel.add(Box.createVerticalStrut(6));
        panel.add(slogan);
        panel.add(Box.createVerticalStrut(25));

        panel.add(label("Username"));
        panel.add(Box.createVerticalStrut(8));
        panel.add(usernameField());
        panel.add(Box.createVerticalStrut(18));

        panel.add(label("Password"));
        panel.add(Box.createVerticalStrut(8));
        panel.add(passwordField());
        panel.add(Box.createVerticalStrut(10));

        errorLabel = new JLabel(" ");
        errorLabel.setForeground(new Color(239, 68, 68));
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(errorLabel);

        panel.add(Box.createVerticalStrut(18));

        JButton loginBtn = createLoginButton();
        loginBtn.addActionListener(e -> handleLogin());
        panel.add(loginBtn);

        panel.add(Box.createVerticalStrut(12));

        JButton registerBtn = new JButton("Buat Akun");
        registerBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        registerBtn.setMaximumSize(BUTTON_SIZE);
        registerBtn.setPreferredSize(BUTTON_SIZE);
        registerBtn.setMinimumSize(BUTTON_SIZE);
        registerBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerBtn.setFocusPainted(false);
        registerBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerBtn.addActionListener(e -> openRegisterFrame());
        panel.add(registerBtn);

        panel.add(Box.createVerticalStrut(14));
        panel.add(registerLink());

        panel.add(Box.createVerticalStrut(25));

        JLabel copyright = new JLabel("© 2025 • Kelompok UAP Pemrograman Lanjut");
        copyright.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        copyright.setForeground(new Color(148, 163, 184));
        copyright.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(copyright);

        return panel;
    }

    private JButton createLoginButton() {
        JButton btn = new JButton("Log in") {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(new Color(109, 148, 197));
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
        btn.setMinimumSize(BUTTON_SIZE);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);

        return btn;
    }

    private JPanel usernameField() {
        return inputField("👤", usernameField = new JTextField());
    }

    private JPanel passwordField() {
        return inputField("🔒", passwordField = new JPasswordField());
    }

    private JPanel inputField(String icon, JComponent field) {
        JPanel p = new JPanel(new BorderLayout(12, 0));
        p.setMaximumSize(new Dimension(340, 50));
        p.setBorder(BorderFactory.createLineBorder(new Color(203, 213, 225)));

        JLabel ic = new JLabel(icon);
        ic.setBorder(new EmptyBorder(0, 10, 0, 0));
        p.add(ic, BorderLayout.WEST);

        field.setBorder(new EmptyBorder(8, 8, 8, 8));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        p.add(field, BorderLayout.CENTER);

        return p;
    }

    private JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(new Color(100, 116, 139));
        l.setAlignmentX(Component.CENTER_ALIGNMENT);
        return l;
    }

    private JPanel registerLink() {
        JPanel p = new JPanel();
        p.setBackground(Color.WHITE);
        p.add(new JLabel("Belum punya akun? "));
        JLabel link = new JLabel("Daftar disini");
        link.setForeground(new Color(59, 130, 246));
        link.setCursor(new Cursor(Cursor.HAND_CURSOR));
        link.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                openRegisterFrame();
            }
        });
        p.add(link);
        return p;
    }

    private void handleLogin() {
        User user = userDAO.validateLogin(
                usernameField.getText(),
                new String(passwordField.getPassword())
        );

        if (user != null) {
            Session.setCurrentUser(user);
            historyDAO.addLog(user.getUsername(), "LOGIN", "Login berhasil");
            new DashboardFrame().setVisible(true);
            dispose();
        } else {
            errorLabel.setText("Username atau Password salah!");
        }
    }

    private void openRegisterFrame() {
        new RegisterFrame(this).setVisible(true);
        setVisible(false);
    }
}
