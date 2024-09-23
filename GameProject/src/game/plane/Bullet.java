package game.plane;

import java.awt.image.BufferedImage;

//子彈類別，是飛行物
public class Bullet extends FlyingObject {
  private static BufferedImage image = loadImage("bullet.png");
  
  private int speed;//移動的速度
  
  public Bullet(int x, int y) {
    super(8, 14, x, y);
    this.speed = 3;
  }
  
  public void step() {//移動方法
    this.y -= this.speed;
  }
  
  public BufferedImage getImage() {
    if (isLife())
      return image; 
    if (isDead())
      this.state = 2; 
    return null;
  }
  
  public boolean outOfBounds() {
    return (this.y <= this.height);
  }
}
