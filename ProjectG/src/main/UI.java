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

import entity.Entity;

public class UI {
	
	GamePanel gp;
	Graphics2D g2;
	Font currentFont, currentFontBold;
	public boolean messageOn = false;
	// public String message = "";
	// int messageCounter = 0;
	ArrayList<String> messages = new ArrayList<>();
	ArrayList<Integer> messageCounter = new ArrayList<>();
	public boolean gameFinished = false;

	public String currentDialogue = "";

	public int commandNum = 0;	//to store the command number of the menu
	
	public int titleScreenState = 0; 

	public int subState = 0; 

	public int controlsCommandNum = 0;
	public boolean waitingForKey = false;
	public String waitingAction = null;
	public final String[] controlActions = {
		KeyConfig.UP, KeyConfig.DOWN, KeyConfig.LEFT, KeyConfig.RIGHT,
		KeyConfig.ATTACK, KeyConfig.CHOOSE, KeyConfig.ESCAPE, KeyConfig.INVENTORY, KeyConfig.CHARACTER, KeyConfig.RESET
	};
	public boolean keyBindWarning = false;
	public long keyBindWarningTime = 0;

	public int graphicsCommandNum = 0;
	public String[] resolutions = {
		"1920x1080", "1600x900", "1280x720", "1024x576", "800x450"
	};
	public int resolutionIndex = 0;
	public boolean vsyncOn = true;
	public String[] qualities = {"Low", "Medium", "High"};
	public int qualityIndex = 2;

	public boolean fullscreenOn = true;

	public UI(GamePanel gp) {
		this.gp = gp;
		
		try {
			//normal
			InputStream is = getClass().getResourceAsStream("/font/PixelOperator.ttf");
			currentFont = Font.createFont(Font.TRUETYPE_FONT, is);
			//bold
			is = getClass().getResourceAsStream("/font/PixelOperator-Bold.ttf");
			currentFontBold = Font.createFont(Font.TRUETYPE_FONT, is);
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
	public void draw(Graphics2D g2) {
		
		this.g2 = g2;
		g2.setFont(currentFont);
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
		}

		//Inventory State
		else if (gp.gameState == gp.inventoryState) {
			drawInventoryScreen();
			drawCharacterScreen();		
		}

		//Chest State
		else if (gp.gameState == gp.chestState) {
			drawInventoryScreen();
			drawChestScreen();
		}

		else if (gp.gameState == gp.optionsState) {
			drawOptionsScreen();
		}


	}

	public void drawTitleScreen(){

		if (titleScreenState == 0) {
			g2.setColor(new Color(243, 193, 8));
			g2.fillRect(0, 0, gp.baseWidth, gp.baseHeight);

			//Title Name
			g2.setFont(currentFontBold.deriveFont(Font.PLAIN, 120F));
			
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
			int x2 = gp.baseWidth/2 - (gp.tileSize * 9)/2;	
			int y2 = gp.tileSize * 1 - gp.tileSize/2;
			g2.drawImage(logo, x2, y2, gp.tileSize *3, gp.tileSize * 3, null);
			g2.drawImage(groupLogo, x2 + gp.tileSize * 6, y2 - gp.tileSize / 8, gp.tileSize * 3, gp.tileSize * 3, null);

			//Menu
			g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 60F));
			text = "NEW GAME";
			x = getXForCenteredText(text);
			y = gp.baseHeight/2 + gp.tileSize * 2 / 3;
			g2.drawString(text, x, y);
			if (commandNum == 0) {
				g2.drawString(">", x - gp.tileSize, y);				
			}

			text = "LOAD GAME";
			x = getXForCenteredText(text);
			y += 3 * gp.tileSize / 2;
			g2.drawString(text, x, y);
			if (commandNum == 1) {
				g2.drawString(">", x - gp.tileSize, y);				
			}

			text = "SETTINGS";
		 	x = getXForCenteredText(text);
			y += 3 * gp.tileSize / 2;
			g2.drawString(text, x, y);
			if (commandNum == 2) {
				g2.drawString(">", x - gp.tileSize, y);				
			}

			text = "QUIT";
			x = getXForCenteredText(text);
			y += 3 * gp.tileSize / 2;
			g2.drawString(text, x, y);
			if (commandNum == 3) {
				g2.drawString(">", x - gp.tileSize, y);				
			}
		} else if (titleScreenState == 1) {

			g2.setColor(new Color(243, 193, 8));
			g2.fillRect(0, 0, gp.baseWidth, gp.baseHeight);

			//Title Name
			g2.setFont(currentFontBold.deriveFont(Font.PLAIN, 120F));
		
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
			int x2 = gp.baseWidth/2 - (gp.tileSize * 9)/2;	
			int y2 = gp.tileSize * 1 - gp.tileSize/2;
			g2.drawImage(logo, x2, y2, gp.tileSize *3, gp.tileSize * 3, null);
			g2.drawImage(groupLogo, x2 + gp.tileSize * 6, y2 - gp.tileSize / 8, gp.tileSize * 3, gp.tileSize * 3, null);
			
			//Add player name

			g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 60F));
			text = "Enter your name: ";
			x = getXForCenteredText(text);
			int lineGap = gp.tileSize;
			y += 3 * lineGap / 2;
			g2.drawString(text, x, y);
			g2.drawString(gp.player.name, x, y + lineGap);

			x2 = getXForCenteredText(gp.player.name);
			
			// Draw keyboard
			// int kbStartX = gp.baseWidth/2 - gp.tileSize * 5;
			int kbStartY = y + 3 * gp.tileSize / 2;
			int keyW = gp.tileSize;
			int keyH = gp.tileSize;
			for (int row = 0; row < keyboard.length; row++) {
			    int rowY = kbStartY + row * (keyH + gp.tileSize/8);
			    int rowLen = keyboard[row].length;
				// Calculate total width for this row, accounting for SPACE being double width
			    int totalRowWidth = 0;
			    for (int col = 0; col < rowLen; col++) {
			        if (keyboard[row][col].equals("SPACE")) {
			            totalRowWidth += keyW * 2;
			        } else {
			            totalRowWidth += keyW;
			        }
			    }
			    int rowX = gp.baseWidth/2 - (totalRowWidth)/2;
			    int currentX = rowX;
			    for (int col = 0; col < rowLen; col++) {
			        int buttonW = keyboard[row][col].equals("SPACE") ? keyW * 2 : keyW;
			        // Highlight selected key
			        if (kbRow == row && kbCol == col && typingName) {
			            g2.setColor(new Color(255, 255, 100));
			            g2.fillRoundRect(currentX, rowY, buttonW, keyH, 20, 20);
			        }
			        g2.setColor(Color.DARK_GRAY);
			        g2.drawRoundRect(currentX, rowY, buttonW, keyH, 20, 20);
			        g2.setColor(Color.BLACK);
			        String key = keyboard[row][col];
			        int tx = currentX + (buttonW - g2.getFontMetrics().stringWidth(key))/2;
			        int ty = rowY + keyH/2 + g2.getFontMetrics().getAscent()/2;
			        g2.drawString(key, tx, ty);
			        currentX += buttonW;
			    }
			}
			
		} else if (titleScreenState == 3) {
			g2.setColor(new Color(243, 193, 8));
			g2.fillRect(0, 0, gp.baseWidth, gp.baseHeight);

			// Title Name
			g2.setFont(currentFontBold.deriveFont(Font.PLAIN, 100F));
			String text = "Options";
			int x = getXForCenteredText(text);
			int y = gp.tileSize * 3;
			g2.setColor(new Color(0, 0, 0, 80));
			g2.drawString(text, x + gp.tileSize / 13, y + gp.tileSize / 13);
			g2.setColor(new Color(206, 22, 40));
			g2.drawString(text, x, y);

			// Menu options
			g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 60F));
			String[] options = {"Music Volume", "SFX Volume", "Graphics", "Language", "Controls","Back"};
			int menuStartY = y + gp.tileSize * 2;
			int lineHeight = gp.tileSize + 10;

			for (int i = 0; i < options.length; i++) {
				String option = options[i];
				int optionX = getXForCenteredText(option);
				int optionY = menuStartY + i * lineHeight;

				// Highlight selected option
				if (commandNum == i) {
					g2.setColor(new Color(255, 255, 100));
					g2.drawString(">", optionX - gp.tileSize, optionY);
					
				}
				g2.setColor(Color.WHITE);
				g2.drawString(option, optionX, optionY);
			}

			// Instructions
			g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 32F));
			g2.setColor(Color.YELLOW);
			String instructions = "Use " +  gp.keyConfig.getKeyName(KeyConfig.UP) + ", " + gp.keyConfig.getKeyName(KeyConfig.DOWN) + " to move, " + gp.keyConfig.getKeyName(KeyConfig.CHOOSE)  + " to select";
			g2.drawString(instructions, getXForCenteredText(instructions), menuStartY + options.length * lineHeight + gp.tileSize / 2);
		} else if (titleScreenState == 4) {
			drawGraphicsScreen();
		}
	}

	public void drawGraphicsScreen() {
		g2.setColor(new Color(243, 193, 8));
		g2.fillRect(0, 0, gp.baseWidth, gp.baseHeight);

		int textX = gp.tileSize;
		int textY = gp.tileSize * 2;
		int lineHeight = gp.tileSize;

		g2.setFont(currentFontBold.deriveFont(Font.PLAIN, 48F));
		g2.setColor(Color.WHITE);
		String gText = "Graphics Settings";
		g2.drawString(gText, getXForCenteredText(gText), textY);

		g2.setFont(currentFont.deriveFont(Font.PLAIN, 36F));
		textY += lineHeight * 2;

		// Resolution
		if (graphicsCommandNum == 0) g2.setColor(Color.YELLOW); else g2.setColor(Color.WHITE);
		g2.drawString("Resolution: " + resolutions[resolutionIndex], textX, textY);
		textY += lineHeight;

		// VSync
		if (graphicsCommandNum == 1) g2.setColor(Color.YELLOW); else g2.setColor(Color.WHITE);
		g2.drawString("VSync: " + (vsyncOn ? "On" : "Off"), textX, textY);
		textY += lineHeight;

		// Quality
		if (graphicsCommandNum == 2) g2.setColor(Color.YELLOW); else g2.setColor(Color.WHITE);
		g2.drawString("Quality: " + qualities[qualityIndex], textX, textY);
		textY += lineHeight;

		// Fullscreen
		if (graphicsCommandNum == 3) g2.setColor(Color.YELLOW); else g2.setColor(Color.WHITE);
		g2.drawString("Fullscreen: " + (fullscreenOn ? "On" : "Off"), textX, textY);
		textY += lineHeight * 2;

		// Back
		if (graphicsCommandNum == 4) g2.setColor(Color.YELLOW); else g2.setColor(Color.WHITE);
		g2.drawString("Back", textX, textY);
	}

	int pauseCommandNum = 0; // to store the command number of the pause menu
	public void drawPauseScreen() {
	    int frameX = gp.tileSize * 6;
	    int frameY = gp.tileSize * 3;
	    int frameWidth = gp.tileSize * 12;
	    int frameHeight = gp.tileSize * 8;

	    drawSubWindow(frameX, frameY, frameWidth, frameHeight);

	    g2.setFont(currentFontBold.deriveFont(Font.PLAIN, 48F));
	    g2.setColor(Color.WHITE);

	    String[] options = {"Continue", "Settings", "Return to Main Menu", "Exit"};
	    int textY = frameY + gp.tileSize * 2;
	    for (int i = 0; i < options.length; i++) {
	        String text = options[i];
	        int textX = getXForCenteredText(text);
	        if (gp.ui.pauseCommandNum == i) {
	            g2.setColor(Color.YELLOW);
	            g2.drawString(">", textX - gp.tileSize, textY);
	        }
	        g2.setColor(Color.WHITE);
	        g2.drawString(text, textX, textY);
	        textY += gp.tileSize + 10;
	    }
	}

	public void drawDialogueScreen() {
		//Window
		int x = gp.tileSize * 4;
		int y = gp.baseHeight - gp.tileSize * 6;
		int width = gp.baseWidth - (gp.tileSize * 8);
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
		g2.setColor(new Color(0, 0, 0));
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
			width = (int) ((gp.player.health / gp.player.getTotalMaxHealth()) * width) - 3 * gp.tileSize / 40;
		} else {
			width = 0;
		}
		height = height - 3 * gp.tileSize / 40;
		g2.fillRoundRect(xHealth, yHealth, width, height, 35, 35);

		//Current Health Text
		g2.setColor(new Color(255, 255, 255));
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 60F));
		String text = (int)gp.player.health + "/" + (int)gp.player.getTotalMaxHealth();
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
			width = (int) ((gp.player.mana / gp.player.getTotalMaxMana()) * width) - 6;
		} else {
			width = 0;
		}
		height = height - 6;
		g2.fillRoundRect(xMana, yMana, width, height, 35, 35);
		//Current Mana Text
		g2.setColor(new Color(255, 255, 255));
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 27F));
		String text = gp.player.mana + "/" + gp.player.getTotalMaxMana();
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
			InputStream is = getClass().getResourceAsStream("/player/player_icon.png");
			if (is == null) {
				System.err.println("ERROR: /player/player_icon.png not found in resources!");
			} else {
				image = ImageIO.read(is);
				image = uTool.scaleImage(image, width, height);
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		if (image != null) {
			g2.drawImage(image, x, y, width, height, null);
		}
	}

	public void drawMessages() {
		int messageX = gp.tileSize / 2;
		int messageY = gp.tileSize * 5;
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 40F));
		
		for (int i = 0; i < messages.size(); i++) {
			if (messageCounter.get(i) < 90) {
				//Draw the message shadow
				g2.setColor(new Color(0, 0, 0, 80));
				g2.drawString(messages.get(i), messageX + gp.tileSize / 25, messageY + gp.tileSize / 25);
				
				//Draw the message
				g2.setColor(Color.white);
				g2.drawString(messages.get(i), messageX, messageY);
				
				messageCounter.set(i, messageCounter.get(i) + 1);
				messageY += gp.tileSize / 2;
			}
		}
	}


	public int slotCol = 0;
	public int slotRow = 0;
	public final int maxInventoryCol = 6; // Example: 6 columns
	public final int maxInventoryRow = 4; // Example: 4 rows

			public static int progressionSelectIndex = 0; // 0: Health, 1: Mana, 2: Attack, 3: Defense, etc.
	//Character Screen
	public void drawCharacterScreen() {
		//Window
		final int frameX = gp.tileSize * 13;
		final int frameY = gp.tileSize;
		final int frameWidth = gp.tileSize * 9;
		final int frameHeight = gp.tileSize * 7 + gp.tileSize / 2;

		drawSubWindow(frameX, frameY, frameWidth, frameHeight);

		//Text
		g2.setColor(Color.white);
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 35F));

		int textX = frameX + gp.tileSize / 2;
		int textY = frameY + gp.tileSize;
		final int lineHeight = 4 * gp.tileSize / 7;

		g2.drawString("Name: " + gp.player.name, textX, textY);
		textY += lineHeight;
		g2.drawString("Level: " + gp.player.level, textX, textY);
		textY += lineHeight;

		// Stats array for easy iteration
		String[] statNames = {"Health", "Mana", "Attack", "Defense"};
		String[] statValues = {
			(int)gp.player.health + "/" + (int)gp.player.getTotalMaxHealth(),
			gp.player.mana + "/" + gp.player.getTotalMaxMana(),
			gp.player.getTotalAttack() + "",
			gp.player.getTotalDefense() + ""
		};

		for (int i = 0; i < statNames.length; i++) {
			boolean selected = (progressionSelectIndex == i);
			int statTextY = textY + i * lineHeight;

			// Highlight selected stat row ONLY if progression points are available
			if (selected && gp.player.progressionPoints > 0) {
				// Only highlight 2/5 of the frame width on the left
				int highlightWidth = (frameWidth * 2) / 5;
				g2.setColor(new Color(255, 255, 100, 200)); // More opaque
				g2.fillRoundRect(textX - 20, statTextY - 35, highlightWidth, lineHeight, 20, 20);
				g2.setColor(Color.white);
			}

			// Draw stat name and value
			g2.drawString(statNames[i] + ": " + statValues[i], textX, statTextY);


			// Draw "+" if player has points, only in the highlighted area (right edge of the 2/5 area)
			if (gp.player.progressionPoints > 0) {
				String plus = "+";
				int plusX = textX - 20 + ((frameWidth * 2) / 5) - g2.getFontMetrics().stringWidth(plus) - 10;
				g2.setColor(selected ? Color.YELLOW : Color.LIGHT_GRAY);
				g2.drawString(plus, plusX, statTextY);
				g2.setColor(Color.white);
			}
		}
		textY += statNames.length * lineHeight;

		// Draw progression points and instructions
		g2.setColor(Color.white);
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 35F));
		g2.drawString("Progression Points: " + gp.player.progressionPoints, textX, textY);
		textY += lineHeight;
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 35F));
		g2.drawString("[" + gp.keyConfig.getKeyName(KeyConfig.UP) + "]/[" + gp.keyConfig.getKeyName(KeyConfig.DOWN) + "]:Select", textX, textY); 
		textY += lineHeight;
		g2.drawString("[" + gp.keyConfig.getKeyName(KeyConfig.CHOOSE) + "]: Add Point", textX, textY);
		textY += lineHeight;
		g2.drawString("[" + gp.keyConfig.getKeyName(KeyConfig.CHARACTER) + "]: Close Window", textX, textY);
		textY += lineHeight;

		// Draw Reset Points button
		String resetText = "[" + gp.keyConfig.getKeyName(KeyConfig.RESET) + "]: Reset Points";
		g2.setColor(new Color(255, 100, 100));
		g2.drawString(resetText, textX, textY);
		g2.setColor(Color.white);
		textY += lineHeight;

		// Draw player image size 4x4
		int playerImageX = frameX + (frameWidth - gp.tileSize * 3) / 2 - gp.tileSize / 2;
		int playerImageY = frameY + gp.tileSize;
		g2.drawImage(gp.player.fullBody, playerImageX, playerImageY - gp.tileSize / 2, gp.tileSize * 6 + gp.tileSize / 2, gp.tileSize * 6 + gp.tileSize / 2, null);
		// Draw 3 slots for hat, armor, boots (vertical, right side)
		int slotSize = gp.tileSize;
		int slotGap = gp.tileSize / 2;
		int slotsStartY = playerImageY;
		int slotsX = frameX + frameWidth - slotSize - gp.tileSize / 2;

		// Hat slot
		g2.setColor(new Color(200, 200, 200, 180));
		g2.fillRoundRect(slotsX, slotsStartY, slotSize, slotSize, 20, 20);
		g2.setColor(Color.WHITE);
		g2.drawRoundRect(slotsX, slotsStartY, slotSize, slotSize, 20, 20);
		if (gp.player.currentHat == null) {
			// Draw the hat image in the slot
			g2.setColor(Color.BLACK);
			g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 20F));
			g2.drawString("Hat", slotsX + 8, slotsStartY + slotSize - 8);
		}
		if (gp.player.currentHat != null) {
			// Draw the hat image in the slot
			g2.drawImage(gp.player.currentHat.down1, slotsX + 2, slotsStartY + 2, slotSize - 4, slotSize - 4, null);
		}

		// Armor slot
		int armorY = slotsStartY + slotSize + slotGap;
		g2.setColor(new Color(200, 200, 200, 180));
		g2.fillRoundRect(slotsX, armorY, slotSize, slotSize, 20, 20);
		g2.setColor(Color.WHITE);
		g2.drawRoundRect(slotsX, armorY, slotSize, slotSize, 20, 20);
		if (gp.player.currentArmor == null) {
			// Draw the armor image in the slot
			g2.setColor(Color.BLACK);
			g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 20F));
			g2.drawString("Armor", slotsX + 8, armorY + slotSize - 8);
		}
		if (gp.player.currentArmor != null) {
			// Draw the armor image in the slot
			g2.drawImage(gp.player.currentArmor.down1, slotsX + 2, armorY + 2, slotSize - 4, slotSize - 4, null);
		}

		// Boots slot
		int bootsY = armorY + slotSize + slotGap;
		g2.setColor(new Color(200, 200, 200, 180));
		g2.fillRoundRect(slotsX, bootsY, slotSize, slotSize, 20, 20);
		g2.setColor(Color.WHITE);
		g2.drawRoundRect(slotsX, bootsY, slotSize, slotSize, 20, 20);
		if (gp.player.currentBoots == null) {
			// Draw the boots image in the slot
			g2.setColor(Color.BLACK);
			g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 20F));
			g2.drawString("Boots", slotsX + 2, bootsY + slotSize - 8);
		}
		if (gp.player.currentBoots != null) {
			// Draw the boots image in the slot
			g2.drawImage(gp.player.currentBoots.down1, slotsX + 2, bootsY + 2, slotSize - 4, slotSize - 4, null);
		}

		// Weapon slot (to the right of the player image, below the 3 slots)
		int weaponSlotY = bootsY + slotSize + slotGap;
		g2.setColor(new Color(200, 200, 200, 180));
		g2.fillRoundRect(slotsX, weaponSlotY, slotSize, slotSize, 20, 20);
		g2.setColor(Color.WHITE);
		g2.drawRoundRect(slotsX, weaponSlotY, slotSize, slotSize, 20, 20);
		if (gp.player.currentWeapon == null) {
			// Draw the weapon image in the slot
			g2.setColor(Color.BLACK);
			g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 20F));
			g2.drawString("Weapon", slotsX + 2, weaponSlotY + slotSize - 8);		
		}
		
		if (gp.player.currentWeapon != null) {
			// Draw the weapon image in the slot
			g2.drawImage(gp.player.currentWeapon.down1, slotsX + 2, weaponSlotY + 2, slotSize - 4, slotSize - 4, null);
		}

		//Variable
		// int tailX = frameX + frameWidth - gp.tileSize / 2;
		// textX = getXForAllignToRightText(value, tailX);
		// g2.drawString(value, textX, textY);
	}
	
	public void drawInventoryScreen() {
		// Window
		int frameX = gp.tileSize * 2;
		int frameY = gp.tileSize;
		int width = gp.tileSize * 9;
		int height = gp.tileSize * 10;

		drawSubWindow(frameX, frameY, width, height);

		// Inner frame (where slots should fit)
		int borderInset = 7;
		int innerX = frameX + borderInset * 3;
		int innerY = frameY + borderInset * 3;
		int innerWidth = width - borderInset * 6;
		// int innerHeight = height - borderInset * 6;

		// Fixed slot size
		int slotWidth = gp.tileSize;
		int slotHeight = gp.tileSize;

		// Calculate slotGap so slots fit the inner frame width exactly
		int sideGap = gp.tileSize / 3;
		int usableWidth = innerWidth - 2 * sideGap;
		int totalSlotWidth = maxInventoryCol * slotWidth;
		int slotGap = 0;
		if (maxInventoryCol > 1) {
			slotGap = (usableWidth - totalSlotWidth) / (maxInventoryCol - 1);
		}

		// The grid starts at innerX + sideGap
		int slotXStart = innerX + sideGap;
		int slotYStart = innerY + gp.tileSize / 3; // Padding from top, adjust as needed

		// Draw slots
		g2.setStroke(new BasicStroke(2));
		for (int row = 0; row < maxInventoryRow; row++) {
			for (int col = 0; col < maxInventoryCol; col++) {
				int x = slotXStart + col * (slotWidth + slotGap);
				int y = slotYStart + row * (slotHeight + slotGap);
				g2.setColor(new Color(80, 80, 80, 120));
				g2.fillRoundRect(x, y, slotWidth, slotHeight, 20, 20);
				g2.setColor(Color.WHITE);
				g2.drawRoundRect(x, y, slotWidth, slotHeight, 20, 20);
			}
		}

		// Draw cursor
		int cursorX = slotXStart + slotCol * (slotWidth + slotGap);
		int cursorY = slotYStart + slotRow * (slotHeight + slotGap);
		int cursorArc = 20;
		Color cursorColor = new Color(255, 255, 255, 100);
		g2.setColor(cursorColor);
		g2.setStroke(new BasicStroke(5));
		g2.fillRoundRect(cursorX, cursorY, slotWidth, slotHeight, cursorArc, cursorArc);
		g2.setStroke(new BasicStroke(1f)); // Reset stroke

		// Draw items in slots (skip nulls)
		int itemIndex = 0;
		for (int row = 0; row < maxInventoryRow; row++) {
			for (int col = 0; col < maxInventoryCol; col++) {
				int x = slotXStart + col * (slotWidth + slotGap);
				int y = slotYStart + row * (slotHeight + slotGap);

				if (itemIndex < gp.player.inventory.size()) {
					Entity item = gp.player.inventory.get(itemIndex);
					if (item != null) {
						if (item.down1 != null) {
							g2.drawImage(
								item.down1,
								x + (slotWidth - gp.tileSize) / 2,
								y + (slotHeight - gp.tileSize) / 2,
								gp.tileSize, gp.tileSize,
								null
							);
						}
						// Display quantity for stackable items
						if (item != null && item.quantity > 1) {
						    g2.setFont(g2.getFont().deriveFont(Font.BOLD, 18F));
						    g2.setColor(Color.WHITE);
						    String qtyText = "x" + item.quantity;
						    int qtyWidth = g2.getFontMetrics().stringWidth(qtyText);
						    g2.drawString(qtyText, x + slotWidth - qtyWidth - 4, y + slotHeight - 6);
						}
					} else {
						// Draw placeholder text or icon for empty slot in inventory
						g2.setColor(new Color(200, 200, 200, 120));
						g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 20F));
						String emptyText = "Empty";
						int textWidth = g2.getFontMetrics().stringWidth(emptyText);
						g2.drawString(emptyText, x + (slotWidth - textWidth) / 2, y + slotHeight / 2 + 6);
					}
				} else {
					// Draw placeholder for slots beyond inventory size
					g2.setColor(new Color(200, 200, 200, 120));
					g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 20F));
					String emptyText = "Empty";
					int textWidth = g2.getFontMetrics().stringWidth(emptyText);
					g2.drawString(emptyText, x + (slotWidth - textWidth) / 2, y + slotHeight / 2 + 6);
				}
				itemIndex++;
			}
		}
		
		// Draw selected item's name and info below the grid
		int selectedIndex = slotRow * maxInventoryCol + slotCol;
		Entity selectedItem = null;
		if (selectedIndex < gp.player.inventory.size()) {
		    selectedItem = gp.player.inventory.get(selectedIndex);
		}

		// Draw selected item's name and info below the grid
		// Calculate info area position (always, so variables are in scope)
		int infoX = frameX + gp.tileSize / 2;
		int infoWidth = width - gp.tileSize;
		int infoHeight = gp.tileSize * 3 + gp.tileSize / 2;

		// Clamp info box to not go beyond the inventory window
		if (infoX + infoWidth > frameX + width - 8) {
		    infoWidth = frameX + width - 8 - infoX;
		}

		// Don't let info box overlap character window
		int characterWindowX = gp.tileSize * 13;
		if (infoX + infoWidth > characterWindowX - 8) {
		    infoWidth = characterWindowX - 8 - infoX;
		}

		// Default: draw below the grid
		int infoY = slotYStart + maxInventoryRow * (slotHeight + slotGap);

		// If it would go off the bottom, draw above the grid instead
		if (infoY + infoHeight > gp.baseHeight - gp.tileSize / 2) {
		    infoY = slotYStart - infoHeight - gp.tileSize / 6;
		    // If still off the top, clamp to at least frameY
		    if (infoY < frameY + 8) infoY = frameY + 8;
		}

		// Draw info background
		g2.setColor(new Color(40, 40, 40, 220));
		g2.fillRoundRect(infoX, infoY, infoWidth, infoHeight, 20, 20);

		if (selectedItem != null) {
		    // Draw selected item's name and info below the grid
		    g2.setColor(Color.WHITE);
		    g2.setFont(g2.getFont().deriveFont(Font.BOLD, 32F));
		    g2.drawString(selectedItem.name, infoX + 24, infoY + 40);

		    // Draw item description/info (wrap or trim as needed)
		    g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 24F));
		    int descX = infoX + 24;
		    int descY = infoY + 80;
		    int lineHeight = g2.getFontMetrics().getHeight();
		    int maxDescWidth = infoWidth - 48;

		    java.util.List<String> lines = wrapText(selectedItem.description, maxDescWidth, g2);
		    for (String line : lines) {
		        g2.drawString(line, descX, descY);
		        descY += lineHeight;
		    }

		    // Draw basic stats if present
		    g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 22F));
		    if (selectedItem.healthBonus != 0) {
		        g2.drawString("Health: " + (selectedItem.healthBonus > 0 ? "+" : "") + selectedItem.healthBonus, descX, descY);
		        descY += lineHeight;
		    }
		    if (selectedItem.manaBonus != 0) {
		        g2.drawString("Mana: " + (selectedItem.manaBonus > 0 ? "+" : "") + selectedItem.manaBonus, descX, descY);
		        descY += lineHeight;
		    }
		    if (selectedItem.attackBonus != 0) {
		        g2.drawString("Attack: " + (selectedItem.attackBonus > 0 ? "+" : "") + selectedItem.attackBonus, descX, descY);
		        descY += lineHeight;
		    }
		    if (selectedItem.defenseBonus != 0) {
		        g2.drawString("Defense: " + (selectedItem.defenseBonus > 0 ? "+" : "") + selectedItem.defenseBonus, descX, descY);
		        descY += lineHeight;
		    }

		    if (selectedItem.type == 3 && selectedItem.itemType == 0 || selectedItem.itemType == 1 || selectedItem.itemType == 2 || selectedItem.itemType == 3) {
		        g2.drawString("Level Required: " + selectedItem.levelRequirement, descX, descY);
		        descY += lineHeight;
		    }

		    // Draw comparison box if selected item is equipment and not the equipped one
		    Entity equippedItem = null;
		    String equippedLabel = "";

		    if (selectedItem != null) {
		        // Determine which equipment slot to compare
		        if (selectedItem.itemType == 0) { // Weapon
		            equippedItem = gp.player.currentWeapon;
		            equippedLabel = "Equipped Weapon";
		        } else if (selectedItem.itemType == 1) { // Hat
		            equippedItem = gp.player.currentHat;
		            equippedLabel = "Equipped Hat";
		        } else if (selectedItem.itemType == 2) { // Armor
		            equippedItem = gp.player.currentArmor;
		            equippedLabel = "Equipped Armor";
		        } else if (selectedItem.itemType == 3) { // Boots
		            equippedItem = gp.player.currentBoots;
		            equippedLabel = "Equipped Boots";
		        }
		    }

		    // Only show if equipped item exists, is not the selected item, and is the same type
		    if (
		        selectedItem != null &&
		        equippedItem != null &&
		        selectedItem != equippedItem &&
				selectedItem.type == equippedItem.type &&
		        selectedItem.itemType == equippedItem.itemType // <--- THIS IS CRUCIAL
		    ) {
		        int charFrameX = gp.tileSize * 13;
		        int charFrameY = gp.tileSize * 11 + gp.tileSize / 2;
		        int charFrameWidth = gp.tileSize * 10 ;
		        int charFrameHeight = gp.tileSize * 10 ;

		        int compareWidth = charFrameWidth - gp.tileSize;
		        int compareHeight = gp.tileSize * 2 + gp.tileSize / 2;
		        int compareX = charFrameX;
		        // Move above the character box:
		        int compareY = charFrameY - compareHeight - gp.tileSize / 2;
		        if (compareY < 0) compareY = 0; // Prevent going off the top of the screen

		        g2.setColor(new Color(60, 60, 60, 240));
		        g2.fillRoundRect(compareX, compareY, compareWidth, compareHeight, 20, 20);

		        g2.setColor(Color.WHITE);
		        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 28F));
		        g2.drawString(equippedLabel + ": " + equippedItem.name, compareX + 24, compareY + 40);

		        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 22F));
		        int statY = compareY + 80;
		        int statLine = g2.getFontMetrics().getHeight();

		        // Only show stats that are relevant for the item type
		        if (selectedItem.itemType == 0 || selectedItem.itemType == 2) { // Weapon or Armor
		            // Attack
		            int selAtk = selectedItem.attackBonus;
		            int eqAtk = equippedItem.attackBonus;
		            int diffAtk = selAtk - eqAtk;
		            g2.setColor(Color.WHITE);
		            g2.drawString("Attack: " + eqAtk, compareX + 24, statY);
		            if (diffAtk != 0) {
		                g2.setColor(diffAtk > 0 ? Color.GREEN : Color.RED);
		                g2.drawString((diffAtk > 0 ? "+" : "") + diffAtk, compareX + 180, statY);
		            }
		            statY += statLine;

		            // Defense
		            int selDef = selectedItem.defenseBonus;
		            int eqDef = equippedItem.defenseBonus;
		            int diffDef = selDef - eqDef;
		            g2.setColor(Color.WHITE);
		            g2.drawString("Defense: " + eqDef, compareX + 24, statY);
		            if (diffDef != 0) {
		                g2.setColor(diffDef > 0 ? Color.GREEN : Color.RED);
		                g2.drawString((diffDef > 0 ? "+" : "") + diffDef, compareX + 180, statY);
		            }
		            statY += statLine;
		        }

		        // Health
		        int selHealth = selectedItem.healthBonus;
		        int eqHealth = equippedItem.healthBonus;
		        int diffHealth = selHealth - eqHealth;
		        g2.setColor(Color.WHITE);
		        g2.drawString("Health: " + eqHealth, compareX + 24, statY);
		        if (diffHealth != 0) {
		            g2.setColor(diffHealth > 0 ? Color.GREEN : Color.RED);
		            g2.drawString((diffHealth > 0 ? "+" : "") + diffHealth, compareX + 180, statY);
		        }
		        statY += statLine;

		        // Mana
		        int selMana = selectedItem.manaBonus;
		        int eqMana = equippedItem.manaBonus;
		        int diffMana = selMana - eqMana;
		        g2.setColor(Color.WHITE);
		        g2.drawString("Mana: " + eqMana, compareX + 24, statY);
		        if (diffMana != 0) {
		            g2.setColor(diffMana > 0 ? Color.GREEN : Color.RED);
		            g2.drawString((diffMana > 0 ? "+" : "") + diffMana, compareX + 180, statY);
		        }
		        statY += statLine;

		        // Speed (for boots, if you have this stat)
		        if (selectedItem.itemType == 3) { // Boots
		            int selSpeed = selectedItem.speedBonus;
		            int eqSpeed = equippedItem.speedBonus;
		            int diffSpeed = selSpeed - eqSpeed;
		            g2.setColor(Color.WHITE);
		            g2.drawString("Speed: " + eqSpeed, compareX + 24, statY);
		            if (diffSpeed != 0) {
		                g2.setColor(diffSpeed > 0 ? Color.GREEN : Color.RED);
		                g2.drawString((diffSpeed > 0 ? "+" : "") + diffSpeed, compareX + 180, statY);
		            }
		            statY += statLine;
		        }
		        g2.setColor(Color.WHITE);
		    }
		}
	}




	public int chestCol = 0;
	public int chestRow = 0;
	public final int maxChestCol = 6; // Example: 6 columns
	public final int maxChestRow = 4; // Example: 4 rows


	public void drawChestScreen() {
		// Window
		int frameX = gp.tileSize * 13;
		int frameY = gp.tileSize;
		int width = gp.tileSize * 9;
		int height = gp.tileSize * 10;

		drawSubWindow(frameX, frameY, width, height);

		// Inner frame (where slots should fit)
		int borderInset = 7;
		int innerX = frameX + borderInset * 3;
		int innerY = frameY + borderInset * 3;
		int innerWidth = width - borderInset * 6;
		// int innerHeight = height - borderInset * 6;

		// Fixed slot size
		int chestWidth = gp.tileSize;
		int chestHeight = gp.tileSize;

		// Calculate slotGap so slots fit the inner frame width exactly
		int sideGap = gp.tileSize / 3;
		int usableWidth = innerWidth - 2 * sideGap;
		int totalChestWidth = maxChestCol * chestWidth;
		int chestGap = 0;
		if (maxChestCol > 1) {
			chestGap = (usableWidth - totalChestWidth) / (maxChestCol - 1);
		}

		// The grid starts at innerX + sideGap
		int chestXStart = innerX + sideGap;
		int chestYStart = innerY + gp.tileSize / 3; // Padding from top, adjust as needed

		// Draw slots
		g2.setStroke(new BasicStroke(2));
		for (int row = 0; row < maxChestRow; row++) {
			for (int col = 0; col < maxChestCol; col++) {
				int x = chestXStart + col * (chestWidth + chestGap);
				int y = chestYStart + row * (chestHeight + chestGap);
				g2.setColor(new Color(80, 80, 80, 120));
				g2.fillRoundRect(x, y, chestWidth, chestHeight, 20, 20);
				g2.setColor(Color.WHITE);
				g2.drawRoundRect(x, y, chestWidth, chestHeight, 20, 20);
			}
		}

		// Draw cursor
		int cursorX = chestXStart + chestCol * (chestWidth + chestGap);
		int cursorY = chestYStart + chestRow * (chestHeight + chestGap);
		int cursorArc = 20;
		Color cursorColor = new Color(255, 255, 255, 100);
		g2.setColor(cursorColor);
		g2.setStroke(new BasicStroke(5));
		g2.fillRoundRect(cursorX, cursorY, chestWidth, chestHeight, cursorArc, cursorArc);
		g2.setStroke(new BasicStroke(1f)); // Reset stroke
	}

	public void drawOptionsScreen(){
		int frameX = gp.tileSize * 3;
		int frameY = gp.tileSize * 2;
		int width = gp.tileSize * 18;
		int height = gp.tileSize * 10;

		drawSubWindow(frameX, frameY, width, height);

		switch (subState) {
			case 0: // Options
				options_Top(frameX, frameY);
				break;
			case 1: // Controls
				drawControlsScreen(frameX, frameY);
				break;
		}

	}

	public void options_Top(int frameX, int frameY){
		int textX;
		int textY;

		String text = "Options";
		textX = getXForCenteredText(text);
		textY = frameY + gp.tileSize * 1;
		g2.setColor(Color.WHITE);
		g2.setFont(currentFontBold.deriveFont(Font.PLAIN, 48F));
		g2.drawString(text, textX, textY);

		// BGM
		textY += gp.tileSize * 2;
		text = "BGM";
		g2.drawString(text, textX, textY);
		if (commandNum == 0){
			g2.drawString(">", textX - gp.tileSize /2, textY);
		} 

		// SE
		textY += gp.tileSize;
		text = "SE";
		g2.drawString(text, textX, textY);
		if (commandNum == 1){
			g2.drawString(">", textX - gp.tileSize /2, textY);
		} 

		// Controls
		textY += gp.tileSize;
		text = "Controls";
		g2.drawString(text, textX, textY);	
		if (commandNum == 2){
			g2.drawString(">", textX - gp.tileSize /2, textY);
		} 

		// Back 
		textY += gp.tileSize;
		text = "Back";
		g2.drawString(text, textX, textY);
		if (commandNum == 3){
			g2.drawString(">", textX - gp.tileSize /2, textY);
		} 
	}

	public void drawControlsScreen(int frameX, int frameY) {
	    int total = controlActions.length;
	    int half = (total + 1) / 2; // left column gets the extra if odd

	    int textX1 = frameX + gp.tileSize;
	    int textX2 = frameX + gp.tileSize * 9; // adjust as needed for your box width
	    int textYStart = frameY + gp.tileSize * 2;
	    int lineHeight = gp.tileSize;

	    g2.setFont(currentFontBold.deriveFont(Font.PLAIN, 48F));
	    g2.setColor(Color.WHITE);
		String cText = "Customize Controls";
	    g2.drawString(cText, getXForCenteredText(cText), textYStart - gp.tileSize / 2);
	    int textY1 = textYStart + lineHeight;
	    int textY2 = textY1;

		g2.setFont(currentFont.deriveFont(Font.PLAIN, 36F));
	    // Draw left column
	    for (int i = 0; i < total; i += 2) {
	        String action = controlActions[i];
	        String keyName = gp.keyConfig.getKeyName(action);
	        if (controlsCommandNum == i && !waitingForKey) {
	            g2.setColor(Color.YELLOW);
	            g2.drawString(">", textX1 - gp.tileSize / 2, textY1);
	        }
	        g2.setColor(Color.WHITE);
	        g2.drawString(action + ": " + keyName, textX1, textY1);
	        textY1 += lineHeight;
	    }

	    // Draw right column
	    for (int i = 1; i < total; i += 2) {
	        String action = controlActions[i];
	        String keyName = gp.keyConfig.getKeyName(action);
	        if (controlsCommandNum == i && !waitingForKey) {
	            g2.setColor(Color.YELLOW);
	            g2.drawString(">", textX2 - gp.tileSize / 2, textY2);
	        }
	        g2.setColor(Color.WHITE);
	        g2.drawString(action + ": " + keyName, textX2, textY2);
	        textY2 += lineHeight;
	    }

	    // Draw "Back" option at the bottom center
	    int backY = Math.max(textY1, textY2) + lineHeight / 4;
	    if (controlsCommandNum == controlActions.length && !waitingForKey) {
	        g2.setColor(Color.YELLOW);
	        g2.drawString(">", getXForCenteredText("Back") - gp.tileSize, backY);
	    }
	    g2.setColor(Color.WHITE);
	    g2.drawString("Back", getXForCenteredText("Back"), backY);

	    if (waitingForKey) {
	        g2.setColor(Color.CYAN);
			String kText = "Press a key...";
	        g2.drawString(kText, getXForCenteredText(kText), backY + 3 * lineHeight / 4);
	    }

	    if (keyBindWarning) {
	        g2.setColor(Color.RED);
	        String warn = "Key already assigned!";
	        g2.drawString(warn, getXForCenteredText(warn), backY + 3 * lineHeight / 4);
	        // Hide warning after 2 seconds
	        if (System.currentTimeMillis() - keyBindWarningTime > 2000) {
	            keyBindWarning = false;
	        }
	    }
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
		int x = gp.baseWidth/2 - length/2;
		return x;
	}

	public int getXForAllignToRightText(String text, int tailX) {
		int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		int x = tailX - length;
		return x;
	}
	
	private java.util.List<String> wrapText(String text, int maxWidth, Graphics2D g2) {
	    java.util.List<String> lines = new java.util.ArrayList<>();
	    String[] words = text.split(" ");
	    StringBuilder line = new StringBuilder();
	    for (String word : words) {
	        String testLine = line.length() == 0 ? word : line + " " + word;
	        int lineWidth = g2.getFontMetrics().stringWidth(testLine);
	        if (lineWidth > maxWidth && line.length() > 0) {
	            lines.add(line.toString());
	            line = new StringBuilder(word);
	        } else {
	            if (line.length() > 0) line.append(" ");
	            line.append(word);
	        }
	    }
	    if (line.length() > 0) lines.add(line.toString());
	    return lines;
	}
	
	final String[][] keyboard = {
		{"1","2","3","4","5","6","7","8","9","0"},
		{"Q","W","E","R","T","Y","U","I","O","P"},
		{"A","S","D","F","G","H","J","K","L","<-"},
		{"Z","X","C","V","B","N","M", "SPACE","OK"},
	};
	int kbRow = 0;
	int kbCol = 0;
	boolean typingName = true; // true while on name input screen

	public int getItemIndexOnSlot() {
		return slotRow * maxInventoryCol + slotCol;
	}


}