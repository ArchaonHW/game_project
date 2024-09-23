package game.dontletballdown;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

class BackgroundImagePanel extends JPanel {
    private static final long serialVersionUID = -5013479064669788468L;
    private BufferedImage backgroundImage;
    private int score;
    private int paddleX;
    private int paddleY;
    private int paddleWidth;
    private int paddleHeight;
    private int ballX;
    private int ballY;
    private int ballDiameter;

    public BackgroundImagePanel(String imagePath, int paddleX, int paddleY, int paddleWidth, int paddleHeight, int ballX, int ballY, int ballDiameter, int score) {
        this.paddleX = paddleX;
        this.paddleY = paddleY;
        this.paddleWidth = paddleWidth;
        this.paddleHeight = paddleHeight;
        this.ballX = ballX;
        this.ballY = ballY;
        this.ballDiameter = ballDiameter;
        this.score = score;
        
        try {
        	backgroundImage = ImageIO.read(getClass().getResource(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }

        // 繪製分數
        g.setColor(Color.GRAY);
        g.setFont(new Font("Verdana", Font.BOLD, 30));
        g.drawString("Score: " + score, 20, 30);

        // 繪製擋板
        g.setColor(Color.BLUE);
        g.fillRect(paddleX, paddleY, paddleWidth, paddleHeight);

        // 繪製球
        g.setColor(Color.RED);
        g.fillOval(ballX, ballY, ballDiameter, ballDiameter);
    }

    public void updatePaddlePosition(int paddleX) {
        this.paddleX = paddleX;
        repaint();  // 添加重繪請求
    }

    public void updateBallPosition(int ballX, int ballY) {
        this.ballX = ballX;
        this.ballY = ballY;
        repaint();  // 添加重繪請求
    }

    public void updateScore(int score) {
        this.score = score;
        repaint();  // 添加重繪請求
    }
}

public class DontLetBallDown extends JFrame implements KeyListener {
    private static final long serialVersionUID = 7721407381483657820L;
    private int score;
    private int paddleX;
    private final int paddleY = 600;
    private final int paddleWidth = 100;
    private final int paddleHeight = 20;
    private final int paddleSpeed = 10;

    private int ballX;
    private int ballY;
    private final int ballDiameter = 20;
    private int ballSpeedX = 2;
    private int ballSpeedY = 2;

    private Timer timer;
    private BackgroundImagePanel backgroundPanel;

    public DontLetBallDown() {
        super("不讓球球掉下來!!");
        setSize(700, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        score = 0;
        paddleX = getWidth() / 2 - paddleWidth / 2;
        ballX = getWidth() / 2 - ballDiameter / 2;
        ballY = 50;

        addKeyListener(this);
        setFocusable(true);

        timer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateGame();
            }
        });
        timer.start();

        backgroundPanel = new BackgroundImagePanel("/game/dontletballdown/ball.jpg", paddleX, paddleY, paddleWidth, paddleHeight, ballX, ballY, ballDiameter, score);
        backgroundPanel.setLayout(null);
        add(backgroundPanel);

        setVisible(true);
    }

    private void updateGame() {
        ballY += ballSpeedY;
        ballX += ballSpeedX;

        if (ballY + ballDiameter >= paddleY && ballY + ballDiameter <= paddleY + paddleHeight
                && ballX + ballDiameter >= paddleX && ballX <= paddleX + paddleWidth) {
            score++;
            ballSpeedY = -ballSpeedY;
            int paddleCenterX = paddleX + paddleWidth / 2;
            int ballCenterX = ballX + ballDiameter / 2;
            int speedChange = (ballCenterX - paddleCenterX) / 5;
            ballSpeedX = ballSpeedX + speedChange;
        }

        if (ballX <= 0 || ballX + ballDiameter >= getWidth()) {
            ballSpeedX = -ballSpeedX;
        }

        if (ballY <= 0) {
            ballSpeedY = -ballSpeedY;
        }

        if (ballY > getHeight()) {
            gameOver();
        }

        backgroundPanel.updateBallPosition(ballX, ballY);
        backgroundPanel.updateScore(score);
    }

    private void gameOver() {
        timer.stop();
        JOptionPane.showMessageDialog(this, "Game Over! Score: " + score);        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            movePaddleLeft();
        } else if (key == KeyEvent.VK_RIGHT) {
            movePaddleRight();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    private void movePaddleLeft() {
        if (paddleX > 0) {
            paddleX -= paddleSpeed;
            backgroundPanel.updatePaddlePosition(paddleX);
        }
    }

    private void movePaddleRight() {
        if (paddleX + paddleWidth < getWidth()) {
            paddleX += paddleSpeed;
            backgroundPanel.updatePaddlePosition(paddleX);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new DontLetBallDown();
            }
        });
    }
}
