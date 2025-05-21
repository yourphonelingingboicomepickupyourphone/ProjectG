package monster;

import java.util.Random;

import entity.Entity;
import item.ITEM_Axe_Normal;
import item.ITEM_Bow_Normal;
import item.ITEM_Mini_Health_Potion;
import item.ITEM_Mini_Mana_Potion;
import item.ITEM_Spear_Normal;
import item.ITEM_Staff_Normal;
import item.ITEM_Sword_Normal;
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
        attack = 700;
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

public void checkDrop() {
    class DropEntry {
        Entity item;
        int weight; // Higher = more common

        DropEntry(Entity item, int weight) {
            this.item = item;
            this.weight = weight;
        }
    }

    DropEntry[] drops = new DropEntry[] {
        new DropEntry(null, 80),                            // Percentage chance of no drop
        new DropEntry(new ITEM_Mini_Health_Potion(gp), 60),      // Very common
        new DropEntry(new ITEM_Mini_Mana_Potion(gp), 60),        // Very common
        new DropEntry(new ITEM_Sword_Normal(gp), 20),            // Rare
        new DropEntry(new ITEM_Axe_Normal(gp), 20),              // Rare
        new DropEntry(new ITEM_Staff_Normal(gp), 20),            // Rare
        new DropEntry(new ITEM_Spear_Normal(gp),20),             // Rare
        new DropEntry(new ITEM_Bow_Normal(gp), 20)               // Rare
        // Add more items and adjust weights as needed
    };

    // Calculate total weight
    int totalWeight = 0;
    for (DropEntry entry : drops) {
        totalWeight += entry.weight;
    }

    // Pick a random number in the total weight range
    int r = new Random().nextInt(totalWeight);
    int sum = 0;
    for (DropEntry entry : drops) {
        sum += entry.weight;
        if (r < sum) {
            if (entry.item != null) {
                dropItem(entry.item);
            }
            break; // Exit the loop once we find the right drop
            
        }
    }
}

}
