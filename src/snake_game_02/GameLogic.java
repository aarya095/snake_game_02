package snake_game_02;

import java.awt.*;
import java.awt.event.KeyEvent;
import javax.swing.*;
import java.util.Random;

public class GameLogic {
	
	private static final int GAME_AREA_START_X = 20;
	private static final int GAME_AREA_START_Y = 40;
	private static final int GAME_AREA_END_X = 320;
	private static final int GAME_AREA_END_Y = 340;
	
    private static final int DOT_SIZE = 10;
    private static final int ALL_DOTS = 900;
    private static final int RANDOM_POSITION = 28;
    private final Random random = new Random();

    private final int[] x = new int[ALL_DOTS];
    private final int[] y = new int[ALL_DOTS];
    private int dots;
    private int appleX;
    private int appleY;
    private int score;
    
    private int SpecialAppleX;
    private int SpecialAppleY;
    private boolean SpecialAppleVisible = false;
    private static final int SPECIAL_APPLE_PROBABILITY = 20;
    private long lastSpecialAppleCheckTime = System.currentTimeMillis();
    private static final int SPECIAL_APPLE_CHECK_DELAY = 5000;  // 1000 ms = 1 second
    private long specialAppleStartTime;
    private int segmentsToAdd = 0; // Tracks how many segments still need to be added

    private Direction currentDirection = Direction.RIGHT;
    private Direction nextDirection = Direction.RIGHT; // Start moving right by default
    private boolean inGame = true;

    private static Image apple;
    private Image dot;
    private Image head;
    private static Image specialApple;
    
    private int frameCounter = 0;

    public void loadImages() {
    	try {
        apple = new ImageIcon(getClass().getResource("/icons/apple.png")).getImage();
        dot = new ImageIcon(getClass().getResource("/icons/dot.png")).getImage();
        head = new ImageIcon(getClass().getResource("/icons/head.png")).getImage();
        specialApple = new ImageIcon(getClass().getResource("/icons/specialApple.png")).getImage();
        
        if (apple == null || dot == null || head == null || specialApple == null) {
            throw new IllegalArgumentException("One or more image resources could not be loaded.");
        }
        
    	} catch (Exception e) {
    		System.out.println("Error loading images: "+e.getMessage());
    		e.printStackTrace();
    	}
    }

    public void initGame() {
        dots = 3;
        for (int i = 0; i < dots; i++) {
            x[i] = 50 - (i * DOT_SIZE);
            y[i] = 50;
        }
        locateApple();
        nextDirection = Direction.RIGHT;
    }

    public void updateGame() {
        if (inGame) {
            // Update the direction before moving
        	currentDirection = nextDirection;
            
        	move();
        	
        	if (segmentsToAdd > 0) {
                addSegment();
                segmentsToAdd--;  // Decrease the counter
            }
        	
            checkApple();
            checkSpecialApple();
            checkCollision();
            
            frameCounter++;

            if (frameCounter % 9 == 0) { // Every 9 frames, which is 1 second at 9 FPS
                handleSpecialApple();
            }
            
            if (SpecialAppleVisible && (System.currentTimeMillis() - lastSpecialAppleCheckTime) > 5000) {
            	SpecialAppleVisible = false;
            }
            
        }
    }


    private void move() {
        for (int i = dots; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        
        switch (currentDirection) {
        case LEFT:
            x[0] -= DOT_SIZE;
            break;
        case RIGHT:
            x[0] += DOT_SIZE;
            break;
        case UP:
            y[0] -= DOT_SIZE;
            break;
        case DOWN:
            y[0] += DOT_SIZE;
            break;
    }
        
    }

    private void checkApple() {
        if (x[0] == appleX && y[0] == appleY) {
            dots++;
            score += 10;
            locateApple();
            }
    }
    
    private void checkSpecialApple() {
        if (x[0] == SpecialAppleX && y[0] == SpecialAppleY) {
            
        	segmentsToAdd += 5; // Gradually add 5 segments
        	score += 20;
            SpecialAppleVisible = false;
}
    }

    
    private void locateApple() {
	    boolean validPosition = false;
	    
	    while (!validPosition) {
	    	appleX = random.nextInt(RANDOM_POSITION) * DOT_SIZE + 20;
	        appleY = random.nextInt(RANDOM_POSITION) * DOT_SIZE + 40;

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
    
    private void locateSpecialApple() {
    	
    	boolean validPosition = false;
 	    while (!validPosition) {
 	    	SpecialAppleX = random.nextInt(RANDOM_POSITION) * DOT_SIZE + 20;
 	    	SpecialAppleY = random.nextInt(RANDOM_POSITION) * DOT_SIZE + 40;
 	        
 	    	// Check if the apple spawns on the snake's body
 	        validPosition = true;
 	        for (int i = 0; i < dots; i++) {
 	            if (x[i] == SpecialAppleX && y[i] == SpecialAppleY) {
 	                validPosition = false; // Found a collision with the snake
 	                break;
 	            }
 	        }
 	        
 	       if (SpecialAppleX == appleX && SpecialAppleY == appleY) {
 	            validPosition = false; // Overlap with regular apple
 	        }
 	    }
    	
 	   lastSpecialAppleCheckTime = System.currentTimeMillis();
 	   specialAppleStartTime = System.currentTimeMillis();
 	   SpecialAppleVisible = true;
    }
    
    private void handleSpecialApple() {
    	
    	long currentTime = System.currentTimeMillis();
    	if (!SpecialAppleVisible && currentTime - lastSpecialAppleCheckTime > SPECIAL_APPLE_CHECK_DELAY) {
    		
    		if (Math.random() < (SPECIAL_APPLE_PROBABILITY/100.0)) {
    			locateSpecialApple();
    			SpecialAppleVisible = true;
    		}
    		lastSpecialAppleCheckTime = currentTime;
    	} 
    }
    
    public void addSegment() {
    	// Place the new segment at the same position as the last segment
        x[dots] = x[dots - 1];
        y[dots] = y[dots - 1];
        dots++; // Increase the total length of the snake
    }
    
    public long getSpecialAppleRemainingTime() {
    	if (SpecialAppleVisible) {
    		long elapsedTime = System.currentTimeMillis() - specialAppleStartTime;
    		long remainingTime = 5000 - elapsedTime; // 5 seconds for the timer
    		return Math.max(remainingTime, 0);  // Return 0 if the time has passed
    	}
    	return 0;
    }

    private void checkCollision() {
        for (int i = dots; i > 0; i--) {
            if (i > 3 && x[0] == x[i] && y[0] == y[i]) {
                inGame = false;
            }
        }
        if (x[0] < GAME_AREA_START_X || x[0] >= GAME_AREA_END_X || y[0] < GAME_AREA_START_Y || y[0] >= GAME_AREA_END_Y) {
            inGame = false;
        }
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode(); // Get the key pressed

        if (inGame) {
           
        	Direction newDirection = Direction.fromKeyCode(key);
        	if (newDirection != null && !newDirection.equals(oppositeDirection(currentDirection))) {
                nextDirection = newDirection;
            }
        }
    }

    private Direction oppositeDirection(Direction direction) {
        switch (direction) {
            case LEFT:
                return Direction.RIGHT;
            case RIGHT:
                return Direction.LEFT;
            case UP:
                return Direction.DOWN;
            case DOWN:
                return Direction.UP;
            default:
                throw new IllegalArgumentException("Unknown direction: " + direction);
        }
    }
    
    public void resetGame() {
        inGame = true;
        score = 0;
        SpecialAppleVisible = false;
        initGame();
        locateApple();
        }
    
    
    public boolean isInGame() {
        return inGame;
    }
    
    public boolean isSpecialAppleVisible() {
    	return SpecialAppleVisible;
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
    
    public int getSpecialAppleX() {
        return SpecialAppleX;
    }

    public int getSpecialAppleY() {
        return SpecialAppleY;
    }

    public Image getApple() {
        return apple;
    }
    
    public Image getSpecialApple() {
        return specialApple;
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
