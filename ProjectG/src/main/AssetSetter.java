package main;

import entity.NPC_Wizard;
import item.ITEM_Dual_Blade_Axe;
import item.ITEM_Mini_Health_Potion;
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
		gp.obj[map][i].worldX = gp.tileSize * 54;
		gp.obj[map][i].worldY = gp.tileSize * 65;
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
		gp.monster[map][i] = new MON_rSlime(gp);
		gp.monster[map][i].worldX = gp.tileSize * 50;
		gp.monster[map][i].worldY = gp.tileSize * 60;
		i++;

		gp.monster[map][i] = new MON_rSlime(gp);
		gp.monster[map][i].worldX = gp.tileSize * 54;
		gp.monster[map][i].worldY = gp.tileSize * 60;
		i++;
	}
}
