package main;

import javax.swing.JFrame;

public class Main {
	public static void main(String[] args) {
		JFrame window = new JFrame();
		GamePanel gamePanel = new GamePanel();
		
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(true);
		window.setTitle("LOTRW - Demo");
		window.add(gamePanel);
		window.pack();
		window.setLocationRelativeTo(null);

		// Load config BEFORE showing the window or setting fullscreen
		gamePanel.config.loadConfig();

		window.setVisible(true);

		// Add this block to save config on close
		window.addWindowListener(new java.awt.event.WindowAdapter() {
		    public void windowClosing(java.awt.event.WindowEvent e) {
		        gamePanel.config.saveConfig();
		    }
		});

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
