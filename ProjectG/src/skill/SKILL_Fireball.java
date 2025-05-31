package skill;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import entity.Player;
import main.GamePanel;

public class SKILL_Fireball implements Skill {
    private String name;
    private String description;
    private int manaCost = 20;
    private int cooldownMax = 180;
    private int cooldown = 0;
    private int levelRequirement = 1;
    private BufferedImage icon;

    public SKILL_Fireball(GamePanel gp) {
        this.name = gp.ui.tr("skill.fireball.name");
        this.description = gp.ui.tr("skill.fireball.description");
        try {
            icon = ImageIO.read(getClass().getResourceAsStream("/skills/skill_fireball.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getManaCost() { return manaCost; }
    public int getCooldownMax() { return cooldownMax; }
    public int getCooldown() { return cooldown; }
    public int getLevelRequirement() { return levelRequirement; }

    public boolean canUse(int playerMana, int playerLevel) {
        return playerMana >= manaCost && cooldown == 0 && playerLevel >= levelRequirement;
    }

    public void use(Player player) {
        cooldown = cooldownMax;
    }

    public void tickCooldown() {
        if (cooldown > 0) cooldown--;
    }

    public BufferedImage getIcon() {
        return icon;
    }

    public void applyEffect(GamePanel gp, Player player) {
        
    }


}