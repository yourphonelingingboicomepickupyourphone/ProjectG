package main;

//import java.awt.GraphicsEnvironment;
//import java.awt.Window;

import javax.swing.JFrame;

public class Main {
	public static void main(String[] args) {
		JFrame window = new JFrame();
		GamePanel gamePanel = new GamePanel();
		
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(true);
		
//		for ( Window w : Window.getWindows() ) {
//		    GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow( w );
//		}

		window.setTitle("Test Game");
		
		
		window.add(gamePanel);
		
		window.pack();	//cause window to be sized to fit the preferred size & layouts of GamePanel
		
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		
		gamePanel.setupGame();
		gamePanel.startGameThread();
	}
}
