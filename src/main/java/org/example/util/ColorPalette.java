package org.example.util;

import java.awt.Color;
import java.awt.Font;

/**
 * Class untuk menyimpan konstanta warna dan font yang digunakan di aplikasi
 * Berdasarkan color palette: True Navy, Blue Door, Harper's Blue, Alice Blue
 */
public class ColorPalette {

    // Primary Colors
    public static final Color PRIMARY_COLOR = new Color(50, 55, 77);      // True Navy #32374D
    public static final Color SECONDARY_COLOR = new Color(67, 104, 117);  // Blue Door #436875
    public static final Color ACCENT_COLOR = new Color(208, 232, 237);    // Alice Blue #D0E8ED
    public static final Color TERTIARY_COLOR = new Color(208, 219, 231);  // Harper's Blue #D0DBE7

    // Functional Colors
    public static final Color SUCCESS_COLOR = new Color(39, 174, 96);     // Green
    public static final Color WARNING_COLOR = new Color(243, 156, 18);    // Orange
    public static final Color DANGER_COLOR = new Color(231, 76, 60);      // Red
    public static final Color INFO_COLOR = new Color(52, 152, 219);       // Blue

    // Text Colors
    public static final Color TEXT_PRIMARY = new Color(33, 37, 41);       // Dark
    public static final Color TEXT_SECONDARY = new Color(108, 117, 125);  // Gray
    public static final Color TEXT_WHITE = Color.WHITE;

    // Background Colors
    public static final Color BG_LIGHT = new Color(248, 249, 250);        // Light Gray
    public static final Color BG_WHITE = Color.WHITE;
    public static final Color BG_DARK = PRIMARY_COLOR;

    // Button Colors
    public static final Color BUTTON_PRIMARY = PRIMARY_COLOR;
    public static final Color BUTTON_PRIMARY_HOVER = new Color(67, 104, 117);
    public static final Color BUTTON_SECONDARY = SECONDARY_COLOR;
    public static final Color BUTTON_SUCCESS = SUCCESS_COLOR;
    public static final Color BUTTON_DANGER = DANGER_COLOR;

    // Table Colors
    public static final Color TABLE_HEADER = PRIMARY_COLOR;
    public static final Color TABLE_ROW_EVEN = Color.WHITE;
    public static final Color TABLE_ROW_ODD = new Color(248, 249, 250);
    public static final Color TABLE_SELECTION = ACCENT_COLOR;

    // Fonts
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_SUBHEADER = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_REGULAR = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 13);

    // Border Radius (untuk referensi)
    public static final int BORDER_RADIUS = 8;
    public static final int BUTTON_RADIUS = 6;
}