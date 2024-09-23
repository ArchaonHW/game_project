package game.plane;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class World extends JPanel {
  private static final long serialVersionUID = 1L;
  
  public static final int WIDTH = 400;//畫面寬
  
  public static final int HEIGHT = 700;//畫面高
  
  public static final int START = 0;
  
  public static final int RUNNING = 1;
  
  public static final int PAUSE = 2;
  
  public static final int GAME_OVER = 3;
  
  private int state = START;
  
  private static BufferedImage start = FlyingObject.loadImage("start.png");
  
  private static BufferedImage pause = FlyingObject.loadImage("pause.png");
  
  private static BufferedImage gameover = FlyingObject.loadImage("gameover.png");
  
  private Sky sky = new Sky();
  
  private Hero hero = new Hero();//主角飛機
  
  private FlyingObject[] enemies = new FlyingObject[0];//敵機陣列
  
  private Bullet[] bullets = new Bullet[0];//子彈陣列
  
  //隨機生成飛行物
  public FlyingObject nextOne() {
    Random rand = new Random();
    int type = rand.nextInt(20);
    if (type < 8)
      return new Airplane(); 
    if (type < 15)
      return new BigAirplane(); 
    return new Bee();
  }
  
  int enterIndex = 0;
  
  public void enterAction() {
    this.enterIndex++;
    if (this.enterIndex % 40 == 0) {//400毫秒==10*40
      FlyingObject obj = nextOne();//隨機生成一個飛行物
      this.enemies = Arrays.<FlyingObject>copyOf(this.enemies, this.enemies.length + 1);//擴容
      this.enemies[this.enemies.length - 1] = obj;//放到最後一位
    } 
  }
  
  public void stepAction() {
    this.sky.step();
    this.hero.step();
    int i;
    for (i = 0; i < this.enemies.length; i++) {    //飛機走一步
      FlyingObject e = this.enemies[i];
      e.step();
    } 
    for (i = 0; i < this.bullets.length; i++) {   //子彈走一步
      FlyingObject b = this.bullets[i];
      b.step();
    } 
  }
  
  int shootIndex = 0;//射擊計數
  //射擊
  public void shootAction() {
    this.shootIndex++;
    if (this.shootIndex % 30 == 0) { //100毫秒發一顆
      Bullet[] bs = this.hero.shoot(); //英雄打出子彈
      this.bullets = Arrays.<Bullet>copyOf(this.bullets, this.bullets.length + bs.length);//擴容
      System.arraycopy(bs, 0, this.bullets, this.bullets.length - bs.length, bs.length);//追加陣列
    } 
  }
  
  public void outOfBoundsAction() {//刪除越界飛行物及子彈
    int index = 0;
    FlyingObject[] enemyLives = new FlyingObject[this.enemies.length];//儲存活著的飛行物
    for (int i = 0; i < this.enemies.length; i++) {
      FlyingObject e = this.enemies[i];
      if (!e.outOfBounds() && !e.isRemove())
        enemyLives[index++] = e; //不越界的留著
    } 
    this.enemies = Arrays.<FlyingObject>copyOf(enemyLives, index);//將不越界的飛行物都留著
    
    index = 0;//重置為0
    Bullet[] bulletLives = new Bullet[this.bullets.length];
    for (int j = 0; j < this.bullets.length; j++) {
      Bullet b = this.bullets[j];
      if (!b.outOfBounds() && !b.isRemove())
        bulletLives[index++] = b; 
    } 
    this.bullets = Arrays.<Bullet>copyOf(bulletLives, index);//將不越界的子彈留著
  }
  
  int score = 0;//得分
  
  public void bulletBangAction() {//子彈與飛行物碰撞檢測
    for (int i = 0; i < this.bullets.length; i++) {//走訪所有子彈
      Bullet b = this.bullets[i];
      for (int j = 0; j < this.enemies.length; j++) {
        FlyingObject e = this.enemies[j];
        if (b.isLife() && e.isLife() && e.hit(b)) {
          b.goDead();//子彈消失
          e.goDead();//飛行物消失
          if (e instanceof Enemy) {//檢查類型，是敵人，則加分
            Enemy en = (Enemy)e;//強制類型轉換
            this.score += en.getScore();//加分
          } 
          if (e instanceof Award) {//若為獎勵，設定獎勵
            Award a = (Award)e;
            int type = a.getType();//獲取獎勵類型
            switch (type) {
              case 1:
                this.hero.addDoubleFire();//設定雙倍火力
                break;
              case 0:
                this.hero.addLife();//設定加生命
                break;
            } 
          } 
        } 
      } 
    } 
  }
  
  public void heroBangAction() {//英雄機碰撞檢測
    for (int i = 0; i < this.enemies.length; i++) {
      FlyingObject e = this.enemies[i];
      if (this.hero.isLife() && e.isLife() && e.hit(this.hero)) {
        e.goDead();
        this.hero.subtractLife();
        this.hero.clearDoubleFire();
        break;
      } 
    } 
  }
  
  public void checkGameOverAction() {//檢查遊戲是否結束
    if (this.hero.getLife() <= 0)
      this.state = GAME_OVER; //改變狀態
  }
  
  public void action() {//啟動執行程式碼
    MouseAdapter l = new MouseAdapter() {//滑鼠監聽事件
        public void mouseMoved(MouseEvent e) {//滑鼠移動
          if (World.this.state == RUNNING) {//運行時移動英雄機
            int x = e.getX();
            int y = e.getY();
            World.this.hero.moveTo(x, y);
          } 
        }
        
        public void mouseClicked(MouseEvent e) {//滑鼠點擊
          switch (World.this.state) {
            case START:
              World.this.state = RUNNING;
              break;
            case GAME_OVER://遊戲結束，清理現場
              World.this.score = 0;
              World.this.hero = new Hero();
              World.this.enemies = new FlyingObject[0];
              World.this.bullets = new Bullet[0];
              World.this.state = START;
              break;
          } 
        }
        
        public void mouseExited(MouseEvent e) {//滑鼠退出
          if (World.this.state == RUNNING)
            World.this.state = PAUSE; //遊戲未結束，則設置為暫停
        }
        
        public void mouseEntered(MouseEvent e) {//滑鼠進入
          if (World.this.state == PAUSE)//暫停時運行
            World.this.state = RUNNING; 
        }
      };
    this.addMouseListener(l);//處理滑鼠點擊操作
    this.addMouseMotionListener(l);//處理滑鼠滑動操作
    
    Timer timer = new Timer();//定時器，主流程控制
    int intevel = 10;//時間間隔(毫秒)
    
    timer.schedule(new TimerTask() {
          public void run() {
            if (World.this.state == RUNNING) {
              World.this.enterAction();//飛行物入場
              World.this.stepAction();//走一步
              World.this.shootAction();//射擊
              World.this.outOfBoundsAction();//刪除越界飛行物及子彈
              World.this.bulletBangAction();//子彈打飛行物
              World.this.heroBangAction();//英雄中彈
              World.this.checkGameOverAction();//檢查遊戲結束
            } 
            World.this.repaint();//重繪，調用paint()方法
          }
        },  intevel, intevel);
  }
  
  public void paint(Graphics g) {
    this.sky.paintObject(g);//畫背景圖
    this.hero.paintObject(g);//畫主角
    int i;
    for (i = 0; i < this.enemies.length; i++)
      this.enemies[i].paintObject(g); //畫敵機
    for (i = 0; i < this.bullets.length; i++)
      this.bullets[i].paintObject(g); //畫子彈
    g.drawString("SCORE:" + this.score, 10, 25);//畫分數
    g.drawString("LIFE:" + this.hero.getLife(), 10, 45);//畫生命
    switch (this.state) {//畫遊戲狀態
      case START:
        g.drawImage(start, 0, 0, null);
        break;
      case PAUSE:
        g.drawImage(pause, 0, 0, null);
        break;
      case GAME_OVER:
        g.drawImage(gameover, 0, 0, null);
        break;
    } 
  }
  
  public static void main(String[] args) {
    JFrame frame = new JFrame();
    World world = new World();//創建一個新的畫面
    frame.add(world);//將面板添加到JFrame中
    frame.setSize(WIDTH, HEIGHT);//大小
    frame.setLocationRelativeTo((Component)null);//設置窗體初始位置
    frame.setDefaultCloseOperation(3);//默認關閉操作
    frame.setVisible(true);//盡快調用paint
    world.action();//啟動執行
  }
}
