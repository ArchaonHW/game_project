package game.plane;

import java.awt.image.BufferedImage;
import java.util.Random;

//蜜蜂
public class Bee extends FlyingObject implements Award {
  private static BufferedImage[] image = new BufferedImage[5];
  
  private int xSpeed;//x座標移動速度
  
  private int ySpeed;//y座標移動速度
  
  private int awardType;//獎勵類型
  
  int deadIndex;
  
  static {
    for (int i = 0; i < image.length; i++)
      image[i] = loadImage("bee" + i + ".png"); 
  }
  
  public Bee() {
    super(60, 50);
    this.deadIndex = 1;
    Random rand = new Random();
    this.xSpeed = 1;
    this.ySpeed = 2;
    this.awardType = rand.nextInt(2);
  }
  
  public void step() {//可斜飛
    this.x += this.xSpeed;
    this.y += this.ySpeed;
    if (this.x <= 0 || this.x >= 400 - this.width)
      this.xSpeed *= -1; 
  }
  
  public BufferedImage getImage() {
    if (isLife())
      return image[0]; 
    if (isDead()) {
      BufferedImage img = image[this.deadIndex++];
      if (this.deadIndex == image.length)
        this.state = 2; 
      return img;
    } 
    return null;
  }
  
  public boolean outOfBounds() {
    return (this.y >= World.HEIGHT);
  }
  
  public int getType() {
    return this.awardType;
  }
}
