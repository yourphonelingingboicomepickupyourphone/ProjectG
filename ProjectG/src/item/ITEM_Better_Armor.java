package item;

import main.GamePanel;

public class ITEM_Better_Armor extends ITEM_Armor_Normal{
    public ITEM_Better_Armor(GamePanel gp) {
        super(gp);
        
        name = "Better Armor";
        level = 1;
        down1 = setup("/items/normal_armor");
        healthBonus = 40;
        defenseBonus = 20;
        rarity = 0; //0 = common, 1 = rare, 2 = epic, 3 = legendary
        levelRequirement = 1; //level requirement to use the item
        description = "Better armor. It is not very powerful, but it is better than nothing.";

    }

}
