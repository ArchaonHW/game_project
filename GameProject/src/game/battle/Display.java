//diplay.java
package game.battle;

import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;

public class Display extends Panel implements MouseListener, KeyListener
{
	private static final long serialVersionUID = -6738619668868978950L;
	private BufferedImage osi = null;	// 離屏圖片，用來在後台進行繪圖
	private Graphics osg = null;		// 離屏圖形，用來在離屏圖片上進行繪圖
	private Processor proc = null;		// 處理器，用來處理遊戲邏輯

	// 構造函數，用來初始化 Display 對象
	Display(Processor proc)
	{
		this.proc = proc;
		addMouseListener(this);    // 添加滑鼠監聽器，以便處理滑鼠事件
		addKeyListener(this);      // 添加鍵盤監聽器，以便處理鍵盤事件
		setBackground(Color.LIGHT_GRAY);    // 設定背景顏色為淺灰色
	}

	// 繪製方法，用來繪製圖形
	public void paint (Graphics g)
	{
		Dimension d = getSize();    // 取得目前面板的大小
		osi = new BufferedImage(d.width, d.height, BufferedImage.TYPE_INT_RGB);    // 建立一個與面板大小相同的離屏圖片，用來繪圖
		osg = osi.getGraphics();    // 取得離屏圖片的繪圖上下文
		update(g);    // 更新畫面並顯示繪圖
	}

	// 更新繪製方法，將離屏圖片顯示到螢幕上
	public void update(Graphics g)
	{
		proc.display(osg, getSize());    // 呼叫處理器的 display 方法來繪製遊戲畫面
		g.drawImage(osi, 0, 0, this);    // 將離屏圖片繪製到螢幕上，這樣可以避免畫面閃爍
	}

	// 當用戶按下鍵盤上的某個鍵時觸發，用來移動角色
	public void keyPressed(KeyEvent ke)
	{
		char ch = ke.getKeyChar();    // 取得按下的字符
		if(ch == KeyEvent.CHAR_UNDEFINED)    // 如果按下的不是字符鍵
			proc.process(ke.getKeyCode());    // 呼叫處理器的 process 方法來處理鍵盤按下事件
		else
			proc.process(ch);    // 否則直接處理字符鍵的輸入
		repaint();    // 重新繪製畫面
	}

	// 當用戶釋放鍵盤上的某個鍵時觸發
	public void keyReleased(KeyEvent ke)
	{
		char ch = ke.getKeyChar();    // 取得釋放的字符
		if(ch == KeyEvent.CHAR_UNDEFINED)    // 如果釋放的不是字符鍵
			proc.processR(ke.getKeyCode());    // 呼叫處理器的 processR 方法來處理鍵盤釋放事件
		else
			proc.processR(ch);    // 否則直接處理字符鍵的釋放
		repaint();    // 重新繪製畫面
	}

	// 不處理 keyTyped 事件，因此空實作
	public void keyTyped(KeyEvent ke) {}

	// 滑鼠點擊事件，不處理，因此空實作
	public void mouseClicked(MouseEvent me) {}

	// 滑鼠進入面板事件，不處理，因此空實作
	public void mouseEntered(MouseEvent me) {}

	// 滑鼠離開面板事件，不處理，因此空實作
	public void mouseExited(MouseEvent me) {}

	// 當用戶按下滑鼠時觸發
	public void mousePressed(MouseEvent me)
	{
		proc.process(me.getButton(),me.getPoint());    // 呼叫處理器的 process 方法來處理滑鼠按下事件
		repaint();    // 重新繪製畫面
	}

	// 滑鼠釋放事件，不處理，因此空實作
	public void mouseReleased(MouseEvent me) {}
}

