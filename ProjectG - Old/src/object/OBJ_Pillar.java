package object;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import entity.Entity;
import main.GamePanel;

public class OBJ_Pillar extends Entity{

    public OBJ_Pillar(GamePanel gp) {
        
        super(gp);
        
        name = "Pillar";
        down1 = setup("/objects/pillar");
        renderLayer = 1;
        
        solidArea.x = 0;
        solidArea.y = gp.tileSize * 3;
        solidArea.width = gp.tileSize * 2;
        solidArea.height = gp.tileSize * 2;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        collision = true;
    }

    @Override
    public void draw(Graphics2D g2) {
        BufferedImage image = down1;

        // Calculate the screen position of the pillar
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        // Check if the player is under the pillar
        boolean playerUnderPillar = gp.player.worldX + gp.player.solidArea.x < worldX + solidArea.width &&
                                     gp.player.worldX + gp.player.solidArea.x + gp.player.solidArea.width > worldX &&
                                     gp.player.worldY + gp.player.solidArea.y < worldY + gp.tileSize * 5 &&
                                     gp.player.worldY + gp.player.solidArea.y + gp.player.solidArea.height > worldY;

        if (playerUnderPillar) {
            // Set transparency to 75% (alpha = 0.75)
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.75f));
        }

        // Render the pillar as 5 tiles tall and 2 tiles wide
        g2.drawImage(image, screenX, screenY, gp.tileSize * 2, gp.tileSize * 5, null);
        
        // Reset transparency to default (opaque)
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }

}
