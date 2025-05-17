package item;

import entity.Entity;
import main.GamePanel;

public class ITEM_Sword_Normal extends Entity{

    public ITEM_Sword_Normal(GamePanel gp) {
        super(gp);
        
        name = "Normal Sword";
        level = 1;
        down1 = setup("/items/normal_sword");
        attackBonus = 10;
    }

}
