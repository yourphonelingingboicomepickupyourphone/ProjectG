package item;

import entity.Entity;
import entity.Player;
import main.GamePanel;

public class ITEM_Mini_Mana_Potion extends Entity{

    public ITEM_Mini_Mana_Potion(GamePanel gp) {
        super(gp);
        
        this.gp = gp;

        name = gp.ui.tr("item.mini_mana_potion.name");
        level = 1;
        down1 = setup("/items/mana_potion");
        manaBonus = 30;
        rarity = 0; //0 = common, 1 = rare, 2 = epic, 3 = legendary
        levelRequirement = 0; //level requirement to use the item
        description = gp.ui.tr("item.mini_mana_potion.description");
        manaHeal = 100; //mana heal of the item
        
        type = 3;
        itemType = 6; //6 = potion
        pickable = true;
        stackable = true;
    }

    public void getImage() {
        down1 = setup("/items/mana_potion");
    }


    public void use(Player player) {
        if (player.mana < player.getTotalMaxMana()) {
            player.mana += manaHeal;
            if (player.mana > player.getTotalMaxMana()) {
                player.mana = player.getTotalMaxMana();
            }
            gp.ui.addMessage(gp.ui.tr("message.use_item", name));
            gp.ui.addMessage(gp.ui.tr("message.restore_mana", manaHeal));
        } else {
            gp.ui.addMessage(gp.ui.tr("message.full_mana"));
        }
    }

}
