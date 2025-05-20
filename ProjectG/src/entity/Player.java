package entity;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import item.ITEM_Sword_Normal;
import main.GamePanel;
import main.KeyHandler;

public class Player extends Entity{
	
	KeyHandler keyH;
	
	public final int screenX;
	public final int screenY;
	public boolean justFinishTalking = false;
	int standCounter = 0;
	public boolean attackCancel = false;
	public ArrayList<Entity> inventory = new ArrayList<>();
	public int maxInventorySize = 24;

	int collisionRecoilCounter = 0;
    final int RECOIL_DURATION = 10;
    public int attackCooldown = 0;
    public int ATTACK_COOLDOWN_MAX; 

	public Player(GamePanel gp, KeyHandler kH) {

		super(gp);
		this.keyH = kH;

		screenX = gp.screenWidth/2 - gp.tileSize/2;
		screenY = gp.screenHeight/2 - gp.tileSize/2;
		
		solidArea = new Rectangle();
		solidArea.x = 40;
		solidArea.y = 40;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		solidArea.width = 80;
		solidArea.height= 80;
		
		
		setDefaultValues();
		getPlayerImage();
		getPlayerAttackImage();
		setItems();
	}
	
	public void setDefaultValues() {
		worldX = gp.tileSize * 50 - gp.tileSize/2;
		worldY = gp.tileSize * 50 - gp.tileSize/2;

		int defaultSpeed = 5;
		speed = defaultSpeed;
		direction = "down";

		//Status
		this.name = "";
		this.maxHealth = 1800;
		this.health = maxHealth;
		this.maxMana = 400;
		this.mana = maxMana;
		this.type = 0;
		this.level = 1;
		this.attack = 50;
		this.defense = 10;
		this.exp = 0;
		this.speed = 5;
		this.nextLevelExp = 10;
		this.totalProgressionPoints = 0;
		this.progressionPoints = 0;
		this.progressionHealthUpgrades = 0;
		this.progressionManaUpgrades = 0;
		this.progressionAttackUpgrades = 0;
		this.progressionDefenseUpgrades = 0;
		this.ATTACK_COOLDOWN_MAX = 30; // 30 frames = 0.5s at 60fps
		currentWeapon = new ITEM_Sword_Normal(gp);
		currentArmor = null;
		currentHat = null;
		currentBoots = null;
		ATTACK_COOLDOWN_MAX += currentWeapon.cooldownBonus;



	}

	public void setItems(){
		stackInventory(); 
	}

	public void getPlayerImage() {

		up1 = setup("/player/player_up_1");
		up2 = setup("/player/player_up_2");
		down1 = setup("/player/player_down_1");
		down2 = setup("/player/player_down_2");
		left1 = setup("/player/player_left_1");
		left2 = setup("/player/player_left_2");
		right1 = setup("/player/player_right_1");
		right2 = setup("/player/player_right_2");
		stand = setup("/player/player_stand");
		standLeft = setup("/player/player_stand_left");
		standRight = setup("/player/player_stand_right");
		standUp = setup("/player/player_stand_up");
		fullBody = setup("/player/player_full_body");


	}

	public void getPlayerAttackImage() {
		attackUp1 = currentWeapon.attackUp1;
		attackUp2 = currentWeapon.attackUp2;
		attackDown1 = currentWeapon.attackDown1;
		attackDown2 = currentWeapon.attackDown2;
		attackLeft1 = currentWeapon.attackLeft1;
		attackLeft2 = currentWeapon.attackLeft2;
		attackRight1 = currentWeapon.attackRight1;
		attackRight2 = currentWeapon.attackRight2;

	}

	public void update() {

		if (attackCooldown > 0) {
            attackCooldown--;
        }

		if (attacking == true) {
			attacking();
			return;
		}

		if (keyH.spacePressed == true && attackCooldown == 0) {
			attacking = true;
			attackCooldown = ATTACK_COOLDOWN_MAX; // Reset cooldown
			keyH.spacePressed = false;
			return;
		}
		
		if (collisionRecoilCounter > 0) {
			collisionRecoilCounter--;
			standCounter = 0; // Reset standCounter when recoiling
			spriteNum = 1; // Set spriteNum to 1 during recoil
			return; // Skip the rest of the update method during recoil
		}

		if (keyH.upPressed == true || keyH.downPressed == true || 
				keyH.leftPressed == true || keyH.rightPressed == true || keyH.enterPressed == true) {
			
			if (keyH.upPressed == true) {
				direction = "up";
			}
			else if (keyH.downPressed == true) {
				direction = "down";
			}
			else if (keyH.leftPressed == true) {
				direction = "left";
			}
			else if (keyH.rightPressed == true) {
				direction = "right";
			}			 
			
			//Check tile collision
			collisionOn = false;
			gp.cChecker.checkTile(this);

			//Check object collision
			int objIndex = gp.cChecker.checkObject(this, true);
			if (keyH.enterPressed == true) {
				pickUpObject(objIndex);
				keyH.enterPressed = false; // Reset enterPressed after picking up
			}

			//Check NPC collision

			int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
			interactNPC(npcIndex);

			//Check monster collision
			int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
			contactMonster(monsterIndex);
			
			//Check event
			gp.eHandler.checkEvent();
			keyH.enterPressed = false; // Reset enterPressed after checking for events
			
			
			if(collisionOn == false && keyH.enterPressed == false) {
				switch(direction) {
				case "up":
					worldY -= speed;
					break;
				case "down":
					worldY += speed;
					break;
				case "left":
					worldX -= speed;
					break;
				case "right":
					worldX += speed;
					break;
				}
			} else {
				collisionRecoilCounter = RECOIL_DURATION;
			}

			attackCancel = false;

			spriteCounter++;
			if(spriteCounter > 14) {
				if(spriteNum == 1) {
					spriteNum = 2;
				}
				else if(spriteNum == 2) {
					spriteNum = 1;
				}
				spriteCounter = 0;
			}	
			
		}
		else {
			standCounter++;
			if(standCounter > 20) {
				standCounter = 0;
				spriteNum = 1;
			}

		}

		if (invincible == true) {
			invincibleCounter++;
			if (invincibleCounter > 20) {
				invincible = false;
				invincibleCounter = 0;
			}
		}
		
		if (justFinishTalking == true) {
			justFinishTalking = false;
		}

	}
	
	public void attacking(){

		spriteCounter++;

		if(spriteCounter <= 5){
			spriteNum = 1;
		}
		if (spriteCounter > 5 && spriteCounter <= 25){
			spriteNum = 2;

			int range = 1;
			int hitboxType = 0;
			if (currentWeapon != null) {
				range = currentWeapon.attackRange;
				hitboxType = currentWeapon.weaponType;
			}

			// --- Custom attack hitbox for each direction ---
			if (hitboxType == 0){
				Rectangle attackHitbox = new Rectangle();

				// Example custom sizes for each direction
				int upWidth = 110;
				int upHeight = 40;
				int downWidth = 110;
				int downHeight = 40;
				int leftWidth = 50;
				int leftHeight = 65;
				int rightWidth = 50;
				int rightHeight = 65;

				switch (direction) { 
					case "up":
						attackHitbox.x = worldX - (upWidth - gp.tileSize)/2;
						attackHitbox.y = worldY - upHeight;
						attackHitbox.width = upWidth;
						attackHitbox.height = upHeight;
						break;
					case "down":
						attackHitbox.x = worldX + gp.tileSize + (downWidth - gp.tileSize)/2;
						attackHitbox.y = worldY + gp.tileSize + downHeight;
						attackHitbox.width = downWidth;
						attackHitbox.height = downHeight;
						break;
					case "left":
						attackHitbox.x = worldX - leftWidth;
						attackHitbox.y = worldY + 10;
						attackHitbox.width = leftWidth;
						attackHitbox.height = leftHeight;
						break;
					case "right":
						attackHitbox.x = worldX + gp.tileSize + rightWidth;
						attackHitbox.y = worldY + gp.tileSize + 10;
						attackHitbox.width = rightWidth;
						attackHitbox.height = rightHeight;
						break;
				}
				// Check collision with monsters using the attackHitbox
				for (int i = 0; i < gp.monster.length; i++) {
					Entity monster = gp.monster[i];
					if (monster != null && monster.alive) {
						Rectangle monsterHitbox = new Rectangle(
							monster.worldX + monster.solidArea.x,
							monster.worldY + monster.solidArea.y,
							monster.solidArea.width,
							monster.solidArea.height
						);
						if (attackHitbox.intersects(monsterHitbox)) {
							damageMonster(i);
							// When damaging a monster
							gp.monster[i].showHpBar = true;
							gp.monster[i].hpBarDisplayCounter = 150; // Show for 2.5 second (150 frames)
						}
					}
				}
			} 
			if (hitboxType == 1){
				Rectangle attackHitbox = new Rectangle();

				// Example custom sizes for each direction
				int upWidth = 110;
				int upHeight = 40;
				int downWidth = 110;
				int downHeight = 40;
				int leftWidth = 50;
				int leftHeight = 65;
				int rightWidth = 50;
				int rightHeight = 65;

				switch (direction) { 
					case "up":
						attackHitbox.x = worldX - (upWidth - gp.tileSize)/2;
						attackHitbox.y = worldY - upHeight;
						attackHitbox.width = upWidth;
						attackHitbox.height = upHeight;
						break;
					case "down":
						attackHitbox.x = worldX + gp.tileSize + (downWidth - gp.tileSize)/2;
						attackHitbox.y = worldY + gp.tileSize + downHeight;
						attackHitbox.width = downWidth;
						attackHitbox.height = downHeight;
						break;
					case "left":
						attackHitbox.x = worldX - leftWidth;
						attackHitbox.y = worldY + 10;
						attackHitbox.width = leftWidth;
						attackHitbox.height = leftHeight;
						break;
					case "right":
						attackHitbox.x = worldX + gp.tileSize + rightWidth;
						attackHitbox.y = worldY + gp.tileSize + 10;
						attackHitbox.width = rightWidth;
						attackHitbox.height = rightHeight;
						break;
				}
				for (int i = 0; i < gp.monster.length; i++) {
					Entity monster = gp.monster[i];
					if (monster != null && monster.alive) {
						Rectangle monsterHitbox = new Rectangle(
							monster.worldX + monster.solidArea.x,
							monster.worldY + monster.solidArea.y,
							monster.solidArea.width,
							monster.solidArea.height
						);
						if (attackHitbox.intersects(monsterHitbox)) {
							damageMonster(i);
							gp.monster[i].showHpBar = true; // Show HP bar
							gp.monster[i].hpBarDisplayCounter = 150; // Show for 2.5 seconds
						}
					}
				}
			}

			if (hitboxType == 2){
				Rectangle attackHitbox = new Rectangle();

				switch (direction) { 
					case "up":
						attackHitbox.x = worldX;
						attackHitbox.y = worldY;
						attackHitbox.width = gp.tileSize;
						attackHitbox.height = gp.tileSize * 2;
						break;
					case "down":
						attackHitbox.x = worldX + gp.tileSize;
						attackHitbox.y = worldY + gp.tileSize;
						attackHitbox.width = gp.tileSize;
						attackHitbox.height = gp.tileSize * 2;
						break;
					case "left":
						attackHitbox.x = worldX - gp.tileSize * 2;
						attackHitbox.y = worldY;
						attackHitbox.width = gp.tileSize * 2;
						attackHitbox.height = gp.tileSize;
						break;
					case "right":
						attackHitbox.x = worldX + gp.tileSize;
						attackHitbox.y = worldY;
						attackHitbox.width = gp.tileSize * 2;
						attackHitbox.height = gp.tileSize;
						break;
				}
				for (int i = 0; i < gp.monster.length; i++) {
					Entity monster = gp.monster[i];
					if (monster != null && monster.alive) {
						Rectangle monsterHitbox = new Rectangle(
							monster.worldX + monster.solidArea.x,
							monster.worldY + monster.solidArea.y,
							monster.solidArea.width,
							monster.solidArea.height
						);
						if (attackHitbox.intersects(monsterHitbox)) {
							damageMonster(i);
							gp.monster[i].showHpBar = true; // Show HP bar
							gp.monster[i].hpBarDisplayCounter = 150; // Show for 2.5 seconds
						}
					}
				}
			}

			
		}
		if (spriteCounter > 25){
			spriteNum = 1;
			spriteCounter = 0;
			attacking = false;
		}
	}

	public void pickUpObject(int i) {
	    if (i != 999 && gp.obj[i].pickable) {
			if (inventory.size() != maxInventorySize) {
				Entity picked = gp.obj[i];
				boolean stacked = false;
				// Try to stack if same item exists
				for (Entity item : inventory) {
					if (item != null && item.getClass() == picked.getClass() && item.stackable) {
						item.quantity++;
						stacked = true;
						break;
					}
				}
				// If not stacked, add as new item
				if (!stacked) {
					inventory.add(picked);
				}
				gp.obj[i] = null; // Remove from world
			}
	    }
	    stackInventory(); // <-- Add this line
	}


	public void interactNPC(int i) {
	
		if (gp.keyH.enterPressed) {
			double talkRange = gp.tileSize * 0.3; // 0.3 tiles

			int closestNpcIndex = -1;
			double closestDistance = Double.MAX_VALUE;

			for (int j = 0; j < gp.npc.length; j++) {
				if (gp.npc[j] != null) {
					// Expand NPC's solid area by talkRange in all directions
					Rectangle npcArea = new Rectangle(
						gp.npc[j].worldX + gp.npc[j].solidArea.x - (int)talkRange,
						gp.npc[j].worldY + gp.npc[j].solidArea.y - (int)talkRange,
						gp.npc[j].solidArea.width + (int)(talkRange * 2),
						gp.npc[j].solidArea.height + (int)(talkRange * 2)
					);
					Rectangle playerArea = new Rectangle(
						worldX + solidArea.x,
						worldY + solidArea.y,
						solidArea.width,
						solidArea.height
					);

					if (npcArea.intersects(playerArea)) {
						// Calculate center-to-center distance for closest NPC
						int npcCenterX = gp.npc[j].worldX + gp.npc[j].solidArea.x + gp.npc[j].solidArea.width / 2;
						int npcCenterY = gp.npc[j].worldY + gp.npc[j].solidArea.y + gp.npc[j].solidArea.height / 2;
						int playerCenterX = worldX + solidArea.x + solidArea.width / 2;
						int playerCenterY = worldY + solidArea.y + solidArea.height / 2;
						double distance = Math.hypot(npcCenterX - playerCenterX, npcCenterY - playerCenterY);

						if (distance < closestDistance) {
							closestNpcIndex = j;
							closestDistance = distance;
						}
					}
				}
			}

			if (closestNpcIndex != -1) {
				attackCancel = true;
				gp.gameState = gp.dialogueState;
				gp.npc[closestNpcIndex].speak();
			}
		}
	}

	public void interactChest(int i) {
		if (i != 999) {
			
		}
	}

	public void contactMonster(int i){
		if(i != 999){

			if (invincible == false && gp.monster[i].collision == true && gp.monster[i].dying == false) {
				int damage = gp.monster[i].attack - getTotalDefense();
				if (damage < 0) {
					damage = 0;
				}
				health -= damage;
				collisionRecoilCounter = RECOIL_DURATION;
				spriteNum = 1; // Set spriteNum to 1 during recoil
				invincible = true;
			}
		}
	}

	public void damageMonster(int i) {
		if(i != 999) {
			if (gp.monster[i].invincible == false) {
				int dmg = getTotalAttack() - gp.monster[i].defense;

				if (dmg < 0) {
					dmg = 0;
				}
				
				gp.monster[i].health -= dmg;
				gp.monster[i].invincible = true;

				if (gp.monster[i].health <= 0 && gp.monster[i].dying == false) {
					gp.monster[i].dying = true;
					gp.monster[i].dyingCounter = 0;
					gp.monster[i].damageReaction();
					gp.ui.addMessage("You defeated " + gp.monster[i].name + "!");
					gp.player.exp += gp.monster[i].expReward;
					gp.player.checkLevelUp();
				}
			}

		}
		else {
			System.out.println("Player attacked nothing");
		}
	}

	public void checkLevelUp() {
		if (exp >= nextLevelExp) {
			level++;
			exp -= nextLevelExp;
			nextLevelExp += 10; // Increase the required experience for the next level
			totalProgressionPoints += 5;
			progressionPoints += 5;
			gp.ui.addMessage("Level up! You are now level " + level + "!");
		}
	}

	public void draw(Graphics2D g2) {
		
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

		BufferedImage image= null;

		if (attacking && currentWeapon != null) {
			switch (direction) {
				case "up":    image = (spriteNum == 1) ? attackUp1 : attackUp2; break;
				case "down":  image = (spriteNum == 1) ? attackDown1 : attackDown2; break;
				case "left":  image = (spriteNum == 1) ? attackLeft1 : attackLeft2; break;
				case "right": image = (spriteNum == 1) ? attackRight1 : attackRight2; break;
			}
		}
		else if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed) {
			switch (direction) {
				case "up":
					image = (spriteNum == 1) ? up1 : up2; break;
				case "down":
					image = (spriteNum == 1) ? down1 : down2;  break;
				case "left":
					image = (spriteNum == 1) ? left1 : left2;  break;
				case "right":
					image = (spriteNum == 1) ? right1 : right2;   break;
			}
		} else {
			switch (direction) {
				case "up": image = standUp; break;
				case "down": image = stand; break;
				case "left": image = standLeft; break;
				case "right": image = standRight; break;
			}
		}

		int x = screenX;
		int y = screenY;

		if (screenX > worldX) {
			x = worldX;
		}
		if (screenY > worldY) {
			y = worldY;
		}

		int rightOffset = gp.screenWidth - screenX;
		if(rightOffset > gp.worldWidth - worldX) {
			x = gp.screenWidth - (gp.worldWidth - worldX);
		}
		int bottomOffset = gp.screenHeight - screenY;
		if(bottomOffset > gp.worldHeight - worldY) {
			y = gp.screenHeight - (gp.worldHeight - worldY);
		}

		if (invincible == true){
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));	
		}
		// Draw image with scaling
    	g2.drawImage(image, x, y, gp.tileSize * 2, gp.tileSize * 2, null); // 160x160 final size
		// Restore composite
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

	}

	public void stackInventory() {
	    ArrayList<Entity> newInventory = new ArrayList<>();
	    for (Entity item : inventory) {
	        if (item == null) continue;
	        boolean stacked = false;
	        for (Entity stackedItem : newInventory) {
	            // Stack if same class and (optionally) same name
	            if (stackedItem != null && item.getClass() == stackedItem.getClass() && 
	                (item.name == null || item.name.equals(stackedItem.name))) {
	                stackedItem.quantity += item.quantity;
	                stacked = true;
	                break;
	            }
	        }
	        if (!stacked) {
	            newInventory.add(item);
	        }
	    }
	    inventory = newInventory;
	}

	
	public void selectItem(int index) {
		int itemIndex = gp.ui.getItemIndexOnSlot();
		if (itemIndex < inventory.size()) {
			Entity selectedItem = inventory.get(itemIndex);

			if (selectedItem.type == 0) { // Weapon
				Entity previousWeapon = currentWeapon;
				currentWeapon = selectedItem;
				inventory.set(itemIndex, previousWeapon);
			}
			else if (selectedItem.type == 1) { // Armor
				Entity previousArmor = currentArmor;
				currentArmor = selectedItem;
				inventory.set(itemIndex, previousArmor);
			}
			else if (selectedItem.type == 2) { // Boots
				Entity previousBoots = currentBoots;
				currentBoots = selectedItem;
				inventory.set(itemIndex, previousBoots);
			}
			else if (selectedItem.type == 3) { // Hat
				Entity previousHat = currentHat;
				currentHat = selectedItem;
				inventory.set(itemIndex, previousHat);
			}
			// For consumables or other types, you can call use() or similar here
			else if (selectedItem.type == 4 || selectedItem.type == 5 || selectedItem.type == 6) {
    // Consumable types (e.g., 4 = interactable, 5 = potion, 6 = food)
    selectedItem.use(this);

    // If stackable, decrease quantity; remove if quantity is 0
    if (selectedItem.stackable) {
        selectedItem.quantity--;
        if (selectedItem.quantity <= 0) {
            inventory.remove(itemIndex);
        }
    } else {
        // Not stackable, just remove from inventory
        inventory.remove(itemIndex);
    }
			}
		}
	}

	public int getEquipmentHealthBonus() {
	    int bonus = 0;
	    if (currentWeapon != null) bonus += currentWeapon.healthBonus;
	    if (currentArmor != null) bonus += currentArmor.healthBonus;
	    if (currentBoots != null) bonus += currentBoots.healthBonus;
	    if (currentHat != null) bonus += currentHat.healthBonus;
	    return bonus;
	}

	public int getEquipmentManaBonus() {
	    int bonus = 0;
	    if (currentWeapon != null) bonus += currentWeapon.manaBonus;
	    if (currentArmor != null) bonus += currentArmor.manaBonus;
	    if (currentBoots != null) bonus += currentBoots.manaBonus;
	    if (currentHat != null) bonus += currentHat.manaBonus;
	    return bonus;
	}

	public int getEquipmentAttackBonus() {
	    int bonus = 0;
	    if (currentWeapon != null) bonus += currentWeapon.attackBonus;
	    if (currentArmor != null) bonus += currentArmor.attackBonus;
	    if (currentBoots != null) bonus += currentBoots.attackBonus;
	    if (currentHat != null) bonus += currentHat.attackBonus;
	    return bonus;
	}

	public int getEquipmentDefenseBonus() {
	    int bonus = 0;
	    if (currentWeapon != null) bonus += currentWeapon.defenseBonus;
	    if (currentArmor != null) bonus += currentArmor.defenseBonus;
	    if (currentBoots != null) bonus += currentBoots.defenseBonus;
	    if (currentHat != null) bonus += currentHat.defenseBonus;
	    return bonus;
	}
	
	public int getTotalAttack() {
	    return attack + getEquipmentAttackBonus();
	}

	public int getTotalDefense() {
	    return defense + getEquipmentDefenseBonus();
	}

	public float getTotalMaxHealth() {
	    return maxHealth + getEquipmentHealthBonus();
	}

	public int getTotalMaxMana() {
	    return maxMana + getEquipmentManaBonus();
	}
}
