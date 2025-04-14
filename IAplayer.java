import java.awt.Color;
import java.util.Random;

public class IAplayer extends Player {

    private Random rand;

    public IAplayer(String playerName, int row, int col, Color color) {
        super(playerName, row, col, color);
        rand = new Random();

    }

    public void IAmove() {
        int rowOffset = 0;
        int colOffset = 0;

        // Player momves horizontally or vertically
        if (rand.nextBoolean()) {
            rowOffset = rand.nextInt(7) - 3; // Move between -3 and 3 rows
        } else {
            colOffset = rand.nextInt(7) - 3; // Move between -3 and 3 columns
        }

        int newRow = getRow() + rowOffset;
        int newCol = getCol() + colOffset;

        // Ensure the move is within the bounds of the board
        if (newRow >= 0 && newRow < 6 && newCol >= 0 && newCol < 14) {
            setRow(newRow);
            setCol(newCol);
        }
    }
}
