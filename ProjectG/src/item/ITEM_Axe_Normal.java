package item;

import entity.Entity;
import main.GamePanel;

public class ITEM_Axe_Normal extends Entity {
    public ITEM_Axe_Normal(GamePanel gp) {
        super(gp);

        name = "Normal Axe";
        type = 0;
        level = 1;
        down1 = setup("/items/normal_axe");

        //Attack animation
        attackUp1 = setup("/items/normal_axe_up");
        attackUp2 = setup("/items/normal_axe_up_2");
        attackDown1 = setup("/items/normal_axe_down");
        attackDown2 = setup("/items/normal_axe_down_2");
        attackLeft1 = setup("/items/normal_axe_left");
        attackLeft2 = setup("/items/normal_axe_left_2");
        attackRight1 = setup("/items/normal_axe_right");
        attackRight2 = setup("/items/normal_axe_right_2");

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
