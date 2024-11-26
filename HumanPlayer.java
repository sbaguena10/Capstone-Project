import java.awt.Color;

public class HumanPlayer extends Player {

    public HumanPlayer(String playerName, int row, int col, Color color) {
        super(playerName, row, col, color);
        // TODO Auto-generated constructor stub
    }

    public void move(int rowOffset, int colOffset) {
        int distance = Math.abs(rowOffset) + Math.abs(colOffset);
        if (distance <= 3) {
            setRow(getRow() + rowOffset);
            setCol(getCol() + colOffset);
        } else {
            System.out.println("Move too far. Max range is 3 cells.");
        }
    }
}
