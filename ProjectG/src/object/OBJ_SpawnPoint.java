package object;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import entity.Entity;
import main.GamePanel;

public class OBJ_SpawnPoint extends Entity{
	
	public OBJ_SpawnPoint(GamePanel gp) {
		
		super(gp);

		name = "Spawn Point";
        down1 = setup("/objects/spawn_point");
		renderLayer = 0;
		pickable = false;
		
		solidArea.x = 0;
		solidArea.y = 0;
		solidArea.width = gp.tileSize * 5;
		solidArea.height = gp.tileSize * 3;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;

		collision = false;
	}

    @Override
    public void draw(Graphics2D g2) {
        BufferedImage image = down1;

        // Calculate the screen position of the pillar
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        // Render the pillar as 5 tiles tall and 2 tiles wide
        g2.drawImage(image, screenX, screenY, gp.tileSize * 5, gp.tileSize * 3, null);
    }
	
}
