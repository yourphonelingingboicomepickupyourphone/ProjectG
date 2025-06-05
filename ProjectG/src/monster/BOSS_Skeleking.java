package monster;

import entity.Entity;
import main.GamePanel;
import java.awt.image.BufferedImage;

public class BOSS_Skeleking extends Entity {

    private int phase = 1; // 1 for first phase, 2 for second phase
    private int attackCooldown; // Cooldown for attacks in frames

    private BufferedImage down3, down4, down5, down6;

    public BOSS_Skeleking(GamePanel gp) {
        super(gp);

        this.attackCooldown = 60; // Cooldown in frames
        
        direction = "down";
        name = "Skeleking";
        type = 1; // BOSS type
        renderLayer = 3; 
        speed = 2;

        down1 = setup("/monsters/skeleton_down_1");
        down2 = setup("/monsters/skeleton_down_2");
        down3 = setup("/monsters/skeleton_down_3");
        down4 = setup("/monsters/skeleton_down_4");
        down5 = setup("/monsters/skeleton_down_5");
        down6 = setup("/monsters/skeleton_down_6");

        //attack animation
        
        // Adjust solid area to match sprite
        solidArea.x = 10;
        solidArea.y = 20;
        solidArea.width = 60;
        solidArea.height = 60;

        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        // getImage();
        
    }

    @Override
    public void setAction() {
        if (phase == 1) {
            super.update();

            // First phase actions
            if (Math.random() < 0.1) { // 10% chance to change direction
                int randomDirection = (int)(Math.random() * 4);
                
            }
        } else if (phase == 2) {
            // Second phase actions
            
        }

        if (phase == 1 && health <= maxHealth / 2) {
            phase = 2;
            // Change stats
            attackCooldown += 60;
            attack += 50;
            // Change appearance
            down1 = setup("/monsters/boss_skeleking_down_1");
            down2 = setup("/monsters/boss_skeleking_down_2");
            down3 = setup("/monsters/boss_skeleking_down_3");
            down4 = setup("/monsters/boss_skeleking_down_4");
            down5 = setup("/monsters/boss_skeleking_down_5");
            down6 = setup("/monsters/boss_skeleking_down_6");

            solidArea.x = 5;
            solidArea.y = 10;
            solidArea.width = 80;
            solidArea.height = 80;

            solidAreaDefaultX = solidArea.x;
            solidAreaDefaultY = solidArea.y;

            // hitbox.x = ...;
            // hitbox.y = ...;
            // hitbox.width = ...;
            // hitbox.height = ...;
        }

        // Example: Fire a projectile toward the player every N frames
        if (attackCooldown == 0) {
        
        }
    }

    @Override
    public void update() {
        super.update();


        // Update sprite animation
        spriteCounter++;
        if (spriteCounter > 10) {
            spriteNum++;
            if (spriteNum > 4) {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }

    }

    @Override
    public void draw(java.awt.Graphics2D g2) {
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        BufferedImage image = null;
        switch (direction) {
            case "down":
                if (spriteNum == 1) {
                    image = down1;
                } else if (spriteNum == 2) {
                    image = down2;
                } else if (spriteNum == 3) {
                    image = down3;
                } else if (spriteNum == 4) {
                    image = down4;
                } else if (spriteNum == 5) {
                    image = down5;
                } else if (spriteNum == 6) {
                    image = down6;
                }
                break;
        }

        if (image != null) {
            g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
        } else {
            g2.setColor(java.awt.Color.RED);
            g2.fillRect(screenX, screenY, gp.tileSize, gp.tileSize);
        }
    }
}
