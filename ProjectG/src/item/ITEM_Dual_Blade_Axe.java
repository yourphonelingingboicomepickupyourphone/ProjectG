package item;

import main.GamePanel;

public class ITEM_Dual_Blade_Axe extends ITEM_Axe_Normal {
    public ITEM_Dual_Blade_Axe(GamePanel gp) {
        super(gp);

        name = gp.ui.tr("item.dual_blade_axe.name");
        description = gp.ui.tr("item.dual_blade_axe.description");
        level = 1;
        down1 = setup("/items/dual_blade_axe");
        rarity = 0;
        cooldownBonus = 30;
        attackBonus = 1000;
        defenseBonus = 10;
        levelRequirement = 2; //level requirement to use the item

    }

    public void getImage() {
        down1 = setup("/items/dual_blade_axe");
        // ... set up other images as in the constructor ...
    }
}
