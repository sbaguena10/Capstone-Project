import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The Board class represents the board where the game is played.
 * The board consists of a 6x14 main grid and a 1x14 top grid.
 */
public class Board extends JPanel implements MouseMotionListener {

    private static final int CELL_SIZE = 50; // Size of the cell
    private static final int ROWS = 6; // Number of rows in the main board
    private static final int COLS = 14; // Number of columns in both grids
    private static final int SEPARATION_GAP = 20; // Gap between the top grid and main grid

    private Cell[] topGrid; // Array for the top grid with 1 row
    private Cell[][] mainGrid; // 2D array for the main 6x14 grid
    private List<Player> players; // List of players

    private int hoverRow = -1; // Hovered row (-1 indicates the top grid)
    private int hoverCol = -1; // Hovered column
    private Color hoverColor = new Color(255, 255, 0, 100); // Semi-transparent yellow for hover

    private int selectedRow = -1;
    private int selectedCol = -1;
    private List<Point> validMoves = new ArrayList<>();

    /**
     * Constructs a Board object, initializes the grids and the players.
     */
    public Board() {
        topGrid = new Cell[COLS];
        mainGrid = new Cell[ROWS][COLS];
        players = new ArrayList<>();
        initializeGrids();
        initializePlayers();
        setupKeyBindings();
        addMouseMotionListener(this);

        // Add mouse listener for clicks
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClick(e);
            }
        });
    }

    /**
     * Initializes both the top and the main grid by creating Cell objects.
     */
    private void initializeGrids() {
        for (int col = 0; col < COLS; col++) {
            topGrid[col] = new Cell();
        }

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                mainGrid[row][col] = new Cell();
            }
        }
    }

    /**
     * Initializes the players and places them at the 4 distinct corners of the main
     * grid.
     */
    private void initializePlayers() {
        players.add(new HumanPlayer("Human Player", 0, 0, Color.RED)); // Top-left corner
        players.add(new IAplayer("AI Player 1", ROWS - 1, COLS - 1, Color.BLUE)); // Bottom-right corner
        players.add(new IAplayer("AI Player 2", ROWS - 1, 0, Color.GREEN)); // Bottom-left corner
        players.add(new IAplayer("AI Player 3", 0, COLS - 1, Color.YELLOW)); // Top-right corner
    }

    /**
     * Sets up key bindings for the board.
     */
    private void setupKeyBindings() {
        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();

        // Key binding for both human and AI player movements
        inputMap.put(KeyStroke.getKeyStroke("A"), "moveAndUpdate");
        actionMap.put("moveAndUpdate", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveHumanPlayer(); // Move the human player
                moveAIPlayers(); // Move all AI players
                updateBoard(); // Update the board
            }
        });
    }

    /**
     * method to invoque the method to move the ia players. It checks if the player
     * is AI, and then if it is moves it
     */
    private void moveAIPlayers() {
        for (Player player : players) {
            if (player instanceof IAplayer) {
                IAplayer aiPlayer = (IAplayer) player;
                aiPlayer.IAmove(); // AI move
            }
        }
    }

    /**
     * method to move the human player, it checks if the movement is valid after the
     * selection
     * and updates the players position and the row
     */
    private void moveHumanPlayer() {
        if (selectedRow != -1 && selectedCol != -1) {
            HumanPlayer humanPlayer = (HumanPlayer) players.getFirst();
            if (humanPlayer != null) {
                // Calculate row and column change from current position
                int rowOffset = selectedRow - humanPlayer.getRow();
                int colOffset = selectedCol - humanPlayer.getCol();
                int distance = Math.abs(rowOffset) + Math.abs(colOffset);

                // Validate move is within the 3-square limit and that the target position isn't
                // occupied
                if (distance <= 3 && !isOccupied(selectedRow, selectedCol)) {
                    // Move the human player using the calculated offset
                    humanPlayer.Humanmove(rowOffset, colOffset);

                    // After the move, reset selectedRow and selectedCol to prevent reuse for
                    // invalid moves
                    selectedRow = -1;
                    selectedCol = -1;

                    // Repaint the board to reflect the player's new position
                    repaint();
                }
            }
        }
    }

    /**
     * this method checks if the cell that the player is moving is occupied or not.
     * Will need change of the logic later on
     * 
     * @param row
     * @param col
     * @return
     */
    private boolean isOccupied(int row, int col) {
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            if (player.getRow() == row && player.getCol() == col) {
                return true; // A player occupies this cell
            }
        }
        return false; // No player occupies this cell
    }

    /**
     * method to handle mouse clicks, checks if the cell of the desired movement is
     * a valid one or not.
     */
    private void handleMouseClick(MouseEvent e) {
        int boardWidth = COLS * CELL_SIZE;
        int boardHeight = (ROWS + 1) * CELL_SIZE + SEPARATION_GAP;
        int xOffset = (getWidth() - boardWidth) / 2;
        int yOffset = (getHeight() - boardHeight) / 2;

        int mouseX = e.getX() - xOffset;
        int mouseY = e.getY() - (yOffset + CELL_SIZE + SEPARATION_GAP);
        int clickedRow = mouseY / CELL_SIZE;
        int clickedCol = mouseX / CELL_SIZE;

        if (clickedRow >= 0 && clickedRow < ROWS && clickedCol >= 0 && clickedCol < COLS) {
            HumanPlayer humanPlayer = (HumanPlayer) players.getFirst();
            if (humanPlayer != null) {
                int rowOffset = clickedRow - humanPlayer.getRow();
                int colOffset = clickedCol - humanPlayer.getCol();
                int distance = Math.abs(rowOffset) + Math.abs(colOffset);

                if (distance <= 3 && !isOccupied(clickedRow, clickedCol)) {
                    selectedRow = clickedRow;
                    selectedCol = clickedCol;
                    repaint();
                }
            }
        }
    }

    private void drawPlayerInfo(Graphics g, int panelWidth, int panelHeight) {
        g.setFont(new Font("Arial", Font.BOLD, 18));
        FontMetrics metrics = g.getFontMetrics();
        int indicatorSize = 25; // Size of the colored circle
        int padding = 20; // Padding from the edges
        int healthBarWidth = 90; // Width of the health bar
        int healthBarHeight = 15; // Height of the health bar

        int offset = 15; // Space between circle and text

        // Positions of players in the four corners
        int[][] playerPositions = {
                { 0, 0 }, // Top-left
                { 3, 3 }, // Top-right
                { 3, 0 }, // Bottom-left
                { 0, 3 } // Bottom-right
        };

        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            int health = player.getTotalHealth();
            String name = player.getPlayerName();

            int row = playerPositions[i][0];
            int col = playerPositions[i][1];

            // Calculate circle position based on the row and column
            int circleX = padding;
            int circleY = padding;
            int textX = circleX + indicatorSize + offset;
            int textY = circleY + indicatorSize - 2;
            int healthX = textX;
            int healthY = textY + 10;

            if (row == 0 && col == 3) { // Top-right
                circleX = panelWidth - padding - indicatorSize;
                textX = circleX - metrics.stringWidth(name) - offset;
                healthX = textX;
            } else if (row == 3 && col == 0) { // Bottom-left
                circleY = panelHeight - padding - indicatorSize;
                textY = circleY + indicatorSize - 2;
                healthY = textY + 10;
            } else if (row == 3 && col == 3) { // Bottom-right
                circleX = panelWidth - padding - indicatorSize;
                circleY = panelHeight - padding - indicatorSize;
                textX = circleX - metrics.stringWidth(name) - offset;
                textY = circleY + indicatorSize - 2;
                healthX = textX;
                healthY = textY + 10;
            }

            // Draw the player's color circle
            g.setColor(player.getColor());
            g.fillOval(circleX, circleY, indicatorSize, indicatorSize);

            // Draw the player's name
            g.setColor(Color.BLACK);
            g.drawString(name, textX, textY);

            // Draw the health bar background
            g.setColor(Color.GRAY);
            g.fillRect(healthX, healthY, healthBarWidth, healthBarHeight);

            // Draw the health bar (colored based on health value)
            g.setColor(getHealthColor(health));
            int currentHealthWidth = (int) ((health / 100.0) * healthBarWidth);
            g.fillRect(healthX, healthY, currentHealthWidth, healthBarHeight);

            // Draw the border for the health bar
            g.setColor(Color.BLACK);
            g.drawRect(healthX, healthY, healthBarWidth, healthBarHeight);

            // Draw the health percentage text
            String healthText = health + "%";
            g.setColor(Color.BLACK);
            g.drawString(healthText, healthX + healthBarWidth + 10, healthY + healthBarHeight / 2 + 5);
        }
    }

    private Color getHealthColor(int health) {
        if (health > 70) {
            return Color.GREEN;
        } else if (health > 30) {
            return Color.YELLOW;
        } else {
            return Color.RED;
        }
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
        drawPlayers(g, xOffset, yOffset + CELL_SIZE + SEPARATION_GAP);
        drawHoverHighlight(g, xOffset, yOffset);
        drawPlayerInfo(g, getWidth(), getHeight());

        // Draw selected cell highlight
        if (selectedRow != -1 && selectedCol != -1) {
            g.setColor(new Color(0, 255, 0, 100));
            int x = xOffset + selectedCol * CELL_SIZE;
            int y = yOffset + (CELL_SIZE + SEPARATION_GAP) + selectedRow * CELL_SIZE;
            g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
        }
    }

    /**
     * Draws the hover highlight if the mouse is over a valid cell.
     */
    private void drawHoverHighlight(Graphics g, int xOffset, int yOffset) {
        if (hoverCol == -1)
            return;

        int x = xOffset + hoverCol * CELL_SIZE;
        int y;

        if (hoverRow == -1) {
            y = yOffset; // Top grid
        } else {
            y = yOffset + (CELL_SIZE + SEPARATION_GAP) + hoverRow * CELL_SIZE;
        }

        g.setColor(hoverColor);
        g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
    }

    /**
     * Draws the players.
     */
    private void drawPlayers(Graphics g, int xOffset, int yOffset) {
        for (Player player : players) {
            int x = xOffset + player.getCol() * CELL_SIZE;
            int y = yOffset + player.getRow() * CELL_SIZE;

            g.setColor(player.getColor());
            g.fillOval(x + 5, y + 5, CELL_SIZE - 10, CELL_SIZE - 10);
        }
    }

    /**
     * Draws the top grid.
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
     * Draws the main grid.
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
     * Wrap-around method to move the board downwards.
     */
    private void updateBoard() {
        Cell[] lastRow = mainGrid[ROWS - 1];
        for (int row = ROWS - 1; row > 0; row--) {
            mainGrid[row] = mainGrid[row - 1];
        }
        mainGrid[0] = topGrid;
        topGrid = lastRow;
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int boardWidth = COLS * CELL_SIZE;
        int boardHeight = (ROWS + 1) * CELL_SIZE + SEPARATION_GAP;
        int xOffset = (getWidth() - boardWidth) / 2;
        int yOffset = (getHeight() - boardHeight) / 2;

        int mouseX = e.getX() - xOffset;
        int mouseY = e.getY() - yOffset;

        int col = mouseX / CELL_SIZE;
        int newRow, newCol;

        if (mouseY < CELL_SIZE) {
            newRow = -1;
            newCol = col;
        } else {
            mouseY -= (CELL_SIZE + SEPARATION_GAP);
            newRow = mouseY / CELL_SIZE;
            newCol = col;
        }

        if (newCol >= 0 && newCol < COLS &&
                (newRow == -1 || (newRow >= 0 && newRow < ROWS))) {
            if (hoverRow != newRow || hoverCol != newCol) {
                hoverRow = newRow;
                hoverCol = newCol;
                repaint();
            }
        } else {
            if (hoverRow != -1 || hoverCol != -1) {
                hoverRow = -1;
                hoverCol = -1;
                repaint();
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // TODO Auto-generated method stub

    }
}
