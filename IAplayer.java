import java.awt.Color;
import java.util.Random;

public class IAplayer extends Player {

    private Random rand;

    public IAplayer(String playerName, int row, int col, Color color) {
        super(playerName, row, col, color);
        rand = new Random();

    }

    public void IAmove() {
        // Random move within -1, 0, or 1 for both row and column
        int rowOffset = rand.nextInt(3) - 1; // Random move within -1, 0, or 1
        int colOffset = rand.nextInt(3) - 1; // Random move within -1, 0, or 1

        // Ensure the move is within the bounds of the board (0 <= row < 6, 0 <= col <
        // 14)
        int newRow = getRow() + rowOffset;
        int newCol = getCol() + colOffset;

        // Check if the new position is within bounds (no check for occupancy)
        if (newRow >= 0 && newRow < 6 && newCol >= 0 && newCol < 14) {
            setRow(newRow);
            setCol(newCol);
        }
    }
}
