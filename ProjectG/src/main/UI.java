package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class UI {
	
	GamePanel gp;
	Graphics2D g2;
	Font pixelOperator, pixelOperatorBold;
	public boolean messageOn = false;
	// public String message = "";
	// int messageCounter = 0;
	ArrayList<String> messages = new ArrayList<>();
	ArrayList<Integer> messageCounter = new ArrayList<>();
	public boolean gameFinished = false;

	public String currentDialogue = "";

	public int commandNum = 0;	//to store the command number of the menu
	
	public int titleScreenState = 0; 
	public UI(GamePanel gp) {
		this.gp = gp;
		
		try {
			//normal
			InputStream is = getClass().getResourceAsStream("/font/PixelOperator.ttf");
			pixelOperator = Font.createFont(Font.TRUETYPE_FONT, is);
			//bold
			is = getClass().getResourceAsStream("/font/PixelOperator-Bold.ttf");
			pixelOperatorBold = Font.createFont(Font.TRUETYPE_FONT, is);
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addMessage (String text) {
		
		messages.add(text);
		messageCounter.add(0);
	}
	public void draw(Graphics2D  g2) {
		
		this.g2 = g2;
		g2.setFont(pixelOperator);
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.setColor(Color.white);

		//Title State
		if(gp.gameState == gp.titleState) {
			drawTitleScreen();
		}

		//Play State
		else if(gp.gameState == gp.playState) {
			drawPlayerHealth();
			drawPlayerMana();
			drawPlayerIcon();
			//Draw messages
			drawMessages();
		}

		//Pause State
		else if (gp.gameState == gp.pauseState) {
			drawPauseScreen();
		}

		//Dialogue State
		else if (gp.gameState == gp.dialogueState) {
			drawDialogueScreen();
		}

		//Character State
		else if (gp.gameState == gp.characterState) {
			drawCharacterScreen();
			drawPlayerHealth();
			drawPlayerMana();
			drawPlayerIcon();
		}
	}

	public void drawTitleScreen(){

		if (titleScreenState == 0) {
			g2.setColor(new Color(243, 193, 8));
			g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

			//Title Name
			g2.setFont(pixelOperatorBold.deriveFont(Font.PLAIN, 120F));
			
			String text = "Legends of the Red Wheels";
			int x = getXForCenteredText(text);
			int y = gp.tileSize * 5;

			//Shadow
			g2.setColor(new Color(0, 0, 0, 80));
			g2.drawString(text, x + gp.tileSize / 13, y + gp.tileSize / 13);

			//Main text
			g2.setColor(new Color(206, 22, 40));
			g2.drawString(text, x, y);

			//Logo
			BufferedImage logo = null;
			BufferedImage groupLogo = null;
			try {
				logo = ImageIO.read(getClass().getResourceAsStream("/logo/logo.png"));
				groupLogo = ImageIO.read(getClass().getResourceAsStream("/logo/group_logo.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			int x2 = gp.screenWidth/2 - (gp.tileSize * 9)/2;	
			int y2 = gp.tileSize * 1 - gp.tileSize/2;
			g2.drawImage(logo, x2, y2, gp.tileSize *3, gp.tileSize * 3, null);
			g2.drawImage(groupLogo, x2 + gp.tileSize * 6, y2 - gp.tileSize / 8, gp.tileSize * 3, gp.tileSize * 3, null);

			//Menu
			g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 60F));
			text = "NEW GAME";
			x = getXForCenteredText(text);
			y = gp.screenHeight/2 + gp.tileSize * 2 / 3;
			g2.drawString(text, x, y);
			if (commandNum == 0) {
				g2.drawString(">", x - gp.tileSize, y);
				g2.drawString("<", gp.screenWidth - x + 2 * gp.tileSize / 3, y);
				
			}

			text = "LOAD GAME";
			x = getXForCenteredText(text);
			y += 3 * gp.tileSize / 2;
			g2.drawString(text, x, y);
			if (commandNum == 1) {
				g2.drawString(">", x - gp.tileSize, y);
				g2.drawString("<", gp.screenWidth - x + 2 * gp.tileSize / 3, y);
				
			}

			text = "SETTINGS";
			x = getXForCenteredText(text);
			y += 3 * gp.tileSize / 2;
			g2.drawString(text, x, y);
			if (commandNum == 2) {
				g2.drawString(">", x - gp.tileSize, y);
				g2.drawString("<", gp.screenWidth - x + 2 * gp.tileSize / 3, y); // the opposite side of the screen
				
			}

			text = "QUIT";
			x = getXForCenteredText(text);
			y += 3 * gp.tileSize / 2;
			g2.drawString(text, x, y);
			if (commandNum == 3) {
				g2.drawString(">", x - gp.tileSize, y);
				g2.drawString("<", gp.screenWidth - x + 2 * gp.tileSize / 3, y);
				
			}
		} else if (titleScreenState == 1) {

			g2.setColor(new Color(243, 193, 8));
			g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

			//Title Name
			g2.setFont(pixelOperatorBold.deriveFont(Font.PLAIN, 120F));
		
			String text = "Legends of the Red Wheels";
			int x = getXForCenteredText(text);
			int y = gp.tileSize * 5;

			//Shadow
			g2.setColor(new Color(0, 0, 0, 80));
			g2.drawString(text, x + gp.tileSize / 13, y + gp.tileSize / 13);

			//Main text
			g2.setColor(new Color(206, 22, 40));
			g2.drawString(text, x, y);

			//Logo
			BufferedImage logo = null;
			BufferedImage groupLogo = null;
			try {
				logo = ImageIO.read(getClass().getResourceAsStream("/logo/logo.png"));
				groupLogo = ImageIO.read(getClass().getResourceAsStream("/logo/group_logo.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			int x2 = gp.screenWidth/2 - (gp.tileSize * 9)/2;	
			int y2 = gp.tileSize * 1 - gp.tileSize/2;
			g2.drawImage(logo, x2, y2, gp.tileSize *3, gp.tileSize * 3, null);
			g2.drawImage(groupLogo, x2 + gp.tileSize * 6, y2 - gp.tileSize / 8, gp.tileSize * 3, gp.tileSize * 3, null);
			
			//Add player name

			g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 60F));
			text = "Enter your name: ";
			x = getXForCenteredText(text);
			y = gp.screenHeight/2 + gp.tileSize * 2 / 3;
			g2.drawString(text, x, y);

			g2.drawString(gp.player.name, x, y + 3 * gp.tileSize / 2);

			x2 = getXForCenteredText(gp.player.name);
			text = "Enter";
			g2.drawString(text, x2, y + 3 * gp.tileSize);
			g2.drawString(">", x2 - gp.tileSize, y + 3 * gp.tileSize);
			g2.drawString("<", gp.screenWidth - x2 + 2 * gp.tileSize / 3, y + 3 * gp.tileSize);	
		}
	}

	public void drawPauseScreen() {

		// Set the color and transparency for the overlay
		g2.setColor(new Color(0, 0, 0, 180)); // Black with 70% opacity (alpha = 180 out of 255)
    
		// Draw the overlay over the entire screen
		g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 80F));
		g2.setColor(Color.WHITE);
		String text = "PAUSED";
		int x = getXForCenteredText(text);
		int y = gp.screenHeight/2 - 5 * gp.tileSize / 2;

		g2.drawString(text, x, y);

	}

	public void drawDialogueScreen() {
		//Window
		int x = gp.tileSize * 4;
		int y = gp.screenHeight - gp.tileSize * 6;
		int width = gp.screenWidth - (gp.tileSize * 8);
		int height = gp.tileSize * 5;

		drawSubWindow(x, y, width, height);

		//Text
		x += 2* gp.tileSize / 3;
		y += gp.tileSize;
		g2.setColor(new Color(255, 255, 255));
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 40F));

		for (String line : currentDialogue.split("\n")) {
			g2.drawString(line, x, y);
			y += 5 * gp.tileSize / 8;
		}


	}



	public void drawPlayerHealth() {
		//Health Bar
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 40F));
		g2.setColor(new Color(255, 255, 255));
		int x = 5 * gp.tileSize / 2;
		int y = gp.tileSize / 2;
		int width = gp.tileSize * 6;
		int height = 2 * gp.tileSize / 3;

		g2.drawRoundRect(x, y, width, height, 7 * gp.tileSize / 13, 7 * gp.tileSize / 13);
		g2.fillRoundRect(x, y, width, height, 7 * gp.tileSize / 13, 7 * gp.tileSize / 13);

		//Current Health
		g2.setColor(new Color(206, 22, 40));
		int xHealth = x + 3 * gp.tileSize / 80;
		int yHealth = y + 3 * gp.tileSize / 80;
		if (gp.player.health > 0){
			width = (int) ((gp.player.health / gp.player.maxHealth) * width) - 3 * gp.tileSize / 40;
		} else {
			width = 0;
		}
		height = height - 3 * gp.tileSize / 40;
		g2.fillRoundRect(xHealth, yHealth, width, height, 35, 35);

		//Current Health Text
		g2.setColor(new Color(255, 255, 255));
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 60F));
		String text = (int)gp.player.health + "/" + (int)gp.player.maxHealth;
		int xText = x + 3 * gp.tileSize / 40;
		int yText = y + height - gp.tileSize / 20;
		g2.drawString(text, xText, yText);
	}


	public void drawPlayerMana() {
		//Mana Bar
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 40F));
		g2.setColor(new Color(255, 255, 255));
		int x = 5 * gp.tileSize / 2;
		int y = 4 * gp.tileSize / 3;
		int width = gp.tileSize * 4;
		int height = gp.tileSize / 3;

		g2.drawRoundRect(x, y, width, height, 7 * gp.tileSize / 13, 7 * gp.tileSize / 13);
		g2.fillRoundRect(x, y, width, height, 7 * gp.tileSize / 13, 7 * gp.tileSize / 13);
		//Current Mana
		g2.setColor(new Color(41, 107, 167));
		int xMana = x + 3 * gp.tileSize / 80;
		int yMana = y + 3 * gp.tileSize / 80;
		if (gp.player.mana > 0){
			width = (int) ((gp.player.mana / gp.player.maxMana) * width) - 6;
		} else {
			width = 0;
		}
		height = height - 6;
		g2.fillRoundRect(xMana, yMana, width, height, 35, 35);
		//Current Mana Text
		g2.setColor(new Color(255, 255, 255));
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 27F));
		String text = gp.player.mana + "/" + gp.player.maxMana;
		int xText = x + 3 * gp.tileSize / 40;
		int yText = y + height;
		g2.drawString(text, xText, yText);
	}

	public void drawPlayerIcon(){
		BufferedImage image = null;
		int x = gp.tileSize / 2;
		int y = gp.tileSize / 2;
		int width = gp.tileSize * 2;
		int height = gp.tileSize * 2;

		UtilityTool uTool = new UtilityTool();
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/player/player_icon.png"));
			image = uTool.scaleImage(image, width, height);
		}catch(IOException e) {
			e.printStackTrace();
		}
		g2.drawImage(image, x, y, width, height, null);
	}

	public void drawMessages() {
		int messageX = gp.tileSize / 2;
		int messageY = gp.tileSize * 5;
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 40F));
		
		for (int i = 0; i < messages.size(); i++) {
			if (messageCounter.get(i) < 90) {
				//Draw the message shadow
				g2.setColor(new Color(0, 0, 0, 80));
				g2.drawString(messages.get(i), messageX + gp.tileSize / 13, messageY + gp.tileSize / 13);
				
				//Draw the message
				g2.setColor(Color.white);
				g2.drawString(messages.get(i), messageX, messageY);
				
				messageCounter.set(i, messageCounter.get(i) + 1);
				messageY += gp.tileSize / 2;
			}
		}
	}

	//Character Screen
	public void drawCharacterScreen() {
		//Window
		final int frameX = gp.tileSize * 14;
		final int frameY = gp.tileSize;
		final int frameWidth = gp.tileSize * 9;
		final int frameHeight = gp.tileSize * 10;

		drawSubWindow(frameX, frameY, frameWidth, frameHeight);

		//Text
		g2.setColor(Color.white);
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 40F));

		int textX = frameX + gp.tileSize / 2;
		int textY = frameY + gp.tileSize;
		final int lineHeight = 5 * gp.tileSize / 8;

		g2.drawString("Name: " + gp.player.name, textX, textY);
		textY += lineHeight;
		g2.drawString("Level: " + gp.player.level, textX, textY);
		textY += lineHeight;
		g2.drawString("Health: " + (int)gp.player.health + "/" + (int)gp.player.maxHealth, textX, textY);
		textY += lineHeight;
		g2.drawString("Mana: " + gp.player.mana + "/" + gp.player.maxMana, textX, textY);
		textY += lineHeight;
		g2.drawString("Attack: " + gp.player.attack, textX, textY);
		textY += lineHeight;
		g2.drawString("Defense: " + gp.player.defense, textX, textY);
		textY += lineHeight;
		g2.drawString("Speed: " + gp.player.speed, textX, textY);
		textY += lineHeight;
		g2.drawString("EXP: " + gp.player.exp, textX, textY);
		textY += lineHeight;
		g2.drawString("Progression Points: " + gp.player.progressionPoint, textX, textY);
		textY += lineHeight;
		g2.drawString("Weapon: " + gp.player.currentWeapon.name, textX, textY);
		

		//Variable
		int tailX = frameX + frameWidth - gp.tileSize / 2;

		String value = "Level: " + gp.player.level;
		textX = getXForAllignToRightText(value, tailX);
		g2.drawString(value, textX, textY);

	}


	public void drawSubWindow(int x, int y, int width, int height) {
		int arc = 35;
		int borderInset = 7;
		Color c = new Color(0, 0, 0, 200);
		g2.setColor(c);
		g2.fillRoundRect(x, y, width, height, arc, arc);

		c = new Color(255, 255, 255);
		g2.setColor(c);
		g2.setStroke(new BasicStroke(borderInset));
		g2.drawRoundRect(x + borderInset * 3, y + borderInset * 3, width - borderInset * 6, height - borderInset * 6, arc, arc);		
		g2.setStroke(new BasicStroke(1f)); // Reset to default stroke
	}
	public int getXForCenteredText(String text) {
		int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		int x = gp.screenWidth/2 - length/2;
		return x;
	}

	public int getXForAllignToRightText(String text, int tailX) {
		int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		int x = tailX - length;
		return x;
	}
}