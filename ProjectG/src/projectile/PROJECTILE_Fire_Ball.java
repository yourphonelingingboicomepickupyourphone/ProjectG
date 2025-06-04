package projectile;

import entity.Projectile;
import main.GamePanel;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class PROJECTILE_Fire_Ball extends Projectile {
    public PROJECTILE_Fire_Ball(GamePanel gp) {
        super(gp);
        this.gp = gp;

        name = "Fire Ball";
        speed = 8;
        attackBonus = 50;
        manaCost = 50;
        getImage();
     
    }

    public void getImage() {
        up1 = setup("/projectiles/fireball_up_1");
        up2 = setup("/projectiles/fireball_up_2");
        down1 = setup("/projectiles/fireball_down_1");
        down2 = setup("/projectiles/fireball_down_2");
        left1 = setup("/projectiles/fireball_left_1");
        left2 = setup("/projectiles/fireball_left_2");
        right1 = setup("/projectiles/fireball_right_1");
        right2 = setup("/projectiles/fireball_right_2");
    }

    @Override
    public void draw(Graphics2D g2) {
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;
        System.out.println("Drawing projectile at screen: " + screenX + "," + screenY + " alive=" + alive);

        g2.setColor(java.awt.Color.RED);
        g2.fillRect(screenX, screenY, gp.tileSize, gp.tileSize);

        // Debug: Draw a red square if image is null
        if (up1 == null) {
            g2.setColor(java.awt.Color.RED);
            g2.fillRect(screenX, screenY, gp.tileSize, gp.tileSize);
        } else {
            BufferedImage image = null;
            switch (direction) {
                case "up": image = up1; break;
                case "down": image = down1; break;
                case "left": image = left1; break;
                case "right": image = right1; break;
            }
            g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
        }
    }
}
