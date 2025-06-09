package entity;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import javax.imageio.ImageIO;

import data.DataStorage;
import data.EntitySaveData;
import main.EventHandler;
import main.GamePanel;
import main.KeyHandler;
import object.OBJ_Chest;
import projectile.PROJECTILE_Fire_Ball;
import skill.Skill;

public class Player extends Entity{
	
	KeyHandler keyH;
	int MAX_LEVEL = 100;
	
	public final int screenX;
	public final int screenY;
	public int defaultWorldX;
    public int defaultWorldY;
	public boolean justFinishTalking = false;
	int standCounter = 0;
	public boolean attackCancel = false;
	public int maxInventorySize = 24;

	public int deathAnimCounter = 0;
	public  final int DEATH_ANIM_DURATION = 120; // 1 second at 60fps

	int collisionRecoilCounter = 0;
    final int RECOIL_DURATION = 10;
    public int attackCooldown = 0;
    public int ATTACK_COOLDOWN_MAX; 
	public ArrayList<Entity> inventory = new ArrayList<>();
	public Entity currentChest;
	public int flashCooldown = 0;
	public int FLASH_COOLDOWN_MAX = 5400; // 90 seconds at 60fps
	public int FLASH_RANGE = 4; // 4 tiles
	public Entity quickUseItem;
	public Class<?> quickUseItemClass = null;
	public String quickUseItemName = null;

	public boolean monsterNearby = false;
	public int monsterNearbyCounter = 0;
	public int MONSTER_NEARBY_DURATION = 60; // 1 seconds at 60fps

	public ArrayList<Skill> skills = new ArrayList<>();
	public ArrayList<Skill> unlockedSkills = new ArrayList<>();
	public Skill[] assignedSkills = new Skill[4];
	public int selectedSkillIndex = 0;

	public boolean skillAnimating = false;
	public int skillAnimCounter = 0;
	public int skillAnimFrame = 0;
	public BufferedImage[] earthquakeAnimFrames; // For Earthquake animation

	public Queue<PendingWave> pendingEarthquakeWaves = new LinkedList<>();

	public int earthquakeWaveCenterX = 0;
	public int earthquakeWaveCenterY = 0;

	public boolean bossArenaActive = false;
	public int bossArenaCenterX, bossArenaCenterY, bossArenaRadius;

	private int failedWeaponPickupCount = 0;
	public boolean pendingTeleport = false;
	public int pendingTeleportMap = 0;
	public int pendingTeleportX = 0;
	public int pendingTeleportY = 0;

	public Player(GamePanel gp, KeyHandler kH) {

		super(gp);
		this.keyH = kH;

		screenX = gp.baseWidth/2 - gp.tileSize/2;
		screenY = gp.baseHeight/2 - gp.tileSize/2;
		
		solidArea = new Rectangle();
		solidArea.x = 40;
		solidArea.y = 40;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		solidArea.width = 80;
		solidArea.height= 80;
		
		
		setDefaultValues();
		getPlayerImage();
		if (this.currentWeapon != null){
			getPlayerAttackImage();
		}
		setItems();
		initializeSkills();
	}
	
	public void setDefaultValues() {
		worldX = gp.tileSize * 50 - gp.tileSize/2;
		worldY = gp.tileSize * 50 - gp.tileSize/2;

		this.defaultWorldX = this.worldX;
        this.defaultWorldY = this.worldY;

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
		this.nextLevelExp = calculateNextLevelExp(this.level);
		this.totalProgressionPoints = 0;
		this.progressionPoints = 0;
		this.progressionHealthUpgrades = 0;
		this.progressionManaUpgrades = 0;
		this.progressionAttackUpgrades = 0;
		this.progressionDefenseUpgrades = 0;
		this.ATTACK_COOLDOWN_MAX = 30; // 30 frames = 0.5s at 60fps
		this.FLASH_COOLDOWN_MAX = 3600; // 60 seconds at 60fps

		currentWeapon = null;
		currentArmor = null;
		currentHat = null;
		currentBoots = null;
		if (currentWeapon != null) ATTACK_COOLDOWN_MAX += currentWeapon.cooldownBonus;
		Projectile projectileList = new PROJECTILE_Fire_Ball(gp);


	}

	public DataStorage toDataStorage() {
		DataStorage data = new DataStorage();
		data.name = this.name;
		data.maxHealth = this.maxHealth;
		data.health = this.health;
		data.maxMana = this.maxMana;
		data.mana = this.mana;
		data.level = this.level;
		data.attack = this.attack;
		data.defense = this.defense;
		data.exp = this.exp;
		data.speed = this.speed;
		data.totalProgressionPoints = this.totalProgressionPoints;
		data.progressionPoints = this.progressionPoints;
		data.progressionHealthUpgrades = this.progressionHealthUpgrades;
		data.progressionManaUpgrades = this.progressionManaUpgrades;
		data.progressionAttackUpgrades = this.progressionAttackUpgrades;
		data.progressionDefenseUpgrades = this.progressionDefenseUpgrades;
		data.flashCooldown = this.flashCooldown;
		data.currentWeapon = (this.currentWeapon != null) ? this.currentWeapon.toEntitySaveData() : null;
		data.currentArmor = (this.currentArmor != null) ? this.currentArmor.toEntitySaveData() : null;
		data.currentHat = (this.currentHat != null) ? this.currentHat.toEntitySaveData() : null;
		data.currentBoots = (this.currentBoots != null) ? this.currentBoots.toEntitySaveData() : null;
		// Save quick use info if needed
		data.quickUseItemClass = this.quickUseItemClass != null ? this.quickUseItemClass.getName() : null;
		data.quickUseItemName = this.quickUseItemName;
		data.playerWorldX = this.worldX;
		data.playerWorldY = this.worldY;
		data.currentMap = gp.currentMap;

		// Save objects, monsters, and NPCs
		for (int map = 0; map < gp.maxMap; map++) {
		    ArrayList<EntitySaveData> objList = new ArrayList<>();
		    for (Entity e : gp.obj[map]) {
		        if (e != null) objList.add(e.toEntitySaveData());
		    }
		    data.savedObjects.add(objList);

		    ArrayList<EntitySaveData> monsterList = new ArrayList<>();
		    for (Entity e : gp.monster[map]) {
		        if (e != null) monsterList.add(e.toEntitySaveData());
		    }
		    data.savedMonsters.add(monsterList);

		    ArrayList<EntitySaveData> npcList = new ArrayList<>();
		    for (Entity e : gp.npc[map]) {
		        if (e != null) npcList.add(e.toEntitySaveData());
		    }
		    data.savedNpcs.add(npcList);
		}

		// Saving inventory
		data.inventory = new ArrayList<>();
		for (Entity item : this.inventory) {
		    if (item != null) data.inventory.add(item.toEntitySaveData());
		}

		// Save unlocked skills
		data.unlockedSkillClassNames.clear();
		for (Skill skill : unlockedSkills) {
			if (skill != null) {
				data.unlockedSkillClassNames.add(skill.getClass().getName());
			} else {
				data.unlockedSkillClassNames.add(null);
			}
		}

		// Save assigned skills and their cooldowns
		data.assignedSkillClassNames.clear();
		data.assignedSkillCooldowns.clear();
		for (Skill skill : assignedSkills) {
			if (skill != null) {
				data.assignedSkillClassNames.add(skill.getClass().getName());
				data.assignedSkillCooldowns.add(skill.getCooldown());
			} else {
				data.assignedSkillClassNames.add(null);
				data.assignedSkillCooldowns.add(0);
			}
		}

		return data;
	}

	public void fromDataStorage(DataStorage data) {
		this.name = data.name;
		this.maxHealth = data.maxHealth;
		this.health = data.health;
		this.maxMana = data.maxMana;
		this.mana = data.mana;
		this.level = data.level;
		this.attack = data.attack;
		this.defense = data.defense;
		this.exp = data.exp;
		this.speed = data.speed;
		this.totalProgressionPoints = data.totalProgressionPoints;
		this.progressionPoints = data.progressionPoints;
		this.progressionHealthUpgrades = data.progressionHealthUpgrades;
		this.progressionManaUpgrades = data.progressionManaUpgrades;
		this.progressionAttackUpgrades = data.progressionAttackUpgrades;
		this.progressionDefenseUpgrades = data.progressionDefenseUpgrades;
		this.flashCooldown = data.flashCooldown;
		this.currentWeapon = data.currentWeapon != null ? Entity.fromEntitySaveData(data.currentWeapon, gp) : null;
		this.currentArmor = data.currentArmor != null ? Entity.fromEntitySaveData(data.currentArmor, gp) : null;
		this.currentHat = data.currentHat != null ? Entity.fromEntitySaveData(data.currentHat, gp) : null;
		this.currentBoots = data.currentBoots != null ? Entity.fromEntitySaveData(data.currentBoots, gp) : null;
		// Convert inventory from EntitySaveData to Entity
		this.inventory = new ArrayList<>();
		if (data.inventory != null) {
			for (EntitySaveData esd : data.inventory) {
				Entity item = Entity.fromEntitySaveData(esd, gp);
				if (item != null) this.inventory.add(item);
			}
		}

		// Restore quick use info if needed
		try {
			this.quickUseItemClass = data.quickUseItemClass != null ? Class.forName(data.quickUseItemClass) : null;
		} catch (Exception e) {
			this.quickUseItemClass = null;
		}
		this.quickUseItemName = data.quickUseItemName;
		this.worldX = data.playerWorldX;
		this.worldY = data.playerWorldY;
		gp.currentMap = data.currentMap;

		// Restore objects on map
		for (int map = 0; map < gp.maxMap; map++) {
			// Clear current objects
			for (int i = 0; i < gp.obj[map].length; i++) {
				gp.obj[map][i] = null;
			}
			// Restore from save
			if (data.savedObjects.size() > map) {
				int i = 0;
				for (EntitySaveData esd : data.savedObjects.get(map)) {
					if (i < gp.obj[map].length) {
						Entity e = Entity.fromEntitySaveData(esd, gp);
						gp.obj[map][i++] = e;
					}
				}
			}
		}
		// Restore monsters on map
		for (int map = 0; map < gp.maxMap; map++) {
			// Clear current monsters
			for (int i = 0; i < gp.monster[map].length; i++) {
				gp.monster[map][i] = null;
			}
			// Restore from save
			if (data.savedMonsters.size() > map) {
				int i = 0;
				for (EntitySaveData esd : data.savedMonsters.get(map)) {
					if (i < gp.monster[map].length) {
						Entity e = Entity.fromEntitySaveData(esd, gp);
						gp.monster[map][i++] = e;
					}
				}
			}
		}

		// Restore NPCs on map
		for (int map = 0; map < gp.maxMap; map++) {
			// Clear current NPCs
			for (int i = 0; i < gp.npc[map].length; i++) {
				gp.npc[map][i] = null;
			}
			// Restore from save
			if (data.savedNpcs.size() > map) {
				int i = 0;
				for (EntitySaveData esd : data.savedNpcs.get(map)) {
					if (i < gp.npc[map].length) {
						Entity e = Entity.fromEntitySaveData(esd, gp);
						gp.npc[map][i++] = e;
					}
				}
			}
		}

		// Restore unlocked skills
		unlockedSkills.clear();
		if (data.unlockedSkillClassNames != null) {
			for (String className : data.unlockedSkillClassNames) {
				if (className != null) {
					try {
						Class<?> clazz = Class.forName(className);
						Skill skill = (Skill) clazz.getConstructor(main.GamePanel.class).newInstance(this.gp);
						unlockedSkills.add(skill);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

		// Restore assigned skills and their cooldowns
		for (int i = 0; i < assignedSkills.length && i < data.assignedSkillClassNames.size(); i++) {
			String className = data.assignedSkillClassNames.get(i);
			int cooldown = data.assignedSkillCooldowns.get(i);
			if (className != null) {
				try {
					Class<?> clazz = Class.forName(className);
					Skill skill = (Skill) clazz.getConstructor(main.GamePanel.class).newInstance(this.gp);
					// Set cooldown via reflection or a setter if available
					java.lang.reflect.Field cooldownField = skill.getClass().getDeclaredField("cooldown");
					cooldownField.setAccessible(true);
					cooldownField.setInt(skill, cooldown);
					assignedSkills[i] = skill;
				} catch (Exception e) {
					assignedSkills[i] = null;
					e.printStackTrace();
				}
			} else {
				assignedSkills[i] = null;
			}
		}
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

		if (knockbackCounter > 0) {
            // Save old position
            int oldX = worldX;
            int oldY = worldY;

            // Move by knockback
            worldX += knockbackDX;
            worldY += knockbackDY;

            // Check all collisions
            collisionOn = false;
            gp.cChecker.checkTile(this);
            gp.cChecker.checkObject(this, true);
            gp.cChecker.checkEntity(this, gp.npc);
            gp.cChecker.checkEntity(this, gp.monster);

            if (collisionOn) {
                // Undo move and stop knockback
                worldX = oldX;
                worldY = oldY;
                knockbackCounter = 0;
            } else {
                knockbackCounter--;
            }
            return; // Skip normal movement while being knocked back
        }

		if (collisionRecoilCounter > 0) {
			collisionRecoilCounter--;
			standCounter = 0; // Reset standCounter when recoiling
			spriteNum = 1; // Set spriteNum to 1 during recoil
			return; // Skip the rest of the update method during recoil
		}

		if (attackCooldown > 0) {
            attackCooldown--;
        }

		if (attacking == true) {
			attacking();
			return;
		}

		if (keyH.spacePressed == true && attackCooldown == 0 && this.currentWeapon != null && 
				!attackCancel) {
			int hitboxType = currentWeapon.weaponType;
			if (hitboxType == 4) { // Staff
				int manaCost = 20;
				if (mana >= manaCost) {
					mana -= manaCost;
					projectile.PROJECTILE_Fire_Ball fireball = new projectile.PROJECTILE_Fire_Ball(gp);
					int px = this.worldX;
					int py = this.worldY;
					switch (direction) {
						case "up":    py -= gp.tileSize; break;
						case "down":  py += gp.tileSize; break;
						case "left":  px -= gp.tileSize; break;
						case "right": px += gp.tileSize; break;
					}
					fireball.set(px, py, direction, true, this);
					gp.projectileList.add(fireball);
					System.out.println("Creating fireball at: " + px + "," + py + " dir=" + direction);
					System.out.println("projectileList size before: " + gp.projectileList.size());
					gp.projectileList.add(fireball);
					attacking = true;
					attackCooldown = ATTACK_COOLDOWN_MAX;
				} else {
					gp.ui.addMessage(gp.ui.tr("message.not_enough_mana"));
				}
				keyH.spacePressed = false;
				return;
			} else if (hitboxType == 3) { // Bow
				// Shoot arrow
				projectile.PROJECTILE_Arrow arrow = new projectile.PROJECTILE_Arrow(gp);
				int px = this.worldX;
				int py = this.worldY;
				arrow.set(px, py, direction, true, this);
				gp.projectileList.add(arrow);
				attacking = true;
				attackCooldown = ATTACK_COOLDOWN_MAX;
				keyH.spacePressed = false;
				return;
			} else {
				attacking = true;
				attackCooldown = ATTACK_COOLDOWN_MAX;
				keyH.spacePressed = false;
				return;
			}
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
			int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
			interactNPC(npcIndex);

			if (keyH.enterPressed == true) {
				objIndex = gp.cChecker.checkObject(this, true);
				if (objIndex != 999 && gp.obj[gp.currentMap][objIndex] != null) {
					Entity obj = gp.obj[gp.currentMap][objIndex];
					if (obj instanceof object.OBJ_Fountain) {
						((object.OBJ_Fountain)obj).interact();
					} else if (obj.name.equals("Chest")) {
						interactChest(objIndex);
					} else {
						pickUpObject(objIndex);
					}
				}
				keyH.enterPressed = false;
			}

			//Check NPC collision

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

			// Clamp player inside boss arena circle if active
			if (bossArenaActive) {
				int px = worldX + solidArea.x + solidArea.width / 2;
				int py = worldY + solidArea.y + solidArea.height / 2;
				int dx = px - bossArenaCenterX;
				int dy = py - bossArenaCenterY;
				double dist = Math.sqrt(dx * dx + dy * dy);
				int allowed = bossArenaRadius - Math.max(solidArea.width, solidArea.height) / 2;
				if (dist > allowed) {
					double angle = Math.atan2(dy, dx);
					int newPx = bossArenaCenterX + (int)(allowed * Math.cos(angle));
					int newPy = bossArenaCenterY + (int)(allowed * Math.sin(angle));
					worldX = newPx - solidArea.x - solidArea.width / 2;
					worldY = newPy - solidArea.y - solidArea.height / 2;
				}
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

		if (health <= 0) {
			health = 0;
			deathAnimCounter = 0;
			gp.gameState = gp.gameOverState;
		}

		if (keyH.flashPressed && flashCooldown == 0){
			flash();
			flashCooldown = FLASH_COOLDOWN_MAX;
			keyH.flashPressed = false; // Reset flashPressed after using
			return;
		}

		if (flashCooldown > 0) {
			flashCooldown--;
		}

		// Check for nearby monsters
		monsterNearby = false;
		int range = gp.tileSize * 4; // 4 tiles
		for (int i = 0; i < gp.monster[gp.currentMap].length; i++) {
			Entity m = gp.monster[gp.currentMap][i];
			if (m != null && m.alive && !m.dying) {
				int dx = (worldX + solidArea.x + solidArea.width / 2) - (m.worldX + m.solidArea.x + m.solidArea.width / 2);
				int dy = (worldY + solidArea.y + solidArea.height / 2) - (m.worldY + m.solidArea.y + m.solidArea.height / 2);
				double distance = Math.sqrt(dx * dx + dy * dy);
				if (distance <= range) {
					monsterNearby = true;
					break;
				}
			}
		}		

		for (int i = 0; i < assignedSkills.length; i++) {
			Skill skill = assignedSkills[i];
			if (skill != null) {
				skill.tickCooldown();
				if (skill instanceof skill.SKILL_Dash) {
                    ((skill.SKILL_Dash)skill).tickDash(this);
                }
			}
		}

		if (keyH.enterPressed) {
			int objIndex = gp.cChecker.checkObject(this, true);
			if (objIndex != 999 && gp.obj[gp.currentMap][objIndex] != null) {
				Entity obj = gp.obj[gp.currentMap][objIndex];
				if (obj instanceof object.OBJ_Fountain) {
					((object.OBJ_Fountain)obj).interact();
				} else if (obj.name.equals("Chest")) {
					interactChest(objIndex);
				} else {
					pickUpObject(objIndex);
				}
			}
			keyH.enterPressed = false;
		}
		
		//Check skill animation
		if (skillAnimating) {
			skillAnimCounter++;
			if (skillAnimCounter < 60) {
				// Show the 1st frame for the first 60 ticks (1 second)
				skillAnimFrame = 0;
			} else {
				// After 60 ticks, keep showing the 2nd frame
				skillAnimFrame = 1;
			}
			// End animation when all pending waves are done
			if (pendingEarthquakeWaves.isEmpty()) {
				skillAnimating = false;
				skillAnimFrame = 0;
			}
		}

		if (!pendingEarthquakeWaves.isEmpty()) {
            Player.PendingWave wave = pendingEarthquakeWaves.peek();
            wave.delay--;
            if (wave.delay <= 0) {
                effect.EFFECT_EarthquakeWave newWave = new effect.EFFECT_EarthquakeWave(
                    gp, earthquakeWaveCenterX, earthquakeWaveCenterY, wave.radius, wave.damage
                );
                gp.projectileList.add(newWave);
                pendingEarthquakeWaves.poll(); // Remove this wave from the queue
            }
        }

		return;
	}
	
	public void attacking(){

		spriteCounter++;

		if(spriteCounter <= 5){
			spriteNum = 1;
		}
		if (spriteCounter > 5 && spriteCounter <= 25){
			spriteNum = 2;

			int range = 4;
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
				for (int i = 0; i < gp.monster[gp.currentMap].length; i++) {
					Entity monster = gp.monster[gp.currentMap][i];
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
							gp.monster[gp.currentMap][i].showHpBar = true;
							gp.monster[gp.currentMap][i].hpBarDisplayCounter = 150; // Show for 2.5 second (150 frames)
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
				for (int i = 0; i < gp.monster[gp.currentMap].length; i++) {
					Entity monster = gp.monster[gp.currentMap][i];
					if (monster != null && monster.alive) {
						Rectangle monsterHitbox = new Rectangle(
							monster.worldX + monster.solidArea.x,
							monster.worldY + monster.solidArea.y,
							monster.solidArea.width,
							monster.solidArea.height
						);
						if (attackHitbox.intersects(monsterHitbox)) {
							damageMonster(i);
							gp.monster[gp.currentMap][i].showHpBar = true; // Show HP bar
							gp.monster[gp.currentMap][i].hpBarDisplayCounter = 150; // Show for 2.5 seconds
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
				for (int i = 0; i < gp.monster[gp.currentMap].length; i++) {
					Entity monster = gp.monster[gp.currentMap][i];
					if (monster != null && monster.alive) {
						Rectangle monsterHitbox = new Rectangle(
							monster.worldX + monster.solidArea.x,
							monster.worldY + monster.solidArea.y,
							monster.solidArea.width,
							monster.solidArea.height
						);
						if (attackHitbox.intersects(monsterHitbox)) {
							damageMonster(i);
							gp.monster[gp.currentMap][i].showHpBar = true; // Show HP bar
							gp.monster[gp.currentMap][i].hpBarDisplayCounter = 150; // Show for 2.5 seconds
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
		if (i != 999 && gp.obj[gp.currentMap][i].pickable) {
			// Only restrict on the first map (map 0)
			if (gp.currentMap == 0) {
				Entity picked = gp.obj[gp.currentMap][i];
				// Check if the item is a weapon (itemType == 0)
				if (picked.itemType == 0) {
					// Check if player already has a weapon in inventory or equipped
					boolean hasWeapon = false;
					if (currentWeapon != null) hasWeapon = true;
					for (Entity item : inventory) {
						if (item != null && item.itemType == 0) {
							hasWeapon = true;
							break;
						}
					}
					if (hasWeapon) {
						// Add a counter to track failed weapon pickup attempts
						failedWeaponPickupCount++;

						// Show a special dialogue from the first available NPC
						for (Entity npc : gp.npc[gp.currentMap]) {
							if (npc != null) {
								int map = gp.currentMap;
								if (failedWeaponPickupCount < 3) {
									npc.dialogues[map][10] = "You may only choose one weapon here. Make it count!";
								} else if (failedWeaponPickupCount < 6) {
									npc.dialogues[map][10] = "Seriously, just pick one weapon and go!";
								} else if (failedWeaponPickupCount < 9) {
									npc.dialogues[map][10] = "Stop trying! You can't take more than one weapon!";
								} else {
									npc.dialogues[map][10] = "Enough! You have taken one. No more!";
									pendingTeleport = true;
									pendingTeleportMap = 1;
									pendingTeleportX = 50;
									pendingTeleportY = 50;
								}
								npc.dialogIndex = 10;
								gp.gameState = gp.dialogueState;
								npc.speak();
								break;
							}
						}
						return;
					}
				}
			}
			if (inventory.size() <= maxInventorySize) {
				Entity picked = gp.obj[gp.currentMap][i];
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
				gp.obj[gp.currentMap][i] = null; // Remove from world
			}
		}
		stackInventory();
	}


	public void interactNPC(int i) {
	
		if (gp.keyH.enterPressed) {
			double talkRange = gp.tileSize * 0.3; // 0.3 tiles

			int closestNpcIndex = -1;
			double closestDistance = Double.MAX_VALUE;

			for (int j = 0; j < gp.npc[gp.currentMap].length; j++) {
				if (gp.npc[gp.currentMap][j] != null) {
					// Expand NPC's solid area by talkRange in all directions
					Rectangle npcArea = new Rectangle(
						gp.npc[gp.currentMap][j].worldX + gp.npc[gp.currentMap][j].solidArea.x - (int)talkRange,
						gp.npc[gp.currentMap][j].worldY + gp.npc[gp.currentMap][j].solidArea.y - (int)talkRange,
						gp.npc[gp.currentMap][j].solidArea.width + (int)(talkRange * 2),
						gp.npc[gp.currentMap][j].solidArea.height + (int)(talkRange * 2)
					);
					Rectangle playerArea = new Rectangle(
						worldX + solidArea.x,
						worldY + solidArea.y,
						solidArea.width,
						solidArea.height
					);

					if (npcArea.intersects(playerArea)) {
						// Calculate center-to-center distance for closest NPC
						int npcCenterX = gp.npc[gp.currentMap][j].worldX + gp.npc[gp.currentMap][j].solidArea.x + gp.npc[gp.currentMap][j].solidArea.width / 2;
						int npcCenterY = gp.npc[gp.currentMap][j].worldY + gp.npc[gp.currentMap][j].solidArea.y + gp.npc[gp.currentMap][j].solidArea.height / 2;
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
				gp.npc[gp.currentMap][closestNpcIndex].speak();
			}
		}
	}

	public void interactChest(int i) {
		if (i != 999 && gp.obj[gp.currentMap][i] != null && gp.obj[gp.currentMap][i].name.equals("Chest")) {
			gp.player.currentChest = (OBJ_Chest) gp.obj[gp.currentMap][i];
			((object.OBJ_Chest)gp.obj[gp.currentMap][i]).openChest();
		}
	}

	public void contactMonster(int i){
		if(i != 999){

			if (invincible == false && gp.monster[gp.currentMap][i].collision == true && gp.monster[gp.currentMap][i].dying == false) {
				int damage = gp.monster[gp.currentMap][i].attack - getTotalDefense();
				if (damage < 0) {
					damage = 0;
				}
				health -= damage;
				collisionRecoilCounter = RECOIL_DURATION;
				spriteNum = 1; // Set spriteNum to 1 during recoil
				invincible = true;

				// Knockback direction: away from monster
				int dx = worldX - gp.monster[gp.currentMap][i].worldX;
				int dy = worldY - gp.monster[gp.currentMap][i].worldY;
				int mag = (int)Math.sqrt(dx*dx + dy*dy);
				if (mag != 0) {
					knockbackDX = (int)(dx * 8 / (double)mag); // 8 is knockback strength, adjust as needed
					knockbackDY = (int)(dy * 8 / (double)mag);
				} else {
					knockbackDX = 0;
					knockbackDY = 0;
				}
				knockbackCounter = knockbackDuration;
			}
		}
	}

	public void damageMonster(int i) {
		if(i != 999) {
			if (gp.monster[gp.currentMap][i].invincible == false) {
				int dmg = getTotalAttack() - gp.monster[gp.currentMap][i].defense;

				if (dmg < 0) {
					dmg = 0;
				}
				
				if (gp.monster[gp.currentMap][i] instanceof monster.BOSS_Skeleking) {
					((monster.BOSS_Skeleking)gp.monster[gp.currentMap][i]).takeDamage(dmg);
				} else {
					gp.monster[gp.currentMap][i].health -= dmg;
				}

				gp.monster[gp.currentMap][i].invincible = true;

				gp.monster[gp.currentMap][i].timeSinceLastHit = 0;

				// --- Always apply knockback on hit ---
				gp.monster[gp.currentMap][i].damageReaction();

				if (gp.monster[gp.currentMap][i].health <= 0 && gp.monster[gp.currentMap][i].dying == false) {
					gp.monster[gp.currentMap][i].dying = true;
					gp.monster[gp.currentMap][i].dyingCounter = 0;
					gp.monster[gp.currentMap][i].checkDrop();
					gp.ui.addMessage(gp.ui.tr("message.defeat_monster", gp.monster[gp.currentMap][i].name));
					gp.player.exp += gp.monster[gp.currentMap][i].expReward;
					gp.player.checkLevelUp();
				}
			}

		}
		else {
			System.out.println("Player attacked nothing");
		}
	}

	public void checkLevelUp() {
		while (exp >= nextLevelExp && level < MAX_LEVEL) {
			level++;
			exp -= nextLevelExp;
			// Update nextLevelExp for the new level
			nextLevelExp = calculateNextLevelExp(level); // Make sure you have this method!
			totalProgressionPoints += 10;
			progressionPoints += 10;
			gp.ui.addMessage(gp.ui.tr("message.level_up", level));
		}
		// Optionally, handle max level overflow
		if (level >= MAX_LEVEL && exp > 17500) {
			totalProgressionPoints += 10;
			progressionPoints += 10;
			exp -= 17500;
		}
	}
	public void draw(Graphics2D g2) {
		
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

		BufferedImage image= null;

		if (attacking) {
            if (currentWeapon != null) {
                switch (direction) {
                    case "up":
                        image = (spriteNum == 1) ? currentWeapon.attackUp1 : currentWeapon.attackUp2;
                        break;
                    case "down":
                        image = (spriteNum == 1) ? currentWeapon.attackDown1 : currentWeapon.attackDown2;
                        break;
                    case "left":
                        image = (spriteNum == 1) ? currentWeapon.attackLeft1 : currentWeapon.attackLeft2;
                        break;
                    case "right":
                        image = (spriteNum == 1) ? currentWeapon.attackRight1 : currentWeapon.attackRight2;
                        break;
                }
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

		int rightOffset = gp.baseWidth - screenX;
		if(rightOffset > gp.worldWidth - worldX) {
			x = gp.baseWidth - (gp.worldWidth - worldX);
		}
		int bottomOffset = gp.baseHeight - screenY;
		if(bottomOffset > gp.worldHeight - worldY) {
			y = gp.baseHeight - (gp.worldHeight - worldY);
		}

		if (invincible == true){
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));	
		}

		if (skillAnimating && earthquakeAnimFrames != null && earthquakeAnimFrames[skillAnimFrame] != null) {
			g2.drawImage(earthquakeAnimFrames[skillAnimFrame], x, y, gp.tileSize * 2, gp.tileSize * 2, null);
			return; // Skip drawing normal sprite if animating
		}
		// Draw image with scaling
    	g2.drawImage(image, x, y, gp.tileSize * 2, gp.tileSize * 2, null); // 160x160 final size

		if (monsterNearby && monsterNearbyCounter < MONSTER_NEARBY_DURATION) {
			g2.setColor(Color.ORANGE);
			g2.setFont(g2.getFont().deriveFont(32f));
			int markX = x + gp.tileSize - 8;
			int markY = y - 6;
			g2.drawString("!", markX, markY);
			monsterNearbyCounter++;
		} if (monsterNearbyCounter >= MONSTER_NEARBY_DURATION) {
			monsterNearby = false;
			monsterNearbyCounter = 0;
		}

		if (gp.debugMode){
			g2.setColor(Color.RED);
			g2.drawRect(x + solidArea.x, y + solidArea.y, solidArea.width, solidArea.height);

			// Show player speed in debug mode
			g2.setFont(g2.getFont().deriveFont(Font.BOLD, 18f));
			g2.setColor(Color.YELLOW);
			String speedText = "Speed: " + speed;
			g2.drawString(speedText, x, y - 10);
		}
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
	            if (stackedItem != null && item.getClass() == stackedItem.getClass() && stackedItem.stackable && 
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
			if (selectedItem == null) return; // Check if the item exists
			if (selectedItem.itemType == 0 || selectedItem.itemType == 1 || selectedItem.itemType == 2 || selectedItem.itemType == 3) {
				// Equipment: Weapon, Hat, Armor, Boots
				if (!canEquip(selectedItem)) {
					gp.ui.addMessage(gp.ui.tr("message.level_too_low", selectedItem.levelRequirement));
					return;
				}
			}
			if (selectedItem.itemType == 0) { // Weapon
				Entity previousWeapon = currentWeapon;
				currentWeapon = selectedItem;
				inventory.set(itemIndex, previousWeapon);
			}
			else if (selectedItem.itemType == 1) {
				Entity previousHat = currentHat;
				currentHat = selectedItem;
				inventory.set(itemIndex, previousHat);
			}
			else if (selectedItem.itemType == 2) {
				Entity previousArmor = currentArmor;
				currentArmor = selectedItem;
				inventory.set(itemIndex, previousArmor);
			}
			else if (selectedItem.itemType == 3) {
				Entity previousBoots = currentBoots;
				currentBoots = selectedItem;
				inventory.set(itemIndex, previousBoots);
			}
			// ...other item types...
		}
	}

	public void disposeSelectedItem() {
	    int itemIndex = gp.ui.getItemIndexOnSlot();
	    if (itemIndex >= 0 && itemIndex < inventory.size()) {
	        inventory.remove(itemIndex);

	        // After removal, adjust cursor if needed
	        int totalItems = inventory.size();
	        int maxIndex = totalItems - 1;
	        int currentIndex = gp.ui.slotRow * gp.ui.maxInventoryCol + gp.ui.slotCol;

	        // If the cursor is now out of bounds, move it left or up
	        if (currentIndex > maxIndex) {
	            if (gp.ui.slotCol > 0) {
	                gp.ui.slotCol--;
	            } else if (gp.ui.slotRow > 0) {
	                gp.ui.slotRow--;
	                gp.ui.slotCol = gp.ui.maxInventoryCol - 1;
	                // Clamp to last item if still out of bounds
	                currentIndex = gp.ui.slotRow * gp.ui.maxInventoryCol + gp.ui.slotCol;
	                if (currentIndex > maxIndex) {
	                    gp.ui.slotCol = maxIndex % gp.ui.maxInventoryCol;
	                }
	            }
	        }
	    }
	}

	public void reset(){
		setDefaultValues();
		health = maxHealth;
		mana = maxMana;
		exp = 0;
		level = 1;
		progressionPoints = 0;
		totalProgressionPoints = 0;
		progressionHealthUpgrades = 0;
		progressionManaUpgrades = 0;
		progressionAttackUpgrades = 0;
		progressionDefenseUpgrades = 0;
		inventory.clear();
		currentWeapon = null;
		currentArmor = null;
		currentHat = null;
		currentBoots = null;
		attackCooldown = 0;
		flashCooldown = 0;
		invincible = false;
		invincibleCounter = 0;
		attacking = false;
		attackCancel = false;
		collisionRecoilCounter = 0;
		knockbackCounter = 0;    
		knockbackDX = 0;            
		knockbackDY = 0;              
		standCounter = 0;
		spriteNum = 1;
		spriteCounter = 0;
		direction = "down";
		bossArenaActive = false;
		bossArenaCenterX = 0;
		bossArenaCenterY = 0;
		bossArenaRadius = 0;
		// Delete old save file
		java.io.File saveFile = new java.io.File("save.dat");
		if (saveFile.exists()) {
			saveFile.delete();
		}
		setItems();
		gp.resetToFirstMap();
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
	
	public boolean canEquip(Entity item) {
	    return this.level >= item.levelRequirement;
	}

	public void equipItem(Entity item) {
	    if (canEquip(item)) {
	        // Equip logic here
	        if (item.itemType == 0) { // Weapon
				Entity previousWeapon = currentWeapon;
				currentWeapon = item;
				inventory.set(inventory.indexOf(item), previousWeapon);
			}
			else if (item.itemType == 1) { // Hat
				Entity previousHat = currentHat;
				currentHat = item;
				inventory.set(inventory.indexOf(item), previousHat);
			}
			else if (item.itemType == 2) { // Armor
				Entity previousArmor = currentArmor;
				currentArmor = item;
				inventory.set(inventory.indexOf(item), previousArmor);
			}
			else if (item.itemType == 3) { // Boots
				Entity previousBoots = currentBoots;
				currentBoots = item;
				inventory.set(inventory.indexOf(item), previousBoots);
			}
	    } else {
	        // Show message:
	    	gp.ui.addMessage("You need to be level " + item.levelRequirement + " to equip this item.");
	    }
	}

	public void setDefaultPosition() {
		// Set default world coordinates (customize as needed)
		this.worldX = 0;
		this.worldY = 0;
		// Optionally reset direction or other properties
		this.direction = "down";
	}

	public void selectChestItem(int row, int col) {
		if (currentChest == null || !(currentChest instanceof OBJ_Chest)) return;

		OBJ_Chest chest = (OBJ_Chest) currentChest;
		int index = row * gp.ui.maxChestCol + col;
		if (index >= 0 && index < chest.chestInventory.size()) {
			Entity item = chest.chestInventory.get(index);
			if (item != null && inventory.size() < maxInventorySize) {
				inventory.add(item);
				chest.chestInventory.set(index, null); // Remove item from chest
			}
		}
	}

	public void flash() {
		int dx = 0;
		int dy = 0;
		switch (direction) {
			case "up": dy = -1; break;
			case "down": dy = 1; break;
			case "left": dx = -1; break;
			case "right": dx = 1; break;
		}

		int lastFreeX = worldX;
		int lastFreeY = worldY;

		for (int i = 1; i <= FLASH_RANGE; i++) {
			int testX = worldX + dx * gp.tileSize * i;
			int testY = worldY + dy * gp.tileSize * i;
			Rectangle testArea = new Rectangle(testX + solidArea.x, testY + solidArea.y, solidArea.width, solidArea.height);
			boolean blocked = false;

			// Check monsters
			for (Entity monster : gp.monster[gp.currentMap]) {
				if (monster != null && monster.alive && !monster.dying) {
					Rectangle monsterArea = new Rectangle(
						monster.worldX + monster.solidArea.x,
						monster.worldY + monster.solidArea.y,
						monster.solidArea.width,
						monster.solidArea.height
					);
					if (testArea.intersects(monsterArea)) {
						blocked = true;
						break;
					}
				}
			}
			// Check NPCs
			if (!blocked) {
				for (Entity npc : gp.npc[gp.currentMap]) {
					if (npc != null) {
						Rectangle npcArea = new Rectangle(
							npc.worldX + npc.solidArea.x,
							npc.worldY + npc.solidArea.y,
							npc.solidArea.width,
							npc.solidArea.height
						);
						if (testArea.intersects(npcArea)) {
							blocked = true;
							break;
						}
					}
				}
			}
			// Check objects (items/chests/etc)
			if (!blocked) {
				for (Entity obj : gp.obj[gp.currentMap]) {
					if (obj != null && obj.collision) {
						Rectangle objArea = new Rectangle(
							obj.worldX + obj.solidArea.x,
							obj.worldY + obj.solidArea.y,
							obj.solidArea.width,
							obj.solidArea.height
						);
						if (testArea.intersects(objArea)) {
							blocked = true;
							break;
						}
					}
				}
			}

			if (blocked) {
				// Stop at the last free position before the block
				break;
			} else {
				lastFreeX = testX;
				lastFreeY = testY;
			}
		}

		// Move player to the furthest free spot
		worldX = lastFreeX;
		worldY = lastFreeY;
	}

	public void initializeSkills() {
	    // Example: Add all unlocked skills here (could be loaded from save or progression)
	    unlockedSkills.clear();
	    unlockedSkills.add(new skill.SKILL_Fireball(this.gp));
	    unlockedSkills.add(new skill.SKILL_Fireball(this.gp));
	    unlockedSkills.add(new skill.SKILL_Dash(this.gp));
	    unlockedSkills.add(new skill.SKILL_Earthquake(this.gp));
	    // ...add more as needed

	    // Assign default skills to Q/W/E/R (indices 0-3)
	    assignedSkills[0] = unlockedSkills.get(0); // Q
	    assignedSkills[1] = unlockedSkills.get(1); // W
	    assignedSkills[2] = unlockedSkills.get(2); // E
	    assignedSkills[3] = unlockedSkills.get(3); // R

		try {
			earthquakeAnimFrames = new BufferedImage[2]; // Adjust the number to your frame count
			for (int i = 0; i < 2; i++) {
				earthquakeAnimFrames[i] = ImageIO.read(getClass().getResourceAsStream("/player/earthquake_anim_" + (i+1) + ".png"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Use the skill assigned to a specific key (Q/W/E/R)
	public void useAssignedSkill(int index) {
	    if (index < 0 || index >= assignedSkills.length) return;
	    Skill skill = assignedSkills[index];
	    if (skill != null && skill.canUse(mana, level)) {
	        skill.use(this); // Only call use!
	        // For dash, effect is handled in use() and tickDash()
	        // For other skills, call applyEffect if needed
	        if (!(skill instanceof skill.SKILL_Dash)) {
	            skill.applyEffect(gp, this);
	        }
	    } else {
	        gp.ui.addMessage("Not enough mana or skill on cooldown!");
	    }
	}

	// Optionally, add a method to assign a skill to a key (for use in a skill menu)
	public void assignSkillToKey(int keyIndex, Skill skill) {
		if (keyIndex >= 0 && keyIndex < assignedSkills.length && unlockedSkills.contains(skill)) {
			try {
				// Create a new instance of the skill for this slot
				Skill newSkill = skill.getClass().getConstructor(main.GamePanel.class).newInstance(this.gp);
				assignedSkills[keyIndex] = newSkill;
			} catch (Exception e) {
				// Fallback: assign the same instance if reflection fails
				assignedSkills[keyIndex] = skill;
			}
		}
	}

	public static class PendingWave {
	    public int delay;
	    public int radius;
	    public int damage;
	    public PendingWave(int delay, int radius, int damage) {
	        this.delay = delay;
	        this.radius = radius;
	        this.damage = damage;
	    }
	}

	public int calculateNextLevelExp(int level){
		return (int)(100 * (1 - ((4 - level * 30) / (0.1 * level + 4))));
	}
}
