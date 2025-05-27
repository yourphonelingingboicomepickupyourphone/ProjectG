package main;

import javax.swing.JFrame;

public class Main {
	public static void main(String[] args) {
		GamePanel gp = new GamePanel();
		JFrame window = new JFrame();
		
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(true);
		window.setTitle("LOTRW - Alpha Test");
		window.add(gp);
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);

		// --- FULLSCREEN ON START ---
		if (gp.ui.fullscreenOn) {
			java.awt.GraphicsDevice gd = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
			window.dispose();
			window.setUndecorated(true);
			window.setResizable(false);
			window.setVisible(true);
			gd.setFullScreenWindow(window);
		}
		// ---------------------------

		// Save config on exit
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			if (gp.config != null) {
				gp.config.saveConfig();	
			}
		}));

		gp.setupGame();
		gp.startGameThread();
	}
}
