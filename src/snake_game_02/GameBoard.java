package snake_game_02;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GameBoard extends JPanel implements ActionListener {
    private final GameController controller;
    private final Timer timer;
    private final JButton restartButton;

    public GameBoard(GameController controller) {
        this.controller = controller;

        setBackground(Color.BLUE);
        setPreferredSize(new Dimension(340, 360));
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                controller.handleKeyPress(e);
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

    private void restartGame() {
        controller.restartGame();
        if (restartButton.getParent() != null) {
            remove(restartButton);
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

        // Render game objects
        if (controller.isInGame()) {
            drawGame(g);
        } else {
            drawGameOver(g);
        }
    }

    private void drawGame(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawRect(20, 40, 300, 300);

        // Draw apple
        g.drawImage(controller.getAppleImage(), controller.getAppleX(), controller.getAppleY(), this);

        // Draw snake
        int[] x = controller.getSnakeX();
        int[] y = controller.getSnakeY();
        for (int i = 0; i < controller.getSnakeLength(); i++) {
            if (i == 0) {
                g.drawImage(controller.getSnakeHeadImage(), x[i], y[i], this);
            } else {
                g.drawImage(controller.getSnakeBodyImage(), x[i], y[i], this);
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

        if (restartButton.getParent() == null) {
            add(restartButton);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (controller.isInGame()) {
            controller.updateGame();
            repaint();
        }
    }
}
