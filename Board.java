import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Board extends JPanel {

    private static final int CELL_SIZE = 50; // Size of each square cell
    private static final int ROWS = 6; // Number of rows in the main board
    private static final int COLS = 14; // Number of columns in both grids
    private static final int SEPARATION_GAP = 20; // Gap between the top grid and main grid

    private Cell[] topGrid; // Array for the top grid with 1 row
    private Cell[][] mainGrid; // 2D array for the main 6x14 grid
    private Player player;

    public Board() {
        topGrid = new Cell[COLS];
        mainGrid = new Cell[ROWS][COLS];
        initializeGrids();
        setupKeyBindings(); // Set up key bindings for the A key
        paintPlayers();
    }

    // Initialize both the top and main grids
    private void initializeGrids() {
        for (int col = 0; col < COLS; col++) {
            topGrid[col] = new Cell(); // Top grid cells
        }

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                mainGrid[row][col] = new Cell(); // Main grid cells
            }
        }
    }

    private void paintPlayers() {
        player = new Player("Player 1", 0, 0, Color.BLACK);
        // player = new Player("Sergio", 4,9, Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.WHITE);

        int boardWidth = COLS * CELL_SIZE;
        int boardHeight = (ROWS + 1) * CELL_SIZE + SEPARATION_GAP;
        int xOffset = (getWidth() - boardWidth) / 2;
        int yOffset = (getHeight() - boardHeight) / 2;

        drawTopGrid(g, xOffset, yOffset);
        drawMainGrid(g, xOffset, yOffset + CELL_SIZE + SEPARATION_GAP);
        drawPlayer(g, xOffset, yOffset + CELL_SIZE + SEPARATION_GAP);
    }

    private void drawPlayer(Graphics g, int xOffset, int yOffset) {
        int playerX = player.getRow();
        int playerY = player.getCol();

        // Calculate the position based on the player's coordinates
        int x = xOffset + playerX * CELL_SIZE;
        int y = yOffset + playerY * CELL_SIZE;

        // Draw the player as a red circle
        g.setColor(player.getColor());
        g.fillOval(x + 5, y + 5, CELL_SIZE - 10, CELL_SIZE - 10); // Offset to center the player
    }

    private void drawTopGrid(Graphics g, int xOffset, int yOffset) {
        for (int col = 0; col < COLS; col++) {
            Cell cell = topGrid[col];
            int x = xOffset + col * CELL_SIZE;
            int y = yOffset;

            g.setColor(cell.getColor());
            g.fillRect(x, y, CELL_SIZE, CELL_SIZE);

            g.setColor(Color.BLACK);
            g.drawRect(x, y, CELL_SIZE, CELL_SIZE);

        }
    }

    private void drawMainGrid(Graphics g, int xOffset, int yOffset) {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                Cell cell = mainGrid[row][col];
                int x = xOffset + col * CELL_SIZE;
                int y = yOffset + row * CELL_SIZE;

                // Set color based on whether the cell has a superpower
                g.setColor(cell.getColor()); // Paint with superpower color

                g.fillRect(x, y, CELL_SIZE, CELL_SIZE);

                // Draw cell border
                g.setColor(Color.BLACK);
                g.drawRect(x, y, CELL_SIZE, CELL_SIZE);
            }
        }
    }

    private void updateBoard() {
        // Store the last row of the main grid
        Cell[] lastRow = mainGrid[ROWS - 1];

        // Moves rows down
        for (int row = ROWS - 1; row > 0; row--) {
            mainGrid[row] = mainGrid[row - 1];
        }

        // Place the saved bottom row at the top
        mainGrid[0] = topGrid;

        // The top grid becomes the last row
        topGrid = lastRow;

        // Repaint the board to reflect the changes
        repaint();
    }

    private void setupKeyBindings() {
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("A"), "wrapBoard");
        getActionMap().put("wrapBoard", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateBoard(); // Call the wrap-around method
            }
        });
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(COLS * CELL_SIZE, (ROWS + 1) * CELL_SIZE + SEPARATION_GAP);
    }
}
