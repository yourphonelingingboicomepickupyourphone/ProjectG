package main;

import java.util.Random;

import entity.Entity;
import entity.NPC_Wizard;
import item.ITEM_Dual_Blade_Axe;
import item.ITEM_Mini_Health_Potion;
import item.ITEM_Spear_Normal;
import item.ITEM_Sword_Normal;
import item.ITEM_Armor_Normal;
import item.ITEM_Better_Armor;
import item.ITEM_Door_Key;
import monster.MON_rSlime;
import object.OBJ_Chest;
// import object.OBJ_Chest;
// import object.OBJ_Chest_Locked;
// import object.OBJ_Door_Closed;
// import object.OBJ_Door_Key;
// import object.OBJ_Door_Open;
// import object.OBJ_Fountain;
// import object.OBJ_Key;
import object.OBJ_Pillar;
import object.OBJ_SpawnPoint;

public class AssetSetter {
	
	GamePanel gp;
	
	public AssetSetter(GamePanel gp) {
		this.gp = gp;
	}
	
	public void setObject() {
	

		int map = 0;
		int i = 0;
		gp.obj[map][i] = new OBJ_Chest(gp);
		gp.obj[map][i].worldX = gp.tileSize * 47;
		gp.obj[map][i].worldY = gp.tileSize * 50;
		i++;
		gp.obj[map][i] = new ITEM_Sword_Normal(gp);
		gp.obj[map][i].worldX = gp.tileSize * 50;
		gp.obj[map][i].worldY = gp.tileSize * 47;
		i++;
		gp.obj[map][i] = new ITEM_Spear_Normal(gp);
		gp.obj[map][i].worldX = gp.tileSize * 50;
		gp.obj[map][i].worldY = gp.tileSize * 51;
		i++;

		map = 1;
		i = 0;
		gp.obj[map][i] = new OBJ_SpawnPoint(gp);
		gp.obj[map][i].worldX = gp.tileSize * 48;
		gp.obj[map][i].worldY = gp.tileSize * 49;
		i++;

		gp.obj[map][i] = new OBJ_Pillar(gp);
		gp.obj[map][i].worldX = gp.tileSize * 46;
		gp.obj[map][i].worldY = gp.tileSize * 43;
		i++;

		gp.obj[map][i] = new OBJ_Pillar(gp);
		gp.obj[map][i].worldX = gp.tileSize * 53;
		gp.obj[map][i].worldY = gp.tileSize * 43;
		i++;

		gp.obj[map][i] = new OBJ_Pillar(gp);
		gp.obj[map][i].worldX = gp.tileSize * 46;
		gp.obj[map][i].worldY = gp.tileSize * 49;
		i++;

		gp.obj[map][i] = new OBJ_Pillar(gp);
		gp.obj[map][i].worldX = gp.tileSize * 53;
		gp.obj[map][i].worldY = gp.tileSize * 49;
		i++;

		gp.obj[map][i] = new ITEM_Door_Key(gp);
		gp.obj[map][i].worldX = gp.tileSize * 50;
		gp.obj[map][i].worldY = gp.tileSize * 59;
		i++;

		gp.obj[map][i] = new ITEM_Door_Key(gp);
		gp.obj[map][i].worldX = gp.tileSize * 54;
		gp.obj[map][i].worldY = gp.tileSize * 59;
		i++;

		gp.obj[map][i]= new ITEM_Dual_Blade_Axe(gp);
		gp.obj[map][i].worldX = gp.tileSize * 50;
		gp.obj[map][i].worldY = gp.tileSize * 62;
		i++;

		gp.obj[map][i] = new ITEM_Mini_Health_Potion(gp);
		gp.obj[map][i].worldX = gp.tileSize * 54;
		gp.obj[map][i].worldY = gp.tileSize * 62;
		i++;

		gp.obj[map][i] = new ITEM_Mini_Health_Potion(gp);
		gp.obj[map][i].worldX = gp.tileSize * 54;
		gp.obj[map][i].worldY = gp.tileSize * 65;
		i++;

		gp.obj[map][i] = new ITEM_Armor_Normal(gp);
		gp.obj[map][i].worldX = gp.tileSize * 50;
		gp.obj[map][i].worldY = gp.tileSize * 65;
		i++;

		gp.obj[map][i] = new ITEM_Better_Armor(gp);
		gp.obj[map][i].worldX = gp.tileSize * 50;
		gp.obj[map][i].worldY = gp.tileSize * 68;
		i++;

	}


	public void setNPC() {
		int map = 1;
		int i = 0;
		gp.npc[map][i] = new NPC_Wizard(gp);
		gp.npc[map][i].worldX = gp.tileSize * 51;
		gp.npc[map][i].worldY = gp.tileSize * 80;
		i++;

		gp.npc[map][i] = new NPC_Wizard(gp);
		gp.npc[map][i].worldX = gp.tileSize * 54;
		gp.npc[map][i].worldY = gp.tileSize * 84;
		i++;
	}


	public void setMonster() {
		int map = 1;
		int i = 0;
		spawnRandomMonstersInArea(map, 45, 45, 55, 55, 2);
	}

	public void spawnRandomMonstersInArea(int map, int startCol, int startRow, int endCol, int endRow, int monsterCount) {
	    // Define the excluded area (example: columns 48-50, rows 48-50)
	    int exStartCol = 48, exStartRow = 49, exEndCol = 52, exEndRow = 51;

	    Random rand = new Random();
	    int placed = 0;
	    while (placed < monsterCount) {
	        int col = rand.nextInt(endCol - startCol + 1) + startCol;
	        int row = rand.nextInt(endRow - startRow + 1) + startRow;

	        // Exclude the sub-area
	        if (col >= exStartCol && col <= exEndCol && row >= exStartRow && row <= exEndRow) {
	            continue; // Skip this tile, it's in the excluded area
	        }

	        int x = col * gp.tileSize;
	        int y = row * gp.tileSize;

	        // Only place if the spot is empty
	        boolean spotTaken = false;
	        for (Entity m : gp.monster[map]) {
	            if (m != null && m.worldX == x && m.worldY == y) {
	                spotTaken = true;
	                break;
	            }
	        }
	        if (spotTaken) continue;

	        // Find an empty slot in the monster array
	        for (int i = 0; i < gp.monster[map].length; i++) {
	            if (gp.monster[map][i] == null) {
	                gp.monster[map][i] = new MON_rSlime(gp, 1);
	                gp.monster[map][i].worldX = x;
	                gp.monster[map][i].worldY = y;
	                placed++;
	                break;
	            }
	        }
	    }
	}
}
