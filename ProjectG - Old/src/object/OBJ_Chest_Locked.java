package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Chest_Locked extends Entity{

	public OBJ_Chest_Locked(GamePanel gp) {
		
		super(gp);
		
		name = "Chest_Locked";
		down1 = setup("/objects/chest_1_locked");

		
		solidArea.x = 0;
		solidArea.y = 15;
		solidArea.width = 80;
		solidArea.height = 65;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;

		collision = true;
	}
}
