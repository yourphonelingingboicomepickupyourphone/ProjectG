package main;

import entity.NPC_Wizard;
import item.ITEM_Dual_Blade_Axe;
import item.ITEM_Mini_Health_Potion;
import item.ITEM_Door_Key;
import monster.MON_rSlime;
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
		gp.obj[0] = new OBJ_SpawnPoint(gp);
		gp.obj[0].worldX = gp.tileSize * 48;
		gp.obj[0].worldY = gp.tileSize * 49;

		gp.obj[1] = new OBJ_Pillar(gp);
		gp.obj[1].worldX = gp.tileSize * 46;
		gp.obj[1].worldY = gp.tileSize * 43;

		gp.obj[2] = new OBJ_Pillar(gp);
		gp.obj[2].worldX = gp.tileSize * 53;
		gp.obj[2].worldY = gp.tileSize * 43;

		gp.obj[3] = new OBJ_Pillar(gp);
		gp.obj[3].worldX = gp.tileSize * 46;
		gp.obj[3].worldY = gp.tileSize * 49;

		gp.obj[4] = new OBJ_Pillar(gp);
		gp.obj[4].worldX = gp.tileSize * 53;
		gp.obj[4].worldY = gp.tileSize * 49;

		gp.obj[5] = new ITEM_Door_Key(gp);
		gp.obj[5].worldX = gp.tileSize * 50;
		gp.obj[5].worldY = gp.tileSize * 59;

		gp.obj[6] = new ITEM_Door_Key(gp);
		gp.obj[6].worldX = gp.tileSize * 54;
		gp.obj[6].worldY = gp.tileSize * 59;

		gp.obj[7]= new ITEM_Dual_Blade_Axe(gp);
		gp.obj[7].worldX = gp.tileSize * 50;
		gp.obj[7].worldY = gp.tileSize * 62;

		gp.obj[8] = new ITEM_Mini_Health_Potion(gp);
		gp.obj[8].worldX = gp.tileSize * 54;
		gp.obj[8].worldY = gp.tileSize * 62;

		gp.obj[9] = new ITEM_Mini_Health_Potion(gp);
		gp.obj[9].worldX = gp.tileSize * 54;
		gp.obj[9].worldY = gp.tileSize * 65;

	}

	public void setNPC() {
		gp.npc[0] = new NPC_Wizard(gp);
		gp.npc[0].worldX = gp.tileSize * 51;
		gp.npc[0].worldY = gp.tileSize * 80;

		gp.npc[1] = new NPC_Wizard(gp);
		gp.npc[1].worldX = gp.tileSize * 54;
		gp.npc[1].worldY = gp.tileSize * 84;
	}


	public void setMonster() {
		gp.monster[0] = new MON_rSlime(gp);
		gp.monster[0].worldX = gp.tileSize * 50;
		gp.monster[0].worldY = gp.tileSize * 60;

		gp.monster[1] = new MON_rSlime(gp);
		gp.monster[1].worldX = gp.tileSize * 54;
		gp.monster[1].worldY = gp.tileSize * 60;
	}
}
