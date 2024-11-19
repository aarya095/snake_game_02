package snake_game_02;

import java.awt.*;
import java.awt.event.KeyEvent;
import javax.swing.*;

public class GameLogic {
    private static final int DOT_SIZE = 10;
    private static final int ALL_DOTS = 900;
    private static final int RANDOM_POSITION = 28;

    private final int[] x = new int[ALL_DOTS];
    private final int[] y = new int[ALL_DOTS];
    private int dots;
    private int appleX;
    private int appleY;
    private int score;

    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private int nextDirection = KeyEvent.VK_RIGHT; // Start moving right by default
    private boolean inGame = true;

    private Image apple;
    private Image dot;
    private Image head;

    public void loadImages() {
        apple = new ImageIcon(getClass().getResource("/icons/apple.png")).getImage();
        dot = new ImageIcon(getClass().getResource("/icons/dot.png")).getImage();
        head = new ImageIcon(getClass().getResource("/icons/head.png")).getImage();
    }

    public void initGame() {
        dots = 3;
        for (int i = 0; i < dots; i++) {
            x[i] = 50 - (i * DOT_SIZE);
            y[i] = 50;
        }
        locateApple();
        nextDirection = KeyEvent.VK_RIGHT;
    }

    public void updateGame() {
        if (inGame) {
            // Update the direction before moving
            switch (nextDirection) {
                case KeyEvent.VK_LEFT:
                    leftDirection = true;
                    rightDirection = false;
                    upDirection = false;
                    downDirection = false;
                    break;
                case KeyEvent.VK_RIGHT:
                    rightDirection = true;
                    leftDirection = false;
                    upDirection = false;
                    downDirection = false;
                    break;
                case KeyEvent.VK_UP:
                    upDirection = true;
                    downDirection = false;
                    leftDirection = false;
                    rightDirection = false;
                    break;
                case KeyEvent.VK_DOWN:
                    downDirection = true;
                    upDirection = false;
                    leftDirection = false;
                    rightDirection = false;
                    break;
            }

            move();
            checkApple();
            checkCollision();
        }
    }


    private void move() {
        for (int i = dots; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        if (leftDirection) x[0] -= DOT_SIZE;
        if (rightDirection) x[0] += DOT_SIZE;
        if (upDirection) y[0] -= DOT_SIZE;
        if (downDirection) y[0] += DOT_SIZE;
    }

    private void checkApple() {
        if (x[0] == appleX && y[0] == appleY) {
            dots++;
            score += 10;
            locateApple();
        }
    }

    public void locateApple() {
	    boolean validPosition = false;
	    
	    while (!validPosition) {
	        int r = (int)(Math.random() * RANDOM_POSITION);
	        appleX = r * DOT_SIZE + 20; // Offset for left border

	        r = (int)(Math.random() * RANDOM_POSITION);
	        appleY = r * DOT_SIZE + 40; // Offset for top border

	        // Check if the apple spawns on the snake's body
	        validPosition = true;
	        for (int i = 0; i < dots; i++) {
	            if (x[i] == appleX && y[i] == appleY) {
	                validPosition = false; // Found a collision with the snake
	                break;
	            }
	        }
	    }
	    
	    }

    private void checkCollision() {
        for (int i = dots; i > 0; i--) {
            if (i > 3 && x[0] == x[i] && y[0] == y[i]) {
                inGame = false;
            }
        }
        if (x[0] < 20 || x[0] >= 320 || y[0] < 40 || y[0] >= 340) {
            inGame = false;
        }
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode(); // Get the key pressed

        if (inGame) {
            // Update the direction based on key press
            if (key == KeyEvent.VK_LEFT && !rightDirection) {
                nextDirection = KeyEvent.VK_LEFT;
            } else if (key == KeyEvent.VK_RIGHT && !leftDirection) {
                nextDirection = KeyEvent.VK_RIGHT;
            } else if (key == KeyEvent.VK_UP && !downDirection) {
                nextDirection = KeyEvent.VK_UP;
            } else if (key == KeyEvent.VK_DOWN && !upDirection) {
                nextDirection = KeyEvent.VK_DOWN;
            }
        }
    }

    public void resetGame() {
        inGame = true;
        score = 0;
        initGame();
    }

    public boolean isInGame() {
        return inGame;
    }

    public int[] getX() {
        return x;
    }

    public int[] getY() {
        return y;
    }

    public int getDots() {
        return dots;
    }

    public int getAppleX() {
        return appleX;
    }

    public int getAppleY() {
        return appleY;
    }

    public Image getApple() {
        return apple;
    }

    public Image getDot() {
        return dot;
    }

    public Image getHead() {
        return head;
    }

    public int getScore() {
        return score;
    }
}
