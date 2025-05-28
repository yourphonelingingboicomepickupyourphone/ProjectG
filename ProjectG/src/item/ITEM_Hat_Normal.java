package item;

import entity.Entity;
import main.GamePanel;

public class ITEM_Hat_Normal extends Entity{
    public ITEM_Hat_Normal(GamePanel gp) {
        super(gp);
        
        name = gp.ui.tr("item.hat_normal.name");
        description = gp.ui.tr("item.hat_normal.description");
        level = 1;
        down1 = setup("/items/normal_hat");
        healthBonus = 10;
        defenseBonus = 5;
        type = 3;
        itemType = 1;

        pickable = true;
        stackable = false;
    }

}
