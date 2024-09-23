package game.plane;

import java.awt.image.BufferedImage;

//主角飛機，是飛行物
public class Hero extends FlyingObject {
  private static BufferedImage[] image = new BufferedImage[6];
  
  private int life;
  
  private int doubleFire;
  
  int index;
  
  int deadIndex;
  
  static {
    for (int i = 0; i < image.length; i++)
      image[i] = loadImage("hero" + i + ".png"); 
  }
  
  public Hero() {
    super(97, 124, 140, 400);
    this.index = 0;
    this.deadIndex = 2;
    this.life = 3;
    this.doubleFire = 0;
  }
  
  public void step() {}
  
  public void moveTo(int x, int y) {
    this.x = x - this.width / 2;
    this.y = y - this.height / 2;
  }
  
  public BufferedImage getImage() {
    if (isLife())
      return image[this.index++ % 2]; 
    if (isDead()) {
      BufferedImage img = image[this.deadIndex++];
      if (this.deadIndex == image.length)
        this.state = 2; 
      return img;
    } 
    return null;
  }
  
  public Bullet[] shoot() {
    int xStep = this.width / 4;
    int yStep = 20;
    if (this.doubleFire > 0) {
      Bullet[] arrayOfBullet = new Bullet[2];
      arrayOfBullet[0] = new Bullet(this.x + 1 * xStep, this.y - yStep);
      arrayOfBullet[1] = new Bullet(this.x + 3 * xStep, this.y - yStep);
      this.doubleFire -= 2;
      return arrayOfBullet;
    } 
    Bullet[] bs = new Bullet[1];
    bs[0] = new Bullet(this.x + 2 * xStep, this.y - yStep);
    return bs;
  }
  
  public boolean outOfBounds() {
    return false;
  }
  
  public void addLife() {
    this.life++;
  }
  
  public void subtractLife() {//減命
    this.life--;
  }
  
  public void clearDoubleFire() {
    this.doubleFire = 0;
  }
  
  public void addDoubleFire() {
    this.doubleFire += 40;
  }
  
  public int getLife() {
    return this.life;
  }
}
