package game.plane;

import java.awt.image.BufferedImage;

public class BigAirplane extends FlyingObject implements Enemy {
  private static BufferedImage[] image = new BufferedImage[5];
  
  private int speed;
  
  int deadIndex;
  
  static {
    for (int i = 0; i < image.length; i++)
      image[i] = loadImage("bigplane" + i + ".png"); 
  }
  
  public BigAirplane() {
    super(69, 99);
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
  
  public boolean outOfBounds() {
    return (this.y >= 700);
  }
  
  public int getScore() {
    return 3;
  }
}
