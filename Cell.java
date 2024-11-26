import java.awt.Color;
import java.util.Random;

public class Cell {
    private Color color; // Color of the cell determines the superpower
    private boolean hasSuperpower; // Whether the cell has a superpower
    private static final Random rand = new Random();

    public Cell() {
        double randomValue = rand.nextDouble(); // Generate a random number between 0 and 1

        // Use `if` statements to decide the cell's properties
        if (randomValue > 0.7) { // 30% chance of assigning a superpower
            int superpowerIndex = rand.nextInt(5); // Randomly pick one of the superpowers
            if (superpowerIndex == 0) {
                color = Color.RED;
            } else if (superpowerIndex == 1) {
                color = Color.BLUE;
            } else if (superpowerIndex == 2) {
                color = Color.GREEN;
            } else if (superpowerIndex == 3) {
                color = Color.ORANGE;
            } else if (superpowerIndex == 4) {
                color = Color.PINK;
            }
            hasSuperpower = true;
        } else {
            color = Color.GRAY;
            hasSuperpower = false;
        }
    }

    public Color getColor() {
        return color;
    }

    public boolean hasSuperpower() {
        return hasSuperpower;
    }
}
