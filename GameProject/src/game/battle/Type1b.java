package game.battle;
import java.awt.*; // 引入Java AWT類庫，用於圖形介面
import java.awt.image.*; // 引入Java AWT圖像處理類庫
import javax.imageio.*; // 引入Java的ImageIO類，用於圖像讀取
//import java.util.*; // 引入Java的實用工具類，如集合
//import java.io.*; // 引入Java的IO類，用於文件讀取

public class Type1b extends Character {
	BufferedImage sprite = null; // 當前使用的圖像精靈
	BufferedImage sprite_r = null; // 面向右邊的站立圖像
	BufferedImage sprite_l = null; // 面向左邊的站立圖像
	BufferedImage[] spr_run_l = new BufferedImage[10]; // 跑步（向左）動畫的圖像陣列
	BufferedImage[] spr_run_r = new BufferedImage[10]; // 跑步（向右）動畫的圖像陣列
	BufferedImage spr_dash_r = null; // 向右衝刺的圖像（未使用）
	BufferedImage spr_dash_l = null; // 向左衝刺的圖像（未使用）
	BufferedImage spr_jump_r = null; // 向右跳躍的圖像
	BufferedImage spr_jump_l = null; // 向左跳躍的圖像
	BufferedImage spr_fall_r = null; // 向右下落的圖像
	BufferedImage spr_fall_l = null; // 向左下落的圖像
	BufferedImage spr_punch_r = null; // 向右揮拳的圖像
	BufferedImage spr_punch_l = null; // 向左揮拳的圖像
	BufferedImage spr_attack_r = null; // 向右揮拳的圖像
	BufferedImage spr_attack_l = null; // 向左揮拳的圖像

	private int hp = 100; // 角色的生命值
	private boolean doubleJump = false; // 是否已經使用過二段跳
	private int dir = -1; // 角色的方向，1代表向左，-1代表向右
	int runAt = 0; // 設置當前跑步動畫的幀數
	
	private String buffMessage;
    private long buffMessageEndTime;
    private Color buffMessageColor;  // 添加顏色變量
	 

	// 構造函數，接收初始位置x和y座標
	Type1b(int x, int y) {
		super(x, y, 10, 10); // 調用父類（Character）的構造函數，設置初始位置和大小
		//private boolean canAttack = true; // 控制是否可以攻擊的變數


		try {
		    // 使用類加載器讀取圖像文件並存儲在對應的變量中
		    sprite_r = ImageIO.read(getClass().getResource("/M_R.png"));
		    sprite_l = ImageIO.read(getClass().getResource("/M_L.png"));

		    spr_run_l[0] = ImageIO.read(getClass().getResource("/M_Run_l_1.png"));
		    spr_run_l[1] = ImageIO.read(getClass().getResource("/M_Run_l_2.png"));
		    spr_run_l[2] = ImageIO.read(getClass().getResource("/M_Run_l_3.png"));
		    spr_run_l[3] = ImageIO.read(getClass().getResource("/M_Run_l_4.png"));
		    spr_run_l[4] = ImageIO.read(getClass().getResource("/M_Run_l_5.png"));
		    spr_run_l[5] = ImageIO.read(getClass().getResource("/M_Run_l_6.png"));
		    spr_run_l[6] = ImageIO.read(getClass().getResource("/M_Run_l_7.png"));
		    spr_run_l[7] = ImageIO.read(getClass().getResource("/M_Run_l_8.png"));
		    spr_run_l[8] = ImageIO.read(getClass().getResource("/M_Run_l_9.png"));
		    spr_run_l[9] = ImageIO.read(getClass().getResource("/M_Run_l_10.png"));

		    spr_run_r[0] = ImageIO.read(getClass().getResource("/M_Run_r_1.png"));
		    spr_run_r[1] = ImageIO.read(getClass().getResource("/M_Run_r_2.png"));
		    spr_run_r[2] = ImageIO.read(getClass().getResource("/M_Run_r_3.png"));
		    spr_run_r[3] = ImageIO.read(getClass().getResource("/M_Run_r_4.png"));
		    spr_run_r[4] = ImageIO.read(getClass().getResource("/M_Run_r_5.png"));
		    spr_run_r[5] = ImageIO.read(getClass().getResource("/M_Run_r_6.png"));
		    spr_run_r[6] = ImageIO.read(getClass().getResource("/M_Run_r_7.png"));
		    spr_run_r[7] = ImageIO.read(getClass().getResource("/M_Run_r_8.png"));
		    spr_run_r[8] = ImageIO.read(getClass().getResource("/M_Run_r_9.png"));
		    spr_run_r[9] = ImageIO.read(getClass().getResource("/M_Run_r_10.png"));

		    spr_dash_r = ImageIO.read(getClass().getResource("/M_Dash_R.png"));
		    spr_dash_l = ImageIO.read(getClass().getResource("/M_Dash_L.png"));

		    spr_jump_r = ImageIO.read(getClass().getResource("/M_Jump_R.png"));
		    spr_jump_l = ImageIO.read(getClass().getResource("/M_Jump_L.png"));

		    spr_fall_r = ImageIO.read(getClass().getResource("/M_Fall_R.png"));
		    spr_fall_l = ImageIO.read(getClass().getResource("/M_Fall_L.png"));

		    spr_punch_l = ImageIO.read(getClass().getResource("/M_Punch_L.png"));
		    spr_punch_r = ImageIO.read(getClass().getResource("/M_Punch_R.png"));
		    
		    spr_attack_l = ImageIO.read(getClass().getResource("/M_Attack_L.png"));
		    spr_attack_r = ImageIO.read(getClass().getResource("/M_Attack_R.png"));

		} catch (Exception ex) {
		    // 如果讀取圖像文件失敗，輸出錯誤信息
		    System.out.println("Cannot Read1");
		    ex.printStackTrace();
		}


		sprite = sprite_l; // 默認將角色圖像設置為面向左邊
		// 角色的方向，-1 代表左，1 代表右，初始設置為 -1（左邊）
		setAttackType(0);//攻擊模式為普攻

		// 設置角色的高度和寬度，使用當前的sprite圖像的大小
		setHeight(sprite.getHeight());
		setWidth(sprite.getWidth());
	}

	public void update() {
		// 更新角色的邏輯，這裡暫時是空的
	}

	public void draw(Graphics g) {
		// 畫出當前的sprite圖像
        g.drawImage(sprite, getX(), getY(), null);

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

	public void repaint(Graphics g) {
		// 這裡空置，可能是預留用於重新繪製角色的畫面
	}

	// 返回角色當前的方向
	public int getDir() {
		return dir;
	}

	// 返回角色的當前生命值
	public int getHP() {
		return hp;
	}

	// 增加或減少角色的生命值
	public void addHP(int amount) {
        this.hp += amount;
        if (this.hp > 100) {
            this.hp = 100; // 假设最大生命值为100
        }
	}
	
	
	
//----------------------------------------------速度及方向-----------------------------------------------------------------	
	
	
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


	// 改變角色的方向
	public void changeDir(double direction) {
		if (direction < 0) {
			if (sprite == sprite_r)
				sprite = sprite_l;
			else if (sprite == spr_dash_r)
				sprite = spr_dash_l;
			else if (sprite == spr_jump_r)
				sprite = spr_jump_l;
			else if (sprite == spr_fall_r)
				sprite = spr_fall_l;
		} else {
			if (sprite == sprite_l)
				sprite = sprite_r;
			else if (sprite == spr_dash_l)
				sprite = spr_dash_r;
			else if (sprite == spr_jump_l)
				sprite = spr_jump_r;
			else if (sprite == spr_fall_l)
				sprite = spr_fall_r;
		}
	}

	public void stopped() {
		setVX(0); // 停止角色移動
	}
	
//----------------------------------------------跳躍與掉落--------------------------------------------------------
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
	//---------------------------------------------------------------------------------------------------------------
	
	
	public void dash(int xShift) {
		if (getDir() == 1) {
			sprite = spr_dash_r; // 設置為向右衝刺
		} else {
			sprite = spr_dash_l; // 設置為向左衝刺

		}

		setX(getX() + xShift * getDir()); // 根據當前方向和xShift改變角色的橫向位置
	}
	
	
//----------------------------------------------圖像-----------------------------------------------------------------
	
	public void move(double time, boolean movedX, boolean movedY, boolean rightcol, boolean downcol) {
		int prevH = sprite.getHeight(); // 記錄當前sprite圖像的高度，用於調整位置
		int prevW = sprite.getWidth(); // 記錄當前sprite圖像的寬度，用於調整位置

		boolean hadPunchedL = false; // 標記是否剛剛揮拳

		if (sprite == spr_punch_l)
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

		if (!movedX)
			updateX(time); // 如果未移動X軸，根據時間更新X位置
		if (!movedY)
			updateY(time); // 如果未移動Y軸，根據時間更新Y位置

		setHeight(sprite.getHeight()); // 根據當前sprite更新角色的高度
		setWidth(sprite.getWidth()); // 根據當前sprite更新角色的寬度

		if (downcol == true)
			setY(getY() + (prevH - getHeight())); // 如果碰撞檢測到下降，調整Y座標

		if (rightcol == true || (getPunch() == true && getDir() == -1) || (hadPunchedL == true && getPunch() == false))
			setX(getX() + (prevW - getWidth())); // 根據碰撞和揮拳狀態調整X座標
		

	}

}