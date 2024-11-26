import javax.swing.*;

public class Game {

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create the custom board and add it to the frame
        Board customBoard = new Board();
        frame.add(customBoard);

        // Pack the frame to fit the preferred size of the custom board
        frame.pack();
        frame.setSize(800, 400);
        frame.setVisible(true);

        // Request focus to listen for key events
        customBoard.requestFocusInWindow();
    }
}
