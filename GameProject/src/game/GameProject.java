package game;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

import game.battle.ProgramWindow;
import game.dontletballdown.DontLetBallDown;
import game.guessnum.GuessNum;
import game.guessnumtwo.GuessNumber_report;
import game.plane.World;
import game.snake.SnakeGame;
import game.tictactoe.TicTacToe;

public class GameProject extends JFrame {

    private static final long serialVersionUID = 5000916123324386074L;

    public GameProject() {
        super("遊戲主選單");

        // 設定視窗大小
        setSize(800, 600);

        // 居中視窗
        setLocationRelativeTo(null);

        // 設定默認關閉操作
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 創建背景圖片面板
        BackgroundImagePanel backgroundPanel = new BackgroundImagePanel("/game/bg.jpg");
        backgroundPanel.setLayout(null); // 設定 layout 為 null 以使用絕對定位

        
       
        // 創建組件
        JLabel titleLabel = new JLabel("歡迎來到小遊戲集");
        titleLabel.setBounds(172, 0, 384, 33);
        titleLabel.setFont(new Font("微軟雅黑", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(Color.BLUE);

        JButton startButton = new JButton("自走貪食蛇");
        startButton.setBounds(402, 212, 135, 42);
        startButton.addActionListener((e) -> {
            JOptionPane.showMessageDialog(GameProject.this, "遊戲開始！");
            
            JFrame frame = new SnakeGame();
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setVisible(true);
        });

        JButton exitButton = new JButton("退出");
        exitButton.setBounds(694, 489, 80, 29);
        exitButton.addActionListener((e) -> {
            System.exit(0);           
        });

        // 添加組件到背景圖片面板
        backgroundPanel.add(titleLabel);

        // 創建另一個背景圖片面板作為按鈕面板
        BackgroundImagePanel buttonPanel = new BackgroundImagePanel("/game/bg.jpg");
        buttonPanel.setBounds(0, 33, 784, 528);
        buttonPanel.setLayout(null);
        buttonPanel.add(startButton);
        buttonPanel.add(exitButton);
        
        JButton btnNewButton = new JButton("飛機大戰");
        btnNewButton.addActionListener((e) -> {
            JOptionPane.showMessageDialog(GameProject.this, "遊戲開始！");
            
            JFrame frame = new JFrame();
            World world = new World();
            frame.getContentPane().add(world);
            frame.setSize(World.WIDTH, World.HEIGHT);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setVisible(true);
            world.action();
        });
        btnNewButton.setBounds(223, 212, 135, 42);
        buttonPanel.add(btnNewButton);
        
        JButton btnNewButton_1 = new JButton("井字遊戲");
        btnNewButton_1.addActionListener((e) -> {
            JOptionPane.showMessageDialog(GameProject.this, "遊戲開始！");
            
            JFrame frame = new TicTacToe();
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setVisible(true);
        });
        btnNewButton_1.setBounds(49, 212, 135, 42);
        buttonPanel.add(btnNewButton_1);
        
        JButton btnNewButton_2 = new JButton("不讓球球掉下來");
        btnNewButton_2.addActionListener((e) -> {
            JOptionPane.showMessageDialog(GameProject.this, "遊戲開始！");
            
            JFrame frame = new DontLetBallDown();         
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setVisible(true);
        });
        btnNewButton_2.setBounds(596, 212, 135, 42);
        buttonPanel.add(btnNewButton_2);
        
        JButton btnNewButton_3 = new JButton("猜數字");
        btnNewButton_3.addActionListener((e) -> {
            JOptionPane.showMessageDialog(GameProject.this, "遊戲開始！");
            
            JFrame frame = new GuessNum();
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setVisible(true);
        });
        btnNewButton_3.setBounds(49, 459, 135, 42);
        buttonPanel.add(btnNewButton_3);

        
        
        JButton btnNewButton_4 = new JButton("終極密碼");
        btnNewButton_4.addActionListener((e)-> {
        	JOptionPane.showMessageDialog(GameProject.this, "遊戲開始！");
        	
        	JFrame frame = new GuessNumber_report();
        	frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setVisible(true);
        });
        btnNewButton_4.setBounds(223, 459, 135, 42);
        buttonPanel.add(btnNewButton_4);
        
        backgroundPanel.add(buttonPanel);
        
        BackgroundImagePanel tictactoe = new BackgroundImagePanel("/game/tictactoe.png");
        tictactoe.setBounds(25, 21, 172, 183);
        buttonPanel.add(tictactoe);
        
        BackgroundImagePanel plane = new BackgroundImagePanel("/game/plane.png");
        plane.setBounds(207, 21, 172, 183);
        buttonPanel.add(plane);
        
        BackgroundImagePanel snake = new BackgroundImagePanel("/game/snake.png");
        snake.setBounds(389, 21, 172, 183);
        buttonPanel.add(snake);
        
        BackgroundImagePanel dontletballdown = new BackgroundImagePanel("/game/dontletballdown.png");
        dontletballdown.setBounds(589, 21, 172, 183);
        buttonPanel.add(dontletballdown);
        
        BackgroundImagePanel guess = new BackgroundImagePanel("/game/guess.png");
        guess.setBounds(25, 264, 172, 183);
        buttonPanel.add(guess);
        
        BackgroundImagePanel Guess_Number_woBoom = new BackgroundImagePanel("/game/Guess_Number_woBoom.png");
        Guess_Number_woBoom.setBounds(207, 266, 172, 183);
        buttonPanel.add(Guess_Number_woBoom);
        
        BackgroundImagePanel battle = new BackgroundImagePanel("/game/battle.png");
        battle.setBounds(389, 264, 172, 183);
        buttonPanel.add(battle);
        
        JButton btnNewButton_5 = new JButton("對戰小遊戲");
        btnNewButton_5.addActionListener((e) -> {
        	
        		JOptionPane.showMessageDialog(GameProject.this, "遊戲開始！");
            	
            	Frame frame = new ProgramWindow();
            	
            	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            	
                frame.setVisible(true);
        		
        });
        btnNewButton_5.setBounds(424, 459, 126, 42);
        buttonPanel.add(btnNewButton_5);
        
        getContentPane().add(backgroundPanel);
        
    }

    public static void main(String[] args) {
        GameProject mainMenu = new GameProject();
        mainMenu.setVisible(true);
    }
}

class BackgroundImagePanel extends JPanel {
    private static final long serialVersionUID = -5013479064669788468L;
    private BufferedImage backgroundImage;

    public BackgroundImagePanel(String imagePath) {
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
    }
}
