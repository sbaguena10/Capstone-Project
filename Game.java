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

        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(70, 130, 180);
                Color color2 = new Color(135, 206, 235);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new GridBagLayout());

        // content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        // title
        JLabel gameTitle = new JLabel("Welcome to the Countdown");
        gameTitle.setFont(new Font("Arial", Font.BOLD, 32));
        gameTitle.setForeground(Color.WHITE);
        gameTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // start button
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

        // hover effect to button
        startButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                startButton.setBackground(new Color(230, 230, 230));
            }

            public void mouseExited(MouseEvent e) {
                startButton.setBackground(Color.WHITE);
            }
        });

        // action listener to start button
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });

        // components to content panel
        contentPanel.add(gameTitle);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        contentPanel.add(startButton);

        // content panel to main panel
        mainPanel.add(contentPanel);

        // main panel to frame
        frame.add(mainPanel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
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
