package item;

import main.GamePanel;

public class ITEM_Dual_Blade_Axe extends ITEM_Axe_Normal {
    public ITEM_Dual_Blade_Axe(GamePanel gp) {
        super(gp);

        name = "Dual Blade Axe";
        level = 1;
        down1 = setup("/items/dual_blade_axe");
        rarity = 0;
        cooldownBonus = 30;
        attackBonus = 50;
        defenseBonus = 10;
        levelRequirement = 2; //level requirement to use the item



        description = "A powerful axe with dual blades. It can deal massive damage, but is heavy to wield.";
    }

}
