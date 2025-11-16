package app;

import view.Controller2D;

import javax.swing.*;

public class Main {

    public static final int APP_MAX_WIDTH = 1920;
    public static final int APP_MAX_HEIGHT = 1080;

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("PGRF1 - Úloha 2");

            Controller2D panel = new Controller2D(800, 600);

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(panel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
