package item;

import entity.Entity;
import main.GamePanel;

public class ITEM_Armor_Normal extends Entity{
    public ITEM_Armor_Normal(GamePanel gp) {
        super(gp);
        
        name = "Normal Armor";
        level = 1;
        down1 = setup("/items/normal_armor");
        healthBonus = 20;
        defenseBonus = 10;
    }

}
