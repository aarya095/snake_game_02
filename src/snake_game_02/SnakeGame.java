package snake_game_02;

import javax.swing.*;

public class SnakeGame extends JFrame {

    public SnakeGame() {
        super("Snake Game");
        initialize();
    }

    private void initialize() {
        // Set up game logic and controller
        GameController gameController = new GameController();

        // Add the game board to the frame
        add(new GameBoard(gameController));
        pack(); // pack() method automatically sizes the JFrame based on the preferred sizes of its child components.

        // Set the application icon
        try {
            ImageIcon icon = new ImageIcon(SnakeGame.class.getResource("/icons/snake1.jpg"));
            setIconImage(icon.getImage());
        } catch (Exception e) {
            System.err.println("Error: Icon image not found!");
        }

        // Set JFrame properties
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window on the screen
        setResizable(false);         // Disable resizing to maintain game integrity
        setVisible(true);            // Make the window visible
    }

    public static void main(String[] args) {
        // Run the game
        SwingUtilities.invokeLater(SnakeGame::new); //creates an instance of JFrame
        /*
         * runs the SnakeGame constructor (new SnakeGame()) 
         * on the Event Dispatch Thread (EDT), 
         * which is the proper thread for Swing GUI updates.
         * */
    }
}
