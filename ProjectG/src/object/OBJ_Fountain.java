package object;

import entity.Entity;
import main.GamePanel;


public class OBJ_Fountain extends Entity{
	
	public OBJ_Fountain(GamePanel gp) {
		
		super(gp);

		name = "Fountain";
		down1 = setup("/objects/healing_fountain");
		
		solidArea.x = 0;
		solidArea.y = 0;
		solidArea.width = 80;
		solidArea.height = 56;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;

		collision = true;
	}
	
}