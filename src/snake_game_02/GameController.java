package snake_game_02;

import java.awt.Image;
import java.awt.event.KeyEvent;

public class GameController {
    private final GameLogic logic;

    public GameController() {
        logic = new GameLogic();
        logic.loadImages();
        logic.initGame();
    }

    public void updateGame() {
        logic.updateGame();
    }

    public void restartGame() {
        logic.resetGame();
    }

    public boolean isInGame() {
        return logic.isInGame();
    }

    public void handleKeyPress(KeyEvent e) {
        logic.keyPressed(e);
    }

    public int[] getSnakeX() {
        return logic.getX();
    }

    public int[] getSnakeY() {
        return logic.getY();
    }

    public int getSnakeLength() {
        return logic.getDots();
    }

    public int getAppleX() {
        return logic.getAppleX();
    }

    public int getAppleY() {
        return logic.getAppleY();
    }
    
    public int getSpecialAppleX() {
        return logic.getSpecialAppleX();
    }

    public int getSpecialAppleY() {
        return logic.getSpecialAppleY();
    }
    
    public Image getSpecialAppleImage() {
        return logic.getSpecialApple();
    }

    public Image getAppleImage() {
        return logic.getApple();
    }

    public Image getSnakeHeadImage() {
        return logic.getHead();
    }

    public Image getSnakeBodyImage() {
        return logic.getDot();
    }

    public int getScore() {
        return logic.getScore();
    }
}
