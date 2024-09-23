package game.plane;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;
import javax.imageio.ImageIO;

public abstract class FlyingObject {
  public static final int LIFE = 0;
  
  public static final int DEAD = 1;
  
  public static final int REMOVE = 2;
  
  protected int state = 0;
  
  protected int width;//寬
  
  protected int height;//高
  
  protected int x;//x座標
  
  protected int y;//y座標
  
  public FlyingObject() {}
  
  public FlyingObject(int width, int height) {
    this.width = width;
    this.height = height;
    Random rand = new Random();
    this.x = rand.nextInt(400 - this.width + 1);
    this.y = -this.height;
  }
  
  public FlyingObject(int width, int height, int x, int y) {
    this.width = width;
    this.height = height;
    this.x = x;
    this.y = y;
  }
  
  public static BufferedImage loadImage(String fileName) {
    try {
      BufferedImage img = ImageIO.read(FlyingObject.class.getResource(fileName));
      return img;
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException();
    } 
  }
  
  public abstract void step();
  
  public abstract boolean outOfBounds();  //檢查是否出界
  
  public void paintObject(Graphics g) {
    g.drawImage(getImage(), this.x, this.y, null);
  }
  
  public abstract BufferedImage getImage();
  
  public boolean isLife() {
    return (this.state == 0);
  }
  
  public boolean isDead() {
    return (this.state == 1);
  }
  
  public boolean isRemove() {
    return (this.state == 2);
  }
  
  public void goDead() {
    this.state = 1;
  }
  
  public boolean hit(FlyingObject other) {//碰撞算法
    int x1 = this.x - other.width;
    int x2 = this.x + this.width;
    int y1 = this.y - other.height;
    int y2 = this.y + this.height;
    int x = other.x;
    int y = other.y;
    return (x >= x1 && x <= x2 && y >= y1 && y <= y2);
  }
}
