package skill;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import entity.Player;
import main.GamePanel;

public class SKILL_Earthquake implements Skill {

    private BufferedImage icon;
    
    private int manaCost = 100;
    private int cooldownMax = 600; // 10 seconds at 60 FPS
    private int cooldown = 0;
    private int levelRequirement = 5;

    public String getName(GamePanel gp) { return gp.ui.tr("skill.earthquake.name"); }
    public String getDescription(GamePanel gp) { return gp.ui.tr("skill.earthquake.description"); }
    public int getManaCost() { return manaCost; }
    public int getCooldownMax() { return cooldownMax; }
    public int getCooldown() { return cooldown; }
    public int getLevelRequirement() { return levelRequirement; }

    public SKILL_Earthquake(GamePanel gp) {
        try {
            icon = ImageIO.read(getClass().getResourceAsStream("/skills/skill_earthquake.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean canUse(int playerMana, int playerLevel) {
        return playerMana >= manaCost && cooldown == 0 && playerLevel >= levelRequirement;
    }

    public void use(Player player) {
        if (canUse(player.mana, player.level)) {
            player.mana -= manaCost;
            cooldown = cooldownMax;
            // Trigger earthquake effect here
        }
    }

    public void tickCooldown() {
        if (cooldown > 0) cooldown--;
    }

    public BufferedImage getIcon() {
        return icon;
    }

    public void applyEffect(GamePanel gp, Player player) {
        // No-op for dash, effect is handled in use() and tickDash()
    }
}
