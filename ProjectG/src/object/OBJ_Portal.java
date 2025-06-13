package object;

import javax.imageio.ImageIO;

import entity.Entity;
import main.GamePanel;

public class OBJ_Portal extends Entity {

    public int targetMap = 0;
    public int targetCol = 0;
    public int targetRow = 0;

    public OBJ_Portal(GamePanel gp, int targetMap, int targetCol, int targetRow) {
        super(gp);
        name = "Portal";
        try {
            down1 = ImageIO.read(getClass().getResourceAsStream("/objects/portal.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        renderLayer = 2;
        pickable = false;
        collision = true;

        solidArea.x = 0;
        solidArea.y = 0;
        solidArea.width = gp.tileSize;
        solidArea.height = gp.tileSize;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        this.targetMap = targetMap;
        this.targetCol = targetCol;
        this.targetRow = targetRow;
    }

    public void interact() {
        if (gp.currentMap == 0){
            if (gp.player.hasBeginnerWeapon()) {
                gp.ui.addMessage("You step into the portal...");
                gp.eHandler.teleport(targetMap, targetCol, targetRow);
            } else {
                // Show a message from the nearest NPC, or just a message
                for (Entity npc : gp.npc[gp.currentMap]) {
                    if (npc != null) {
                        int map = gp.currentMap;
                        npc.dialogues[map][10] = "You must take a weapon before leaving!";
                        npc.dialogIndex = 10;
                        gp.gameState = gp.dialogueState;
                        npc.speak();
                        break;
                    }
                }
            }
        }
        else {
            gp.ui.addMessage("You step into the portal...");
            gp.eHandler.teleport(targetMap, targetCol, targetRow);
        }
    }
}
