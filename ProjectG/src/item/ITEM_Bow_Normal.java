package item;

import entity.Entity;
import main.GamePanel;

public class ITEM_Bow_Normal extends Entity {
    public ITEM_Bow_Normal(GamePanel gp) {
        super(gp);
        
        name = gp.ui.tr("item.bow_normal.name");
        type = 3;
        itemType = 0;
        level = 1;
        down1 = setup("/items/normal_bow");
        attackBonus = 10;
        cooldownBonus = 10; //time in ms to wait before the
        // next attack
        rarity = 0; //0 = common, 1 = rare, 2 = epic, 3 = legendary
        levelRequirement = 1; //level requirement to use the item
        attackRange = 3; //attack range of the item
        weaponType = 3; //0 = sword, 1 = axe, 2 = spear, 3 = bow, 4 = wand
        description = gp.ui.tr("item.bow_normal.description"); ;

        pickable = true;
        stackable = false;
    }

    public void getImage() {
        down1 = setup("/items/normal_bow");
        // ... set up other images as in the constructor ...
    }

}
