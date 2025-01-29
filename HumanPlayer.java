import java.awt.Color;

public class HumanPlayer extends Player {

    public HumanPlayer(String playerName, int row, int col, Color color) {
        super(playerName, row, col, color);
        // TODO Auto-generated constructor stub
    }

    public void Humanmove(int rowChange, int colChange) {
        // Validate move range and direction
        if ((Math.abs(rowChange) <= 3 && colChange == 0) ||
                (Math.abs(colChange) <= 3 && rowChange == 0)) {
            setRow(getRow() + rowChange); // Update row position
            setCol(getCol() + colChange); // Update column position
        } else {
            System.out.println("Movement not allowed");
        }
    }
}
