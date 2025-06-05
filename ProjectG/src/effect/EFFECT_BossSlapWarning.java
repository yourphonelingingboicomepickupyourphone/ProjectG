package effect;

import entity.Entity;
import main.GamePanel;
import java.awt.*;

public class EFFECT_BossSlapWarning extends Entity {
    private int duration; // frames to show warning

    public EFFECT_BossSlapWarning(GamePanel gp, int x, int y, int duration) {
        super(gp);
        this.worldX = x;
        this.worldY = y;
        this.duration = duration;
        this.alive = true;
    }

    @Override
    public void update() {
        duration--;
        if (duration <= 0) {
            alive = false;
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;
        g2.setColor(new Color(255, 0, 0, 120));
        g2.fillOval(screenX, screenY, gp.tileSize, gp.tileSize);
    }
}
