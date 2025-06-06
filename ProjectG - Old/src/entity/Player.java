package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;


import main.GamePanel;
import main.KeyHandler;

public class Player extends Entity{
	
	KeyHandler keyH;
	
	public final int screenX;
	public final int screenY;
	public boolean justFinishTalking = false;
	int standCounter = 0;
	int collisionRecoilCounter = 0;
    final int RECOIL_DURATION = 10;
	
	public Player(GamePanel gp, KeyHandler kH) {

		super(gp);
		this.keyH = kH;

		screenX = gp.screenWidth/2 - gp.tileSize/2;
		screenY = gp.screenHeight/2 - gp.tileSize/2;
		
		solidArea = new Rectangle();
		solidArea.x = 10;
		solidArea.y = 15;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		solidArea.width = 60;
		solidArea.height= 65;
		
		
		setDefaultValues();
	}
	
	public void setDefaultValues() {
		worldX = gp.tileSize * 50;
		worldY = gp.tileSize * 50;
		int defaultSpeed = 5;
		speed = defaultSpeed;
		direction = "down";

		//Status
		this.name = "Player";
		this.maxHealth = 1800;
		this.health = maxHealth;
		this.maxMana = 400;
		this.mana = maxMana;

		getPlayerImage();
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

	}

	public void update() {
		
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
			pickUpObject(objIndex);

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
		
		if (justFinishTalking == true) {
			justFinishTalking = false;
		}

	}
	

	public void pickUpObject(int i) {
		if(i != 999) {

			}
		
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

	public void contactMonster(int i){
		if(i != 999){

			health -= gp.monster[i].attack;
		}
	}

	public void draw(Graphics2D g2) {
		
		BufferedImage image= null;
		
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
		default:
			image = stand;
			break;
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

		g2.drawImage(image, x, y, null);
		g2.setColor(Color.red);
		g2.drawRect(x + solidArea.x, y + solidArea.y, solidArea.width, solidArea.height);
		
	}
}
