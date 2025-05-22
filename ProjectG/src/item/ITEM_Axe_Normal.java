package item;

import entity.Entity;
import main.GamePanel;

public class ITEM_Axe_Normal extends Entity {
    public ITEM_Axe_Normal(GamePanel gp) {
        super(gp);

        name = "Normal Axe";
        type = 3;
        itemType = 0;
        level = 1;
        down1 = setup("/items/normal_axe");

        //Attack animation
        attackUp1 = setup("/player/attack_axe_up_1");
        attackUp2 = setup("/player/attack_axe_up_2");
        attackDown1 = setup("/player/attack_axe_down_1");
        attackDown2 = setup("/player/attack_axe_down_2");
        attackLeft1 = setup("/player/attack_axe_left_1");
        attackLeft2 = setup("/player/attack_axe_left_2");
        attackRight1 = setup("/player/attack_axe_right_1");
        attackRight2 = setup("/player/attack_axe_right_2");

        attackBonus = 10;
        cooldownBonus = 10; //time in ms to wait before the
        // next attack
        rarity = 0; //0 = common, 1 = rare, 2 = epic, 3 = legendary
        levelRequirement = 1; //level requirement to use the item
        attackRange = 1; //attack range of the item
        weaponType = 1; //0 = sword, 1 = axe, 2 = spear, 3 = bow, 4 = wand
        description = "A normal axe. It is not very powerful, but it is better than nothing." ;

        pickable = true;
        stackable = false;
    }
}
