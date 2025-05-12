package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;

public class Entity {
	
	public GamePanel gp;

	public int worldX, worldY;
	public int renderLayer;
	public int speed;
	
	public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2, stand;	//image with  an accessible buffer of image data

	public String direction = "down"; //to store the direction of the entity
	
	public int spriteCounter = 0;
	public int spriteNum = 1;
	
	public Rectangle solidArea = new Rectangle(2, 19, 76, 61); //x, y, width, height
	public int solidAreaDefaultX = 4; 
	public int solidAreaDefaultY = 19; //to store the default position of the solid area
	public boolean collisionOn = false;

	public int actionLockCounter = 0; //to control the action of the entity
	String dialogues[] = new String[40]; //to store the dialogues of the entity
	int dialogIndex = 0; //to store the index of the dialogues

	public BufferedImage image;
	public String name;
	public boolean collision = true;
	
	public boolean alive = true;
	public float maxHealth;
	public float health;
	public int maxMana;
	public int mana;
	public int level;
	public int attack;
	public int defense;

	public Entity(GamePanel gp) {
		this.gp = gp;
		this.renderLayer = 0;
	}


	public void setAction() {
		
	}


	public void speak() {
		
		if (dialogues[dialogIndex] == null) {
			dialogIndex = 0;
		}
		gp.ui.currentDialogue = dialogues[dialogIndex];
		dialogIndex++;

		facePlayer();
	}

	public void update() {

		if (gp.gameState == gp.dialogueState) {
			spriteCounter = 0;
			spriteNum = 1;
			return;
		}
		
		setAction();

		collisionOn = false;
		gp.cChecker.checkTile(this); //check tile collision
		gp.cChecker.checkObject(this, false); //check object collision
		gp.cChecker.checkEntity(this, gp.npc); //check entity 
		gp.cChecker.checkEntity(this, gp.monster); //check monster collision
		gp.cChecker.checkPlayer(this); //check player collision

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

			g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);		
			g2.setColor(Color.red);
			g2.drawRect(screenX + solidAreaDefaultX, screenY + solidAreaDefaultY, solidArea.width, solidArea.height); // debug rectangle
		}
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

	public void facePlayer(){
		// Make NPC face the player (no position flip)
		if (gp.player.direction.equals("up")) this.direction = "down";
		else if (gp.player.direction.equals("down")) this.direction = "up";
		else if (gp.player.direction.equals("left")) this.direction = "right";
		else if (gp.player.direction.equals("right")) this.direction = "left";
	}
}
