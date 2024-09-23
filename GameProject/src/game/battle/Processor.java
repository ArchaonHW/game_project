package game.battle;
import java.awt.* ;
import java.util.*;

public class Processor extends Panel
{

	private static final long serialVersionUID = 1370219979545467371L;
	private Display display = null;	// 參考顯示面板
	private ControlPanel control = null;	// 參考控制面板

	private Game game = null;	// 參考遊戲本身
	private Timer tm = new Timer();	// 計時器，用來控制遊戲的更新頻率

	// 構造函數，用來初始化 Processor 類
  	public Processor( )
 	{
		this.setBackground(Color.LIGHT_GRAY);	// 設置背景顏色為淺灰色
		this.setLayout(new BorderLayout());	// 設置佈局為邊界佈局

		game = new Game(this);	// 初始化 Game 類

		display = new Display(this);	// 初始化 Display 類

		control = new ControlPanel(this);	// 初始化 ControlPanel 類

		this.add(display, BorderLayout.CENTER);	// 將 display 添加到面板中央
		this.add(control, BorderLayout.NORTH);	// 將 control 添加到面板頂部

		tm.scheduleAtFixedRate(ttask,0,50);	// 設置計時器，每 50 毫秒執行一次 ttask
 	}

	// TimerTask 內部類，用來定期更新遊戲狀態
	TimerTask ttask = new TimerTask()
	{
		public void run(){
			game.move(0.05);	// 移動所有遊戲元素，步長為 0.05
			display.repaint();	// 重繪顯示面板
			showStats();	// 顯示遊戲狀態信息
		}
	};

	// 開啟新遊戲的方法
	public void newGame()
	{
		remove(display);	// 移除顯示面板
		tm.cancel();	// 停止計時器
	}

 	// 繪製遊戲畫面的方法
 	public void display(Graphics g, Dimension d)
 	{
		game.display(g, d);	// 調用遊戲的 display 方法來繪製畫面
	}

	// 暫停計時器的方法
	public void stopTimer()
	{
		System.out.println("Here");
		try{
			ttask.wait(10000);	// 暫停 10 秒
		}catch (Exception wait){System.out.println("NOT WORK");}	// 捕獲異常並顯示錯誤信息
	}

 	// 公共方法，用來退出遊戲
 	public void exit()
	{
		remove(display);	// 移除顯示面板
		remove(control);	// 移除控制面板
		repaint();	// 重繪畫面
	}

	// 繪製 "The End!" 字樣的方法，當遊戲結束時調用
	public void paint(Graphics g)
	{
		Dimension d = this.getSize();	// 獲取面板尺寸

		String str = "The End!";	// 設置顯示的文字內容

		FontMetrics fm = getFontMetrics(this.getFont());	// 獲取字體度量

		int w = fm.stringWidth(str);	// 計算字串的寬度
		int h = fm.getHeight();	// 計算字串的高度

		g.drawString(str, d.width / 2 - w / 2, d.height / 2 + h / 4);	// 在面板中央繪製字串
	}

	// 處理滑鼠按下或點擊事件
	public void process(int button, Point p)
	{
		game.process(button, p);	// 調用遊戲的 process 方法處理滑鼠事件
	}

	// 處理鍵盤按下事件（字符鍵）
	public void process(char ch)
	{
		game.process(ch);	// 調用遊戲的 process 方法處理字符鍵事件
	}

	// 處理鍵盤按下事件（功能鍵）
	public void process(int key)
	{
		game.process(key);	// 調用遊戲的 process 方法處理功能鍵事件
	}

	// 處理鍵盤釋放事件（字符鍵）
	public void processR(char ch)
	{
		game.processR(ch);	// 調用遊戲的 processR 方法處理字符鍵釋放事件
	}

	// 處理鍵盤釋放事件（功能鍵）
	public void processR(int key)
	{
		game.processR(key);	// 調用遊戲的 processR 方法處理功能鍵釋放事件
	}

	// 顯示遊戲狀態信息
	public void showStats()
	{
		control.showStats(game.getStats());	// 調用控制面板的 showStats 方法來顯示遊戲狀態
	}
}
