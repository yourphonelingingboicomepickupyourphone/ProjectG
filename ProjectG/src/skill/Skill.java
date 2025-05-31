package skill;

import java.awt.image.BufferedImage;
import java.io.Serializable;

import entity.Player;
import main.GamePanel;

public interface Skill extends Serializable {
    String getName();
    String getDescription();
    int getManaCost();
    int getCooldownMax();
    int getCooldown();
    int getLevelRequirement();
    BufferedImage getIcon();

    boolean canUse(int playerMana, int playerLevel);
    void use(Player player);
    void tickCooldown();
    void applyEffect(GamePanel gp, Player player);
}
