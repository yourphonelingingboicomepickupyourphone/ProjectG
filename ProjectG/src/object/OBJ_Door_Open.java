package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Door_Open extends Entity{
	
	public OBJ_Door_Open(GamePanel gp) {
		
		super(gp);

		name = "Door_Open";
		down1 = setup("/objects/door_wooden_open");
		
		solidArea.x = 0;
		solidArea.y = 0;
		solidArea.width = 80;
		solidArea.height = 80;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		
		collision = false;
	}
}
