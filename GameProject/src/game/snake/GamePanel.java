package game.snake;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class GamePanel extends JPanel implements ActionListener {

    private static final long serialVersionUID = 113087437361675636L;
    private final int B_WIDTH = 300;   // 游戲面板寬度
    private final int B_HEIGHT = 300;  // 游戲面板高度
    private final int DOT_SIZE = 10;   // 每個點的大小（蛇的每一段）
    private final int ALL_DOTS = 900;  // 游戲面板上可能的最大點數
    private final int RAND_POS = 29;   // 隨機放置蘋果的位置因數
    private final int DELAY = 140;     // 計時器延遲（更新頻率）：毫秒

    private final int x[] = new int[ALL_DOTS];  // 存儲蛇身所有點的x坐標的數組
    private final int y[] = new int[ALL_DOTS];  // 存儲蛇身所有點的y坐標的數組

    private int dots;          // 蛇身段數
    private int apple_x;       // 蘋果的x坐標
    private int apple_y;       // 蘋果的y坐標

    private boolean inGame = true;        // 游戲進行中標志
    private boolean isPlayerControl = true; // 玩家控制標志
    private boolean isStarted = false; // 游戲是否已開始標志
    private int direction = KeyEvent.VK_RIGHT; // 蛇的初始移動方向

    private Timer timer;        // 游戲更新計時器
    private Image ball;         // 蛇身圖像
    private Image apple;        // 蘋果圖像
    private Image head;         // 蛇頭圖像

    private boolean isAIControl = false; // AI控制標志

    // 構造方法，初始化游戲面板
    public GamePanel() {
        initBoard();
    }

    // 初始化游戲面板
    private void initBoard() {
        setBackground(Color.DARK_GRAY); // 設定背景顏色
        setFocusable(true); // 設定焦點可獲取
        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT)); // 設定面板首選大小
        loadImages(); // 加載游戲圖像資源
        initGame(); // 初始化游戲狀態
        addKeyListener(new TAdapter()); // 添加鍵盤事件監聽器
    }

    // 加載游戲所需的圖像資源
    private void loadImages() {
        ball = Toolkit.getDefaultToolkit().getImage(getClass().getResource("dot.png")); // 加載蛇身圖像
        apple = Toolkit.getDefaultToolkit().getImage(getClass().getResource("apple.png")); // 加載蘋果圖像
        head = Toolkit.getDefaultToolkit().getImage(getClass().getResource("head.png")); // 加載蛇頭圖像
    }

    // 初始化游戲狀態
    private void initGame() {
        dots = 3; // 初始蛇身長度
        direction = KeyEvent.VK_RIGHT; // 初始移動方向設為右
        isPlayerControl = false; // 非玩家控制
        isAIControl = true; // AI控制
        inGame = true; // 游戲進行中

        // 初始化蛇身段的位置
        for (int z = 0; z < dots; z++) {
            x[z] = 50 - z * DOT_SIZE;
            y[z] = 50;
        }

        // 隨機放置第一個蘋果
        locateApple();

        // 創建並啟動游戲更新計時器
        if (timer == null) {
            timer = new Timer(DELAY, this);
        } else {
            timer.restart();
        }
    }

    // 繪制面板組件
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (isStarted) {
            doDrawing(g); // 繪制游戲元素
        } else {
            showStartScreen(g); // 顯示游戲開始畫面
        }
    }

    // 顯示游戲開始畫面
    private void showStartScreen(Graphics g) {
        String[] messages = {
            "按Enter開始遊戲",
            "方向鍵可操控",
            "ESC切換AI控制"
        };
        Font small = new Font("Helvetica", Font.BOLD, 14); // 字體設定
        FontMetrics metr = getFontMetrics(small); // 獲取字體測量信息

        g.setColor(Color.white); // 設定顏色
        g.setFont(small); // 設定字體
        
        int lineHeight = metr.getHeight(); // 獲取行高
        int y = B_HEIGHT / 2 - ((messages.length - 1) * lineHeight) / 2; // 計算起始y坐標
        
        for (String msg : messages) {
            int x = (B_WIDTH - metr.stringWidth(msg)) / 2; // 計算x坐標
            g.drawString(msg, x, y); // 繪制提示消息
            y += lineHeight; // 更新y坐標
        }
    }

    // 繪制游戲元素
    private void doDrawing(Graphics g) {
        if (inGame) {
            g.drawImage(apple, apple_x, apple_y, this); // 繪制蘋果

            for (int z = 0; z < dots; z++) {
                if (z == 0) {
                    g.drawImage(head, x[z], y[z], this); // 繪制蛇頭
                } else {
                    g.drawImage(ball, x[z], y[z], this); // 繪制蛇身段
                }
            }

            Toolkit.getDefaultToolkit().sync(); // 同步繪圖
        } else {
            gameOver(g); // 顯示游戲結束消息
        }
    }

    // 顯示游戲結束消息
    private void gameOver(Graphics g) {
        String msg = "遊戲結束"; // 游戲結束消息
        Font small = new Font("Helvetica", Font.BOLD, 14); // 字體設定
        FontMetrics metr = getFontMetrics(small); // 獲取字體測量信息

        g.setColor(Color.white); // 設定顏色
        g.setFont(small); // 設定字體
        g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, B_HEIGHT / 2); // 繪制游戲結束消息
    }

    // 檢查蛇是否吃到了蘋果
    private void checkApple() {
        if ((x[0] == apple_x) && (y[0] == apple_y)) { // 如果蛇頭碰到了蘋果
            dots++; // 蛇身增加一節
            locateApple(); // 重新放置蘋果
        }
    }

    // 移動蛇的位置
    private void move() {
        for (int z = dots; z > 0; z--) {
            x[z] = x[z - 1];
            y[z] = y[z - 1];
        }

        if (isPlayerControl) { // 如果是玩家控制
            switch (direction) {
                case KeyEvent.VK_LEFT:
                    x[0] -= DOT_SIZE;
                    break;
                case KeyEvent.VK_RIGHT:
                    x[0] += DOT_SIZE;
                    break;
                case KeyEvent.VK_UP:
                    y[0] -= DOT_SIZE;
                    break;
                case KeyEvent.VK_DOWN:
                    y[0] += DOT_SIZE;
                    break;
            }
        } else if (isAIControl) { // 如果是AI控制
            int[] nextMove = findNextMove(x[0], y[0], apple_x, apple_y);
            if (nextMove != null) {
                x[0] = nextMove[0];
                y[0] = nextMove[1];
            }
        }
    }

    // 檢查蛇是否與自身或牆壁碰撞
    private void checkCollision() {
        for (int z = dots; z > 0; z--) {
            if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z])) { // 如果蛇頭碰到了蛇身的任意一段
                inGame = false; // 游戲結束
            }
        }

        if (y[0] >= B_HEIGHT || y[0] < 0 || x[0] >= B_WIDTH || x[0] < 0) { // 如果蛇頭碰到了牆壁
            inGame = false; // 游戲結束
        }

        if (!inGame) {
            timer.stop(); // 停止計時器
        }
    }

    // 隨機放置蘋果在游戲面板上
    private void locateApple() {
        int r = (int) (Math.random() * RAND_POS);
        apple_x = (r * DOT_SIZE);

        r = (int) (Math.random() * RAND_POS);
        apple_y = (r * DOT_SIZE);
    }

    // 計時器的事件處理方法
    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            checkApple(); // 檢查是否吃到蘋果
            checkCollision(); // 檢查碰撞
            move(); // 移動蛇
        }
        repaint(); // 重繪面板
    }

    // 使用A*演算法找到下一個移動位置
    private int[] findNextMove(int startX, int startY, int goalX, int goalY) {
        PriorityQueue<Node> openList = new PriorityQueue<>(Comparator.comparingInt(n -> n.cost));
        Set<Node> closedList = new HashSet<>();

        Node startNode = new Node(startX, startY, null, 0, estimateCost(startX, startY, goalX, goalY));
        openList.add(startNode);

        while (!openList.isEmpty()) {
            Node currentNode = openList.poll();

            if (currentNode.x == goalX && currentNode.y == goalY) {
                while (currentNode.parent != null && currentNode.parent != startNode) {
                    currentNode = currentNode.parent;
                }
                return new int[]{currentNode.x, currentNode.y};
            }

            closedList.add(currentNode);

            for (int[] direction : new int[][]{{0, DOT_SIZE}, {0, -DOT_SIZE}, {DOT_SIZE, 0}, {-DOT_SIZE, 0}}) {
                int newX = currentNode.x + direction[0];
                int newY = currentNode.y + direction[1];

                if (newX >= 0 && newX < B_WIDTH && newY >= 0 && newY < B_HEIGHT && !isOccupied(newX, newY)) {
                    Node neighborNode = new Node(newX, newY, currentNode, currentNode.gCost + 1, estimateCost(newX, newY, goalX, goalY));

                    if (!closedList.contains(neighborNode) && openList.stream().noneMatch(n -> n.equals(neighborNode) && n.cost <= neighborNode.cost)) {
                        openList.add(neighborNode);
                    }
                }
            }
        }

        return null;
    }

    // 檢查位置是否被蛇身占據
    private boolean isOccupied(int x, int y) {
        for (int i = 0; i < dots; i++) {
            if (this.x[i] == x && this.y[i] == y) {
                return true;
            }
        }
        return false;
    }

    // 估算從當前位置到目標位置的成本（啟發式評估）
    private int estimateCost(int startX, int startY, int goalX, int goalY) {
        return Math.abs(goalX - startX) + Math.abs(goalY - startY);
    }

    // 內部類，用於路徑查找中的節點表示
    private static class Node {
        int x, y;
        Node parent;
        int gCost; // 從起點到該節點的代價
        int cost; // 從起點經過該節點到達目標的總代價

        Node(int x, int y, Node parent, int gCost, int cost) {
            this.x = x;
            this.y = y;
            this.parent = parent;
            this.gCost = gCost;
            this.cost = cost;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return x == node.x &&
                    y == node.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    // 鍵盤事件適配器
    private class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if (key == KeyEvent.VK_ENTER) {
                if (!isStarted) {
                    isStarted = true;
                    initGame();
                    timer.start();
                }
            } else if (key == KeyEvent.VK_ESCAPE) {
                if (isPlayerControl && isStarted) {
                    isPlayerControl = false; // 切換為AI控制
                    isAIControl = true;
                } else if (isAIControl && isStarted) {
                    isAIControl = false; // 切換回玩家控制
                    isPlayerControl = true;
                }
            } else {
                isPlayerControl = true; // 確保在任何移動鍵上玩家控制

                switch (key) {
                    case KeyEvent.VK_LEFT:
                        if (direction != KeyEvent.VK_RIGHT) {
                            direction = KeyEvent.VK_LEFT;
                        }
                        break;
                    case KeyEvent.VK_RIGHT:
                        if (direction != KeyEvent.VK_LEFT) {
                            direction = KeyEvent.VK_RIGHT;
                        }
                        break;
                    case KeyEvent.VK_UP:
                        if (direction != KeyEvent.VK_DOWN) {
                            direction = KeyEvent.VK_UP;
                        }
                        break;
                    case KeyEvent.VK_DOWN:
                        if (direction != KeyEvent.VK_UP) {
                            direction = KeyEvent.VK_DOWN;
                        }
                        break;
                }
            }
        }
    }
}
