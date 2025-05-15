package entity;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
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
		solidArea.x = 40;
		solidArea.y = 40;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		solidArea.width = 80;
		solidArea.height= 80;
		
		
		setDefaultValues();
	}
	
	public void setDefaultValues() {
		worldX = gp.tileSize * 50 - gp.tileSize/2;
		worldY = gp.tileSize * 50 - gp.tileSize/2;
		int defaultSpeed = 5;
		speed = defaultSpeed;
		direction = "down";

		//Status
		this.name = "Player";
		this.maxHealth = 1800;
		this.health = maxHealth;
		this.maxMana = 400;
		this.mana = maxMana;
		this.type = 0;

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
		stand = setup("/player/player_stand");
		standLeft = setup("/player/player_stand_left");
		standRight = setup("/player/player_stand_right");
		standUp = setup("/player/player_stand_up");


	}

	public void getPlayerAttackImage() {
		attackUp1 = setup("/player/player_attack_up_1");
		attackUp2 = setup("/player/player_attack_up_2");
		attackDown1 = setup("/player/player_attack_down_1");
		attackDown2 = setup("/player/player_attack_down_2");
		attackLeft1 = setup("/player/player_attack_left_1");
		attackLeft2 = setup("/player/player_attack_left_2");
		attackRight1 = setup("/player/player_attack_right_1");
		attackRight2 = setup("/player/player_attack_right_2");
		
	}

	public void update() {

		if (attacking == true) {
			// Attack logic here
			attacking();
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
		}
		if (spriteCounter > 25){
			spriteNum = 1;
			spriteCounter = 0;
			attacking = false;
		}
	}

	public void pickUpObject(int i) {
		if(i != 999) {

			}
		
	}

	public void interactNPC(int i) {
		
		if (gp.keyH.enterPressed == true) {

			if(i != 999) {
				gp.gameState = gp.dialogueState;
				gp.npc[i].speak();
			} else {
				attacking = true;
			}
		}
	}

	public void contactMonster(int i){
		if(i != 999){

			if (invincible == false && gp.monster[i].collision == true) {
				gp.player.health -= gp.monster[i].attack - defense;
				collisionRecoilCounter = RECOIL_DURATION;
				spriteNum = 1; // Set spriteNum to 1 during recoil
				invincible = true;
			}
		}
	}

	public void draw(Graphics2D g2) {
		
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

		BufferedImage image= null;

		if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed) {
        switch (direction) {
            case "up":
                if (!attacking) image = (spriteNum == 1) ? up1 : up2;
				if (attacking) image = (spriteNum == 1) ? attackUp1 : attackUp2;
                break;
            case "down":
                if (!attacking) image = (spriteNum == 1) ? down1 : down2;
				if (attacking) image = (spriteNum == 1) ? attackDown1 : attackDown2;
                break;
            case "left":
				if (!attacking) image = (spriteNum == 1) ? left1 : left2;
				if (attacking) image = (spriteNum == 1) ? attackLeft1 : attackLeft2;
                break;
            case "right":
                if (!attacking) image = (spriteNum == 1) ? left1 : left2;
				if (attacking) image = (spriteNum == 1) ? attackLeft1 : attackLeft2;
                break;
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
		

    	// Draw scaled buffer at position
    	g2.drawImage(image, x, y, gp.tileSize * 2, gp.tileSize * 2, null); // 160x160 final size
		// Restore composite
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

		// Draw solidArea rectangle for debugging
		g2.setColor(Color.red);
		g2.drawRect(x + solidArea.x, y + solidArea.y, solidArea.width, solidArea.height);
		
	}
}
