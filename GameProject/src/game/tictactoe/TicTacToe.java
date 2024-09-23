package game.tictactoe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TicTacToe extends JFrame implements ActionListener {
    /**
	 * 
	 */
	private static final long serialVersionUID = 7537719836478696244L;
	// 建立3x3的按鈕陣列
    private JButton[][] buttons = new JButton[3][3];
    private boolean isXTurn = true; // 記錄是否為 X 玩家回合

    public TicTacToe() {
        setTitle("Tic Tac Toe"); // 設定視窗標題
        setSize(400, 400); // 設定視窗大小
        setLayout(new GridLayout(3, 3)); // 使用3x3的格子佈局
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 關閉視窗時結束程式

        initializeButtons(); // 初始化按鈕

        setVisible(true); // 顯示視窗
    }

    private void initializeButtons() {
        // 初始化按鈕並將其加入到視窗中
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton("");
                buttons[i][j].setFont(new Font("Arial", Font.PLAIN, 60)); // 設定字體
                buttons[i][j].setFocusPainted(false); // 去除按鈕聚焦邊框
                buttons[i][j].addActionListener(this); // 添加事件監聽
                add(buttons[i][j]); // 將按鈕添加到佈局
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton buttonClicked = (JButton) e.getSource(); // 獲取被點擊的按鈕
        if (buttonClicked.getText().equals("")) { // 檢查按鈕是否已被點擊過
            buttonClicked.setText(isXTurn ? "X" : "O"); // 設定按鈕文字為 X 或 O
            isXTurn = !isXTurn; // 切換回合
            checkForWinOrDraw(); // 檢查是否有人獲勝或平手
        }
    }

    private void checkForWinOrDraw() {
        if (checkForWin()) { // 檢查是否有人獲勝
            return;
        }
        if (checkForDraw()) { // 檢查是否平手
            JOptionPane.showMessageDialog(this, "遊戲結束，平手");
            resetBoard(); // 重置遊戲板
        }
    }

    private boolean checkForWin() {
        // 檢查行、列和對角線是否有獲勝
        for (int i = 0; i < 3; i++) {
            if (checkLine(buttons[i][0], buttons[i][1], buttons[i][2]) ||
                checkLine(buttons[0][i], buttons[1][i], buttons[2][i])) {
                displayWinner(buttons[i][0].getText()); // 顯示獲勝者
                return true;
            }
        }
        if (checkLine(buttons[0][0], buttons[1][1], buttons[2][2]) ||
            checkLine(buttons[0][2], buttons[1][1], buttons[2][0])) {
            displayWinner(buttons[1][1].getText()); // 顯示獲勝者
            return true;
        }
        return false;
    }

    private boolean checkLine(JButton b1, JButton b2, JButton b3) {
        // 檢查三個按鈕是否有相同的非空文字
        return !b1.getText().equals("") &&
               b1.getText().equals(b2.getText()) &&
               b2.getText().equals(b3.getText());
    }

    private void displayWinner(String winner) {
        JOptionPane.showMessageDialog(this, "玩家 " + winner + " 贏了"); // 顯示獲勝訊息
        resetBoard(); // 重置遊戲板
    }

    private boolean checkForDraw() {
        // 檢查是否平手
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j].getText().equals("")) {
                    return false; // 如果有未被點擊的按鈕則不是平手
                }
            }
        }
        return true; // 所有按鈕都被點擊則平手
    }

    private void resetBoard() {
        // 重置所有按鈕
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
            }
        }
        isXTurn = true; // 重置為 X 玩家回合
    }

    public static void main(String[] args) {
        new TicTacToe(); // 啟動遊戲
    }
}
