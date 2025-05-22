package item;

import entity.Entity;
import main.GamePanel;

public class ITEM_Mini_Mana_Potion extends Entity{

    public ITEM_Mini_Mana_Potion(GamePanel gp) {
        super(gp);
        
        this.gp = gp;

        name = "Mini Mana Potion";
        level = 1;
        down1 = setup("/items/mana_potion");
        manaBonus = 30;
        rarity = 0; //0 = common, 1 = rare, 2 = epic, 3 = legendary
        levelRequirement = 0; //level requirement to use the item
        description = "A potion that restores mana. It is not very powerful, but it is better than nothing.";
        manaHeal = 100; //mana heal of the item
        
        type = 3;
        itemType = 6; //6 = potion
        pickable = true;
        stackable = true;
    }

    public void use(Entity user) {
        if (user.mana < user.maxMana) {
            user.mana += manaHeal;
            if (user.mana > user.maxMana) {
                user.mana = user.maxMana;
            }
            gp.ui.addMessage("You used a " + name + "!");
            gp.ui.addMessage("You restored " + manaHeal + " mana!");
        } else {
            gp.ui.addMessage("You are already at full mana!");
        }
    }

}
