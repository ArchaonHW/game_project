package game.guessnum;

//猜數字(加介面)


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;



class BackgroundImagePanel extends JPanel {
    /**
	 * 
	 */
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



public class GuessNum extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = -6870725905478403408L;
	private int secretNumber;
    private int attempts;
    private JLabel promptLabel;
    private JTextField guessField;
    private JButton guessButton;
    private JTextArea resultArea;

    public GuessNum() {
        super("Number Guessing Game");
        secretNumber = (int) (Math.random() * 100) + 1;
        attempts = 0;

        promptLabel = new JLabel("Enter your guess (between 1 and 100):");
        promptLabel.setFont(new Font("Arial", Font.PLAIN, 18)); // Increase font size for better readability
        guessField = new JTextField(10);
        guessField.setFont(new Font("Arial", Font.PLAIN, 18)); // Increase font size for better readability
        guessButton = new JButton("Guess");
        guessButton.setFont(new Font("Arial", Font.PLAIN, 18)); // Increase font size for better readability
        resultArea = new JTextArea(10, 30);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Arial", Font.PLAIN, 16)); // Increase font size for better readability

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());
        inputPanel.add(promptLabel);
        inputPanel.add(guessField);
        inputPanel.add(guessButton);
        inputPanel.setOpaque(false);
        
        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BorderLayout());
        resultPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding around result panel
        resultPanel.add(new JScrollPane(resultArea), BorderLayout.CENTER); // Add scroll pane to result area
        resultPanel.setOpaque(false);
        
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(inputPanel, BorderLayout.NORTH);
        getContentPane().add(resultPanel, BorderLayout.CENTER);

        guessButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkGuess();
            }
        });
        
     // 創建背景圖片面板
        BackgroundImagePanel backgroundPanel = new BackgroundImagePanel("/game/guessnum/guess.png");
        backgroundPanel.setLayout(new BorderLayout());
        backgroundPanel.add(inputPanel, BorderLayout.NORTH);
        backgroundPanel.add(resultPanel, BorderLayout.CENTER);

        getContentPane().add(backgroundPanel);   
        
        
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null); // Center the window
        setVisible(true);

        
     
           
        
    }

    private void checkGuess() {
        try {
            int guess = Integer.parseInt(guessField.getText());
            attempts++;

            if (guess < secretNumber) {
                resultArea.append(guess + " is too low! Try again.\n");
            } else if (guess > secretNumber) {
                resultArea.append(guess + " is too high! Try again.\n");
            } else {
                resultArea.append("Congratulations! You've guessed the correct number "
                        + secretNumber + " in " + attempts + " attempts.\n");
                guessButton.setEnabled(false); // Disable the guess button after correct guess
            }

       
            
            
            
            guessField.setText(""); // Clear the text field for next input
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid integer guess.");
            
      
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GuessNum();
            }
        });
    }
}
