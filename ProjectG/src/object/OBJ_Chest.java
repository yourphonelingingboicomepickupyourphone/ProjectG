package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Chest extends Entity{
	
	public OBJ_Chest(GamePanel gp) {
		
		super(gp);

		name = "Chest";
		down1 = setup("/objects/chest_1");
		renderLayer = 1;
		pickable = false;
		
		solidArea.x = 0;
		solidArea.y = 15;
		solidArea.width = 80;
		solidArea.height = 65;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		
		collision = true;
	}
	
	
}