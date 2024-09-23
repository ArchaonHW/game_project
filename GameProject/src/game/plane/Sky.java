package game.plane;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Sky extends FlyingObject {
  private static BufferedImage image = loadImage("background.png");
  
  private int speed;
  
  private int y1;
  
  public Sky() {
    super(400, 700, 0, 0);
    this.speed = 1;
    this.y1 = -this.height;
  }
  
  public void step() {
    this.y += this.speed;
    this.y1 += this.speed;
    if (this.y >= this.height)
      this.y = -this.height; 
    if (this.y1 >= this.height)
      this.y1 = -this.height; 
  }
  
  public void paintObject(Graphics g) {
    g.drawImage(getImage(), this.x, this.y, null);
    g.drawImage(getImage(), this.x, this.y1, null);
  }
  
  public BufferedImage getImage() {
    return image;
  }
  
  public boolean outOfBounds() {
    return false;
  }
}
