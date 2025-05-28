package item;

import entity.Entity;
import main.GamePanel;

public class ITEM_Armor_Normal extends Entity{
    public ITEM_Armor_Normal(GamePanel gp) {
        super(gp);
        
        name = gp.ui.tr("item.armor_normal.name");
        type = 3;
        itemType = 2;
        level = 1;
        down1 = setup("/items/normal_armor");
        healthBonus = 20;
        defenseBonus = 10;
        rarity = 0; //0 = common, 1 = rare, 2 = epic, 3 = legendary
        levelRequirement = 1; //level requirement to use the item
        description = gp.ui.tr("item.armor_normal.description");

        pickable = true;
        stackable = false;
    }

}