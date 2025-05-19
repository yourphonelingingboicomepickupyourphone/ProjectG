package item;

import entity.Entity;
import main.GamePanel;

public class ITEM_Sword_Normal extends Entity{
    public ITEM_Sword_Normal(GamePanel gp) {
        super(gp);
        
        name = "Normal Sword";
        type = 0;
        level = 1;
        down1 = setup("/items/normal_sword");
        attackBonus = 10;
        cooldownBonus = 10; //time in ms to wait before the next attack
        rarity = 0; //0 = common, 1 = rare, 2 = epic, 3 = legendary
        levelRequirement = 1; //level requirement to use the item
        description = "A normal sword. It is not very powerful, but it is better than nothing." ;

        pickable = true;
        stackable = false;
    }

}
