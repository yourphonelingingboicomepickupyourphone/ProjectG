package main;

//import java.awt.GraphicsEnvironment;
//import java.awt.Window;

import javax.swing.JFrame;

public class Main {

	public static JFrame window;
	public static void main(String[] args) {
		Main.window = new JFrame();
		GamePanel gamePanel = new GamePanel();
		
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(true);
	
		window.setTitle("Test Game");
		window.setUndecorated(true);
		
		
		window.add(gamePanel);
		
		window.pack();	//cause window to be sized to fit the preferred size & layouts of GamePanel
		
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		
		gamePanel.setupGame();
		gamePanel.startGameThread();
	}
}
