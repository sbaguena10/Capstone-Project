
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * the board class represents the board where the game is played. The board
 * consists in a 6 x 14 grid plus a 1 x 14 top grid
 */
public class Board extends JPanel {

    private static final int CELL_SIZE = 50; // Size of each square cell
    private static final int ROWS = 6; // Number of rows in the main board
    private static final int COLS = 14; // Number of columns in both grids
    private static final int SEPARATION_GAP = 20; // Gap between the top grid and main grid

    private Cell[] topGrid; // Array for the top grid with 1 row
    private Cell[][] mainGrid; // 2D array for the main 6x14 grid
    private List<Player> players; // List of players

    /**
     * it constructs a board object, it initializes the grids and the players.
     */
    public Board() {
        topGrid = new Cell[COLS];
        mainGrid = new Cell[ROWS][COLS];
        players = new ArrayList<>();
        initializeGrids();
        initializePlayers(); // Initialize players on the board
        setupKeyBindings(); // Set up key bindings for the A key
    }

    /**
     * this method initialized both the top and the main grid creating cell objects.
     */
    private void initializeGrids() { // Initialize both the top and main grids
        for (int col = 0; col < COLS; col++) {
            topGrid[col] = new Cell(); // Top grid cells
        }

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                mainGrid[row][col] = new Cell(); // Main grid cells
            }
        }
    }

    /**
     * this method intialized the players and places them at the 4 distinct corners
     * of the main grid.
     */
    private void initializePlayers() {
        // Add four players at different positions
        players.add(new HumanPlayer("Human Player", 0, 0, Color.RED)); // Top-left corner
        players.add(new IAplayer("AI Player 1", ROWS - 1, COLS - 1, Color.BLUE)); // Bottom-right corner
        players.add(new IAplayer("AI Player 2", ROWS - 1, 0, Color.GREEN)); // Bottom-left corner
        players.add(new IAplayer("AI Player 3", 0, COLS - 1, Color.YELLOW)); // Top-right corner
    }

    /**
     * the paintComponent method to render the board, including the top grid,
     * main grid, and player positions. The board is centered within the JPanel.
     */
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.WHITE);

        int boardWidth = COLS * CELL_SIZE;
        int boardHeight = (ROWS + 1) * CELL_SIZE + SEPARATION_GAP;
        int xOffset = (getWidth() - boardWidth) / 2;
        int yOffset = (getHeight() - boardHeight) / 2;

        drawTopGrid(g, xOffset, yOffset);
        drawMainGrid(g, xOffset, yOffset + CELL_SIZE + SEPARATION_GAP);
        drawPlayers(g, xOffset, yOffset + CELL_SIZE + SEPARATION_GAP);
    }

    /**
     * This method draw the players.
     * 
     * @param g       Graphics object used
     * @param xOffset horizontal offset to center the grid
     * @param yOffset vertical offset to center the grid
     */
    private void drawPlayers(Graphics g, int xOffset, int yOffset) {
        for (Player player : players) {
            int playerX = player.getCol();
            int playerY = player.getRow();

            // Calculate the position based on the player's coordinates
            int x = xOffset + playerX * CELL_SIZE;
            int y = yOffset + playerY * CELL_SIZE;

            // Draw the player as a circle with the player's color
            g.setColor(player.getColor());
            g.fillOval(x + 5, y + 5, CELL_SIZE - 10, CELL_SIZE - 10); // Offset to center the player
        }
    }

    /**
     * draws the top grid. it cell it is filled with its color
     * 
     * @param g       graphics object used
     * @param xOffset xOffset horizontal offset to center the grid
     * @param yOffset yOffset vertical offset to center the grid
     */
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

    /**
     * draws the main grid, where each cell is filled with its color
     * 
     * @param g       graphics object used
     * @param xOffset xOffset horizontal offset to center the grid
     * @param yOffset yOffset vertical offset to center the grid
     */
    private void drawMainGrid(Graphics g, int xOffset, int yOffset) {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                Cell cell = mainGrid[row][col];
                int x = xOffset + col * CELL_SIZE;
                int y = yOffset + row * CELL_SIZE;

                g.setColor(cell.getColor());
                g.fillRect(x, y, CELL_SIZE, CELL_SIZE);

                g.setColor(Color.BLACK);
                g.drawRect(x, y, CELL_SIZE, CELL_SIZE);
            }
        }
    }

    /**
     * wrap around method to move the board downwards. The bottom row of the board
     * becomes the row at the top grid, and the top grid row becomes the first line
     * of the main grid.
     * 
     */
    private void updateBoard() {
        // Store the last row of the main grid
        Cell[] lastRow = mainGrid[ROWS - 1];

        // Move rows down
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

    /**
     * Sets up a key binding for the "A" key. When is pressed the board updates
     */
    private void setupKeyBindings() {
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("A"), "wrapBoard");
        getActionMap().put("wrapBoard", new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                updateBoard(); // Call the wrap-around method
            }
        });
    }

    // @Override
    // public Dimension getPreferredSize() {
    // return new Dimension(COLS * CELL_SIZE, (ROWS + 1) * CELL_SIZE +
    // SEPARATION_GAP);
    // }
}
