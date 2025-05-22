package item;

import entity.Entity;
import main.GamePanel;

public class ITEM_Door_Key extends Entity{

	public ITEM_Door_Key(GamePanel gp) {
		
		super(gp);

		name = "Door Key";
		down1 = setup("/objects/door_key");
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

		description = "A key to open every door.";
	}
	
}