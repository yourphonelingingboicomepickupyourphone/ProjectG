package effect;

import entity.Entity;
import main.GamePanel;

import java.awt.*;

public class EFFECT_EarthquakeWave extends Entity {
    private int duration = 120; // frames the wave lasts
    private int damage = gp.player.getTotalAttack() * 2;
    private int radius; // in pixels
    private boolean hasDealtDamage = false;

    public EFFECT_EarthquakeWave(GamePanel gp, int x, int y, int radius, int damage) {
        super(gp);
        this.worldX = x;
        this.worldY = y;
        this.radius = radius;
        this.damage = damage;
        this.alive = true;
    }

    @Override
    public void update() {
        duration--;
        if (!hasDealtDamage) {
            for (int i = 0; i < gp.monster[gp.currentMap].length; i++) {
                Entity m = gp.monster[gp.currentMap][i];
                if (m != null && m.alive && !m.dying) {
                    int dx = (worldX + gp.tileSize/2) - (m.worldX + m.solidArea.x + m.solidArea.width/2);
                    int dy = (worldY + gp.tileSize/2) - (m.worldY + m.solidArea.y + m.solidArea.height/2);
                    double dist = Math.sqrt(dx*dx + dy*dy);
                    if (dist <= radius) {
                        // Damage the monster
                        int dmg = damage - m.defense;
                        if (dmg < 0) dmg = 0;
                        m.health -= dmg;
                        m.timeSinceLastHit = 0;
                        m.invincible = true;
                        m.timeSinceLastHit = 0;
                        m.damageReaction();
                        m.showHpBar = true;
                        m.hpBarDisplayCounter = 150;
                        if (m.health <= 0 && !m.dying) {
                            m.dying = true;
                            m.dyingCounter = 0;
                            m.checkDrop();
                            gp.ui.addMessage(gp.ui.tr("message.defeat_monster", m.name));
                            gp.player.exp += m.expReward;
                            gp.player.checkLevelUp();
                        }
                    }
                }
            }
            hasDealtDamage = true;
        }
        if (duration <= 0) {
            alive = false;
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(new Color(180, 120, 60, 128));
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;
        g2.fillOval(screenX - radius, screenY - radius, radius * 2, radius * 2);
    }
}
