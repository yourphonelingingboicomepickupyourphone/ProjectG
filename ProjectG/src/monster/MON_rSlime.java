package monster;

import java.util.Random;

import entity.Entity;
import main.GamePanel;

public class MON_rSlime extends Entity{

    public MON_rSlime(GamePanel gp) {
        super(gp);
        name = "Red Slime";
        type = 1;
        
        level = 1;
        speed = 1;
        maxHealth = 70;
        health = maxHealth;
        attack = 20;
        defense = 0;
        expReward = 10;

        collision = true;
        renderLayer = 1; 
   
        solidArea.x = 8;
        solidArea.y = 30;
        solidArea.width = 64;
        solidArea.height = 50;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();
        setAction();
    }

    public void getImage(){
        up1 = setup("/monsters/slime_up_1");
        up2 = setup("/monsters/slime_up_2");
        down1 = setup("/monsters/slime_down_1");
        down2 = setup("/monsters/slime_down_2");
        left1 = setup("/monsters/slime_left_1");
        left2 = setup("/monsters/slime_left_2");
        right1 = setup("/monsters/slime_right_1");
        right2 = setup("/monsters/slime_right_2");

        
    }

    public void setAction() {
        actionLockCounter++;
            if(actionLockCounter == 240) //direction changes after 4 secs
            {
                Random random = new Random();
                int i = random.nextInt(100) + 1;  // pick up a number from 1 to 100
                if(i <= 25)
                {
                    direction = "up";
                }
                if(i>25 && i <= 50)
                {
                    direction = "down";
                }
                if(i>50 && i <= 75)
                {
                    direction = "left";
                }
                if(i>75 && i <= 100)
                {
                    direction = "right";
                }
                actionLockCounter = 0; // reset
            }
    }

    public void damageReaction() {
        direction = gp.player.direction;
        actionLockCounter = 0;
    }

}
