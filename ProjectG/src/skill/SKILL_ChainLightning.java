package skill;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import entity.Player;
import entity.Entity;
import main.GamePanel;

public class SKILL_ChainLightning implements Skill {
    private int manaCost = 40;
    private int cooldownMax = 300; // 5 seconds at 60 FPS
    private int cooldown = 0;
    private int levelRequirement = 1;
    private BufferedImage icon;

    public SKILL_ChainLightning(GamePanel gp) {
        try {
            icon = ImageIO.read(getClass().getResourceAsStream("/skills/skill_chainlightning.png"));
        } catch (IOException e) {
            // fallback: icon = null;
        }
    }

    public String getName(GamePanel gp) { return "Chain Lightning"; }
    public String getDescription(GamePanel gp) { return "Zap up to 3 nearby monsters, dealing damage and healing you."; }
    public int getManaCost() { return manaCost; }
    public int getCooldownMax() { return cooldownMax; }
    public int getCooldown() { return cooldown; }
    public int getLevelRequirement() { return levelRequirement; }
    public BufferedImage getIcon() { return icon; }

    public boolean canUse(int playerMana, int playerLevel) {
        return playerMana >= manaCost && cooldown == 0 && playerLevel >= levelRequirement;
    }

    public void use(Player player) {
        cooldown = cooldownMax;
        player.mana -= manaCost;
    }

    public void tickCooldown() {
        if (cooldown > 0) cooldown--;
    }

    public void applyEffect(GamePanel gp, Player player) {
        int range = gp.tileSize * 6;
        int maxTargets = 3;
        int damage = 100;
        double healPercent = 0.5;

        ArrayList<Entity> targets = new ArrayList<>();
        int playerCenterX = player.worldX + player.solidArea.x + player.solidArea.width / 2;
        int playerCenterY = player.worldY + player.solidArea.y + player.solidArea.height / 2;

        for (Entity m : gp.monster[gp.currentMap]) {
            if (m != null && m.alive && !m.dying) {
                int mx = m.worldX + m.solidArea.x + m.solidArea.width / 2;
                int my = m.worldY + m.solidArea.y + m.solidArea.height / 2;
                double dist = Math.hypot(mx - playerCenterX, my - playerCenterY);
                if (dist <= range) {
                    targets.add(m);
                }
            }
        }
        // Sort by distance and pick up to maxTargets
        targets.sort((a, b) -> {
            int ax = a.worldX + a.solidArea.x + a.solidArea.width / 2;
            int ay = a.worldY + a.solidArea.y + a.solidArea.height / 2;
            int bx = b.worldX + b.solidArea.x + b.solidArea.width / 2;
            int by = b.worldY + b.solidArea.y + b.solidArea.height / 2;
            double da = Math.hypot(ax - playerCenterX, ay - playerCenterY);
            double db = Math.hypot(bx - playerCenterX, by - playerCenterY);
            return Double.compare(da, db);
        });
        if (targets.size() > maxTargets) {
            targets = new ArrayList<>(targets.subList(0, maxTargets));
        }

        int totalDamage = 0;
        for (Entity m : targets) {
            int actualDamage = Math.max(0, damage - m.defense);
            m.health -= actualDamage;
            if (m.health < 0) m.health = 0;
            m.showHpBar = true;
            m.hpBarDisplayCounter = 150;
            totalDamage += actualDamage;
        }
        // Heal the player
        int heal = (int)(totalDamage * healPercent);
        player.health += heal;
        if (player.health > player.getTotalMaxHealth()) player.health = player.getTotalMaxHealth();

        // Store targets for drawing the chain in UI
        player.chainSkillTargets = new ArrayList<>(targets);
        player.chainSkillActive = true;
        player.chainSkillAnimCounter = 30; // show for 30 frames
    }
}
