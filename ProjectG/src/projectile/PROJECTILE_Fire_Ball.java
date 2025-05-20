package projectile;

import entity.Projectile;
import main.GamePanel;

public class PROJECTILE_Fire_Ball extends Projectile {
    public PROJECTILE_Fire_Ball(GamePanel gp) {
        super(gp);
        this.gp = gp;

        name = "Fire Ball";
        speed = 8;
        attackBonus = 50;
        manaCost = 50;
        alive = false;
        getImage();
     
    }

    public void getImage() {
        up1 = setup("/projectiles/fire_ball_up_1");
        up2 = setup("/projectiles/fire_ball_up_2");
        down1 = setup("/projectiles/fire_ball_down_1");
        down2 = setup("/projectiles/fire_ball_down_2");
        left1 = setup("/projectiles/fire_ball_left_1");
        left2 = setup("/projectiles/fire_ball_left_2");
        right1 = setup("/projectiles/fire_ball_right_1");
        right2 = setup("/projectiles/fire_ball_right_2");
    }
}
