package game.battle;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class BuffIcon {
    private int x, y;
    private BufferedImage image;
    private String type;
    private long createTime;
    private boolean isVisible = true;
    

    private static final int WIDTH = 40;
    private static final int HEIGHT = 40;

    public BuffIcon(String type, int x, int y) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.createTime = System.currentTimeMillis();

        try {
            // 加载对应的 Buff 图示
            switch (type) {
                case "SPEED":
                    image = ImageIO.read(getClass().getResource("/Buff_speed.png"));
                    break;
                case "DAMAGE":
                    image = ImageIO.read(getClass().getResource("/Buff_damage.png"));
                    break;
                case "HEAL":
                    image = ImageIO.read(getClass().getResource("/Buff_addhp.png"));
                    break;
                case "MULTI_SHOT":
                    image = ImageIO.read(getClass().getResource("/Buff_mulattack.png"));
                    break;
                case "BOMB":
                    image = ImageIO.read(getClass().getResource("/Buff_bigattack.png"));
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void draw(Graphics g) {
        if (isVisible) {
            Graphics2D g2d = (Graphics2D) g;
            float alpha = calculateAlpha(); // 計算透明度
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g2d.drawImage(image, x, y, WIDTH, HEIGHT, null);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f)); // 重設為不透明
        }
    }
    
    private float calculateAlpha() {
        long elapsed = System.currentTimeMillis() - createTime;
        if (elapsed > 12000) { // 最後 3 秒閃爍
            return (elapsed % 500) < 250 ? 0.2f : 1.0f;
        }
        return 1.0f;
    }

    public void update() {
        // 檢查 Buff 是否超過 10 秒，如果是則設置為不可見
        long elapsed = System.currentTimeMillis() - createTime;
        if (elapsed > 15000) {
            isVisible = false;
        }
    }

    public boolean isVisible() {
        return isVisible;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }

    public String getType() {
        return type;
    }

    // 檢測碰撞並應用 Buff 效果
    public void checkCollision(Character player) {
        if (this.isVisible && player.getBounds().intersects(this.getBounds())) {
            this.applyBuffEffect(player); // 应用 Buff 效果
            this.isVisible = false; // BuffIcon 碰撞後隱藏
        }
    }

    // 应用 Buff 效果
    public void applyBuffEffect(Character player) {
        if (player instanceof Type1b) {
            Type1b p1 = (Type1b) player;
            switch (type) {
                case "HEAL":
                    p1.addHP(15); // 增加生命值
                    p1.showBuffMessage("HP+15",Color.GREEN, 1000); // 顯示 "+15 HP" 提示 1 秒
                    break;
                case "DAMAGE":
                    p1.addHP(-30); // 減少生命值
                    p1.showBuffMessage("HP-30",Color.RED, 1000); // 顯示 "+15 HP" 提示 1 秒
                    break;
                case "SPEED":
                	p1.setBaseSpeed(20);//增加速度
                	p1.showBuffMessage("Speed+20",Color.GREEN, 1000);
                    // 增加速度的处理逻辑
                    break;
                case "MULTI_SHOT":
                	p1.attackBuff(15); // 增加多重射擊效果，持續15秒
                	p1.setAttackType(1);//改變攻擊類型
                    // 多重射击的处理逻辑
                    break;
                case "BOMB":
                	p1.attackBuff(15); // 增加炸彈效果，持續15秒
                	p1.setAttackType(2);//改變攻擊類型
                    // 炸弹效果的处理逻辑
                    break;
            }
        } else if (player instanceof Type2) {
            Type2 p2 = (Type2) player;
            switch (type) {
                case "HEAL":
                    p2.addHP(15); // 增加生命值
                    p2.showBuffMessage("HP+15",Color.GREEN, 1000); // 顯示 "+15 HP" 提示 1 秒
                    break;
                case "DAMAGE":
                    p2.addHP(-30); // 減少生命值
                    p2.showBuffMessage("HP-30",Color.RED, 1000); // 顯示 "+15 HP" 提示 1 秒
                    break;
                case "SPEED":
                	p2.setBaseSpeed(20);//增加速度
                	p2.showBuffMessage("Speed+20",Color.GREEN, 1000);
                    // 增加速度的处理逻辑
                    break;
                case "MULTI_SHOT":
                	p2.attackBuff(15); // 增加多重射擊效果，持續15秒
                	p2.setAttackType(1);
                    // 多重射击的处理逻辑
                    break;
                case "BOMB":
                	p2.attackBuff(15); // 增加炸彈效果，持續15秒
                	p2.setAttackType(2);
                    // 炸弹效果的处理逻辑
                    break;
            }
        }
    }
}
