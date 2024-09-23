//Character
package game.battle;

import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
//import java.util.*;
//import java.io.*;

public class Type2 extends Character
{
	BufferedImage	sprite			= null; 					//Sprite in use
	BufferedImage 	sprite_r 		= null; 					//Stand facing Right
	BufferedImage	sprite_l 		= null; 					//Stand facing Left
	BufferedImage[]	spr_run_l		= new BufferedImage[9];		//Running facing left
	BufferedImage[]	spr_run_r		= new BufferedImage[9];		//Running facing right
	BufferedImage	spr_dash_r 		= null;						//Dash facing right (NOT USED)
	BufferedImage	spr_dash_l 		= null;						//Dash facing left  (NOT USED)
	BufferedImage	spr_jump_r		= null;						//Jump facing right
	BufferedImage	spr_jump_l		= null;						//Jump facing
	BufferedImage	spr_fall_r		= null;						//Fall facing right
	BufferedImage	spr_fall_l		= null;						//Fall facing left
	BufferedImage	spr_punch_r		= null;						//Punching Right
	BufferedImage	spr_punch_l		= null;						//Punching Left
	
	BufferedImage	spr_attack_r		= null;						//Punching Right
	BufferedImage	spr_attack_l		= null;						//Punching Left

	private int 	hp 				= 100;						//Health Points
	private boolean	doubleJump 		= false;					//Whether the person has used his second jump, such that he cannot continuously jump.
	private int 	dir				= 1; 						//-1 = Right, 1 = Left
	int 			runAt			= 0;						//designates which running sprite is used for the animation.
	private String buffMessage;
    private long buffMessageEndTime;
    private Color buffMessageColor;  // 添加顏色變量

	Type2(int x, int y)
	{
		super(x,y, 10,10);
		//this.setAttack(true); // 設置 attack 為 true

		try {
		    // 使用類加載器讀取圖像文件並存儲在對應的變量中
		    sprite_r = ImageIO.read(getClass().getResource("/S_R.png"));
		    sprite_l = ImageIO.read(getClass().getResource("/S_L.png"));

		    spr_run_l[0] = ImageIO.read(getClass().getResource("/S_Run_l_1.png"));
		    spr_run_l[1] = ImageIO.read(getClass().getResource("/S_Run_l_2.png"));
		    spr_run_l[2] = ImageIO.read(getClass().getResource("/S_Run_l_3.png"));
		    spr_run_l[3] = ImageIO.read(getClass().getResource("/S_Run_l_4.png"));
		    spr_run_l[4] = ImageIO.read(getClass().getResource("/S_Run_l_5.png"));
		    spr_run_l[5] = ImageIO.read(getClass().getResource("/S_Run_l_6.png"));
		    spr_run_l[6] = ImageIO.read(getClass().getResource("/S_Run_l_7.png"));
		    spr_run_l[7] = ImageIO.read(getClass().getResource("/S_Run_l_8.png"));
		    spr_run_l[8] = ImageIO.read(getClass().getResource("/S_Run_l_9.png"));

		    spr_run_r[0] = ImageIO.read(getClass().getResource("/S_Run_r_1.png"));
		    spr_run_r[1] = ImageIO.read(getClass().getResource("/S_Run_r_2.png"));
		    spr_run_r[2] = ImageIO.read(getClass().getResource("/S_Run_r_3.png"));
		    spr_run_r[3] = ImageIO.read(getClass().getResource("/S_Run_r_4.png"));
		    spr_run_r[4] = ImageIO.read(getClass().getResource("/S_Run_r_5.png"));
		    spr_run_r[5] = ImageIO.read(getClass().getResource("/S_Run_r_6.png"));
		    spr_run_r[6] = ImageIO.read(getClass().getResource("/S_Run_r_7.png"));
		    spr_run_r[7] = ImageIO.read(getClass().getResource("/S_Run_r_8.png"));
		    spr_run_r[8] = ImageIO.read(getClass().getResource("/S_Run_r_9.png"));

		    spr_dash_r = ImageIO.read(getClass().getResource("/S_Dash_R.png"));
		    spr_dash_l = ImageIO.read(getClass().getResource("/S_Dash_L.png"));

		    spr_jump_r = ImageIO.read(getClass().getResource("/S_Jump_R.png"));
		    spr_jump_l = ImageIO.read(getClass().getResource("/S_Jump_L.png"));

		    spr_fall_r = ImageIO.read(getClass().getResource("/S_Fall_R.png"));
		    spr_fall_l = ImageIO.read(getClass().getResource("/S_Fall_L.png"));

		    spr_punch_l = ImageIO.read(getClass().getResource("/S_Punch_L.png"));
		    spr_punch_r = ImageIO.read(getClass().getResource("/S_Punch_R.png"));
		    spr_attack_l = ImageIO.read(getClass().getResource("/S_Attack_L.png"));
		    spr_attack_r = ImageIO.read(getClass().getResource("/S_Attack_R.png"));

		} catch (Exception ex) {
		    // 如果讀取圖像文件失敗，輸出錯誤信息
		    System.out.println("Cannot Read2");
		    ex.printStackTrace(); // 輸出完整的錯誤信息
		}


		sprite = sprite_r;
		setAttackType(0);//攻擊模式為普攻
		setHeight(sprite.getHeight());
		setWidth(sprite.getWidth());
	}

	public void update()
	{

	}

	public int getHP()
	{
		return hp;
	}

	public void addHP(int amount)
	{
		hp += amount;
		if (this.hp > 100) {
            this.hp = 100; // 假设最大生命值为100
	}}
	

	public void draw(Graphics g)
	{
		g.drawImage(sprite, getX() , getY(), null);
		//g.drawRect(getX(), getY(), getWidth(),getHeight());
		// 如果有 Buff 提示，且還在顯示時間內，顯示提示信息
        if (System.currentTimeMillis() < buffMessageEndTime) {
            g.setColor(buffMessageColor);  // 使用指定顏色
            g.setFont(new Font("Arial", Font.BOLD, 12));
            g.drawString(buffMessage, getX(), getY() - 10);
            }
	}
	 // 顯示 Buff 提示信息
    public void showBuffMessage(String message,Color color, int durationInMillis) {
        this.buffMessage = message;
        this.buffMessageColor = color;  // 設置顏色
        this.buffMessageEndTime = System.currentTimeMillis() + durationInMillis;
    }
	public void repaint (Graphics g)
	{
	}

	// 在類的屬性中宣告 baseSpeed
	private int baseSpeed = 100; // 設置一個默認的基本速度
	//基礎速度
	public void setBaseSpeed(int newSpeedamount) {
        this.baseSpeed += newSpeedamount;
        if (this.baseSpeed > 200) {
            this.baseSpeed = 200; // 假设最大速度=200
        }
	}

	// 改變角色的橫向速度
	public void changeV(int xfactor) {
	    dir = (int) Math.signum(xfactor); // 根據速度方向設置角色方向
	    setVX(baseSpeed * getDir()); // 使用基本速度設置新速度
	}
	
	//returns direction 1 is right, -1 is left.
	public int getDir()
	{
		return dir;
	}
//	public void changeV(int xfactor)
//	{
//		if(getVX() * xfactor > 0){
//			if(Math.abs(getVX()) < 100){
//				setVX(getVX() + xfactor);
//			}
//			else{}
//		}
//		else{
//			stopped();
//
//			dir = (int)Math.signum(xfactor);	//change direction
//
//			setVX(50*getDir());
//		}
//	}

	//change TO DIRECTION double direction when vx == 0
	public void changeDir(double direction)
	{
		if(direction < 0)
		{
			if(sprite == sprite_r)
				sprite = sprite_l;

			else if(sprite == spr_dash_r)
				sprite = spr_dash_l;

			else if(sprite == spr_jump_r)
				sprite = spr_jump_l;

			else if(sprite == spr_fall_r)
				sprite = spr_fall_l;
		}
		else
		{
			if(sprite == sprite_l)
				sprite = sprite_r;

			else if(sprite == spr_dash_l)
				sprite = spr_dash_r;

			else if(sprite == spr_jump_l)
				sprite = spr_jump_r;

			else if(sprite == spr_fall_l)
				sprite = spr_fall_r;
		}
	}

	public void stopped()
	{
		setVX(0);
	}
	public void fall() {
		if(getFalling() == true&&getY() <= 550)
		setVY(getVY() + 20); // 增加角色的垂直速度，使其向下掉落

		// 如果玩家位置超出了地板，修正玩家位置
		int groundY=550;
		if (getY() > groundY) {
			setY(groundY); // 将玩家Y坐标设置为地板的Y坐标
			setVY(0); // 停止垂直运动
			setFalling(false); // 停止下落
			}

	}
	public void jump() {
		boolean falling = getFalling(); // 檢查角色是否在掉落

		if (!falling) {
	        doubleJump = false; // 未進行二段跳

	        // 將角色的 Y 坐標減少 10 單位，稍微上升
	        setY(getY() - 20);

	        // 設置垂直速度為 -201，角色開始向上跳躍
	        setVY(-201);
	    } 
	    // 如果角色正在下落，並且尚未進行二段跳
	    else if (falling && !doubleJump) {
	        doubleJump = true; // 標記二段跳已經使用

	        // 將角色的 Y 坐標減少 10 單位，稍微上升
	        setY(getY() - 50);

	        // 設置垂直速度為 -201，角色再次向上跳躍（進行二段跳）
	        setVY(-301);
		}
	}
	public void dash(int xShift)
	{
		if(getDir() == 1)
		{
			sprite = spr_dash_r;
		}
		else
		{
			sprite = spr_dash_l;
		}

		setX(getX() + xShift*getDir());
	}

	public void move(double time, boolean movedX, boolean movedY, boolean rightcol, boolean downcol)
	{
		int prevH = sprite.getHeight();	//used to account for the difference in height between sprites
		int prevW = sprite.getWidth();	//used to account for the difference in width between sprites
		boolean hadPunchedL = false;

		if(sprite == spr_punch_l)
			hadPunchedL = true;


		if (getDir() == -1) { // 當前角色面向左
			if (getPunch() == true) {
				if(getAttackType()==0) {
					sprite = spr_punch_l;
					} // 設置揮拳圖像
				else {
					sprite = spr_attack_l; // 設置攻擊圖像
					}
				}
			
			else if (getVY() == 0) { // 如果沒有垂直運動
				if (getVX() == 0) // 沒有水平運動
					sprite = sprite_l; // 設置站立圖像
				else if (sprite == sprite_l) { // 開始跑步
					runAt = 0;
					sprite = spr_run_l[runAt]; // 設置為跑步的第一幀
				} else {
					runAt = (runAt + 2) % spr_run_l.length; // 切換到下一幀
					sprite = spr_run_l[runAt]; // 設置為跑步圖像
				}
			} else if (getVY() > 0)
				sprite = spr_fall_l; // 如果在下降，設置下降圖像
			else
				sprite = spr_jump_l; // 如果在上升，設置跳躍圖像
			

		} else if (getDir() == 1) { // 當前角色面向右
			if (getPunch() == true)
				if(getAttackType()==0) {
					sprite = spr_punch_r;
					} // 設置揮拳圖像
				else {
					sprite = spr_attack_r; // 設置攻擊圖像
					}
			else if (getVY() == 0) { // 如果沒有垂直運動
				if (getVX() == 0) // 沒有水平運動
					sprite = sprite_r; // 設置站立圖像
				else if (sprite == sprite_r) { // 開始跑步
					runAt = 0;
					sprite = spr_run_r[runAt]; // 設置為跑步的第一幀
				} else {
					runAt = (runAt + 2) % spr_run_r.length; // 切換到下一幀
					sprite = spr_run_r[runAt]; // 設置為跑步圖像
				}
			} else if (getVY() > 0)
				sprite = spr_fall_r; // 如果在下降，設置下降圖像
			else
				sprite = spr_jump_r; // 如果在上升，設置跳躍圖像
		}

		if(!movedX)
			updateX(time);
		if(!movedY)
			updateY(time);

		setHeight(sprite.getHeight());		//comment out to stop the running and standing glitch (while sprites are not the same height)
		setWidth(sprite.getWidth());

		//System.out.println("Prev H " + prevH + " CurH " + getHeight());
		//System.out.println("Prev Y " + getY() + " CurY " + (getY() + (prevH - getHeight())));

		if(downcol == true)
			setY(getY() + (prevH - getHeight()));
		if(rightcol == true || (getPunch() == true && getDir() == -1)|| (hadPunchedL == true && getPunch() == false))
			setX(getX() + (prevW - getWidth()));

	}
}
