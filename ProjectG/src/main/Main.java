package main;

import javax.swing.JFrame;

public class Main {
	public static void main(String[] args) {
		JFrame window = new JFrame();
		GamePanel gamePanel = new GamePanel();
		
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(true);
		window.setTitle("Test Game");
		window.add(gamePanel);
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);

		// --- FULLSCREEN ON START ---
		if (gamePanel.ui.fullscreenOn) {
			java.awt.GraphicsDevice gd = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
			window.dispose();
			window.setUndecorated(true);
			window.setResizable(false);
			window.setVisible(true);
			gd.setFullScreenWindow(window);
		}
		// ---------------------------

		gamePanel.setupGame();
		gamePanel.startGameThread();
	}
}
