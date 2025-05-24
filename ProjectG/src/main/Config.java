package main;

import java.io.*;
import java.util.Map;

public class Config {
    GamePanel gp;

    public Config(GamePanel gp) {
        this.gp = gp;
    }

    public void saveConfig() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("config.txt"))) {
            // UI settings
            bw.write("fullscreen=" + gp.ui.fullscreenOn); bw.newLine();
            bw.write("resolution=" + gp.ui.resolutionIndex); bw.newLine();
            bw.write("vsync=" + gp.ui.vsyncOn); bw.newLine();
            bw.write("quality=" + gp.ui.qualityIndex); bw.newLine();

            // Key bindings
            for (Map.Entry<String, Integer> entry : gp.keyConfig.getAllBindings().entrySet()) {
                bw.write("key_" + entry.getKey() + "=" + entry.getValue()); bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadConfig() {
        File file = new File("config.txt");
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("fullscreen=")) {
                    gp.ui.fullscreenOn = Boolean.parseBoolean(line.split("=")[1]);
                } else if (line.startsWith("resolution=")) {
                    gp.ui.resolutionIndex = Integer.parseInt(line.split("=")[1]);
                } else if (line.startsWith("vsync=")) {
                    gp.ui.vsyncOn = Boolean.parseBoolean(line.split("=")[1]);
                } else if (line.startsWith("quality=")) {
                    gp.ui.qualityIndex = Integer.parseInt(line.split("=")[1]);
                } else if (line.startsWith("key_")) {
                    String[] parts = line.split("=");
                    String action = parts[0].substring(4);
                    int keyCode = Integer.parseInt(parts[1]);
                    gp.keyConfig.setKey(action, keyCode);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
