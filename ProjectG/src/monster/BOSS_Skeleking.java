package monster;

import entity.Entity;
import main.GamePanel;
import java.awt.image.BufferedImage;

public class BOSS_Skeleking extends Entity {

    private int phase = 1; // 1 for first phase, 2 for second phase
    private int attackCooldown; // Cooldown for attacks in frames

    private BufferedImage down3, down4, down5, down6;

    private boolean slapWarningActive = false;
    private int slapWarningTimer = 0;
    private int slapWarningDuration = 40; // frames to show warning
    private int slapDamage = 100;
    private int slapTargetX, slapTargetY;

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
        left1 = setup("/monsters/skeleton_left_1");
        left2 = setup("/monsters/skeleton_left_2");
        right1 = setup("/monsters/skeleton_right_1");
        right2 = setup("/monsters/skeleton_right_2");
        up1 = setup("/monsters/skeleton_up_1");
        up2 = setup("/monsters/skeleton_up_2");

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
            if (Math.random() < 0.1) {
                int randomDirection = (int)(Math.random() * 4);
                switch (randomDirection) {
                    case 0: direction = "up"; break;
                    case 1: direction = "down"; break;
                    case 2: direction = "left"; break;
                    case 3: direction = "right"; break;
                }
            }
        } else if (phase == 2) {
            // Boss phase 2 logic (special attacks, etc.)
            // --- Boss Slap Logic with Warning (only in phase 2) ---
            if (!slapWarningActive && Math.random() < 0.01) { // 1% chance per frame to start slap
                slapWarningActive = true;
                slapWarningTimer = slapWarningDuration;
                // Target the player's current position
                slapTargetX = gp.player.worldX + gp.player.solidArea.x;
                slapTargetY = gp.player.worldY + gp.player.solidArea.y;
                // Add warning effect
                effect.EFFECT_BossSlapWarning warning = new effect.EFFECT_BossSlapWarning(gp, slapTargetX, slapTargetY, slapWarningDuration);
                gp.projectileList.add(warning);
            }

            if (slapWarningActive) {
                slapWarningTimer--;
                if (slapWarningTimer <= 0) {
                    // Do the slap at the warned position
                    effect.EFFECT_BossSlap slap = new effect.EFFECT_BossSlap(gp, slapTargetX, slapTargetY, slapDamage);
                    gp.projectileList.add(slap);
                    slapWarningActive = false;
                }
            }
        }

        if (phase == 1 && health <= maxHealth / 2) {
            phase = 2;
            // Change stats
            attackCooldown += 60;
            attack += 50;
            // Change appearance
            down1 = setup("/monsters/boss_skeleking_down_1");
            down2 = setup("/monsters/boss_skeleking_down_1");
            down3 = setup("/monsters/boss_skeleking_down_1");
            down4 = setup("/monsters/boss_skeleking_down_1");
            down5 = setup("/monsters/boss_skeleking_down_1");
            down6 = setup("/monsters/boss_skeleking_down_1");

            solidArea.x = 5;
            solidArea.y = 10;
            solidArea.width = 80;
            solidArea.height = 80;

            solidAreaDefaultX = solidArea.x;
            solidAreaDefaultY = solidArea.y;
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
