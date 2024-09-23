package game.battle;

import java.awt.*;

public class EffectText {
    private int x, y; // 文字的位置
    private String text; // 顯示的文字
    private Color color; // 文字的顏色
    private long creationTime; // 文字創建的時間
    private int duration; // 文字顯示的持續時間

    public EffectText(String text, int x, int y, Color color, int duration) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.color = color;
        this.duration = duration;
        this.creationTime = System.currentTimeMillis();
    }

    // 檢查文字是否已經過期
    public boolean isExpired() {
        return System.currentTimeMillis() - creationTime > duration;
    }

    // 繪製文字
    public void draw(Graphics g) {
        if (!isExpired()) {
            g.setColor(color);
            g.setFont(new Font("Arial", Font.BOLD, 16));
            g.drawString(text, x, y);
        }
    }
}
