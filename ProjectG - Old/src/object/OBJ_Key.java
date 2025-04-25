package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Key extends Entity{
	
	public OBJ_Key(GamePanel gp) {
		
		super(gp);

		name = "Chest_Key";
		down1 = setup("/objects/key");
		
		solidArea.x = 30;
		solidArea.y = 25;
		solidArea.width = 15;
		solidArea.height = 35;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;

		collision = true;
	}
	
}
