package item;

import entity.Entity;
import main.GamePanel;

public class ITEM_Chest_Key extends Entity{
	
	public ITEM_Chest_Key(GamePanel gp) {
		
		super(gp);

		name = "Chest_Key";
		down1 = setup("/objects/key");
		renderLayer = 1;
		pickable = true;
		stackable = true;
		type = 3;
		itemType = 4;
		
		solidArea.x = 30;
		solidArea.y = 25;
		solidArea.width = 15;
		solidArea.height = 35;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;

		collision = true;
	}
	
}
