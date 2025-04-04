import java.awt.Color;

public class Player {

    private String playerName;
    private int totalHealth;
    private int row;
    private int col;
    private int prevRow;
    private int prevCol;

    private boolean isDead;
    private Color color; // Unique color for the player
    private boolean hasShield;

    // TODO hacer booleano para escudo

    public Player(String playerName, int row, int col, Color color) {
        this.playerName = playerName;
        this.row = row;
        this.col = col;
        this.prevRow = row;
        this.prevCol = col;
        this.totalHealth = 100; // Default health
        this.color = color;
        hasShield = false;
        isDead = false;
    }

    public boolean isHasShield() {
        return hasShield;
    }

    public void setHasShield(boolean hasShield) {
        this.hasShield = hasShield;
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
        return isDead;

    }

    public void setDead(boolean isDead) {
        this.isDead = isDead;
    }

    public int getTotalHealth() {
        return totalHealth;
    }

    public int getPrevRow() {
        return prevRow;
    }

    public void setPrevRow(int prevRow) {
        this.prevRow = prevRow;
    }

    public int getPrevCol() {
        return prevCol;
    }

    public void setPrevCol(int prevCol) {
        this.prevCol = prevCol;
    }

    public void setPosition(int newRow, int newCol) {
        this.prevRow = this.row; // Store old position
        this.prevCol = this.col;
        this.row = newRow; // Update to new position
        this.col = newCol;
    }

    // TODO comprobar si el jugador tiene escudo
    public void takeDamage(int damage) {
        if (!isDead) {
            if (hasShield) {
                System.out.println(playerName + " player has a shield!");
                hasShield = false; // Shield is used
                return; // No damage made
            } else {
                totalHealth -= damage;
            }

            if (totalHealth <= 0) {
                totalHealth = 0;
                isDead = true;
            }
        }
    }

    public void heal(int healingAmount) {
        if (!isDead) {
            totalHealth += healingAmount;
            if (totalHealth > 100) {
                totalHealth = 100;
            }
        }
    }
}
