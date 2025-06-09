package monster;

import entity.Entity;
import main.GamePanel;
import java.awt.image.BufferedImage;

public class BOSS_Skeleking extends Entity {

    private int phase = 1; // 1 = boss, 2 = enraged boss
    private int attackCooldown = 60; // Cooldown for attacks in frames
    public int invincibleCounter = 0;
    public int invincibleTime = 20; // frames (adjust as needed)

    // Boss frames
    private BufferedImage down1, down2, down3, down4, down5, down6;

    private boolean slapWarningActive = false;
    private int slapWarningTimer = 0;
    private int slapWarningDuration = 40; // frames to show warning
    private int slapDamage = 100;
    private int slapTargetX, slapTargetY;

    public BOSS_Skeleking(GamePanel gp) {
        super(gp);

        direction = "down";
        name = "Skeleking";
        type = 1; // BOSS type

        maxHealth = 3000;
        health = maxHealth;
        attack = 200; // Base attack damage
        defense = 50; // Base defense
        expReward = 1000; // Experience points rewarded for defeating the boss
        collision = true; // Boss can collide with player
        alive = true; // Boss is alive at start
        dying = false; // Boss is not dying at start    
        renderLayer = 3;
        speed = 0; // Boss will not move



        // Load boss frames (replace with unique frames if you have them)
        down1 = setup("/monsters/boss_skeleking_down_1");
        down2 = setup("/monsters/boss_skeleking_down_1");
        down3 = setup("/monsters/boss_skeleking_down_1");
        down4 = setup("/monsters/boss_skeleking_down_1");
        down5 = setup("/monsters/boss_skeleking_down_1");
        down6 = setup("/monsters/boss_skeleking_down_1");

        // Adjust solid area to match sprite
        solidArea.x = 0;
        solidArea.y = 0;
        solidArea.width = gp.tileSize * 5; // Boss is 5 tiles wide
        solidArea.height = gp.tileSize * 5; // Boss is 5 tiles tall
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }

    @Override
    public void setAction() {
        // Boss stands still and only attacks if player is in range
        int dx = (gp.player.worldX + gp.player.solidArea.x) - (this.worldX + this.solidArea.x);
        int dy = (gp.player.worldY + gp.player.solidArea.y) - (this.worldY + this.solidArea.y);
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (gp.player.bossArenaActive) {
            // Attack logic (slap attack)
            if (!slapWarningActive && Math.random() < 0.01) {
                slapWarningActive = true;
                slapWarningTimer = slapWarningDuration;
                slapTargetX = gp.player.worldX + gp.player.solidArea.x;
                slapTargetY = gp.player.worldY + gp.player.solidArea.y;
                effect.EFFECT_BossSlapWarning warning = new effect.EFFECT_BossSlapWarning(gp, slapTargetX, slapTargetY, slapWarningDuration);
                gp.projectileList.add(warning);
            }

            if (slapWarningActive) {
                slapWarningTimer--;
                if (slapWarningTimer <= 0) {
                    effect.EFFECT_BossSlap slap = new effect.EFFECT_BossSlap(gp, slapTargetX, slapTargetY, slapDamage);
                    gp.projectileList.add(slap);
                    slapWarningActive = false;
                }
            }
        }

        // Transition to enraged boss (phase 2)
        if (phase == 1 && health <= maxHealth / 2) {
            phase = 2;
            attackCooldown = Math.max(20, attackCooldown - 30); // faster attacks
            attack += 50; // more damage
            speed = 0; // still does not move
            slapDamage += 50; // slap does more damage
            // Same hitbox
            
        }
    }

    @Override
    public void update() {
        if (health < 0) health = 0;

        if (health == 0 && alive) {
            alive = false;
            dying = true;

            // Remove boss arena when boss is dead
            gp.player.bossArenaActive = false;

            // Remove boss from the monster array
            for (int i = 0; i < gp.monster[gp.currentMap].length; i++) {
                if (gp.monster[gp.currentMap][i] == this) {
                    gp.monster[gp.currentMap][i] = null;
                    break;
                }
            }

            // Remove from entityList if present
            gp.entityList.removeIf(e -> e == this);

            System.out.println("Boss removed from monster array and entityList!");
            return;
        }

        setAction();

        // Update sprite animation
        spriteCounter++;
        if (spriteCounter > 10) {
            spriteNum++;
            if (spriteNum > 6) spriteNum = 1;
            spriteCounter = 0;
        }

        // --- Reset invincibility after cooldown ---
        if (invincible) {
            invincibleCounter++;
            if (invincibleCounter > invincibleTime) {
                invincible = false;
                invincibleCounter = 0;
            }
        }
    }

    @Override
    public void draw(java.awt.Graphics2D g2) {
        if (!alive) return;

        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        BufferedImage image = null;
        if (direction.equals("down")) {
            switch (spriteNum) {
                case 1: image = down1; break;
                case 2: image = down2; break;
                case 3: image = down3; break;
                case 4: image = down4; break;
                case 5: image = down5; break;
                case 6: image = down6; break;
            }
        }

        int renderSize = gp.tileSize * 5;

        if (image != null) {
            g2.drawImage(image, screenX, screenY, renderSize, renderSize, null);
        } else {
            g2.setColor(java.awt.Color.RED);
            g2.drawRect(screenX, screenY, renderSize, renderSize);
        }

        if (gp.debugMode) {
            // TEMP: draw collision box for debugging
            g2.setColor(java.awt.Color.GREEN);
            g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
        }

        // Calculate boss center
        int bossCenterX = this.worldX + renderSize / 2;
        int bossCenterY = this.worldY + renderSize / 2;

        // Calculate player center
        int playerCenterX = gp.player.worldX + gp.player.solidArea.x + gp.player.solidArea.width / 2;
        int playerCenterY = gp.player.worldY + gp.player.solidArea.y + gp.player.solidArea.height / 2;

        // Use center-to-center distance
        int dx = playerCenterX - bossCenterX;
        int dy = playerCenterY - bossCenterY;
        double distance = Math.sqrt(dx * dx + dy * dy);

        int attackRange = gp.tileSize * 4;

        // Only activate arena if boss is alive
        if (alive && !gp.player.bossArenaActive && distance <= attackRange) {
            gp.player.bossArenaActive = true;
            gp.player.bossArenaCenterX = bossCenterX;
            gp.player.bossArenaCenterY = bossCenterY;
            gp.player.bossArenaRadius = gp.tileSize * 8; // Adjust as needed
        }

        // Only show boss bar if boss is alive and arena is active
        if (alive && gp.player.bossArenaActive) {
            int barWidth = gp.screenWidth - gp.tileSize * 4;
            int barHeight = gp.tileSize / 5;
            int barX = gp.tileSize * 2;
            int barY = gp.screenHeight - gp.tileSize * 2;

            // Background bar
            g2.setColor(new java.awt.Color(40, 40, 40, 220));
            g2.fillRoundRect(barX, barY, barWidth, barHeight, 20, 20);

            // Health bar (red)
            double healthPercent = (double)health / maxHealth;
            int healthBarWidth = (int)(barWidth * healthPercent);
            g2.setColor(new java.awt.Color(180, 40, 40));
            g2.fillRoundRect(barX, barY, healthBarWidth, barHeight, 20, 20);

            // Boss name (adjusted to the left of the health bar)
            g2.setColor(java.awt.Color.WHITE);
            g2.setFont(gp.ui.currentFont.deriveFont(java.awt.Font.BOLD, 24f));
            java.awt.FontMetrics fm = g2.getFontMetrics();
            String bossName = name;
            int nameX = barX + 10;
            int nameY = barY + barHeight - 10;
            g2.drawString(bossName, nameX, nameY);

            // Draw boss health numbers (centered in the bar)
            String healthText = health + " / " + maxHealth;
            int healthTextX = gp.ui.getXForCenteredText(healthText);
            int healthTextY = barY + barHeight;
            g2.setColor(java.awt.Color.WHITE);
            g2.setFont(gp.ui.currentFont.deriveFont(java.awt.Font.BOLD, 16f));
            g2.drawString(healthText, healthTextX, healthTextY);
        }
    }

    public void takeDamage(int amount) {
        if (!invincible && alive) {
            int damage = Math.max(0, amount - defense);
            if (damage > maxHealth / 2) {
                // Heal and increase max health
                maxHealth = 20000;
                health = maxHealth;
                gp.ui.addMessage("The Skeleking roars and becomes stronger!");
            } else {
                health -= damage;
                if (health < 0) health = 0;
            }
            invincible = true;
            invincibleCounter = 0;
        }
    }
}
