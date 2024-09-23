package game.battle;
import java.util.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class BuffManager {
    private List<BuffIcon> buffs = new ArrayList<>(); // 場上的 Buff 圖示列表
    private long lastBuffTime1 = System.currentTimeMillis(); // 記錄上一次生成第一個 Buff 的時間
    private long nextBuffInterval1 = getRandomInterval(); // 下次生成第一個 Buff 的間隔
    
    private long lastBuffTime2 = System.currentTimeMillis(); // 記錄上一次生成第二個 Buff 的時間
    private long nextBuffInterval2 = getRandomInterval(); // 下次生成第二個 Buff 的間隔
    
    private long lastBuffTime3 = System.currentTimeMillis(); // 記錄上一次生成第三個 Buff 的時間
    private long nextBuffInterval3 = getRandomInterval(); // 下次生成第三個 Buff 的間隔

    // BuffManager 的構造函數，在初始化時生成兩個隨機 BuffIcon
    public BuffManager() {
        // 初始化時生成兩個 BuffIcon
        generateBuff();
        generateBuff();
    }

    // 隨機生成下一個 Buff 的時間間隔，介於 10 到 20 秒之間
    private long getRandomInterval() {
        Random rand = new Random();
        return (10 + rand.nextInt(6)) * 1000; // 產生 10 到 15 秒之間的隨機毫秒數
    }


    // 在場上生成一個 BuffIcon
    public void generateBuff() {
        Random rand = new Random();
        int x = 100 + rand.nextInt(770); // X 範圍：100~870
        int y = 50 + rand.nextInt(501);  // Y 範圍：50~550


        String[] buffTypes = {"SPEED","DAMAGE", "HEAL", "MULTI_SHOT", "BOMB"};
        String randomBuffType = buffTypes[rand.nextInt(buffTypes.length)]; // 隨機選擇 Buff 類型

        BuffIcon buff = new BuffIcon(randomBuffType, x, y);
        buffs.add(buff);
    }

    public void updateBuffs() {
        // 檢查是否需要生成第一個 BuffIcon
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastBuffTime1 >= nextBuffInterval1) {
            generateBuff(); // 生成新的 BuffIcon
            lastBuffTime1 = currentTime; // 更新最後生成 Buff 的時間
            nextBuffInterval1 = getRandomInterval(); // 設置下一次生成 Buff 的時間間隔
        }

        // 檢查是否需要生成第二個 BuffIcon
        if (currentTime - lastBuffTime2 >= nextBuffInterval2) {
            generateBuff(); // 生成新的 BuffIcon
            lastBuffTime2 = currentTime; // 更新最後生成 Buff 的時間
            nextBuffInterval2 = getRandomInterval(); // 設置下一次生成 Buff 的時間間隔
        }
     // 檢查是否需要生成第三個 BuffIcon
        if (currentTime - lastBuffTime3 >= nextBuffInterval3) {
            generateBuff(); // 生成新的 BuffIcon
            lastBuffTime3 = currentTime; // 更新最後生成 Buff 的時間
            nextBuffInterval3 = getRandomInterval(); // 設置下一次生成 Buff 的時間間隔
        }
        
        

        // 更新現有 BuffIcon 的狀態
        Iterator<BuffIcon> iterator = buffs.iterator(); // BuffIcon 的迭代器
        while (iterator.hasNext()) {
            BuffIcon buff = iterator.next();
            buff.update(); // 更新 Buff 的狀態（例如閃爍效果）

            if (!buff.isVisible()) {
                iterator.remove(); // BuffIcon 消失後從列表中移除
            }
        }
    }

    // 繪製所有 BuffIcon，並在右上角顯示當前 BuffIcon 的數量
    public void drawBuffs(Graphics g) {
        for (BuffIcon buff : buffs) {
            buff.draw(g); // `draw` 是 `BuffIcon` 類的方法，用來繪製 Buff 圖標
        }

        // 設定文字顏色為白色
        g.setColor(Color.WHITE);

        // 設定文字字體
        g.setFont(new Font("Arial", Font.BOLD, 16));

        // 在右上角繪製文字，顯示 BuffIcon 的數量
       //g.drawString("場上產生了 " + buffs.size() + " 個BUFFICON", 700, 20);
    }
    
    public List<BuffIcon> getBuffs() {
        return buffs;
    }

}
