package main;

import java.io.*;
import java.util.Map;

import data.DataStorage;
import entity.Entity;
import entity.Player;

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
            bw.write("language=" + gp.ui.language); bw.newLine(); // <--- Add this line

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
                } else if (line.startsWith("language=")) {
                    gp.ui.language = line.split("=")[1]; // <--- Add this line
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

    public void savePlayer(Player player) {
		DataStorage data = player.toDataStorage();
		data.savePlayerData(data);
	}

    public void loadPlayer(Player player) {
        File saveFile = new File("save.dat");
        if (!saveFile.exists()) {
            System.out.println("No save file found, skipping load.");
            return;
        }
        DataStorage data = new DataStorage().loadPlayerData();
        if (data != null) {
            player.fromDataStorage(data);
        }

        // Helper to "re-instantiate" an item and copy fields
        java.util.function.Function<Entity, Entity> reloadItem = (oldItem) -> {
            if (oldItem == null) return null;
            try {
                Entity newItem = oldItem.getClass()
                    .getConstructor(main.GamePanel.class)
                    .newInstance(player.gp);
                // Copy important fields
                newItem.quantity = oldItem.quantity;
                newItem.stackable = oldItem.stackable;
                newItem.pickable = oldItem.pickable;
                newItem.type = oldItem.type;
                newItem.level = oldItem.level;
                // newItem.upgrade = oldItem.upgrade;
                newItem.name = oldItem.name;
                newItem.description = oldItem.description;
                newItem.healthBonus = oldItem.healthBonus;
                newItem.manaBonus = oldItem.manaBonus;
                newItem.attackBonus = oldItem.attackBonus;
                newItem.defenseBonus = oldItem.defenseBonus;
                newItem.speedBonus = oldItem.speedBonus;
                newItem.rarity = oldItem.rarity;
                newItem.levelRequirement = oldItem.levelRequirement;
                // ...add more fields as needed...
                return newItem;
            } catch (Exception e) {
                e.printStackTrace();
                // if (oldItem != null) {
                //     oldItem.restoreTransientFields(player.gp);
                // }
                return oldItem; // fallback: use the old item (will have no image)
            }
        };

        // Reload inventory items
        for (int i = 0; i < player.inventory.size(); i++) {
            Entity oldItem = player.inventory.get(i);
            Entity newItem = reloadItem.apply(oldItem);
            player.inventory.set(i, newItem);
        }

        // Reload equipped items
        player.currentWeapon = reloadItem.apply(player.currentWeapon);
        player.currentArmor = reloadItem.apply(player.currentArmor);
        player.currentHat = reloadItem.apply(player.currentHat);
        player.currentBoots = reloadItem.apply(player.currentBoots);

        player.setItems(); // reload player images if needed
        player.gp.gameState = player.gp.playState;
    }
}
