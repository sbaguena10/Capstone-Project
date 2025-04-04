import java.awt.Color;
import java.util.Random;

public class Cell {
    private Color color; // Color of the cell determines the superpower
    private boolean hasSuperpower; // Whether the cell has a superpower
    private int damage; // value given to each superpower
    private static final Random rand = new Random();
    private static final Color[] superpowers = { Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE, Color.PINK };
    private static final int[] DamageOfSuperpowers = { 25, 50, 0, 35, 0 };
    private int cellType;

    public Cell() {
        double randomValue = rand.nextDouble(); // Generate a random number between 0 and 1

        if (randomValue > 0.7) { // 30% chance of having superpower
            int superpowerIndex = rand.nextInt(superpowers.length); // Randomly pick one of the superpowers
            color = superpowers[superpowerIndex];
            damage = DamageOfSuperpowers[superpowerIndex]; // Assign corresponding damage value
            hasSuperpower = true;
            cellType = superpowerIndex;
        } else {
            color = Color.GRAY; // Normal cell color
            damage = 0; // Normal cells have no damage
            hasSuperpower = false;
            cellType = -1;
        }
    }

    /**
     * getter to get the color of the cell
     * 
     * @return
     */
    public Color getColor() {
        return color;
    }

    /**
     * bolean that checks if the cell has a superpower or not
     * 
     * @return
     */
    public boolean hasSuperpower() {
        return hasSuperpower;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getCellType() {
        return cellType;
    }

}
