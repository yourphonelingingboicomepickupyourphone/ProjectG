package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Chest_Locked extends Entity{

	boolean locked = true;

	public OBJ_Chest_Locked(GamePanel gp) {
		
		super(gp);
		
		name = "Chest_Locked";
		down1 = setup("/objects/chest_1_locked");
		renderLayer = 1;
		pickable = false;
		type = 3;

		
		solidArea.x = 0;
		solidArea.y = 15;
		solidArea.width = 80;
		solidArea.height = 65;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;

		collision = true;
		this.locked = true; // Chest is locked by default
	}

	public void openChest() {
		if (!locked) {
			gp.gameState = gp.chestState; // Open the chest if it's not locked
		} else {
			gp.ui.addMessage("The chest is locked!"); // Show message if locked
		}
	}
}
