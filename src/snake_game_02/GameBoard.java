package snake_game_02;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GameBoard extends JPanel implements ActionListener {
    
	private final GameController controller;
    private final Timer timer;
    private final JButton restartButton;
    
    private static final int TIMER_BAR_WIDTH = 200;  // Width of the timer bar
    private static final int TIMER_BAR_HEIGHT = 10;  // Height of the timer bar
    private static final int TIMER_X = 20; // Starting X position of the timer bar
    private static final int TIMER_Y = 20; // Starting Y position of the timer bar
   
    public GameBoard(GameController controller) {
        this.controller = controller;

        setBackground(Color.BLUE);
        setPreferredSize(new Dimension(340, 360));
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
            	int key = e.getKeyCode();
            	if (key == KeyEvent.VK_P || key == KeyEvent.VK_ESCAPE) {
            		togglePause();
            	} else {
                controller.handleKeyPress(e);
            	}
            }
        });

        // Initialize restart button
        restartButton = new JButton("Restart");
        restartButton.setBounds(120, 200, 100, 30);
        restartButton.setFocusable(false);
        restartButton.addActionListener(e -> restartGame());

        timer = new Timer(120, this);
        timer.start();
    }

    private void togglePause() {
    	controller.togglePause();
    	if(controller.isPaused()) {
    		timer.stop();
    	}	else {
    		timer.start();
    	}
    	repaint();
    }
    
    private void restartGame() {
        controller.restartGame();
        if (restartButton.getParent() != null) {
            remove(restartButton);
        }
        if(controller.isPaused()) {
        	controller.togglePause();
        }
        timer.start();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw game boundaries
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, 340, 360);
        
        g.setColor(Color.BLACK);
        g.fillRect(20, 40, 300, 300);
        
        if(controller.isPaused()) {
        	drawPaused(g);
        }
        // Render game objects
        if (controller.isInGame()) {
            drawGame(g);
            
            if (controller.isSpecialAppleVisible()) {
            	drawTimerBar(g);
            }
        } else {
            drawGameOver(g);
        }
    }
    
    private void drawPaused(Graphics g) {
        String msg = "Game Paused";
        Font font = new Font("SAN SERIF", Font.BOLD, 14);
        FontMetrics metrics = getFontMetrics(font);

        g.setColor(Color.WHITE);
        g.setFont(font);
        g.drawString(msg, (getWidth() - metrics.stringWidth(msg)) / 2, getHeight() / 2);
    }

    private void drawGame(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawRect(20, 40, 300, 300);

        // Draw apple
        g.drawImage(controller.getAppleImage(), controller.getAppleX(), controller.getAppleY(), this);
        // Draw Special apple
        if(controller.isSpecialAppleVisible()) {
        	g.drawImage(controller.getSpecialAppleImage(), controller.getSpecialAppleX(), controller.getSpecialAppleY(), this);
    }
        // Draw snake
        int[] x = controller.getSnakeX();
        int[] y = controller.getSnakeY();
        
        for (int i = 0; i < controller.getSnakeLength(); i++) {
        	
        	 if (x[i] >= 20 && x[i] < 320 && y[i] >= 40 && y[i] < 340) {
            if (i == 0) {
                g.drawImage(controller.getSnakeHeadImage(), x[i], y[i], this);
            } else {
                g.drawImage(controller.getSnakeBodyImage(), x[i], y[i], this);
            }
        }
        }
        // Display score
        g.setColor(Color.WHITE);
        g.setFont(new Font("SAN SERIF", Font.BOLD, 14));
        g.drawString("Score: " + controller.getScore(), getWidth() - 100, 25);
    }

    private void drawGameOver(Graphics g) {
        String msg = "Game Over";
        Font font = new Font("SAN SERIF", Font.BOLD, 14);
        FontMetrics metrics = getFontMetrics(font);

        g.setColor(Color.WHITE);
        g.setFont(font);
        g.drawString(msg, (getWidth() - metrics.stringWidth(msg)) / 2, getHeight() / 2);
        g.drawString("Score: " + controller.getScore(), getWidth() - 100, 25);

        if (restartButton.getParent() == null) {
            add(restartButton);
        }
    }
    
    private void drawTimerBar (Graphics g) {
    	
    	long remainingTime = Math.max(0, Math.min(controller.getSpecialAppleRemainingTime(), 5000));
    	
    	
    	int progressWidth = (int)((remainingTime/5000.0) * TIMER_BAR_WIDTH);
    	
    	g.setColor(Color.gray);
    	g.fillRect(TIMER_X, TIMER_Y, TIMER_BAR_WIDTH, TIMER_BAR_HEIGHT);
    	
    	if(progressWidth > 0) {
    	g.setColor(Color.GREEN);
    	g.fillRect(TIMER_X, TIMER_Y, progressWidth, TIMER_BAR_HEIGHT);
    	}
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (controller.isInGame() && !controller.isPaused()) {
            controller.updateGame();
            repaint();
        }
    }
}
