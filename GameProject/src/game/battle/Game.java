package game.battle;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.*;
import java.util.Iterator;
//import java.util.*;
import java.io.*;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioInputStream;
//import java.io.File;



public class Game {
	private BuffManager buffManager = new BuffManager(); // 初始化 BuffManager
	// 儲存遊戲狀態的數組，例如位置、速度、HP等
	private int[] stats = new int[17];
	// 用於碰撞檢測的字符變數，'z' 表示沒有碰撞
	private char col = 'z'; // 角色1的碰撞狀態
	private char col2 = 'z'; // 角色2的碰撞狀態
	// 用於表示角色是否正在下落的布林變數
	private boolean falling = false;
	private boolean falling2 = false;
	// 背景圖片
	private BufferedImage bg = null,player1RightImage =null,player2LeftImage =null,pkImage=null;
	// 在 Game 類中定義 BUFF 圖示
	//private BufferedImage speedBuffIcon;
	private BufferedImage attackBuffIcon1; // 對應 setAttackType(1)
	private BufferedImage attackBuffIcon2; // 對應 setAttackType(2)

	// 處理遊戲邏輯的處理器物件
	Processor proc;
	int bulletsToRemoveCount = 0;//偵測子彈移除數量

	
	// 兩個玩家角色物件
	private Type1b player1 = new Type1b(900, 530); // 初始化角色1，位置(20, 120)
	private Type2 player2 = new Type2(10,192); // 初始化角色2，位置(50, 120)
	// 場景部分的數組，用於存放遊戲中的平台
	private Stage[] stageParts = new Stage[10];

	
	// 角色物件數組，目前只包含一個角色
	Character[] elements = new Character[1];
	
	private boolean gameOver = false; // 添加一个标志位来跟踪游戏是否结束
	private Character winner = null; // 存储胜利者
	
	private Clip backgroundMusic; // 用於存儲背景音樂的Clip
	


	private void playBackgroundMusic(String filePath) {
	    try {
	        // 使用資源加載器來加載音頻流
	        InputStream audioSrc = getClass().getResourceAsStream("/" + filePath);
	        InputStream bufferedIn = new BufferedInputStream(audioSrc);
	        AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);
	        
	        backgroundMusic = AudioSystem.getClip();
	        backgroundMusic.open(audioStream);
	        backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
	        backgroundMusic.start();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}


	// Game類的建構函數，用於初始化處理器並加載背景圖片
	public Game(Processor proc) {
		this.proc = proc;

		try {
			// 使用類加載器加載背景圖片
			bg = ImageIO.read(getClass().getResource("/stage2.png"));
			// 加載玩家1面向右邊的圖像
	        player1RightImage = ImageIO.read(getClass().getResource("/M_R.png"));

	        // 加載玩家2面向左邊的圖像
	        player2LeftImage = ImageIO.read(getClass().getResource("/S_L.png"));
	        //speedBuffIcon = ImageIO.read(getClass().getResource("/Buff_speed.png"));
	        attackBuffIcon1 = ImageIO.read(getClass().getResource("/Buff_mulattack.png"));
	        attackBuffIcon2 = ImageIO.read(getClass().getResource("/Buff_bigattack.png"));
	        // 加載PK圖像
	        pkImage = ImageIO.read(getClass().getResource("/PK.png"));
		} catch (Exception ex) {
			System.out.println("Cannot ReadG"); // 如果加載失敗，輸出錯誤信息
		}
		
		

		// 初始化場景中的各個平台位置和大小
		stageParts[0] = new Stage(-3, 0, 5, 600);
		stageParts[1] = new Stage(940, 0, 10, 600);
		stageParts[2] = new Stage(0, 222, 174, 24);
		//stageParts[3] = new Stage(156, 237, 216, 30);
		stageParts[4] = new Stage(252, 362, 219, 25);
		stageParts[5] = new Stage(435, 509, 215, 23);
		stageParts[6] = new Stage(58, 469, 215, 23);
		//stageParts[7] = new Stage(545, 176, 213, 22);
		stageParts[8] = new Stage(542, 309, 216, 20);
		stageParts[9] = new Stage(790, 433, 170, 19);
		stageParts[7] = new Stage(865, 175, 75, 19);
		stageParts[3] = new Stage(0, 585, 960, 15);
		
		// 加載背景音樂並播放
	    playBackgroundMusic("backgroundmusic.wav");
	    
	 // 設置目標
//        if (player1 instanceof Type1b) {
//            player1.setTarget(player2); // 設置 player2 為 player1 的目標
//        }
//
//        if (player2 instanceof Type2) {
//            player2.setTarget(player1); // 設置 player1 為 player2 的目標
//        }
//
	}
	
	

	// 移動遊戲中的角色並處理碰撞檢測
	public void move(double timestep) {
		if(!gameOver) {
		boolean rightcol = false;
		boolean movedY = false;
		boolean movedX = false;
		boolean rightcol2 = false;
		boolean movedY2 = false;
		boolean movedX2 = false;

		char playerCol = 'z';

		falling = true; // 預設角色1處於下落狀態
		falling2 = true; // 預設角色2處於下落狀態

		// 更新角色1的狀態（位置、寬高、速度、HP）
		stats[3] = player1.getX();
		stats[4] = player1.getY();
		stats[5] = player1.getWidth();
		stats[6] = player1.getHeight();
		stats[11] = player1.getVX();
		stats[12] = player1.getVY();
		stats[15] = player1.getHP();
		// 更新角色的 Buff 狀態
	    player1.checkMultiShotBuff();

		// 更新角色2的狀態（位置、寬高、速度、HP）
		stats[7] = player2.getX();
		stats[8] = player2.getY();
		stats[9] = player2.getWidth();
		stats[10] = player2.getHeight();
		stats[13] = player2.getVX();
		stats[14] = player2.getVY();
		stats[16] = player2.getHP();
		   // 更新角色的 Buff 狀態
	    player2.checkMultiShotBuff();
		
		 buffManager.updateBuffs();
	        for (BuffIcon buff : buffManager.getBuffs()) {
	            buff.checkCollision(player1);
	            buff.checkCollision(player2);
	        }


		// 檢查角色1和角色2與場景中的各個平台是否發生碰撞
		for (int i = 0; i < stageParts.length; i++) {
			col = player1.checkCollision(timestep, stageParts[i], true);
			col2 = player2.checkCollision(timestep, stageParts[i], true);

			// 根據碰撞結果更新角色1的狀態
			if (col == 'd') { // 角色1在平台上
				falling = false;
				movedY = true;
			} else if (col == 'r') { // 角色1與右側平台發生碰撞
				rightcol = true;
				movedX = true;
			} else if (col == 'l') { // 角色1與左側平台發生碰撞
				movedX = true;
			} else if (col == 'u') { // 角色1撞到上方平台
				movedY = true;
				player1.setVY(1); // 角色1的垂直速度設為1，表示開始下落
				player1.fall(); // 觸發下落邏輯
			}

			// 根據碰撞結果更新角色2的狀態
			if (col2 == 'd') { // 角色2在平台上
				falling2 = false;
				movedY2 = true;
			} else if (col2 == 'r') { // 角色2與右側平台發生碰撞
				rightcol2 = true;
				movedX2 = true;
			} else if (col2 == 'l') { // 角色2與左側平台發生碰撞
				movedX2 = true;
			} else if (col2 == 'u') { // 角色2撞到上方平台
				movedY2 = true;
				player2.setVY(1); // 角色2的垂直速度設為1，表示開始下落
				player2.fall(); // 觸發下落邏輯
			}
		}

		// 檢查角色1和角色2之間是否發生碰撞
		playerCol = player2.checkCollision(timestep, player1, false);

		// 處理角色1的攻擊動作
		if (player1.getPunch() == true) {  // 如果角色1正在攻擊
		    if (player2.getPunch() == true) {  // 如果角色2也正在攻擊
		        if (playerCol != 'z') {  // 如果兩個角色發生碰撞
		            player2.collide(timestep, player1, playerCol);  // 處理角色2的碰撞反應
		            movedX2 = true;  // 設定角色2的水平移動標記為真
		            movedX = true;   // 設定角色1的水平移動標記為真
		        }
		    } else if (playerCol != 'z') {  // 如果只有角色1在攻擊且兩個角色發生碰撞
		        player2.collide(timestep, player1, playerCol);  // 處理角色2的碰撞反應
		        movedX2 = true;  // 設定角色2的水平移動標記為真
		        player2.addHP(-5);  // 角色2受到傷害，扣除5點生命值
		        player1.setPunch(false);
		       
		    }
		} else if (player2.getPunch() == true) {  // 如果角色1沒有攻擊但角色2正在攻擊
		    // 處理角色2的攻擊動作
		    playerCol = player1.checkCollision(timestep, player2, false);  // 檢查角色2和角色1是否發生碰撞

		    if (playerCol != 'z') {  // 如果角色2與角色1發生碰撞
		        if (player2.getDir() == 1)  // 如果角色2面向右側
		            playerCol = 'l';  // 設定碰撞方向為左
		        else  // 如果角色2面向左側
		            playerCol = 'r';  // 設定碰撞方向為右

		        player1.collide(timestep, player2, playerCol);  // 處理角色1的碰撞反應
		        movedX = true;  // 設定角色1的水平移動標記為真
		        player1.addHP(-5);  // 角色1受到傷害，扣除5點生命值
		        player2.setPunch(false);
		    }
		}


		// 更新角色1的下落狀態
		if (falling == true) {
			player1.setFalling(falling);
			player1.fall();
		} else if (falling == false) {
			player1.setVY(0);
			player1.setFalling(false);
		}

		// 更新角色2的下落狀態
		if (falling2 == true) {
			player2.setFalling(falling2);
			player2.fall();
		} else if (falling2 == false) {
			player2.setVY(0);
			player2.setFalling(false);
		}

		// 移動角色1和角色2，並根據碰撞結果進行調整
		player1.move(timestep, movedX, movedY, rightcol, !falling);
		player2.move(timestep, movedX2, movedY2, rightcol2, !falling2);
		checkGameOver();
		}
//--------------------------------------多重射擊BUFF------------------------------------------------------------		
		for (Bullet bullet : player1.getBullets()) {
	        bullet.update(player1, player2); // 更新子彈的狀態和位置
	    }

	    // 更新玩家2發射的所有子彈
	    for (Bullet bullet : player2.getBullets()) {
	        bullet.update(player1, player2); // 更新子彈的狀態和位置
	    }
	    
	 // 檢查玩家1的 MultiShot Buff 狀態
	    player1.checkMultiShotBuff();

	    // 檢查玩家2的 MultiShot Buff 狀態
	    player2.checkMultiShotBuff();
	    player1.updateBombs(player1, player2);
        player2.updateBombs(player1, player2);
	}  
	
	// Game.java 中的代码
	// Game.java 中的代码
	public void updateAndDrawBullets(Graphics g) {
	    // 使用单个迭代来处理 player1 的更新和绘制
	    Iterator<Bullet> iterator1 = player1.getBullets().iterator();
	    while (iterator1.hasNext()) {
	        Bullet bullet = iterator1.next();
	        bullet.update(player1, player2); // 更新子弹状态和位置

	        if (bullet.shouldBeRemoved()) {
	            iterator1.remove(); // 移除已标记为删除的子弹
	        } else {
	            bullet.drawBullet(g); // 绘制子弹
	        }
	    }

	    // 使用单个迭代来处理 player2 的更新和绘制
	    Iterator<Bullet> iterator2 = player2.getBullets().iterator();
	    while (iterator2.hasNext()) {
	        Bullet bullet = iterator2.next();
	        bullet.update(player1, player2); // 更新子弹状态和位置

	        if (bullet.shouldBeRemoved()) {
	            iterator2.remove(); // 移除已标记为删除的子弹
	        } else {
	            bullet.drawBullet(g); // 绘制子弹
	        }
	    }
	}

	
//--------------------------------------多重射擊BUFF------------------------------------------------------------
	
//	public void update() {
//	    player1.checkMultiShotBuff();
//	    player2.checkMultiShotBuff();
//
//	    for (Bullet bullet : bullets) {
//	        bullet.update();
//	        if (bullet.checkCollision(player2)) {
//	            player2.takeDamage(bullet.getDamage());
//	        }
//	        // 检查子弹是否出界并移除
//	    }
//	}

	private void checkGameOver() {
	    if (player1.getHP() <= 0) {
	        gameOver = true;
	        winner = player2;
	        stopBackgroundMusic(); // 停止背景音樂
	    } else if (player2.getHP() <= 0) {
	        gameOver = true;
	        winner = player1;
	        stopBackgroundMusic(); // 停止背景音樂
	    }
	}
	// 停止背景音樂的方法
	private void stopBackgroundMusic() {
	    if (backgroundMusic != null && backgroundMusic.isRunning()) {
	        backgroundMusic.stop();
	    }
	}

	// 繪製遊戲畫面，包括背景、角色和平台
	public void display(Graphics g, Dimension d) {
		buffManager.updateBuffs();

		 if (gameOver) {
		        // 显示黑色背景
		        g.setColor(Color.BLACK);
		        g.fillRect(0, 0, d.width, d.height);

		        // 显示胜利者的图像在屏幕中心
		        BufferedImage winnerImage = winner == player1 ? player1RightImage : player2LeftImage;
		        g.drawImage(winnerImage, d.width / 2 - 50, d.height / 2 - 50, 100, 100, null);

		        // 显示"YOU ARE THE WINNER!"文本
		        g.setColor(Color.WHITE);
		        g.setFont(new Font("Arial", Font.BOLD, 36));
		        g.drawString("YOU ARE THE WINNER!", d.width / 2 - 200, d.height / 2 + 80);
		    } else {
		// 繪製背景
		g.drawImage(bg, 0, 0, null);

		// 繪製角色1和角色2
		player1.draw(g);
		player2.draw(g);

		// 繪製所有平台
		for (int i = 0; i < stageParts.length; i++) {
			stageParts[i].draw(g);
		}	
	
		
		// 繪製黑色矩形作為平台
//	    g.setColor(Color.BLACK);
//	    for (int i = 0; i < stageParts.length; i++) {
//	        g.fillRect(stageParts[i].getX(), stageParts[i].getY(), 
//	                   stageParts[i].getWidth(), stageParts[i].getHeight());
//	    }
	    
	    
	 // 绘制两个玩家的血条
	    int p1HP = player1.getHP();
	    int p2HP = player2.getHP();

	    // 假设最大血量是100，可以根据需要调整
	    int maxHP = 100;

	    // 计算血条的长度
	    int p1HPWidth = (int) ((p1HP / (double) maxHP) * 200);
	    int p2HPWidth = (int) ((p2HP / (double) maxHP) * 200);

	    // 设置血条的位置和尺寸
	    int barHeight = 20;
	    int centerX = d.width / 2;
	    int barY = 30; // 血条距离顶部的高度
	    
	    if(player1.getAttackType()!=0) {//繪製BUFFICON
	    	if(player1.getAttackType()==1) {
	    		g.drawImage(attackBuffIcon1, 405, 65, 25, 25, null);	
	    	}
	    	else if(player1.getAttackType()==2) {
	    		g.drawImage(attackBuffIcon2, 405, 65, 25, 25, null);	
	    	}
	    }
	    
	    if(player2.getAttackType()!=0) {//繪製BUFFICON
	    	if(player2.getAttackType()==1) {
	    		g.drawImage(attackBuffIcon1, 500, 65, 25, 25, null);
	    		//g.drawString("ATTACKBuff Remaining: " + remainingTimeInSeconds + "s", 10, 140);
	    	}
	    	else if(player2.getAttackType()==2) {
	    		g.drawImage(attackBuffIcon2, 500, 65, 25, 25, null);	
	    	}
	    }

	    // 绘制玩家1的血条（红色）
	    g.setColor(Color.BLUE);
	    g.fillRect(centerX - 260 + (200 - p1HPWidth), barY, p1HPWidth, barHeight);
	    g.setColor(Color.BLACK);
	    g.drawRect(centerX - 260, barY, 200, barHeight);

	    // 绘制玩家2的血条（蓝色）
	    g.setColor(Color.RED);
	    g.fillRect(centerX + 40, barY, p2HPWidth, barHeight);
	    g.setColor(Color.BLACK);
	    g.drawRect(centerX + 40, barY, 200, barHeight);
	    
	 // 在兩個血條中間繪製玩家1的圖像、PK圖像和玩家2的圖像
	    g.drawImage(player1RightImage, centerX - 70, barY - 10, 33, 36, null);
	    g.drawImage(pkImage, centerX - 25, barY - 10, 40, 40, null);
	    g.drawImage(player2LeftImage, centerX + 20, barY - 10, 33, 36, null);

	    // 绘制所有 BuffIcon
	    buffManager.drawBuffs(g);
	    
//	    player1.drawBullets(g); // 绘制 player1 的所有子弹
//	    player2.drawBullets(g); // 绘制 player2 的所有子弹

	    
	    player1.drawBombs(g);
	    player2.drawBombs(g);
	    
//--------------------------------------子彈繪製------------------------------------------------------------
	 // 获取 player1 和 player2 的子弹列表并绘制
	 // 繪製所有子彈，並移除已播放完爆炸動畫的子彈
	    updateAndDrawBullets(g);
	    
	    if (player1.attackBuffActive) {
	        long remainingTimeInMillis = player1.attackBuffEndTime - System.currentTimeMillis();
	        long remainingTimeInSeconds = remainingTimeInMillis / 1000;
	        g.drawString(""+ remainingTimeInSeconds , 415, 100);
	        }

	    
	    if (player2.attackBuffActive) {
	        long remainingTimeInMillis = player2.attackBuffEndTime - System.currentTimeMillis();
	        long remainingTimeInSeconds = remainingTimeInMillis / 1000;
	        g.drawString(""+ remainingTimeInSeconds , 510, 100);
	        }
	    
	    
	    //int bulletsToRemoveCount = 0;
	    
	    for (Bullet bullet : player1.getBullets()) {
	        if (bullet.shouldBeRemoved()) {
	            bulletsToRemoveCount++;
	        }
	    }
	    // 繪製玩家1的狀態信息
//        g.setColor(Color.WHITE);
//        g.setFont(new Font("Arial", Font.PLAIN, 14));
//        g.drawString("Player1 Punch: " + player1.getPunch(), 10, 60);
//        g.drawString("Player1 Attack: " + player1.canAttack(), 10, 80);
//        g.drawString("getAttackType: " + player1.getAttackType(), 10, 100);
//        g.drawString("multiShotActive: " + player1.attackBuffActive, 10, 120);
//        g.drawString("character.direction: " + player1.direction , 10, 160);
//        g.drawString("character.direction: " + player1.direction , 10, 180);
//        g.drawString("num of bullet remove: " + bulletsToRemoveCount , 10, 200);
        //g.drawString("System.currentTimeMillis(): " + player1.multiShotEndTime , 10, 180);
	    	
		}
	}

//--------------------------------------鍵盤事件------------------------------------------------------------

	// 處理滑鼠按下事件
	public void process(int button, Point p) {
		stats[0] = button; // 記錄按下的按鈕
		stats[1] = p.x; // 記錄滑鼠點擊的x座標
		stats[2] = p.y; // 記錄滑鼠點擊的y座標
	}

	// 處理鍵盤按下事件（數字按鍵）
	public void process(int key) {
		if (key == KeyEvent.VK_RIGHT) {
			if (col != 'r') {
				player1.changeV(80); // 向右移動角色1
			}
		} else if (key == KeyEvent.VK_LEFT) {
			if (col != 'l') {
				player1.changeV(-80); // 向左移動角色1
			}
		} else if (key == KeyEvent.VK_UP) {
			if (col != 'u') {
				player1.jump(); // 讓角色1跳躍
			}
		} else if (key == KeyEvent.VK_DOWN && player1.canAttack()) {
			player1.setPunch(true);
	        player1.setCanAttack(false); // 攻擊後設置為不可攻擊，直到釋放按鍵
	   
	        

	     
	    	  }
	      }
	      
	  
	     

	// 處理鍵盤按下事件（字符按鍵）
	public void process(char ch) {
		if (ch == 'd') {
			player2.changeV(80); // 向右移動角色2
		} else if (ch == 'a') {
			player2.changeV(-80); // 向左移動角色2
		} else if (ch == 'w') {
			player2.jump(); // 讓角色2跳躍
		} else if (ch == 's'&& player2.canAttack()) {
			player2.setPunch(true); // 讓角色2進行攻擊
			player2.setCanAttack(false); // 攻擊後設置為不可攻擊，直到釋放按鍵
		}
	}

	// 處理鍵盤釋放事件（數字按鍵）
	public void processR(int key) {
		if (key == KeyEvent.VK_RIGHT) {
			player1.stopped(); // 停止角色1的移動
		}
		if (key == KeyEvent.VK_LEFT) {
			player1.stopped(); // 停止角色1的移動
		}
		if (key == KeyEvent.VK_UP) {
			player1.setFalling(true); // 設定角色1為下落狀態
		}
		if (key == KeyEvent.VK_DOWN) {
			player1.setPunch(false); // 取消角色1的攻擊
			player1.setCanAttack(true); // 釋放按鍵後允許再次攻擊
			player1.shootBullets();
			// 假設 player1 和 player2 是在 Game 類中已經定義的角色對象
			player1.boombBullets(player1, player2);

			
			
		}
	}

	// 處理鍵盤釋放事件（字符按鍵）
	public void processR(char ch) {
		if (ch == 'a') {
			player2.stopped(); // 停止角色2的移動
		}
		if (ch == 'd') {
			player2.stopped(); // 停止角色2的移動
		}
		if (ch == 's') {
			player2.setPunch(false); // 取消角色2的攻擊
			player2.setCanAttack(true); // 釋放按鍵後允許再次攻擊
			player2.shootBullets();
			// 假設 player1 和 player2 是在 Game 類中已經定義的角色對象
			player2.boombBullets(player1, player2);
		}
		if (ch == 'w') {
			player2.setFalling(true); // 設定角色2為下落狀態
		}
	}

	// 返回當前遊戲狀態的字串數組
	public String[] getStats() {
		String[] message = new String[] { "Mouse " + stats[0] + " clicked at " + stats[1] + ", " + stats[2],
				"Player 1 at (" + stats[3] + " - " + (stats[3] + stats[5]) + " , " + stats[4] + " - "
						+ (stats[4] + stats[6]) + " )" + " Vx " + stats[11] + " Vy " + stats[12] + " HP : " + stats[15],
				"Player 2 at (" + stats[7] + " - " + (stats[7] + stats[9]) + " , " + stats[8] + " - "
						+ (stats[8] + stats[10]) + " )" + " Vx " + stats[13] + " Vy " + stats[14] + " HP : "
						+ stats[16] };
		return message; // 返回包含滑鼠點擊位置和兩個角色狀態的字串數組
	}
}
