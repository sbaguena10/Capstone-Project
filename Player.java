import java.awt.Color;

public class Player {

    private String playerName;
    private int totalHealth;
    private int row;
    private int col;
    private boolean isDead;
    private int[][] intialPos;
    private Color color; // color for the player

    public Player(String playerName, int row, int col, Color color) {
        this.playerName = playerName;
        this.row = row;
        this.col = col;
        this.totalHealth = 100; // Default health
        this.color = color;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isDead() {
        return totalHealth <= 0;
    }

    public void setDead(boolean isDead) {
        this.isDead = isDead;
    }

    public int getTotalHealth() {
        return totalHealth;
    }

    public void takeDamage(int damage) {
        totalHealth -= damage;
        if (totalHealth < 0) {
            totalHealth = 0;
        }
    }

    public void heal(int healingAmount) {
        totalHealth += healingAmount;
        if (totalHealth > 100) {
            totalHealth = 100;
        }
    }

}
