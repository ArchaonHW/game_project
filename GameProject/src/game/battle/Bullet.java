package game.battle;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

public class Bullet {
    private  int x; // 子彈的座標
	private  int y;
  //  private int speed; // 子彈的速度
    private double direction; // 子彈的移動方向，使用弧度表示
    private int playerType; // 玩家類型，1 表示 Player1，2 表示 Player2
    private BufferedImage[] bulletImages; // 子彈的圖片數組，用來存放動畫幀
    private int animationIndex = 0; // 當前顯示的動畫幀索引
   // private boolean forward = true; // 控制動畫的播放方向，true 表示正向播放
    private long lastUpdateTime; // 上次更新動畫幀的時間
    private static final int ANIMATION_DELAY = 100; // 每幀動畫的間隔時間（毫秒）

    private BufferedImage[] explosionImages; // 爆炸的圖片數組
    private boolean exploded = false; // 爆炸動畫是否已經播放
    private boolean toBeRemoved = false; // 用於標記子彈是否應該被刪除
	private double angle;

    public Bullet(int x, int y, int playerType, int direction,double angle) {
        this.x = x;
        this.y = y;
        this.playerType = playerType;
        this.direction = direction;
        this.angle = angle; // 这里使用传入的角度
     //   this.speed = speed;
        this.bulletImages = new BufferedImage[4];

        try {
            // 加載子彈圖片
            if (playerType == 1) {
                bulletImages[0] = ImageIO.read(getClass().getResource("/D_bull1_M.png"));
                bulletImages[1] = ImageIO.read(getClass().getResource("/D_bull2_M.png"));
                bulletImages[2] = ImageIO.read(getClass().getResource("/D_bull3_M.png"));
                bulletImages[3] = ImageIO.read(getClass().getResource("/D_bull4_M.png"));
                System.out.println("Loaded D_bull1_M.png for Player1");
            } else if (playerType == 2) {
                bulletImages[0] = ImageIO.read(getClass().getResource("/D_bull1_S.png"));
                bulletImages[1] = ImageIO.read(getClass().getResource("/D_bull2_S.png"));
                bulletImages[2] = ImageIO.read(getClass().getResource("/D_bull3_S.png"));
                bulletImages[3] = ImageIO.read(getClass().getResource("/D_bull4_S.png"));
                System.out.println("Loaded S_bull1_S.png for Player2");
            }

            // 加載爆炸圖片
            explosionImages = new BufferedImage[4];
            //explosionImages[0] = ImageIO.read(getClass().getResource("/D_explosion1.png"));
            explosionImages[0] = ImageIO.read(getClass().getResource("/D_explosion2.png"));
            explosionImages[1] = ImageIO.read(getClass().getResource("/D_explosion3.png"));
            explosionImages[2] = ImageIO.read(getClass().getResource("/D_explosion4.png"));
            explosionImages[3] = ImageIO.read(getClass().getResource("/D_explosion5.png"));
        } catch (IOException e) {
            e.printStackTrace(); // 加載圖片失敗時輸出錯誤信息
        }

        this.lastUpdateTime = System.currentTimeMillis(); // 設置初始動畫幀更新時間
    }

 // 更新子彈的狀態，包括動畫和移動
    public void update(Type1b p1, Type2 p2) {
        long currentTime = System.currentTimeMillis(); // 獲取當前時間
        if (!exploded) {
            if (currentTime - lastUpdateTime > ANIMATION_DELAY) {
                animationIndex++;
                if (animationIndex >= bulletImages.length) {
                    animationIndex = 0;
                }
                lastUpdateTime = currentTime;
            }

            // 根據方向和速度更新子彈的位置
            x += (int) (30 * Math.cos(angle)* direction);
            y += (int) (30 * Math.sin(angle));

            // 檢查是否超過 Y 值的限制或超出畫面邊界
            int maxY = 670; // Y 軸最大值
            int screenWidth = 800; // 假設畫面寬度為 800

            if (y > maxY || x < 0 || x > screenWidth) { 
            	System.out.println("Bullet out of bounds, setting toBeRemoved to true");
                toBeRemoved = true; // 超過指定 Y 值或超出畫面邊界後標記子彈應該被移除
            }

            // 檢測子彈與敵方角色的碰撞
            if (playerType == 1 && getBounds().intersects(p2.getBounds())) {
                p2.addHP(-3);
                exploded = true; // 發生碰撞後觸發爆炸
                animationIndex = 0; // 重置動畫索引
            } else if (playerType == 2 && getBounds().intersects(p1.getBounds())) {
                p1.addHP(-3);
                exploded = true; // 發生碰撞後觸發爆炸
                animationIndex = 0; // 重置動畫索引
            }
        } else {
            // 如果爆炸動畫已經開始
            if (currentTime - lastUpdateTime > ANIMATION_DELAY) {
                animationIndex++;
                if (animationIndex >= explosionImages.length) {
                    toBeRemoved = true; // 爆炸動畫播放完畢，標記子彈為需移除
                }
                lastUpdateTime = currentTime;
            }
        }
        
    }



    public void drawBullet(Graphics g) {
        System.out.println("Drawing bullet for playerType: " + playerType);
        if (!exploded) {
            g.drawImage(bulletImages[animationIndex], x, y, null);
            System.out.println("Drawing bullet at: " + x + ", " + y);
        } else {
            // 确保索引不超出范围
            if (animationIndex < explosionImages.length) {
                // 将爆炸图片放大一倍进行绘制
                int explosionX = x - (explosionImages[animationIndex].getWidth() * 3 - bulletImages[animationIndex].getWidth()) / 2;
                int explosionY = y - (explosionImages[animationIndex].getHeight() * 3 - bulletImages[animationIndex].getHeight()) / 2;
                g.drawImage(explosionImages[animationIndex], explosionX, explosionY, explosionImages[animationIndex].getWidth() * 3, explosionImages[animationIndex].getHeight() * 3, null);
                System.out.println("Drawing explosion at: " + explosionX + ", " + explosionY);
            } else {
                // 如果索引超出范围，则标记子弹为需要删除
                toBeRemoved = true;
                return; // 立即返回，停止进一步的绘制
            }
        }
    }





    // 獲取子彈的邊界框，用於碰撞檢測
    public Rectangle getBounds() {
        return new Rectangle(x, y, bulletImages[0].getWidth(), bulletImages[0].getHeight());
    }

    // 檢查子彈是否需要被移除
    public boolean shouldBeRemoved() {
    	System.out.println("shouldBeRemoved called: " + toBeRemoved);
        return toBeRemoved;
    }
}
