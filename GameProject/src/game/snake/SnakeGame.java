package game.snake;
import javax.swing.JFrame;

// SnakeGame 類繼承 JFrame 創建游戲視窗
public class SnakeGame extends JFrame {

    private static final long serialVersionUID = -3709868461426915784L;

    // 構造函數初始化游戲視窗
    public SnakeGame() {
        // 添加游戲面板到視窗
        add(new GamePanel());
        // 設定視窗不可調整大小
        setResizable(false);
        // 調整視窗大小以適應其內容
        pack();
        // 設定視窗標題
        setTitle("自走貪食蛇");
        // 將視窗居中顯示
        setLocationRelativeTo(null);
        // 設定關閉視窗時退出程式
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // 主方法運行游戲
    public static void main(String[] args) {
        // 創建一個新的游戲視窗
        JFrame frame = new SnakeGame();
        // 使游戲視窗可見
        frame.setVisible(true);
    }
}