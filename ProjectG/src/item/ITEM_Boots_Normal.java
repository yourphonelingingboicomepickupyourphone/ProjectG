package item;

import entity.Entity;
import main.GamePanel;

public class ITEM_Boots_Normal extends Entity{
    public ITEM_Boots_Normal(GamePanel gp) {
        super(gp);
        
        name = "Normal Boots";
        type = 3;
        itemType = 3;
        level = 1;
        down1 = setup("/items/normal_boots");
        speedBonus = 2;

        pickable = true;
        stackable = false;
    }

}
