package game.battle;
import java.awt.*;

public class Stage extends Character
{
	@SuppressWarnings("unused")
	private int x;			//x is the x coordinate of the top left corner
	@SuppressWarnings("unused")
	private int y;			//y is the y coordinate of the top left corner
	@SuppressWarnings("unused")
	private int width;	//width of stage part
	@SuppressWarnings("unused")
	private int height; 	//height of stage part

									
																	
	public Stage (int x, int y, int width, int height)
	{
		super(x,y,width,height);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	public void update()
	{}

	public void draw(Graphics g)
	{
		//g.drawRect(x,y,width,height);
	}
}