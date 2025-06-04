package monster;

import entity.Entity;
import main.GamePanel;

public class BOSS_Skeleking extends Entity {

    private int phase = 1; // 1 for first phase, 2 for second phase
    private int attackCooldown; // Cooldown for attacks in frames

    public BOSS_Skeleking(GamePanel gp) {
        super(gp);

        this.attackCooldown = 60; // Cooldown in frames
        
        direction = "down";
        name = "Skeleking";
        type = 1; // BOSS type
        renderLayer = 3; 
        speed = 2;

        down1 = setup("/monsters/boss_skeleking_down_1");
        down2 = setup("/monsters/boss_skeleking_down_2");
        up1 = setup("/monsters/boss_skeleking_up_1");
        up2 = setup("/monsters/boss_skeleking_up_2");
        left1 = setup("/monsters/boss_skeleking_left_1");
        left2 = setup("/monsters/boss_skeleking_left_2");
        right1 = setup("/monsters/boss_skeleking_right_1");
        right2 = setup("/monsters/boss_skeleking_right_2");
        
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
            int attackCooldown = 60; // Cooldown in frames

            // First phase actions
            if (Math.random() < 0.1) { // 10% chance to change direction
                int randomDirection = (int)(Math.random() * 4);
                switch (randomDirection) {
                    case 0: direction = "up"; break;
                    case 1: direction = "down"; break;
                    case 2: direction = "left"; break;
                    case 3: direction = "right"; break;
                }
            }
        } else if (phase == 2) {
            // Second phase actions
            if (Math.random() < 0.2) { // 20% chance to change direction
                int randomDirection = (int)(Math.random() * 4);
                switch (randomDirection) {
                    case 0: direction = "up"; break;
                    case 1: direction = "down"; break;
                    case 2: direction = "left"; break;
                    case 3: direction = "right"; break;
                }
            }
        }

        if (phase == 1 && health <= maxHealth / 2) {
            phase = 2;
            // Change stats
            speed += 2;
            attack += 50;
            // Change appearance
            down1 = setup("/monsters/boss_skeleking_phase2_down_1");
            down2 = setup("/monsters/boss_skeleking_phase2_down_2");
            up1 = setup("/monsters/boss_skeleking_phase2_up_1");
            up2 = setup("/monsters/boss_skeleking_phase2_up_2");
            left1 = setup("/monsters/boss_skeleking_phase2_left_1");
            left2 = setup("/monsters/boss_skeleking_phase2_left_2");
            right1 = setup("/monsters/boss_skeleking_phase2_right_1");
            right2 = setup("/monsters/boss_skeleking_phase2_right_2");
        }

        // Example: Fire a projectile toward the player every N frames
        if (attackCooldown == 0) {
            int dx = gp.player.worldX - this.worldX;
            int dy = gp.player.worldY - this.worldY;

            // Normalize direction
            double length = Math.sqrt(dx * dx + dy * dy);
            double dirX = dx / length;
            double dirY = dy / length;

            // Create and fire a projectile
            PROJECTILE_Fire_Ball proj = new PROJECTILE_Fire_Ball(gp);
            proj.set(this.worldX, this.worldY, "custom", true, this);
            proj.customDX = dirX;
            proj.customDY = dirY;
            gp.projectileList.add(proj);

            attackCooldown = 60; // Cooldown in frames
        } else {
            attackCooldown--;
        }
    }
}
