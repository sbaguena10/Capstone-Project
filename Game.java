
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Game {
    private static JFrame frame;
    private static Board customBoard;

    public static void createStartScreen() {
        frame = new JFrame("Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 400);

        // Main panel with a gradient background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(70, 130, 180); // Steel Blue
                Color color2 = new Color(135, 206, 235); // Sky Blue
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new GridBagLayout());

        // Content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        // Title
        JLabel gameTitle = new JLabel("Welcome to the Countdown");
        gameTitle.setFont(new Font("Arial", Font.BOLD, 32));
        gameTitle.setForeground(Color.WHITE);
        gameTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Start button
        JButton startButton = new JButton("Start Game");
        startButton.setFont(new Font("Arial", Font.BOLD, 16));
        startButton.setForeground(new Color(50, 50, 50));
        startButton.setBackground(new Color(255, 255, 255));
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.setMaximumSize(new Dimension(200, 50));
        startButton.setFocusPainted(false);
        startButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)));

        // Hover effect for the start button
        startButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                startButton.setBackground(new Color(230, 230, 230));
            }

            public void mouseExited(MouseEvent e) {
                startButton.setBackground(Color.WHITE);
            }
        });

        // Start button action listener
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });

        // Settings button
        JButton settingsButton = new JButton("Help");
        settingsButton.setFont(new Font("Arial", Font.BOLD, 16));
        settingsButton.setForeground(new Color(50, 50, 50));
        settingsButton.setBackground(new Color(255, 255, 255));
        settingsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        settingsButton.setMaximumSize(new Dimension(200, 50));
        settingsButton.setFocusPainted(false);
        settingsButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)));

        // Hover effect for the settings button
        settingsButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                settingsButton.setBackground(new Color(230, 230, 230));
            }

            public void mouseExited(MouseEvent e) {
                settingsButton.setBackground(Color.WHITE);
            }
        });

        // Settings button action listener (show game instructions)
        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showSettings();
            }
        });

        // Components to content panel
        contentPanel.add(gameTitle);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        contentPanel.add(startButton);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add space between buttons
        contentPanel.add(settingsButton);

        // Content panel to main panel
        mainPanel.add(contentPanel);

        frame.add(mainPanel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // Show settings explanation when settings button is clicked
    private static void showSettings() {
        // Display game instructions in a dialog box
        String instructions = "Welcome to the Countdown! Here's how it works:\n\n" +
                "1. Each player starts with full health.\n" +
                "2. In each round, players will make a move.\n" +
                "3. Power-ups are scattered across the board and activate when a player lands on them:\n" +
                "   • Laser (Red): Damages all opponents in the same row.\n" +
                "   • Bomb (Blue): Damages all opponents in adjacent squares.\n" +
                "   • Shield (Green): Grants protection from the next hit.\n" +
                "   • Fire (Orange): Randomly places fire on the board which can damage others.\n" +
                "   • Health Pack (Pink): Heals the player who lands on it.\n" +
                "4. Players lose health when they take damage.\n" +
                "5. The game ends when the human player dies or all AI opponents are eliminated.";

        JOptionPane.showMessageDialog(frame, instructions, "How the Game Works", JOptionPane.INFORMATION_MESSAGE);
    }

    private static void startGame() {
        frame.getContentPane().removeAll();
        customBoard = new Board();
        frame.add(customBoard);
        frame.revalidate();
        frame.repaint();
        customBoard.requestFocusInWindow();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createStartScreen();
            }
        });
    }
}
