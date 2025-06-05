package effect;

import entity.Entity;
import main.GamePanel;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class EFFECT_BossSlap extends Entity {
    private int duration = 30; // frames the effect lasts
    private int damage = 100;
    private boolean hasDealtDamage = false;
    private BufferedImage slapImage;

    public EFFECT_BossSlap(GamePanel gp, int x, int y, int damage) {
        super(gp);
        this.worldX = x;
        this.worldY = y;
        this.damage = damage;
        this.alive = true;

        try {
            slapImage = ImageIO.read(getClass().getResourceAsStream("/effects/skeleking_slap.png"));
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            slapImage = null;
        }
    }

    @Override
    public void update() {
        duration--;
        if (!hasDealtDamage) {
            // Check if player is in the area
            Rectangle slapArea = new Rectangle(worldX, worldY, gp.tileSize, gp.tileSize);
            Rectangle playerArea = new Rectangle(
                gp.player.worldX + gp.player.solidArea.x,
                gp.player.worldY + gp.player.solidArea.y,
                gp.player.solidArea.width,
                gp.player.solidArea.height
            );
            if (slapArea.intersects(playerArea)) {
                gp.player.health -= damage;
                gp.player.timeSinceLastHit = 0;
                // Optionally, set invincibility frames or knockback
            }
            hasDealtDamage = true;
        }
        if (duration <= 0) {
            alive = false;
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;
        if (slapImage != null) {
            g2.drawImage(slapImage, screenX, screenY, gp.tileSize, gp.tileSize, null);
        } else {
            g2.setColor(new Color(255, 80, 80, 180));
            g2.fillOval(screenX, screenY, gp.tileSize, gp.tileSize);
        }
    }
}
