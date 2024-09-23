package game.battle;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.awt.Graphics;
import java.util.Iterator;


public abstract class Character {
	// 角色的左上角 x 座標
	private int x;		
	// 角色的左上角 y 座標
	private int y;		
	// 角色的寬度
	private int width;	
	// 角色的高度
	private int height;	
	// 角色的水平速度（x 方向速度）
	private int vx = 0;	
	// 角色的垂直速度（y 方向速度）
	private int vy = 0;	
	// 角色是否正在出拳
	private boolean punch;	
	// 角色可否攻擊
		private boolean canAttack;
		private int attacktype=0;// 默認攻擊類型
		
	// 角色是否正在下落
	private boolean falling;
	
	private List<Bullet> bullets = new ArrayList<>(); // 子彈列表
	// 定義炸彈列表
	private List<Bomb01> bombs = new ArrayList<>();
//	private Character target;  // 目標角色
	
//	private String buffMessage; // 保存 Buff 提示信息
//    private long buffMessageEndTime; // 提示信息的結束顯示時間
	
	


	// 構造函數，初始化角色的坐標和尺寸
	public Character(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		// 在構造函數中初始化 bullets 列表
		bullets = new ArrayList<>();
		setCanAttack(true);
		this.bombs = new ArrayList<>();
	}

	// 檢查角色是否與另一個角色 `e` 發生碰撞，`checks` 指定是水平（'h'）還是垂直（'v'）方向
	public char isColliding(Character e, char checks) {
		char rv = 'z';

		// 計算當前角色的邊界
		int leftSide = getX();
		int rightSide = getX() + getWidth();
		int top = getY();
		int bottom = getY() + getHeight();

		// 計算另一個角色 `e` 的邊界
		int eLeftSide = e.getX();
		int eRightSide = e.getX() + e.getWidth();
		int eTop = e.getY();
		int eBottom = e.getY() + e.getHeight();

		rv = 'z'; // 初始設置碰撞結果為 'z' 表示沒有碰撞

		// 水平碰撞檢查
		if(checks == 'h') {
			// 如果當前角色的右側與另一個角色的左側重疊，且在 y 範圍內
			if(rightSide >= eLeftSide && leftSide < eLeftSide) {
				if((top >= eTop && top <= eBottom) || (bottom > eTop && bottom <= eBottom) || (eTop >= top && eBottom <= bottom)) {
					rv = 'r'; // 右邊碰撞
				}
			}

			// 如果當前角色的左側與另一個角色的右側重疊，且在 y 範圍內
			if(leftSide <= eRightSide && rightSide > eRightSide) {
				if((top >= eTop && top <= eBottom) || (bottom > eTop && bottom <= eBottom) || (eTop >= top && eBottom <= bottom)) {
					rv = 'l'; // 左邊碰撞
				}
			}
		}

		// 垂直碰撞檢查
		if(checks == 'v') {
			// 如果當前角色的底部與另一個角色的頂部重疊，且在 x 範圍內
			if(bottom >= eTop && top < eTop) {
				if((rightSide > eLeftSide && rightSide < eRightSide) || (leftSide > eLeftSide && leftSide < eRightSide) || (eRightSide > rightSide && eLeftSide < leftSide)) {
					rv = 'd'; // 下方碰撞
				}
			}

			// 如果當前角色的頂部與另一個角色的底部重疊，且在 x 範圍內
			if(top <= eBottom && bottom > eBottom) {
				if((rightSide > eLeftSide && rightSide < eRightSide) || (leftSide > eLeftSide && rightSide < eRightSide)
					|| (eRightSide > rightSide && eLeftSide < leftSide)) {
					rv = 'u'; // 上方碰撞
				}
			}
		}

		return rv; // 返回碰撞結果
	}

	// 檢查當前角色是否與角色 `e` 碰撞，並在發生碰撞時移動角色以避免碰撞
	public char checkCollision(double timestep, Character e, boolean collide) {
		char rv = 'z';
		char rvTemp = 'z';

		// 更新當前角色和另一個角色的位置
		setX(getX() + (int)(vx * timestep));
		setY(getY() + (int)(vy * timestep));

		e.setX(e.getX() + (int)(e.getVX() * timestep));
		e.setY(e.getY() + (int)(e.getVY() * timestep));

		// 水平碰撞檢查
		rvTemp = isColliding(e, 'h');

		if(rvTemp != 'z') {	// 如果發生水平碰撞
			setX(getX() - (int)(vx * timestep)); // 恢復位置

			if(collide == true)
				collide(timestep, e, rvTemp); // 處理碰撞
		}

		// 垂直碰撞檢查
		rv = isColliding(e, 'v');

		if(rv != 'z') { // 如果發生垂直碰撞
			setY(getY() - (int)(vy * timestep)); // 恢復位置

			if(collide == true)
				collide(timestep, e, rv); // 處理碰撞
		}

		if(rv == 'z') { // 如果沒有發生垂直碰撞，使用水平碰撞結果
			setY(getY() - (int)(vy * timestep));
			setX(getX() - (int)(vx * timestep));
			rv = rvTemp;
		}

		// 恢復另一個角色的位置
		e.setX(e.getX() - (int)(e.getVX() * timestep));
		e.setY(e.getY() - (int)(e.getVY() * timestep));

		return rv; // 返回碰撞結果
	}

	// 處理角色之間的碰撞反應
	public void collide (double timestep, Character e, int rv) {
		switch(rv) {
			case 'r':	
				setX(e.getX() - getWidth()); // 右邊碰撞，移動角色以避免重疊
			break;

			case 'l':	
				setX(e.getX() + e.getWidth()); // 左邊碰撞，移動角色以避免重疊
			break;

			case 'd':	
				setY(e.getY() - getHeight()); // 下方碰撞，移動角色以避免重疊
			break;

			case 'u':	
				setY(e.getY() + e.getHeight()); // 上方碰撞，移動角色以避免重疊
			break;
		}
	}

	// 設置角色的 x 坐標
	public void setX(int x) {
		this.x = x;
	}

	// 設置角色的 y 坐標
	public void setY(int y) {
		this.y = y;
	}

	// 獲取角色的 x 坐標
	public int getX() {
		return x;
	}

	// 獲取角色的 y 坐標
	public int getY() {
		return y;
	}

	// 設置角色的寬度
	public void setWidth(int width) {
		this.width = width;
	}

	// 設置角色的高度
	public void setHeight(int height) {
		this.height = height;
	}

	// 獲取角色的寬度
	public int getWidth() {
		return width;
	}

	// 獲取角色的高度
	public int getHeight() {
		return height;
	}

	// 設置角色的水平速度
	public void setVX(int vx) {
		this.vx = vx;
	}

	// 設置角色的垂直速度
	public void setVY(int vy) {
		this.vy = vy;
	}

	// 獲取角色的水平速度
	public int getVX() {
		return vx;
	}

	// 獲取角色的垂直速度
	public int getVY() {
		return vy;
	}

	// 更新角色的 x 坐標
	public void updateX(double time){
		x += vx * time;
	}

	// 更新角色的 y 坐標
	public void updateY(double time){
		y += vy * time;
	}

	// 設置角色是否正在出拳
	public void setPunch(boolean punch) {
		this.punch = punch;
	}

	// 獲取角色是否正在出拳
	public boolean getPunch() {
		return punch;
	}
	
	public void setCanAttack(boolean canAttack) {
	    this.canAttack = canAttack;
	}
	
	public boolean canAttack() {
	    return canAttack;
	}
	
	public void setAttackType(int attacktype) {
	    this.attacktype = attacktype;
	}
	
	public int getAttackType() {
	    return attacktype;
	}


	// 設置角色是否正在下落
	public void setFalling(boolean falling) {
		this.falling = falling;
	}

	// 獲取角色是否正在下落
	public boolean getFalling() {
		return falling;
	}

	// 獲取角色的邊界矩形，用於碰撞檢測
	public Rectangle getBounds() {
	    return new Rectangle(x, y, width, height);
	}
	
	
	//--------------------------------------BUFF提示顯示------------------------------------------------------------
	
//	// 當角色獲得 Buff 時調用此方法來顯示提示
//    public void showBuffMessage(String message, int durationInMillis) {
//        this.buffMessage = message;
//        this.buffMessageEndTime = System.currentTimeMillis() + durationInMillis;
//    }
//
//    // 在繪製角色時顯示提示信息
//    public void draw(Graphics g) {
//        // 繪製角色自身的邏輯
//        g.drawImage(characterImage, this.x, this.y, null);
//
//        // 如果當前時間小於提示信息的結束時間，顯示提示信息
//        if (System.currentTimeMillis() < buffMessageEndTime) {
//            g.setColor(Color.YELLOW); // 設置提示信息的顏色
//            g.setFont(new Font("Arial", Font.BOLD, 12)); // 設置提示信息的字體
//            g.drawString(buffMessage, this.x, this.y - 10); // 在角色頭上顯示提示信息
//        }
//    }
//}
	//--------------------------------------多重射擊BUFF------------------------------------------------------------
	// 設置一個布林變數，標記攻擊BUFF是否啟動
	
	public boolean attackBuffActive = false; 
	// 設置一個變數，用於記錄攻擊Buff結束的時間
	public long attackBuffEndTime;

	// 增加攻擊Buff，並設置持續時間
	public void attackBuff(int durationInSeconds) {
		
	    this.attackBuffActive = true; // 啟動多重射擊
	   
	    this.attackBuffEndTime = System.currentTimeMillis() + (durationInSeconds*1000);
	}

	// 檢查攻擊Buff是否仍然有效
	public void checkMultiShotBuff() {
		long currentTime = System.currentTimeMillis();
		if (attackBuffActive && currentTime > attackBuffEndTime) {
			attackBuffActive = false; // 如果多重射擊的持續時間已過，將其設置為無效
	        setAttackType(0);
	    }
	}

	
	// 獲取子彈列表
	public List<Bullet> getBullets() {
	    return bullets;
	}
	
	
	public int direction = 1; // 默認方向為右
	// 在 Character 類中處理子彈發射的邏輯
	    public void shootBullets() {
	        // 检查角色的方向
	        if (this instanceof Type1b) {
	            direction = ((Type1b) this).getDir(); // 使用 Type1b 的 getDir() 方法获取方向
	        } else if (this instanceof Type2) {
	            direction = ((Type2) this).getDir(); // 使用 Type2 的 getDir() 方法获取方向
	        }
	        
	        int PlayerType = (this instanceof Type1b) ? 1 : 2;
	        
	        // 检查 attackType，如果为 1，则发射多重子弹
	        if (getAttackType() == 1) {
	            for (int i = -2; i <= 2; i++) { // 发射五颗子弹，角度从 -30° 到 +30°
	                double angle = Math.toRadians(i * 15); // 每颗子弹间隔 15 度
	                int bulletX = direction == 1 ? this.x + this.width : this.x - 10; // 根据方向设置子弹的 X 位置
	                int bulletY = this.y + (i * 10); // 子弹的 Y 位置根据角度变化

	                // 创建新的子弹对象
	                Bullet bullet = new Bullet(bulletX, bulletY, PlayerType,direction, angle);
	                bullets.add(bullet); // 将子弹添加到子弹列表中
	            }
	        }
	    }

	 // 更新子彈的狀態並移除過期的子彈
	    public void updateBullets(Type1b p1, Type2 p2) {
	        Iterator<Bullet> iterator = bullets.iterator();
	        while (iterator.hasNext()) {
	            Bullet bullet = iterator.next();
	            bullet.update(p1, p2);
	            if (bullet.shouldBeRemoved()) {
	                iterator.remove(); // 移除已標記為刪除的子彈
	            }
	        }
	    }


	    // 绘制角色的子弹
	    public void drawBullets(Graphics g) {
	        for (Bullet bullet : bullets) {
	            bullet.drawBullet(g);
	        
	    

	    }
	}

	

	
	//--------------------------------------炸彈BUFF------------------------------------------------------------

	public void boombBullets(Character player1, Character player2) {
	    if (getAttackType() == 2) { // 檢查是否激活炸彈攻擊
	        int bombX = this.getX() + this.getWidth() / 2; // 計算炸彈的 X 位置（角色的中間）
	        int bombY = this.getY(); // 炸彈的 Y 位置與角色相同
	        int direction = 1; // 默認方向為右

	        // 偵測角色面向方向，設置投擲方向
	        if (this instanceof Type1b) {
	            direction = ((Type1b) this).getDir(); // 如果是 Type1b，使用 getDir() 方法來判斷面向方向
	        } else if (this instanceof Type2) {
	            direction = ((Type2) this).getDir(); // 如果是 Type2，使用 getDir() 方法來判斷面向方向
	        }
	        
	     // 调整炸弹生成的位置，假设往角色右侧 30 像素，往上 10 像素
	        bombX += (direction == 1) ? 0 : -30;
	       

	        Bomb01 bomb = new Bomb01(bombX, bombY, direction, player1, player2); // 創建新的炸彈物件，帶上方向和玩家
	        bomb.throwBomb(); // 投擲炸彈
	        bombs.add(bomb); // 將炸彈加入到炸彈列表中
	    }
	}


    // 更新炸彈狀態的方法
    public void updateBombs(Character player1, Character player2) {
        for (Bomb01 bomb : bombs) { // 遍歷所有炸彈
            bomb.update(); // 更新炸彈狀態
            if (bomb.hasExploded()) { // 如果炸彈已經爆炸
                bomb.explode(); // 檢查並處理爆炸效果
            }
        }
        bombs.removeIf(Bomb01::hasExploded); // 移除已經爆炸的炸彈
    }

    // 繪製炸彈的方法
    public void drawBombs(Graphics g) {
        for (Bomb01 bomb : bombs) { // 遍歷所有炸彈
            bomb.draw(g); // 繪製炸彈或爆炸動畫
        }
    
	}


}
	