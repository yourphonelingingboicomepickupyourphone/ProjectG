package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;

public class Config {

    GamePanel gp;

    public Config(GamePanel gp) {
        this.gp = gp;
    }

    public void saveConfig() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("config.txt"));
            bw.write(String.valueOf(gp.ui.resolutionIndex));
            bw.newLine();
            bw.write(gp.ui.fullscreenOn ? "On" : "Off");
            bw.newLine();
            bw.write(gp.ui.language); 
            bw.newLine();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadConfig() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("config.txt"));
            String resolutionIndexStr = br.readLine();
            String fullscreenOn = br.readLine();
            String language = br.readLine();

            // Parse the index directly
            try {
                gp.ui.resolutionIndex = Integer.parseInt(resolutionIndexStr);
            } catch (NumberFormatException e) {
                gp.ui.resolutionIndex = 0; // fallback to default
            }
            gp.ui.fullscreenOn = "On".equals(fullscreenOn);

            // Set resolution and update player screen position
            String res = gp.ui.resolutions[gp.ui.resolutionIndex];
            String[] parts = res.split("x");
            int width = Integer.parseInt(parts[0]);
            int height = Integer.parseInt(parts[1]);
            gp.baseWidth = width;
            gp.baseHeight = height;
            gp.screenWidth = width;
            gp.screenHeight = height;
            gp.updateTileSize();
            gp.player.screenX = gp.baseWidth / 2 - gp.tileSize / 2;
            gp.player.screenY = gp.baseHeight / 2 - gp.tileSize / 2;
            gp.setPreferredSize(new java.awt.Dimension(width, height));
            gp.revalidate();
            java.awt.Window window = javax.swing.SwingUtilities.getWindowAncestor(gp);
            if (window != null) window.pack();

            if (language != null && !language.isEmpty()) {
                gp.ui.language = language;
                gp.ui.loadLanguage(language); // <--- Load language file
            } else {
                gp.ui.loadLanguage("en");
            }

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}


