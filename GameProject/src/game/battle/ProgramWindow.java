// 引入必要的Java套件
package game.battle;

import java.awt.*;
import java.awt.event.*;

// ProgramWindow類別繼承了Frame並且實作了WindowListener介面
public class ProgramWindow extends Frame implements WindowListener
{
	private static final long serialVersionUID = -1884356455294586682L;
	// 宣告並初始化一個Processor物件，這個物件負責處理遊戲的主要邏輯
	Processor proc = new Processor();		// 處理器（啟動處理器）

	// ProgramWindow類別的建構函數，用來設定視窗的屬性
	public ProgramWindow ()
	{
		// 設定視窗的標題
		setTitle("Panel");

		// 設定視窗的大小為960x670像素
		setSize(960,660);

		// 設定視窗在螢幕上的顯示位置為(50, 50)
		setLocation(50,50);

		// 允許視窗的大小可以調整
		setResizable(false);

		// 將Processor物件添加到視窗中，這樣Processor就能在視窗中顯示
		add(proc);

		// 將視窗設為可見
		setVisible(true);

		// 為視窗添加一個WindowListener，以便可以處理視窗的關閉、最小化等事件
		addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose(); // 關閉窗口但不終止應用程序
            }
        });
	}

	// 當視窗被啟用時會呼叫此方法
	public void windowActivated(WindowEvent we){}

	// 當視窗被關閉時會呼叫此方法，並終止程序
	public void windowClosed(WindowEvent we){System.exit(0);}

	// 當使用者試圖關閉視窗時會呼叫此方法，這裡使用dispose()來釋放視窗資源
	public void windowClosing(WindowEvent we){dispose();}

	// 當視窗被停用時會呼叫此方法
	public void windowDeactivated(WindowEvent we){}

	// 當視窗從圖示狀態恢復時會呼叫此方法
	public void windowDeiconified(WindowEvent we){}

	// 當視窗被最小化為圖示時會呼叫此方法
	public void windowIconified(WindowEvent we){}

	// 當視窗被首次開啟時會呼叫此方法
	public void windowOpened(WindowEvent we){}
}
