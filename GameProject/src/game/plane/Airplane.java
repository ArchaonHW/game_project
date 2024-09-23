package game.plane;

import java.awt.image.BufferedImage;

//敵飛機，是飛行物，也是敵人
public class Airplane extends FlyingObject implements Enemy {
  private static BufferedImage[] image = new BufferedImage[5];
  
  private int speed;
  
  int deadIndex;
  
  static {
    for (int i = 0; i < image.length; i++)
      image[i] = loadImage("airplane" + i + ".png"); 
  }
  
  //初始化資料
  public Airplane() {
    super(49, 36);
    this.deadIndex = 1;
    this.speed = 2;
  }
  
  public void step() {
    this.y += this.speed;
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
  
  public boolean outOfBounds() {//越界處理
    return (this.y >= World.HEIGHT);
  }
  
  public int getScore() {
    return 1;
  }
}
