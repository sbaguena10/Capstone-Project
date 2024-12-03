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

    private static final int CELL_SIZE = 50; // Size of each square cell
    private static final int ROWS = 6; // Number of rows in the main board
    private static final int COLS = 14; // Number of columns in both grids
    private static final int SEPARATION_GAP = 20; // Gap between the top grid and main grid

    private Cell[] topGrid; // Array for the top grid with 1 row
    private Cell[][] mainGrid; // 2D array for the main 6x14 grid
    private List<Player> players; // List of players

    private int hoverRow = -1; // Hovered row (-1 indicates the top grid)
    private int hoverCol = -1; // Hovered column
    private Color hoverColor = new Color(255, 255, 0, 100); // Semi-transparent yellow for hover

    // New fields for movement
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

    private HumanPlayer findHumanPlayer() {
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            if (player instanceof HumanPlayer) {
                return (HumanPlayer) player;
            }
        }
        return null;
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

    private void moveAIPlayers() {
        for (Player player : players) {
            if (player instanceof IAplayer) {
                IAplayer aiPlayer = (IAplayer) player;
                aiPlayer.IAmove(); // Trigger AI move
            }
        }
    }

    private void moveHumanPlayer() {
        if (selectedRow != -1 && selectedCol != -1) {
            HumanPlayer humanPlayer = findHumanPlayer();
            if (humanPlayer != null) {
                // Calculate row and column change from current position
                int rowOffset = selectedRow - humanPlayer.getRow();
                int colOffset = selectedCol - humanPlayer.getCol();
                int distance = Math.abs(rowOffset) + Math.abs(colOffset);

                // Validate move is within the 3-square limit and that the target position isn't
                // occupied
                if (distance <= 3 && !isOccupied(selectedRow, selectedCol)) {
                    // Move the human player using the calculated offset
                    humanPlayer.move(rowOffset, colOffset);

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

    private boolean isOccupied(int row, int col) {
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            if (player.getRow() == row && player.getCol() == col) {
                return true; // A player occupies this cell
            }
        }
        return false; // No player occupies this cell
    }

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
            HumanPlayer humanPlayer = findHumanPlayer();
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

    /**
     * Paints the board, including the top grid, main grid, and player positions.
     */
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

        // Draw selected cell highlight
        if (selectedRow != -1 && selectedCol != -1) {
            g.setColor(new Color(0, 255, 0, 100)); // Semi-transparent green
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
            y = yOffset + (CELL_SIZE + SEPARATION_GAP) + hoverRow * CELL_SIZE; // Main grid
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
