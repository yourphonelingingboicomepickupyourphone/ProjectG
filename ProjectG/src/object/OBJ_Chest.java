package object;

import java.util.ArrayList;

import entity.Entity;
import main.GamePanel;

public class OBJ_Chest extends Entity{
	
	public ArrayList<Entity> chestInventory = new ArrayList<>();
	
	public OBJ_Chest(GamePanel gp) {
		
		super(gp);

		name = "Chest";
		down1 = setup("/objects/chest_1");
		renderLayer = 1;
		pickable = false;
		type = 3; // 0 = player, 1 = npc, 2 = monster, 3 = object, 4 = projectile, 5 = effect, 6 = ui
		
		solidArea.x = 0;
		solidArea.y = 15;
		solidArea.width = 80;
		solidArea.height = 65;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		
		collision = true;
	}
	
	public void openChest(){
		gp.gameState = gp.chestState;
	}
	
}