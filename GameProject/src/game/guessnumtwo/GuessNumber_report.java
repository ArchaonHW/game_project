package game.guessnumtwo;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import java.awt.*;

class BackgroundImagePanel extends JPanel {
    private static final long serialVersionUID = -5013479064669788468L;
    private BufferedImage backgroundImage;

    // 使用圖片路徑作為參數的構造函數
    public BackgroundImagePanel(String imagePath) {
        try {
            // 加載背景圖片
            backgroundImage = ImageIO.read(getClass().getResource(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            // 繪制背景圖片
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}

public class GuessNumber_report extends JFrame {
    private static final long serialVersionUID = 1L;
    static int min_val = 1;
    static int max_val = 100;
    static int random_Num;

    public GuessNumber_report() {
        // 隨機生成亂數
        random_Num = generateRandomNumber();

        // 設置 JFrame 標題和初始尺寸
        setTitle("終極密碼 遊戲");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 創建背景圖片面板
        BackgroundImagePanel backgroundPanel = new BackgroundImagePanel("/game/guessnumtwo/Guess_Number_woBoom.png");
        backgroundPanel.setLayout(null); // 設定 layout 為 null 以使用絕對定位

        // 建立 JTextField
        JTextField guessField = new JTextField();
        guessField.setBounds(100, 145, 210, 30);
        backgroundPanel.add(guessField);

        // 建立 JLabel
//        JLabel l1 = new JLabel("歡迎進入 終極密碼 遊戲!!");
//        l1.setBounds(80, 50, 400, 30);
//        l1.setFont(new Font("Serif", Font.PLAIN, 20));
//        l1.setForeground(Color.RED);
//        backgroundPanel.add(l1);

        JLabel l2 = new JLabel("請輸入2位數值/整數, 範圍 1~100");
        l2.setBackground(new Color(255, 255, 255));
        l2.setBounds(85, 118, 216, 20);
        l2.setFont(new Font("Serif", Font.PLAIN, 15));
        l2.setOpaque(true);
        l2.setBackground(Color.YELLOW);
        l2.setForeground(Color.BLUE);
        backgroundPanel.add(l2);

        // 建立 JButton
        JButton b1 = new JButton("再玩一次");
        b1.setBounds(100, 200, 100, 30);
        backgroundPanel.add(b1);

        JButton b2 = new JButton("提交");
        b2.setBounds(210, 200, 100, 30);
        backgroundPanel.add(b2);

        // 添加背景面板到 JFrame
        setContentPane(backgroundPanel);

        // 按鈕監聽
        b2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // 从 JTextField 輸入值轉為整數
                    int guess_Num = Integer.parseInt(guessField.getText());

                    // 判斷式
                    if (guess_Num == random_Num) {
                        JOptionPane.showMessageDialog(GuessNumber_report.this, "恭喜你, 猜中了!");
                    } else if (guess_Num < min_val) {
                        JOptionPane.showMessageDialog(GuessNumber_report.this, "不可小於等於 " + min_val);
                    } else if (guess_Num > max_val) {
                        JOptionPane.showMessageDialog(GuessNumber_report.this, "不可大於等於 " + max_val);
                    } else if (guess_Num > random_Num) {
                        max_val = guess_Num;
                        JOptionPane.showMessageDialog(GuessNumber_report.this, "範圍縮小為 " + min_val + "~" + max_val);
                    } else if (guess_Num < random_Num) {
                        min_val = guess_Num;
                        JOptionPane.showMessageDialog(GuessNumber_report.this, "範圍縮小為 " + min_val + "~" + max_val);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(GuessNumber_report.this, "請輸入有效的數值");
                }
            }
        });

        // 顯示 JFrame
        setVisible(true);
    }

    // 隨機生成亂數
    private static int generateRandomNumber() {
        Random rand = new Random();
        return rand.nextInt(max_val - min_val + 1) + min_val;
    }

    public static void main(String[] args) {
        new GuessNumber_report(); // 建立 GuessNumber_report 實例，初始化遊戲
    }
}
