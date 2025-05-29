package entity;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;

public class Entity implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public transient GamePanel gp;

	public int worldX, worldY;
	public int renderLayer;
	public int speed;
	
	public transient BufferedImage up1, up2, down1, down2, left1, left2, right1, right2, stand, standLeft, standRight, standUp, fullBody;	//image with  an accessible buffer of image data
	public transient BufferedImage attackUp1, attackUp2, attackDown1, attackDown2, attackLeft1, attackLeft2, attackRight1, attackRight2; //image with an accessible buffer of image data

	public String direction = "down"; //to store the direction of the entity
	
	public int spriteCounter = 0;
	public int spriteNum = 1;
	
	public Rectangle solidArea = new Rectangle(2, 19, 76, 61); //x, y, width, height
	public int solidAreaDefaultX = 4; 
	public int solidAreaDefaultY = 19; //to store the default position of the solid area
	public Rectangle attackArea = new Rectangle(0, 0, 0, 0); //to store the attack area of the entity
	public int attackAreaDefaultX = 0;
	public int attackAreaDefaultY = 0;
	public boolean collisionOn = false;
	public int type; //0 = player, 1 = monster, 2 = npc, 3 = object, 4 = tile

	public int knockbackCounter = 0;
	public int knockbackDuration = 10; // frames
	public int knockbackDX = 0;
	public int knockbackDY = 0;
	public int actionLockCounter = 0; //to control the action of the entity
	public boolean invincible = false; //to check if the entity is invincible
	public int invincibleCounter = 0; //to check the invincibility counter
	String dialogues[] = new String[40]; //to store the dialogues of the entity
	int dialogIndex = 0; //to store the index of the dialogues

	public boolean showHpBar = false;
	public int hpBarDisplayCounter = 0;

	public BufferedImage image;
	public String name;
	public boolean collision = true;
	public boolean attacking = false;

	public boolean alive = true;
	public boolean dying = false;
	public int dyingCounter = 0;
	public boolean onPath = false; //to check if the entity is on a path
	public transient ArrayList<ai.Node> pathList = new ArrayList<>();
	
	//Attributes
	public float maxHealth;
	public float health;
	public int maxMana;
	public int mana;
	public int level;
	public int attack;
	public int defense;
	public int progressionHealthUpgrades;
	public int progressionManaUpgrades;
	public int progressionAttackUpgrades;
	public int progressionDefenseUpgrades;

	

	//Player extra attributes
	public int exp;
	public int nextLevelExp;
	public int totalProgressionPoints;
	public int progressionPoints;
	public Entity currentHat;
	public Entity currentWeapon;
	public Entity currentArmor;
	public Entity currentBoots;
	public Projectile projectile;


	//Item attributes
	public int itemType; //0 = weapon, 1 = hat, 2 = armor, 3 = boots, 4 = interactable, 5 = potion, 6 = food
	public int healthBonus;
	public int manaBonus;
	public int attackBonus;
	public int defenseBonus;
	public int speedBonus;
	public int attackRange;
	public int weaponType; //0 = sword, 1 = axe, 2 = spear, 3 = bow, 4 = wand
	public int rarity = 0; //0 = common, 1 = rare, 2 = epic, 3 = legendary
	public int levelRequirement = 0; //level requirement to use the item
	public String description = "";
	public int cooldownBonus;
	public boolean stackable = false; //to check if the item is stackable
	public boolean pickable = false;

	public int quantity = 1; //quantity of the item
	public int healthHeal = 0; //health heal of the item
	public int manaHeal = 0; //mana heal of the item

	public float healthCost = 0; //health cost of the item
	public int manaCost = 0; //mana cost of the item

	//Monster attributes
	public int expReward;
	public int timeSinceLastHit = 0; // in frames
	public int healDelay = 300; // 5 seconds at 60fps


	public Entity(GamePanel gp) {
		this.gp = gp;
		this.renderLayer = 0;
	}


	public void setAction() {
		
	}

	public void damageReaction() {
		int dx = worldX - gp.player.worldX;
		int dy = worldY - gp.player.worldY;
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

	public void use(Entity entity) {

	}

	public void speak() {
		
		if (dialogues[dialogIndex] == null) {
			dialogIndex = 0;
		}
		gp.ui.currentDialogue = dialogues[dialogIndex];
		dialogIndex++;

		facePlayer();
	}

	public void checkDrop() {


	}

	public void dropItem(Entity droppedItem) {
		for (int i = 0; i < gp.obj.length; i++) {
			if (gp.obj[gp.currentMap][i] == null) {

				gp.obj[gp.currentMap][i] = droppedItem;
				gp.obj[gp.currentMap][i].worldX = worldX;
				gp.obj[gp.currentMap][i].worldY = worldY;
				break;
				
			}
		}
	}

	public void checkCollision(){
		collisionOn = false;
		gp.cChecker.checkTile(this); //check tile collision
		gp.cChecker.checkObject(this, false); //check object collision
		gp.cChecker.checkEntity(this, gp.npc); //check entity 
		gp.cChecker.checkEntity(this, gp.monster); //check monster collision
		boolean contactPlayer = gp.cChecker.checkPlayer(this); //check player collision

		if (this.type == 1 && contactPlayer == true) {
			damagePlayer(attack);
		}
	}
	public void update() {

		if (gp.gameState == gp.dialogueState) {
			spriteCounter = 0;
			spriteNum = 1;
			return;
		}
		
		setAction();
		checkCollision();

		
		if (this.type == 1 && alive && !dying){
			timeSinceLastHit++;
			if (timeSinceLastHit >= healDelay) {
				if (health < maxHealth) {
					health += 0.1 * maxHealth; // Heal 10% health every 5 seconds
					if (health > maxHealth) {
						health = maxHealth;
					}
				}	
			}
		}

		if(collisionOn == false) {
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
			String[] directions = {"up", "down", "left", "right"};
		direction = directions[new java.util.Random().nextInt(directions.length)];
		actionLockCounter = 0;

		}
		

		spriteCounter++;
		if(spriteCounter > 12) {
			if(spriteNum == 1) {
				spriteNum = 2;
			}
			else if(spriteNum == 2) {
				spriteNum = 1;
			}
			spriteCounter = 0;
		}

		if (invincible == true) {
			invincibleCounter++;
			if (invincibleCounter > 30) {
				invincible = false;
				invincibleCounter = 0;
			}
		}		

		if (showHpBar == true) {
			hpBarDisplayCounter--;
			if (hpBarDisplayCounter <= 0) {
				showHpBar = false;
			}
		}

		if (knockbackCounter > 0) {
			worldX += knockbackDX;
			worldY += knockbackDY;
			knockbackCounter--;
			// Optionally, check collision and stop knockback if blocked
			return; // Skip normal movement while being knocked back
		}
	}

	public void draw(Graphics2D g2){

		BufferedImage image = null;

		int screenX = worldX - gp.player.worldX + gp.player.screenX;
		int screenY = worldY - gp.player.worldY + gp.player.screenY;
		
		if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
			worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
			worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
			worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {

				switch(direction) {
					case "up":
						if (spriteNum == 1) {
							image = up1;
						}
						if (spriteNum == 2) {
							image = up2;
						}
						break;
					case "down":
						if (spriteNum == 1) {
							image = down1;
						}
						if (spriteNum == 2) {
							image = down2;
						}
						break;
					case "left":
						if (spriteNum == 1) {
							image = left1;
						}
						if (spriteNum == 2) {
							image = left2;
						}
						break;
					case "right":
						if (spriteNum == 1) {
							image = right1;
						}
						if (spriteNum == 2) {
							image = right2;
						}
						break;
					}

					//HP Bar
					if (this.type == 1 && (showHpBar == true || dying == true)) {
						double oneScale = (double)gp.tileSize / maxHealth;
						double hpBarValue = Math.max(0, (double)health * oneScale);
						g2.setColor(new Color(35, 35, 35));
						g2.fillRect(screenX - 1, screenY - 16, gp.tileSize + 2, 12);
						g2.setColor(new Color(255, 0, 30));
						g2.fillRect(screenX, screenY - 15, (int)hpBarValue, 10);
					}

					if (invincible == true) {
						g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
					}

					if (dying == true) {
						deathAnimation(g2);
					}

			g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);		
			g2.setColor(Color.red);
			g2.drawRect(screenX + solidAreaDefaultX, screenY + solidAreaDefaultY, solidArea.width, solidArea.height); // debug rectangle
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
		}
	}
	
	
	public void deathAnimation(Graphics2D g2){ 
		int i = 5;
		dyingCounter++;
		if (dyingCounter <= i) changeAlpha(g2, 0f);
		if (dyingCounter > 2*i && dyingCounter <= 3*i) changeAlpha(g2, 0f);
		if (dyingCounter > 4*i && dyingCounter <= 5*i) changeAlpha(g2, 0f);
		if(dyingCounter > 6*i && dyingCounter <= 7*i) changeAlpha(g2, 0f);
		if (dyingCounter > i && dyingCounter <= 2*i)   changeAlpha(g2, 1f);
		if (dyingCounter > 3*i && dyingCounter <= 4*i)  changeAlpha(g2, 1f);
		if (dyingCounter > 5*i && dyingCounter <= 6*i)  changeAlpha(g2, 1f);
		if (dyingCounter > 7*i && dyingCounter <= 8*i)  changeAlpha(g2, 1f);
		if (dyingCounter > 8*i) {
			alive = false;
		}
	}

	public void changeAlpha(Graphics2D g2, float alpha) {
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
	}

	public BufferedImage setup(String imagePath) {

		UtilityTool uTool = new UtilityTool();
		BufferedImage image = null;
		try {
			image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
			image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);
		}catch(IOException e) {
			e.printStackTrace();
		}
		return image;
	}

	public void damagePlayer(int attack){
		if (gp.player.invincible == false && gp.player.collision == true) {
			int damage = attack - gp.player.getTotalDefense();
			if (damage < 0) {
				damage = 0;
			}
			gp.player.health -= damage;
			gp.player.invincible = true;
			gp.player.invincibleCounter = 0;
		}
	}

	public void facePlayer(){
		// Make NPC face the player (no position flip)
		if (gp.player.direction.equals("up")) this.direction = "down";
		else if (gp.player.direction.equals("down")) this.direction = "up";
		else if (gp.player.direction.equals("left")) this.direction = "right";
		else if (gp.player.direction.equals("right")) this.direction = "left";
	}

	public void searchPath(int goalCol, int goalRow) {

		int startCol = (worldX + solidArea.x) / gp.tileSize;
		int startRow = (worldY + solidArea.y) / gp.tileSize;

		gp.pFinder.setNodes(startCol, startRow, goalCol, goalRow, this);

		if (gp.pFinder.search() == true){

			int nextX = gp.pFinder.pathList.get(0).col * gp.tileSize;
			int nextY = gp.pFinder.pathList.get(0).row * gp.tileSize;

			int enLeftX = worldX + solidArea.x;
			int enRightX = worldX + solidArea.x + solidArea.width;
			int enTopY = worldY + solidArea.y;
			int enBottomY = worldY + solidArea.y + solidArea.height;

			if (enTopY > nextY && enLeftX >= nextX && enRightX < nextX + gp.tileSize) {
				if (enTopY > nextY) {
					direction = "up";
				}
				if (enTopY < nextY) {
					direction = "down";
				}
			} else if (enTopY >= nextY && enBottomY < nextY + gp.tileSize) {
				if (enLeftX < nextX) {
					direction = "right";
				} else if (enRightX > nextX + gp.tileSize) {
					direction = "left";
				}
			} else if (enTopY > nextY && enLeftX > nextX){
				direction = "up";
				checkCollision();
				if (collisionOn == true) {
					direction = "left";
				}
			} else if (enTopY > nextY && enLeftX < nextX){
				direction = "up";
				checkCollision();
				if (collisionOn == true) {
					direction = "right";
				}
			} else if (enTopY < nextY && enLeftX > nextX){
				direction = "down";
				checkCollision();
				if (collisionOn == true) {
					direction = "left";
				}
			} else if (enTopY < nextY && enLeftX < nextX){
				direction = "down";
				checkCollision();
				if (collisionOn == true) {
					direction = "right";
				}
			}

			int nextCol = gp.pFinder.pathList.get(0).col;
			int nextRow = gp.pFinder.pathList.get(0).row;
			if(goalCol == nextCol && goalRow == nextRow && (worldX / gp.tileSize == goalCol && worldY / gp.tileSize == goalRow)) {
				onPath = false;
			} 
		}
	}

	public void restoreTransientFields(GamePanel gp) {
    this.gp = gp;
    try {
        if (this.type == 0 && this instanceof Player) {
            ((Player)this).getPlayerImage();
            if (((Player)this).currentWeapon != null) {
                ((Player)this).getPlayerAttackImage();
            }
        } else {
            // Try all common image-loading methods in order
            boolean loaded = false;
            String[] methodNames = {
                "getImage",         // Most of your entities use this
                "getMonsterImage",  // Some monsters
                "getNPCImage",      // Some NPCs
                "getObjectImage",   // Some objects
                "setup"             // Some items/objects
            };
            for (String method : methodNames) {
                try {
                    this.getClass().getMethod(method).invoke(this);
                    loaded = true;
                    break;
                } catch (NoSuchMethodException ignored) {
                    // Try next method
                }
            }
            // If none of the above methods exist, do nothing
        }
    } catch (Exception ignored) {}
}
}
