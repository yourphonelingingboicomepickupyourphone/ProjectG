package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Door_Closed extends Entity{
	
	public OBJ_Door_Closed(GamePanel gp) {
		
		super(gp);

		name = "Door_Closed";
		down1 = setup("/objects/door_wooden_close");
		renderLayer = 1;
		
		solidArea.x = 0;
		solidArea.y = 0;
		solidArea.width = 80;
		solidArea.height = 80;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;

		collision = true;
	}
}
