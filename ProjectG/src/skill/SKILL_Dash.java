package skill;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import entity.Player;
import main.GamePanel;

public class SKILL_Dash implements Skill{

    private int manaCost = 50;
    private int cooldownMax = 1800;
    private int cooldown = 0;
    private int levelRequirement = 1;

    private static final int DASH_DURATION = 5; // seconds
    private static final int DASH_SPEED_BOOST = 3; // Speed boost during dash

    private boolean dashing = false;
    private int dashTimer = 0;
    private int originalSpeed = -1;

    private BufferedImage icon;

    public String getName(GamePanel gp) { return gp.ui.tr("skill.dash.name"); }
    public String getDescription(GamePanel gp) { return java.text.MessageFormat.format(gp.ui.tr("skill.dash.description"), DASH_SPEED_BOOST, DASH_DURATION, cooldownMax / 60.0); }
    public int getManaCost() { return manaCost; }
    public int getCooldownMax() { return cooldownMax; }
    public int getCooldown() { return cooldown; }
    public int getLevelRequirement() { return levelRequirement; }

    public SKILL_Dash(GamePanel gp) {
        try {
            icon = ImageIO.read(getClass().getResourceAsStream("/skills/skill_dash.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean canUse(int playerMana, int playerLevel) {
        return playerMana >= manaCost && cooldown == 0 && playerLevel >= levelRequirement;
    }

    // Only call this ONCE per use!
    public void use(Player player) {
        if (player.mana < manaCost || cooldown > 0 || player.level < levelRequirement) {
            // Not enough mana, on cooldown, or level too low: do nothing
            return;
        }
        player.mana -= manaCost;
        cooldown = cooldownMax;
        if (!dashing) {
            dashing = true;
            dashTimer = DASH_DURATION * 60; // 60 ticks per second
            if (originalSpeed == -1) {
                originalSpeed = player.speed;   // Save original speed only if not already dashing
            }
            player.speed = originalSpeed + DASH_SPEED_BOOST;
        }
    }

    // Call this every frame in Player.update()
    public void tickDash(Player player) {
        if (dashing) {
            dashTimer--;
            if (dashTimer <= 0) {
                dashing = false;
                player.speed = originalSpeed; // Restore original speed
                originalSpeed = -1;
            }
        }
    }

    public void tickCooldown() {
        if (cooldown > 0) cooldown--;
    }

    // Only apply the effect, do NOT call use() here!
    public void applyEffect(GamePanel gp, Player player) {
        // No-op for dash, effect is handled in use() and tickDash()
    }

    public BufferedImage getIcon() {
        return icon;
    }
}