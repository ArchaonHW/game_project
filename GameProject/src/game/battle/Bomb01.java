package game.battle;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.Timer;
import java.util.TimerTask;

public class Bomb01 {

    private int x, y; // 炸彈的當前位置
    private int startX, startY; // 炸彈的起始位置
    private int direction; // 投擲方向
    private BufferedImage[] bombImages; // 存放炸彈動畫圖片的數組
    private BufferedImage[] explosionImages; // 存放爆炸動畫圖片的數組
    private boolean exploded = false; // 炸彈是否已經爆炸
    private boolean explosionStarted = false; // 爆炸動畫是否開始
    private boolean explosionCompleted = false; // 爆炸動畫是否完成
    private int frameIndex = 0; // 當前顯示的動畫幀索引
    private Timer explosionTimer; // 用於控制炸彈爆炸的計時器
    private Character player1; // 第一個玩家
    private Character player2; // 第二個玩家
    private long lastFrameTime; // 上一幀的時間戳
    private static final int FRAME_DELAY = 200; // 幀間延遲（毫秒）
    private long throwTime; // 投擲時間

    // 建構子，用於初始化炸彈位置、圖片和玩家對象
    public Bomb01(int x, int y, int direction, Character player1, Character player2) { 
        this.x = x;
        this.y = y;
        this.startX = x;
        this.startY = y;
        this.direction = direction;
        this.player1 = player1;
        this.player2 = player2;

        bombImages = new BufferedImage[3]; // 初始化炸彈圖片數組
        explosionImages = new BufferedImage[5]; // 初始化爆炸圖片數組

        try {
            // 加載炸彈圖片
            bombImages[0] = ImageIO.read(getClass().getResource("/D_bomb_1.png"));
            bombImages[1] = ImageIO.read(getClass().getResource("/D_bomb_2.png"));
            bombImages[2] = ImageIO.read(getClass().getResource("/D_bomb_3.png"));

            // 加載爆炸圖片
            explosionImages[0] = ImageIO.read(getClass().getResource("/D_explosion1.png"));
            explosionImages[1] = ImageIO.read(getClass().getResource("/D_explosion2.png"));
            explosionImages[2] = ImageIO.read(getClass().getResource("/D_explosion3.png"));
            explosionImages[3] = ImageIO.read(getClass().getResource("/D_explosion4.png"));
            explosionImages[4] = ImageIO.read(getClass().getResource("/D_explosion5.png"));
        } catch (Exception e) {
            e.printStackTrace(); // 如果圖片加載失敗，打印錯誤訊息
        }
    }

    // 更新炸彈的狀態
    public void update() { 
        if (!exploded) {
            double timeSinceThrown = (System.currentTimeMillis() - throwTime) / 1000.0; // 計算從投擲開始到現在的時間
            
            // 水平速度（初始快，隨時間逐漸減慢，並使其呈現弧線軌跡）
            double velocityX = 300 * direction *  timeSinceThrown; // 水平速度衰减，起始快后面慢
            // 垂直速度（重力影響下的運動，讓其拋物線更接近圖示效果）
            double velocityY = -580 * timeSinceThrown + 600 * timeSinceThrown * timeSinceThrown;

            x = startX + (int)(velocityX * timeSinceThrown); // 更新x座標
            y = startY + (int)(velocityY * timeSinceThrown); // 更新y座標
        }

        if (explosionStarted && !explosionCompleted) { // 如果爆炸動畫開始但尚未完成
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastFrameTime >= FRAME_DELAY) {
                lastFrameTime = currentTime;
                frameIndex++; // 增加動畫幀索引
                if (frameIndex >= explosionImages.length) { // 如果動畫已到最後一幀
                    explosionCompleted = true; // 標記爆炸動畫完成
                    startDisappearTimer(); // 開始消失計時器
                }
            }
        }
    }

    // 投擲炸彈的方法
    public void throwBomb() { 
        if (!exploded) { // 如果炸彈還沒爆炸
            throwTime = System.currentTimeMillis(); // 記錄投擲時間
            startExplosionTimer(); // 啟動爆炸計時器
        }
    }

    // 啟動爆炸計時器的方法
    private void startExplosionTimer() { 
        explosionTimer = new Timer(); // 初始化計時器
        explosionTimer.schedule(new TimerTask() { // 安排計時任務
            @Override
            public void run() {
                explode(); // 計時器到期時觸發爆炸
                explosionTimer.cancel(); // 結束計時器
            }
        }, 1000); // 計時器設置為1秒後觸發
    }

    // 開始消失計時器的方法
    private void startDisappearTimer() {
        explosionTimer = new Timer(); // 重用計時器
        explosionTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                exploded = false; // 停止爆炸
                explosionTimer.cancel(); // 結束計時器
            }
        }, 1000); // 停留1秒後消失
    }

    // 爆炸並檢查是否命中玩家的方法
    public void explode() { 
        exploded = true; // 設置炸彈為已爆炸
        explosionStarted = true; // 設置爆炸動畫開始
        lastFrameTime = System.currentTimeMillis(); // 設置初始時間
        Rectangle explosionBounds = new Rectangle(x, y, explosionImages[0].getWidth() * 4, explosionImages[0].getHeight() * 4); // 計算爆炸範圍

        // 如果爆炸範圍與 player1 的邊界重疊，檢查類型並扣血
        if (explosionBounds.intersects(player1.getBounds())) {
            if (player1 instanceof Type1b) {
                ((Type1b) player1).addHP(-20); // 如果是 Type1b，扣 20 血
            } else if (player1 instanceof Type2) {
                ((Type2) player1).addHP(-20); // 如果是 Type2，扣 20 血
            }
        }

        // 如果爆炸範圍與 player2 的邊界重疊，檢查類型並扣血
        if (explosionBounds.intersects(player2.getBounds())) {
            if (player2 instanceof Type1b) {
                ((Type1b) player2).addHP(-20); // 如果是 Type1b，扣 20 血
            } else if (player2 instanceof Type2) {
                ((Type2) player2).addHP(-20); // 如果是 Type2，扣 20 血
            }
        }
    }

    // 檢查炸彈是否已經爆炸
    public boolean hasExploded() { 
        return explosionCompleted && !explosionStarted; // 完成爆炸並且爆炸动画已播放完毕
    }

 // 繪製炸彈和爆炸動畫
    public void draw(Graphics g) { 
        if (explosionStarted) { // 如果炸彈已經爆炸
            if (frameIndex < explosionImages.length) { // 確保索引不超過範圍
                int explosionX = x - (explosionImages[frameIndex].getWidth() * 4 - bombImages[frameIndex % bombImages.length].getWidth()) / 2;
                int explosionY = y - (explosionImages[frameIndex].getHeight() * 4 - bombImages[frameIndex % bombImages.length].getHeight()) / 2;
                g.drawImage(explosionImages[frameIndex], explosionX, explosionY, explosionImages[frameIndex].getWidth() * 4, explosionImages[frameIndex].getHeight() * 4, null); // 繪製爆炸動畫
            }
        } else { // 如果炸彈還未爆炸
            g.drawImage(bombImages[frameIndex % bombImages.length], x, y, null); // 繪製炸彈動畫
        }
    }

}
