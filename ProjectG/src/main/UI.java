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
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Properties;

import javax.imageio.ImageIO;

import entity.Entity;
import object.OBJ_Chest;
import skill.Skill;

public class UI {
	
	BufferedImage flash;
	GamePanel gp;
	Graphics2D g2;
	public Font currentFont; 
	public Font currentFontBold;
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

	public int menuControlsCommandNum = 0; // for menu controls screen
	public int controlsCommandNum = 0;     // for options controls screen

	public boolean waitingForKey = false;
	public String waitingAction = null;
	public final String[] controlActions = {
		KeyConfig.UP, KeyConfig.DOWN, KeyConfig.LEFT, KeyConfig.RIGHT,
		KeyConfig.ATTACK, KeyConfig.CHOOSE, KeyConfig.ESCAPE, KeyConfig.INVENTORY, KeyConfig.CHARACTER, KeyConfig.RESET, KeyConfig.FLASH, KeyConfig.QUICK_USE, KeyConfig.SKILL1, KeyConfig.SKILL2, KeyConfig.SKILL3, KeyConfig.SKILL4
	};
	public boolean keyBindWarning = false;
	public long keyBindWarningTime = 0;

	public int graphicsCommandNum = 0;
	public String[] resolutions = {
		"1920x1080", "1600x900", "1280x720", "1024x576", "800x450"
	};
	public String[] languageCodes = {"en", "vi", "fr", "es", "jp"};
	public String[] languageNames = {"English", "Tiếng Việt", "Français", "Español", "日本語"};
	public int languageIndex = 0; // Index of selected language
	public int resolutionIndex = 0;
	public boolean vsyncOn = true;
	
	public int qualityIndex = 2;

	public boolean fullscreenOn = true;

	public String language = "en"; // Default language

	public Properties langProps = new Properties();

	public String[] qualities; // <-- just declare, don't initialize

	public int counter = 0; // Counter for various animations and effects

	public int skillListScroll = 0;
	public final int maxVisibleSkills = 7; // Number of skills visible at once

	public UI(GamePanel gp) {
		this.gp = gp;
		
		try {
			//normal
			InputStream is = getClass().getResourceAsStream("/font/GNUUnifont9.ttf");
			currentFont = Font.createFont(Font.TRUETYPE_FONT, is);
			//bold
			is = getClass().getResourceAsStream("/font/GNUUnifont9.ttf");
			currentFontBold = Font.createFont(Font.TRUETYPE_FONT, is);

			is = getClass().getResourceAsStream("/skills/flash.png");
			if (is != null) {
				flash = ImageIO.read(is);
			} else {
				System.err.println("ERROR: /skills/flash.png not found in resources!");
			}
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// After loading config or in UI constructor
		for (int i = 0; i < languageCodes.length; i++) {
		    if (languageCodes[i].equals(language)) {
		        languageIndex = i;
		        break;
		    }
		}
		loadLanguage();
		updateQualities(); // <-- add this
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
			//drawFlashCooldown();
			//Draw messages
			drawMessages();
			drawSkillBar();
			// drawMiniMap(g2);
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
			drawChestScreen();
			drawInventoryScreen();
		}

		else if (gp.gameState == gp.optionsState) {
			drawOptionsScreen();
		}
		else if(gp.gameState == gp.gameOverState) {
			drawGameOverScreen();
		}
		else if (gp.gameState == gp.deathState) {
			drawDeathAnimation();
		}
		else if (gp.gameState == gp.transitionState) {
			drawTransitionScreen();
		}
		else if (gp.gameState == gp.skillsState){
			drawSkillScreen();
		}
		else if (gp.gameState == gp.skillTreeState) {
			drawSkillTreeScreen();
			return;
		}
	}


	public void drawTitleScreen(){

		if (titleScreenState == 0) {
			g2.setColor(new Color(243, 193, 8));
			g2.fillRect(0, 0, gp.baseWidth, gp.baseHeight);

			//Title Name
			g2.setFont(currentFontBold.deriveFont(Font.BOLD, 100F));
			
			String text = tr("game.title");
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
			text = tr("menu.new_game");
			x = getXForCenteredText(text);
			y = gp.baseHeight/2 + gp.tileSize * 2 / 3;
			g2.drawString(text, x, y);
			if (commandNum == 0) {
				g2.drawString(">", x - gp.tileSize, y);				
			}

			text = tr("menu.load_game");
			x = getXForCenteredText(text);
			y += 3 * gp.tileSize / 2;
			g2.drawString(text, x, y);
			if (commandNum == 1) {
				g2.drawString(">", x - gp.tileSize, y);				
			}

			text = tr("menu.settings");
		 	x = getXForCenteredText(text);
			y += 3 * gp.tileSize / 2;
			g2.drawString(text, x, y);
			if (commandNum == 2) {
				g2.drawString(">", x - gp.tileSize, y);				
			}

			text = tr("menu.quit");
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
			g2.setFont(currentFontBold.deriveFont(Font.BOLD, 100F));
		
			String text = tr("game.title");
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
			text = tr("menu.enter_name");
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
			        if (keyboard[row][col].equals("     ")) {
			            totalRowWidth += keyW * 2;
			        } else {
			            totalRowWidth += keyW;
			        }
			    }
			    int rowX = gp.baseWidth/2 - (totalRowWidth)/2;
			    int currentX = rowX;
			    for (int col = 0; col < rowLen; col++) {
			        int buttonW = keyboard[row][col].equals("     ") ? keyW * 2 : keyW;
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
			g2.setFont(currentFontBold.deriveFont(Font.BOLD, 100F));
			String text = tr("options.title");
			int x = getXForCenteredText(text);
			int y = gp.tileSize * 3;
			g2.setColor(new Color(0, 0, 0, 80));
			g2.drawString(text, x + gp.tileSize / 13, y + gp.tileSize / 13);
			g2.setColor(new Color(206, 22, 40));
			g2.drawString(text, x, y);

			// Menu options
			g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 60F));
			String[] options = {
				tr("options.music_volume"), 
				tr("options.sfx_volume"), 
				tr("options.graphics"), 
				tr("options.language"), 
				tr("options.controls"),
				tr("options.back")};
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
			String instructions = java.text.MessageFormat.format(
				tr("instructions.menu"),
				gp.keyConfig.getKeyName(KeyConfig.UP),
				gp.keyConfig.getKeyName(KeyConfig.DOWN),
				gp.keyConfig.getKeyName(KeyConfig.CHOOSE)
			);
			g2.drawString(instructions, getXForCenteredText(instructions), menuStartY + options.length * lineHeight + gp.tileSize / 2);
		} else if (titleScreenState == 4) {
			drawGraphicsScreen();
		} else if (titleScreenState == 5){
			drawLanguageScreen();
		} else if (titleScreenState == 6) {
			drawMenuControlsScreen();
		}
	}

	public void drawGraphicsScreen() {
		g2.setColor(new Color(243, 193, 8));
		g2.fillRect(0, 0, gp.baseWidth, gp.baseHeight);

		int textX = gp.tileSize;
		int textY = gp.tileSize * 2;
		int lineHeight = gp.tileSize;

		g2.setFont(currentFontBold.deriveFont(Font.BOLD, 100F));
		g2.setColor(Color.WHITE);
		String gText = tr("graphics.title");
		g2.drawString(gText, getXForCenteredText(gText), textY);

		g2.setFont(currentFont.deriveFont(Font.PLAIN, 36F));
		textY += lineHeight * 2;

		// Resolution
		if (graphicsCommandNum == 0) g2.setColor(Color.YELLOW); else g2.setColor(Color.WHITE);
		g2.drawString(tr("graphics.resolution") + ": " + resolutions[resolutionIndex], textX, textY);
		textY += lineHeight;

		// VSync
		if (graphicsCommandNum == 1) g2.setColor(Color.YELLOW); else g2.setColor(Color.WHITE);
		g2.drawString(tr("graphics.vsync") + ": " + (vsyncOn ? tr("on"): tr("off")), textX, textY);
		textY += lineHeight;

		// Quality
		if (graphicsCommandNum == 2) g2.setColor(Color.YELLOW); else g2.setColor(Color.WHITE);
		g2.drawString(tr("graphics.quality") + ": " + qualities[qualityIndex], textX, textY);
		textY += lineHeight;

		// Fullscreen
		if (graphicsCommandNum == 3) g2.setColor(Color.YELLOW); else g2.setColor(Color.WHITE);
		g2.drawString(tr("graphics.fullscreen") + ": " + (fullscreenOn ? tr("on"): tr("off")), textX, textY);
		textY += lineHeight * 2;

		// Back
		if (graphicsCommandNum == 4) g2.setColor(Color.YELLOW); else g2.setColor(Color.WHITE);
		g2.drawString(tr("graphics.back"), textX, textY);
	}

	int pauseCommandNum = 0; // to store the command number of the pause menu
	public void drawPauseScreen() {
	    int frameX = gp.tileSize * 6;
	    int frameY = gp.tileSize * 3;
	    int frameWidth = gp.tileSize * 12;
	    int frameHeight = gp.tileSize * 8;

	    drawSubWindow(frameX, frameY, frameWidth, frameHeight);

	    g2.setFont(currentFontBold.deriveFont(Font.BOLD, 48F));
	    g2.setColor(Color.WHITE);

	    String[] options = {tr("pause.continue"), tr("pause.save"), tr("pause.settings"), tr("pause.return_main_menu"), tr("pause.exit")};
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
			float manaPercent = (float)gp.player.mana / gp.player.getTotalMaxMana();
			width = (int)(manaPercent * width) - 6;
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

	public void drawPlayerIcon() {
		BufferedImage image = null;
		int x = gp.tileSize / 2;
		int y = gp.tileSize / 2;
		int diameter = gp.tileSize * 2;
		int centerX = x + diameter / 2;
		int centerY = y + diameter / 2;

		// Draw circular background
		g2.setColor(new Color(40, 40, 40, 220));
		g2.fillOval(x, y, diameter, diameter);

		// --- Draw EXP "liquid" fill ---
		float expPercent = (float)gp.player.exp / Math.max(1, gp.player.nextLevelExp);
		int fillHeight = (int)((diameter - 10) * expPercent); // -10 for padding
		int fillY = y + diameter - 5 - fillHeight; // -5 for bottom padding

		// Save old clip
		java.awt.Shape oldClip = g2.getClip();
		// Clip to the inside of the circle
		g2.setClip(new java.awt.geom.Ellipse2D.Float(x + 5, y + 5, diameter - 10, diameter - 10));
		// Draw the "liquid" fill (blue)
		g2.setColor(new Color(41, 107, 167, 180));
		g2.fillRect(x + 5, fillY, diameter - 10, fillHeight);
		// Restore clip
		g2.setClip(oldClip);

		// Draw the circle border again for clarity
		g2.setColor(new Color(41, 107, 167, 220));
		g2.setStroke(new BasicStroke(4));
		g2.drawOval(x + 5, y + 5, diameter - 10, diameter - 10);

		// Draw level number in the center
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 24F));
		String level = tr("character.level");
		int levelWidth = g2.getFontMetrics().stringWidth(level);
		int levelHeight = g2.getFontMetrics().getAscent();
		g2.setColor(Color.WHITE);
		g2.drawString(level, centerX - levelWidth / 2, centerY - levelHeight);
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48F));
		String levelText = String.valueOf(gp.player.level);
		levelWidth = g2.getFontMetrics().stringWidth(levelText);
		g2.drawString(levelText, centerX - levelWidth / 2, centerY + 5 * levelHeight / 3);

		// Draw EXP text below the circle
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 18F));
		String expText = gp.player.exp + " / " + gp.player.nextLevelExp + " EXP";
		int expWidth = g2.getFontMetrics().stringWidth(expText);
		g2.setColor(Color.YELLOW);
		g2.drawString(expText, centerX - expWidth / 2, y + diameter + 22);

		// Reset stroke
		g2.setStroke(new BasicStroke(1f));
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
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 25F));

		int textX = frameX + gp.tileSize / 2;
		int textY = frameY + gp.tileSize;
		final int lineHeight = 4 * gp.tileSize / 7;

		g2.drawString(tr("character.name") + ": " + gp.player.name, textX, textY);
		textY += lineHeight;
		g2.drawString(tr("character.level") + ": " + gp.player.level, textX, textY);
		textY += lineHeight;

		// Stats array for easy iteration
		String[] statNames = {tr("character.health"), tr("character.mana"), tr("character.attack"), tr("character.defense")};
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
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 25F));
		g2.drawString(tr("character.progression_points") + ": " + gp.player.progressionPoints, textX, textY);
		textY += lineHeight;
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 25F));
		g2.drawString("[" + gp.keyConfig.getKeyName(KeyConfig.UP) + "]/[" + gp.keyConfig.getKeyName(KeyConfig.DOWN) + "]: " + tr("character.select"), textX, textY); 
		textY += lineHeight;
		g2.drawString("[" + gp.keyConfig.getKeyName(KeyConfig.CHOOSE) + "]: " + tr("character.add_point"), textX, textY);
		textY += lineHeight;
		g2.drawString("[" + gp.keyConfig.getKeyName(KeyConfig.CHARACTER) + "]: " + tr("character.close_window"), textX, textY);
		textY += lineHeight;

		// Draw Reset Points button
		String resetText = "[" + gp.keyConfig.getKeyName(KeyConfig.RESET) + "]: " + tr("character.reset_points");
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
			g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 15F));
			g2.drawString(tr("character.hat"), slotsX + 8, slotsStartY + slotSize - 8);
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
			g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 15F));
			g2.drawString(tr("character.armor"), slotsX + 8, armorY + slotSize - 8);
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
			g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 15F));
			g2.drawString(tr("character.boots"), slotsX + 2, bootsY + slotSize - 8);
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
			g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 15F));
			g2.drawString(tr("character.weapon"), slotsX + 2, weaponSlotY + slotSize - 8);		
		}
		
		if (gp.player.currentWeapon != null) {
			// Draw the weapon image in the slot
			g2.drawImage(gp.player.currentWeapon.down1, slotsX + 2, weaponSlotY + 2, slotSize - 4, slotSize - 4, null);
		}

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
						String emptyText = tr("inventory.empty");
						int textWidth = g2.getFontMetrics().stringWidth(emptyText);
						g2.drawString(emptyText, x + (slotWidth - textWidth) / 2, y + slotHeight / 2 + 6);
					}
				} else {
					// Draw placeholder for slots beyond inventory size
					g2.setColor(new Color(200, 200, 200, 120));
					g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 20F));
					String emptyText = tr("inventory.empty");
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

		// Draw selected item's name and info below the grid
		if (selectedItem != null) {
		    // Draw selected item's name
		    g2.setColor(Color.WHITE);
		    g2.setFont(g2.getFont().deriveFont(Font.BOLD, 32F));
		    g2.drawString(selectedItem.name, infoX + 24, infoY + 40);

		    String desc = selectedItem.description;
		    int descX = infoX + 24;
		    int descY = infoY + 80;
		    int maxDescWidth = infoWidth - 48;

		    // Reserve space for stats (e.g., 5 lines of stats)
		    int reservedStatLines = 5;
		    int statLineHeight = 22; // Approximate, matches your stat font size
		    int maxDescHeight = infoHeight - 40 - reservedStatLines * statLineHeight; // 40 for name/title

		    int fontSize = 22; // Start a bit smaller
		    java.util.List<String> lines;
		    int totalHeight;
		    do {
		        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, (float)fontSize));
		        lines = wrapText(desc, maxDescWidth, g2);
		        int lineHeight = g2.getFontMetrics().getHeight();
		        totalHeight = lines.size() * lineHeight;
		        fontSize--;
		    } while ((totalHeight > maxDescHeight) && fontSize > 10);

		    // Now use the final font size and line height
		    g2.setFont(g2.getFont().deriveFont(Font.PLAIN, (float)(fontSize + 1)));
		    int lineHeight = g2.getFontMetrics().getHeight();
		    for (String line : lines) {
		        g2.drawString(line, descX, descY);
		        descY += lineHeight;
		    }

		    // Now descY is just below the description, and there is always space for stats!
		    g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 22F));
		    if (selectedItem.healthBonus != 0) {
		        g2.drawString(tr("character.health") + ": " + (selectedItem.healthBonus > 0 ? "+" : "") + selectedItem.healthBonus, descX, descY);
		        descY += lineHeight;
		    }
		    if (selectedItem.manaBonus != 0) {
		        g2.drawString(tr("character.mana") + ": " + (selectedItem.manaBonus > 0 ? "+" : "") + selectedItem.manaBonus, descX, descY);
		        descY += lineHeight;
		    }
		    if (selectedItem.attackBonus != 0) {
		        g2.drawString(tr("character.attack") + ": " + (selectedItem.attackBonus > 0 ? "+" : "") + selectedItem.attackBonus, descX, descY);
		        descY += lineHeight;
		    }
		    if (selectedItem.defenseBonus != 0) {
		        g2.drawString(tr("character.defense") + ": " + (selectedItem.defenseBonus > 0 ? "+" : "") + selectedItem.defenseBonus, descX, descY);
		        descY += lineHeight;
		    }
		    if (selectedItem.type == 3 && (selectedItem.itemType == 0 || selectedItem.itemType == 1 || selectedItem.itemType == 2 || selectedItem.itemType == 3)) {
		        g2.drawString(tr("item.level_required") + ": " + selectedItem.levelRequirement, descX, descY);
		        descY += lineHeight;
		    }
		}
	}


	public int chestCol = 0;
	public int chestRow = 0;
	public final int maxChestCol = 6; // Example: 6 columns
	public final int maxChestRow = 4; // Example: 4 rows


	public void drawChestScreen() {
		OBJ_Chest chest = (OBJ_Chest) gp.player.currentChest;
		if (chest == null) return; // No chest
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

		// Draw items in slots (skip nulls)
		int itemIndex = 0;
		for (int row = 0; row < maxChestRow; row++) {
			for (int col = 0; col < maxChestCol; col++) {
				int x = chestXStart + col * (chestWidth + chestGap);
				int y = chestYStart + row * (chestHeight + chestGap);

				if (itemIndex < chest.chestInventory.size()) {
					Entity item = chest.chestInventory.get(itemIndex);
					if (item != null) {
						if (item.down1 != null) {
							g2.drawImage(
								item.down1,
								x + (chestWidth - gp.tileSize) / 2,
								y + (chestHeight - gp.tileSize) / 2,
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
						    g2.drawString(qtyText, x + chestWidth - qtyWidth - 4, y + chestHeight - 6);
						}
					} else {
						// Draw placeholder text or icon for empty slot in inventory
						g2.setColor(new Color(200, 200, 200, 120));
						g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 20F));
						String emptyText = tr("inventory.empty");
						int textWidth = g2.getFontMetrics().stringWidth(emptyText);
						g2.drawString(emptyText, x + (chestWidth - textWidth) / 2, y + chestHeight / 2 + 6);
					}
				} else {
					// Draw placeholder for slots beyond inventory size
					g2.setColor(new Color(200, 200, 200, 120));
					g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 20F));
					String emptyText = tr("inventory.empty");
					int textWidth = g2.getFontMetrics().stringWidth(emptyText);
					g2.drawString(emptyText, x + (chestWidth - textWidth) / 2, y + chestHeight / 2 + 6);
				}
				itemIndex++;
			}
		}
		
		// Draw selected item's name and info below the grid
		int selectedIndex = chestRow * maxChestCol + chestCol;
		Entity selectedItem = null;
		if (selectedIndex < chest.chestInventory.size()) {
		    selectedItem = chest.chestInventory.get(selectedIndex);
		}

		// Calculate info area position (always, so variables are in scope)
		int infoX = frameX + gp.tileSize / 2;
		int infoWidth = width - gp.tileSize;
		int infoHeight = gp.tileSize * 3 + gp.tileSize / 2;

		// Clamp info box to not go beyond the chest window
		if (infoX + infoWidth > frameX + width - 8) {
		    infoWidth = frameX + width - 8 - infoX;
		}

		// Don't let info box overlap character window
		int characterWindowX = gp.tileSize * 26 + gp.tileSize / 2;
		if (infoX + infoWidth > characterWindowX - 8) {
		    infoWidth = characterWindowX - 8 - infoX;
		}

		// Default: draw below the grid
		int infoY = chestYStart + maxChestRow * (chestHeight + chestGap);

		// If it would go off the bottom, draw above the grid instead
		if (infoY + infoHeight > gp.baseHeight - gp.tileSize / 2) {
		    infoY = chestYStart - infoHeight - gp.tileSize / 6;
		    // If still off the top, clamp to at least frameY
		    if (infoY < frameY + 8) infoY = frameY + 8;
		}

		// Draw info background (subwindow)
		g2.setColor(new Color(40, 40, 40, 220));
		g2.fillRoundRect(infoX, infoY, infoWidth, infoHeight, 20, 20);

		if (selectedItem != null) {
		    // Draw selected item's name
		    g2.setColor(Color.WHITE);
		    g2.setFont(g2.getFont().deriveFont(Font.BOLD, 32F));
		    g2.drawString(selectedItem.name, infoX + 24, infoY + 40);

		    // --- Auto-fit description (same as inventory) ---
		    String desc = selectedItem.description;
		    int descX = infoX + 24;
		    int descY = infoY + 80;
		    int maxDescWidth = infoWidth - 48;

		    // Reserve space for stats (e.g., 5 lines of stats)
		    int reservedStatLines = 5;
		    int statLineHeight = 22; // Approximate, matches your stat font size
		    int maxDescHeight = infoHeight - 40 - reservedStatLines * statLineHeight; // 40 for name/title

		    int fontSize = 22; // Start a bit smaller
		    java.util.List<String> lines;
		    int totalHeight;
		    do {
		        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, (float)fontSize));
		        lines = wrapText(desc, maxDescWidth, g2);
		        int lineHeight = g2.getFontMetrics().getHeight();
		        totalHeight = lines.size() * lineHeight;
		        fontSize--;
		    } while ((totalHeight > maxDescHeight) && fontSize > 10);

		    // Now use the final font size and line height
		    g2.setFont(g2.getFont().deriveFont(Font.PLAIN, (float)(fontSize + 1)));
		    int lineHeight = g2.getFontMetrics().getHeight();
		    for (String line : lines) {
		        g2.drawString(line, descX, descY);
		        descY += lineHeight;
		    }

		    // Now descY is just below the description, and there is always space for stats!
		    g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 22F));
		    if (selectedItem.healthBonus != 0) {
		        g2.drawString(tr("character.health") + ": " + (selectedItem.healthBonus > 0 ? "+" : "") + selectedItem.healthBonus, descX, descY);
		        descY += lineHeight;
		    }
		    if (selectedItem.manaBonus != 0) {
		        g2.drawString(tr("character.mana") + ": " + (selectedItem.manaBonus > 0 ? "+" : "") + selectedItem.manaBonus, descX, descY);
		        descY += lineHeight;
		    }
		    if (selectedItem.attackBonus != 0) {
		        g2.drawString(tr("character.attack") + ": " + (selectedItem.attackBonus > 0 ? "+" : "") + selectedItem.attackBonus, descX, descY);
		        descY += lineHeight;
		    }
		    if (selectedItem.defenseBonus != 0) {
		        g2.drawString(tr("character.defense") + ": " + (selectedItem.defenseBonus > 0 ? "+" : "") + selectedItem.defenseBonus, descX, descY);
		        descY += lineHeight;
		    }
		    if (selectedItem.type == 3 && (selectedItem.itemType == 0 || selectedItem.itemType == 1 || selectedItem.itemType == 2 || selectedItem.itemType == 3)) {
		        g2.drawString(tr("item.level_required") + ": " + selectedItem.levelRequirement, descX, descY);
		        descY += lineHeight;
		    }
		}
	}

	public void drawOptionsScreen(){
		
		int width = gp.tileSize * 18;
		int height = gp.tileSize * 10;
		int frameX = (gp.baseWidth - width) / 2;
    	int frameY = (gp.baseHeight - height) / 2;

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
		int borderInset = 7;
		int windowWidth = gp.tileSize * 18;
		int innerWidth = windowWidth - borderInset * 6;

		// Title
		g2.setFont(currentFontBold.deriveFont(Font.BOLD, 48F));
		String text = tr("options.title");
		int textWidth = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		int textX = frameX + borderInset * 3 + (innerWidth - textWidth) / 2;
		int textY = frameY + gp.tileSize * 3/2;
		g2.setColor(Color.WHITE);
		g2.drawString(text, textX, textY);

		// BGM
		textY += gp.tileSize * 2;
		g2.setFont(currentFont.deriveFont(Font.PLAIN, 36F));
		text = tr("options.music_volume");
		textWidth = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		textX = frameX + borderInset * 3 + (innerWidth - textWidth) / 2;
		g2.drawString(text, textX, textY);
		if (commandNum == 0){
			g2.drawString(">", textX - gp.tileSize /2, textY);
		} 

		// SE
		textY += gp.tileSize;
		text = tr("options.sfx_volume");
		textWidth = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		textX = frameX + borderInset * 3 + (innerWidth - textWidth) / 2;
		g2.drawString(text, textX, textY);
		if (commandNum == 1){
			g2.drawString(">", textX - gp.tileSize /2, textY);
		} 

		// Controls
		textY += gp.tileSize;
		text = tr("options.controls");
		textWidth = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		textX = frameX + borderInset * 3 + (innerWidth - textWidth) / 2;
		g2.drawString(text, textX, textY);	
		if (commandNum == 2){
			g2.drawString(">", textX - gp.tileSize /2, textY);
		} 

		// Language
		textY += gp.tileSize;
		text = tr("options.language") +": " + languageNames[languageIndex];
		textWidth = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		textX = frameX + borderInset * 3 + (innerWidth - textWidth) / 2;
		g2.drawString(text, textX, textY);
		if (commandNum == 3){
			g2.drawString(">", textX - gp.tileSize /2, textY);
		} 

		// Back 
		textY += gp.tileSize;
		text = tr("options.back");
		textWidth = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		textX = frameX + borderInset * 3 + (innerWidth - textWidth) / 2;
		g2.drawString(text, textX, textY);
		if (commandNum == 4){
			g2.drawString(">", textX - gp.tileSize /2, textY);
		} 
	}

	public void drawControlsScreen(int frameX, int frameY) {
		int total = controlActions.length;

		int windowWidth = gp.tileSize * 18;
		int innerWidth = windowWidth - 7 * 6; // borderInset * 6
		int colGap = gp.tileSize; // Gap between columns

		// --- 3 columns ---
		int cols = 3;
		int rows = (int) Math.ceil((double) total / cols);

		int colWidth = (innerWidth - (cols - 1) * colGap) / cols;
		int textYStart = frameY + gp.tileSize * 2;
		int lineHeight = gp.tileSize;

		g2.setFont(currentFontBold.deriveFont(Font.BOLD, 48F));
		g2.setColor(Color.WHITE);
		String cText = tr("controls.title");
		g2.drawString(cText, getXForCenteredText(cText), textYStart - gp.tileSize / 2);

		g2.setFont(currentFont.deriveFont(Font.PLAIN, 36F));
		int startY = textYStart + lineHeight / 2;
		int[] colXs = new int[cols];
		for (int c = 0; c < cols; c++) {
			colXs[c] = frameX + gp.tileSize + c * (colWidth + colGap);
		}

		// Draw controls in columns
		for (int i = 0; i < total; i++) {
			int col = i / rows;
			int row = i % rows;
			int x = colXs[col];
			int y = startY + row * lineHeight;

			String action = controlActions[i];
			String keyName = gp.keyConfig.getKeyName(action);

			String actionLabel = tr("controls." + action.toLowerCase());
			if (controlsCommandNum == i && !waitingForKey) {
				g2.setColor(Color.YELLOW);
				g2.drawString(">", x - gp.tileSize / 2, y);
			}
			g2.setColor(Color.WHITE);
			g2.drawString(actionLabel + ": " + keyName, x, y);
		}

		// After drawing all key bindings, draw "Back" and "Reset" side by side at the bottom center
		int buttonY = startY + rows * lineHeight; // Adjust vertical position as needed

		String back = tr("controls.back");
		String reset = tr("controls.reset");

		g2.setFont(currentFont.deriveFont(Font.PLAIN, 36F));
		int backWidth = g2.getFontMetrics().stringWidth(back);
		int resetWidth = g2.getFontMetrics().stringWidth(reset);
		int buttonGap = gp.tileSize; // Space between buttons

		int totalButtonsWidth = backWidth + resetWidth + buttonGap;
		int buttonsStartX = (gp.baseWidth - totalButtonsWidth) / 2;

		int backX = buttonsStartX + gp.tileSize / 2;
		int resetX = backX + backWidth + buttonGap;

		// Draw "Back" button
		if (controlsCommandNum == total && !waitingForKey) {
		    g2.setColor(Color.YELLOW);
		    g2.drawString(">", backX - gp.tileSize / 2, buttonY);
		}
		g2.setColor(Color.WHITE);
		g2.drawString(back, backX, buttonY);

		// Draw "Reset" button
		if (controlsCommandNum == total + 1 && !waitingForKey) {
		    g2.setColor(Color.YELLOW);
		    g2.drawString(">", resetX - gp.tileSize / 2, buttonY);
		}
		g2.setColor(Color.WHITE);
		g2.drawString(reset, resetX, buttonY);

		if (waitingForKey) {
			g2.setColor(Color.CYAN);
			String kText = tr("message.press_key");
			int msgX = getXForCenteredText(kText);
			int msgY = buttonY + 3 * gp.tileSize / 4;
			g2.drawString(kText, msgX, msgY);
		}

		// Show duplicate key warning if needed
		if (keyBindWarning) {
			g2.setColor(Color.RED);
			String warn = tr("message.key_already_assigned");
			int warnX = getXForCenteredText(warn);
			int warnY = buttonY + 3 * gp.tileSize / 4;
			g2.drawString(warn, warnX, warnY);
			// Hide warning after 2 seconds
			if (System.currentTimeMillis() - keyBindWarningTime > 2000) {
				keyBindWarning = false;
			}
		}
	}

	public void drawLanguageScreen(){
		g2.setColor(new Color(243, 193, 8));
		g2.fillRect(0, 0, gp.baseWidth, gp.baseHeight);

		int frameY = gp.tileSize * 2;
		int lineHeight = gp.tileSize;


		g2.setFont(currentFontBold.deriveFont(Font.BOLD, 100F));
		String title = tr("options.language");
		int titleX = getXForCenteredText(title);
		int titleY = frameY;
		g2.setColor(Color.WHITE);
		g2.drawString(title, titleX, titleY);

		titleY += gp.tileSize * 2;

		g2.setFont(currentFont.deriveFont(Font.PLAIN, 36F));
		int optionY = titleY + gp.tileSize;
		for (int i = 0; i < languageNames.length; i++) {
			int optionX = getXForCenteredText(languageNames[i]);
			if (languageIndex == i) {
				g2.setColor(Color.YELLOW);
				g2.drawString(">", optionX - gp.tileSize, optionY);
			}
			g2.setColor(Color.WHITE);
			g2.drawString(languageNames[i], optionX, optionY);
			optionY += lineHeight;
		}

		// Draw "Back" option
		String back = tr("options.back");
		int backX = getXForCenteredText(back);
		if (languageIndex == languageNames.length) {
			g2.setColor(Color.YELLOW);
			g2.drawString(">", backX - gp.tileSize, optionY);
		}
		g2.setColor(Color.WHITE);
		g2.drawString(back, backX, optionY);
	}

	public void drawMenuControlsScreen() {
		g2.setColor(new Color(243, 193, 8));
		g2.fillRect(0, 0, gp.baseWidth, gp.baseHeight);

		int cols = 3;
		int colGap = gp.tileSize * 2; // Change this to adjust gap
		int total = controlActions.length;
		int rows = (int) Math.ceil((double) total / cols);

		int usableWidth = gp.baseWidth - gp.tileSize * 4; // 2 tile margin on each side
		int colWidth = (usableWidth - colGap * (cols - 1)) / cols;
		int col1X = gp.tileSize * 2;
		int col2X = col1X + colWidth + colGap;
		int col3X = col2X + colWidth + colGap;
		int startY = gp.tileSize * 4;

		g2.setFont(currentFontBold.deriveFont(Font.BOLD, 100F));
		String title = tr("controls.title");
		int titleX = getXForCenteredText(title);
		int titleY = gp.tileSize * 2;
		g2.setColor(Color.WHITE);
		g2.drawString(title, titleX, titleY);

		g2.setFont(currentFont.deriveFont(Font.PLAIN, 36F));
		int lineHeight = gp.tileSize;

		for (int row = 0; row < rows; row++) {
			// Left column
			int i = row;
			if (i < total) {
				String action = controlActions[i];
				String actionName = tr("controls." + action.toLowerCase());
				String keyName = gp.keyConfig.getKeyName(action);
				if (menuControlsCommandNum == i && !waitingForKey) {
					g2.setColor(Color.YELLOW);
					g2.drawString(">", col1X - gp.tileSize / 2, startY + row * lineHeight);
				}
				g2.setColor(Color.WHITE);
				g2.drawString(actionName + ": " + keyName, col1X, startY + row * lineHeight);
			}
			// Middle column
			int j = row + rows;
			if (j < total) {
				String action = controlActions[j];
				String actionName = tr("controls." + action.toLowerCase());
				String keyName = gp.keyConfig.getKeyName(action);
				if (menuControlsCommandNum == j && !waitingForKey) {
					g2.setColor(Color.YELLOW);
					g2.drawString(">", col2X - gp.tileSize / 2, startY + row * lineHeight);
				}
				g2.setColor(Color.WHITE);
				g2.drawString(actionName + ": " + keyName, col2X, startY + row * lineHeight);
			}
			// Right column
			int k = row + rows * 2;
			if (k < total) {
				String action = controlActions[k];
				String actionName = tr("controls." + action.toLowerCase());
				String keyName = gp.keyConfig.getKeyName(action);
				if (menuControlsCommandNum == k && !waitingForKey) {
					g2.setColor(Color.YELLOW);
					g2.drawString(">", col3X - gp.tileSize / 2, startY + row * lineHeight);
				}
				g2.setColor(Color.WHITE);
				g2.drawString(actionName + ": " + keyName, col3X, startY + row * lineHeight);
			}
		}

		// Draw "Back" and "Reset" side by side at the bottom center
		int buttonY = startY + rows * lineHeight + gp.tileSize; // Adjust vertical position as needed

		String back = tr("controls.back");
		String reset = tr("controls.reset");

		g2.setFont(currentFont.deriveFont(Font.PLAIN, 36F));
		int backWidth = g2.getFontMetrics().stringWidth(back);
		int resetWidth = g2.getFontMetrics().stringWidth(reset);
		int buttonGap = gp.tileSize; // Space between buttons

		int totalButtonsWidth = backWidth + resetWidth + buttonGap;
		int buttonsStartX = (gp.baseWidth - totalButtonsWidth) /  2;

		int backX = buttonsStartX;
		int resetX = backX + backWidth + buttonGap;

		// Draw "Back" button
		if (menuControlsCommandNum == total && !waitingForKey) {
		    g2.setColor(Color.YELLOW);
		    g2.drawString(">", backX - gp.tileSize / 2, buttonY);
		}
		g2.setColor(Color.WHITE);
		g2.drawString(back, backX, buttonY);

		// Draw "Reset" button
		if (menuControlsCommandNum == total + 1 && !waitingForKey) {
		    g2.setColor(Color.YELLOW);
		    g2.drawString(">", resetX - gp.tileSize / 2, buttonY);
		}
		g2.setColor(Color.WHITE);
		g2.drawString(reset, resetX, buttonY);

		if (waitingForKey) {
			g2.setColor(Color.CYAN);
			String kText = tr("message.press_key");
			int msgX = getXForCenteredText(kText);
			int msgY = buttonY + 3 * gp.tileSize / 4;
			g2.drawString(kText, msgX, msgY);
		}

		// Show duplicate key warning if needed
		if (keyBindWarning) {
			g2.setColor(Color.RED);
			String warn = tr("message.key_already_assigned");
			int warnX = getXForCenteredText(warn);
			int warnY = buttonY + 3 * gp.tileSize / 4;
			g2.drawString(warn, warnX, warnY);
			// Hide warning after 2 seconds
			if (System.currentTimeMillis() - keyBindWarningTime > 2000) {
				keyBindWarning = false;
			}
		}
	}

	public void drawGameOverScreen() {
		g2.setColor(new Color(0, 0, 0, 150));
		g2.fillRect(0, 0, gp.baseWidth, gp.baseHeight);
		
		g2.setColor(Color.WHITE);

		g2.setFont(currentFontBold.deriveFont(Font.BOLD,  48F));
		String text = tr("game_over.title");
		int textX = getXForCenteredText(text);
		int textY = gp.tileSize * 2;
		g2.drawString(text, textX, textY);

		textY += gp.tileSize * 8;
		g2.setFont(currentFont.deriveFont(Font.PLAIN, 36F));
			text = tr("game_over.restart");
		textX = getXForCenteredText(text);
		g2.drawString(text, textX, textY);
		if (commandNum == 0) {
			g2.drawString(">", textX - gp.tileSize / 2, textY);
		}

		textY += gp.tileSize;
		text = tr("game_over.exit_to_main_menu");
		textX = getXForCenteredText(text);
		g2.drawString(text, textX, textY);
		if (commandNum == 1) {
			g2.drawString(">", textX - gp.tileSize / 2, textY);
		}
	}

	public void drawTransitionScreen() {
		counter++;
		g2.setColor(new Color(0, 0, 0, counter * 5));
		g2.fillRect(0, 0, gp.baseWidth, gp.baseHeight);

		if (counter >= 50) {
			gp.eHandler.finishTeleport(); // <-- ADD THIS LINE
			gp.gameState = gp.playState; // Go back to play state after transition
			counter = 0; // Reset counter for next transition
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
		int x = (gp.baseWidth - length) / 2;
		return x;
	}

	public int getXForAllignToRightText(String text, int tailX) {
		int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		int x = tailX - length;
		return x;
	}
	
	private java.util.List<String> wrapText(String text, int maxWidth, Graphics2D g2) {
    java.util.List<String> lines = new java.util.ArrayList<>();
    if (text == null || text.isEmpty()) return lines;

    // If the text contains spaces, wrap by word; otherwise, wrap by character
    boolean hasSpace = text.contains(" ");
    if (hasSpace) {
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
    } else {
        // No spaces: wrap by character
        StringBuilder line = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            line.append(text.charAt(i));
            int lineWidth = g2.getFontMetrics().stringWidth(line.toString());
            if (lineWidth > maxWidth && line.length() > 1) {
                // Remove last char, start new line with it
                char lastChar = line.charAt(line.length() - 1);
                line.deleteCharAt(line.length() - 1);
                lines.add(line.toString());
                line = new StringBuilder();
                line.append(lastChar);
            }
        }
        if (line.length() > 0) lines.add(line.toString());
    }
    return lines;
}
	
	final String[][] keyboard = {
		{"1","2","3","4","5","6","7","8","9","0"},
		{"Q","W","E","R","T","Y","U","I","O","P"},
		{"A","S","D","F","G","H","J","K","L","<-"},
		
		{"Z","X","C","V","B","N","M", "     ","OK"},
	};
	int kbRow = 0;
	int kbCol = 0;
	boolean typingName = true; // true while on name input screen

	public int getItemIndexOnSlot() {
		return slotRow * maxInventoryCol + slotCol;
	}

	public void loadLanguage() {
    // Sync languageIndex with language code
    for (int i = 0; i < languageCodes.length; i++) {
        if (languageCodes[i].equals(language)) {
            languageIndex = i;
           
            break;
        }
    }
    String langFile = "/lang/lang_" + language + ".properties";
    try (InputStream is = getClass().getResourceAsStream(langFile)) {
        if (is != null) {
            try (InputStreamReader reader = new InputStreamReader(is, java.nio.charset.StandardCharsets.UTF_8)) {
                langProps.load(reader);
            }
        } else {
            System.out.println("Language file not found: " + langFile);
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    updateQualities(); // <-- add this at the end
}

    public String tr(String key) {
        return langProps.getProperty(key, key); // fallback to key if not found
    }
    public String tr(String key, Object... params) {
        String template = langProps.getProperty(key, key);
        if (params.length > 0) {
            return String.format(template, params);
        }
        return template;
    }

    public void resetKeyboardCursor() {
        kbRow = 0;
        kbCol = 0;
        typingName = true;
    }

    public void drawDeathAnimation() {
        // Example: Draw the player sprite with a fade-out effect
        float alpha = 1.0f - (float)gp.player.deathAnimCounter / gp.player.DEATH_ANIM_DURATION;
        java.awt.Composite original = g2.getComposite();
        g2.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, Math.max(0, alpha)));
        // Draw player at their position
        g2.drawImage(gp.player.fullBody, gp.player.screenX, gp.player.screenY, gp.tileSize * 2, gp.tileSize * 2, null);
        g2.setComposite(original);
    }

    // Add this method:
	public void updateQualities() {
		qualities = new String[] {
			tr("quality.low"),
			tr("quality.medium"),
			tr("quality.high")
		};
	}

	public void drawMiniMap(Graphics2D g2) {
		// Mini map size and position
		int mapWidth = gp.tileSize * 4;
		int mapHeight = gp.tileSize * 4;
		int mapX = gp.baseWidth - mapWidth - 24; // 24px from right
		int mapY = 24; // 24px from top

		// Draw background
		g2.setColor(new Color(0, 0, 0, 180));
		g2.fillRoundRect(mapX, mapY, mapWidth, mapHeight, 16, 16);

		// Calculate scale
		int worldWidth = gp.maxWorldCol * gp.tileSize;
		int worldHeight = gp.maxWorldRow * gp.tileSize;
		float scaleX = (float) mapWidth / worldWidth;
		float scaleY = (float) mapHeight / worldHeight;

		// --- Draw map tiles as pixels ---
		for (int col = 0; col < gp.maxWorldCol; col++) {
			for (int row = 0; row < gp.maxWorldRow; row++) {
				int tileNum = gp.tileM.mapTileNum[gp.currentMap][col][row];
				// Choose a color for the tile (customize as needed)
				if (gp.tileM.tile[tileNum].collision) {
					g2.setColor(new Color(80, 80, 80)); // Wall/blocked
				} else {
					g2.setColor(new Color(120, 180, 120)); // Floor/grass
				}
				int miniX = mapX + (int)(col * gp.tileSize * scaleX);
				int miniY = mapY + (int)(row * gp.tileSize * scaleY);
				int miniTile = Math.max(1, (int)(gp.tileSize * scaleX));
				g2.fillRect(miniX, miniY, miniTile, miniTile);
			}
		}

		// --- Draw objects (optional) ---
		for (Entity obj : gp.obj[gp.currentMap]) {
			if (obj != null && obj.pickable) {
				int objMiniX = mapX + (int)(obj.worldX * scaleX);
				int objMiniY = mapY + (int)(obj.worldY * scaleY);
				g2.setColor(Color.YELLOW);
				g2.fillRect(objMiniX, objMiniY, 3, 3);
			}
		}

		// --- Draw player position ---
		int playerMiniX = mapX + (int)(gp.player.worldX * scaleX);
		int playerMiniY = mapY + (int)(gp.player.worldY * scaleY);
		int playerDotSize = 8;
		g2.setColor(Color.RED);
		g2.fillOval(playerMiniX - playerDotSize/2, playerMiniY - playerDotSize/2, playerDotSize, playerDotSize);
	}

	public int skillsCommandNum = 0; // 0-3: Q/W/E/R, 4+: unlocked skills
	public boolean assigningSkill = false; // true if picking a skill to assign
	public int skillAssignSlotIndex = 0;

	public void drawSkillScreen() {
		int width = gp.tileSize * 14;
		int height = gp.tileSize * 10;
		int frameX = (gp.baseWidth - width) / 2;
		int frameY = (gp.baseHeight - height) / 2;

		drawSubWindow(frameX, frameY, width, height);

		// Title
		g2.setFont(currentFontBold.deriveFont(Font.BOLD, 48F));
		String title = tr("skills.title");
		int titleX = getXForCenteredText(title);
		int titleY = frameY + gp.tileSize;
		g2.setColor(Color.WHITE);
		g2.drawString(title, titleX, titleY);

		// Assigned skills (Q/W/E/R)
		String[] keys = {gp.keyConfig.getKeyName(KeyConfig.SKILL1), gp.keyConfig.getKeyName(KeyConfig.SKILL2), gp.keyConfig.getKeyName(KeyConfig.SKILL3), gp.keyConfig.getKeyName(KeyConfig.SKILL4)};
		int slotSize = gp.tileSize;
		int slotGap = gp.tileSize / 2;
		// Center the 4 slots horizontally in the skill window:
		int totalSlotsWidth = 4 * slotSize + 3 * slotGap;
		int slotsStartX = frameX + (width - totalSlotsWidth) / 2;
		int slotsY = titleY + gp.tileSize * 3 / 4;

		g2.setFont(currentFontBold.deriveFont(Font.BOLD, 32F));
		for (int i = 0; i < 4; i++) {
			int slotX = slotsStartX + i * (slotSize + slotGap);

			// Highlight if selected
			if (!assigningSkill && skillsCommandNum == i) {
				g2.setColor(new Color(255, 255, 100, 220));
				g2.fillRoundRect(slotX - 6, slotsY - 6, slotSize + 12, slotSize + 12, 24, 24);
			}
			g2.setColor(new Color(255, 255, 100, 180));
			g2.fillRoundRect(slotX, slotsY, slotSize, slotSize, 20, 20);

			g2.setColor(Color.WHITE);
			g2.setStroke(new BasicStroke(4));
			g2.drawRoundRect(slotX, slotsY, slotSize, slotSize, 20, 20);

			// Draw skill icon to fill the slot (edge-to-edge)
			Skill skill = gp.player.assignedSkills[i];
			if (skill != null && skill.getIcon() != null) {
				g2.drawImage(skill.getIcon(), slotX, slotsY, slotSize, slotSize, null);
			}

			// Draw key label at bottom right corner
			g2.setFont(currentFontBold.deriveFont(Font.BOLD, 22F));
			String keyLabel = keys[i];
			int labelWidth = g2.getFontMetrics().stringWidth(keyLabel);
			int labelHeight = g2.getFontMetrics().getAscent();
			g2.setColor(Color.WHITE);
			g2.drawString(keyLabel, slotX + slotSize - labelWidth - 6, slotsY + slotSize - 8);
		}

		// List all unlocked skills
		int listX = frameX + gp.tileSize;
		int listY = slotsY + slotSize + gp.tileSize / 2;
		g2.setFont(currentFontBold.deriveFont(Font.BOLD, 28F));
		g2.setColor(Color.WHITE);
		g2.drawString(tr("skills.unlocked"), listX, listY);
		listY += gp.tileSize / 2;

		g2.setFont(currentFont.deriveFont(Font.PLAIN, 22F));
		ArrayList<skill.Skill> unlockedSkills = (ArrayList<Skill>) gp.player.getAllUnlockedSkills();
		int unlockedCount = unlockedSkills.size();
		int start = skillListScroll;
		int end = Math.min(start + maxVisibleSkills, unlockedCount);

		for (int i = start; i < end; i++) {
			skill.Skill skill = unlockedSkills.get(i);
			int y = listY + (i - start) * gp.tileSize;
			// Highlight if assigning and this skill is selected
			if (assigningSkill && skillsCommandNum == i + 4) {
				g2.setColor(new Color(100, 200, 255, 180));
				g2.fillRoundRect(listX - 8, y - 24, width - 2 * gp.tileSize, gp.tileSize, 16, 16);
				g2.setColor(Color.WHITE);
			}
			String skillText = skill.getName(this.gp) + " - " + skill.getDescription(this.gp);
			g2.drawString(skillText, listX, y);
		}

		// Draw scroll bar if needed
		if (unlockedCount > maxVisibleSkills) {
			int barHeight = gp.tileSize * maxVisibleSkills;
			int barY = listY;
			int barX = frameX + width - gp.tileSize / 2;
			int scrollHeight = Math.max(gp.tileSize / 2, (int) ((float)maxVisibleSkills / unlockedCount * barHeight));
			int scrollY = barY + (int) ((float)skillListScroll / unlockedCount * barHeight);

			g2.setColor(new Color(180, 180, 180, 180));
			g2.fillRoundRect(barX, barY, gp.tileSize / 6, barHeight, 8, 8);
			g2.setColor(new Color(80, 80, 255, 220));
			g2.fillRoundRect(barX, scrollY, gp.tileSize / 6, scrollHeight, 8, 8);
		}

		// Instructions
		g2.setFont(currentFont.deriveFont(Font.PLAIN, 20F));
		g2.setColor(Color.YELLOW);
		if (!assigningSkill) {
			String instr1 = java.text.MessageFormat.format(tr("skills.instruction1"), gp.keyConfig.getKeyName(KeyConfig.CHOOSE));
			String instr2 = java.text.MessageFormat.format(tr("skills.instruction2"), gp.keyConfig.getKeyName(KeyConfig.ESCAPE));
			g2.drawString(instr1, frameX + gp.tileSize, frameY + height - gp.tileSize * 2);
			g2.drawString(instr2, frameX + gp.tileSize, frameY + height - gp.tileSize);
		} else {
			String instr = java.text.MessageFormat.format(tr("skills.assign_instruction"), gp.keyConfig.getKeyName(KeyConfig.CHOOSE), gp.keyConfig.getKeyName(KeyConfig.ESCAPE));
			g2.drawString(instr, frameX + gp.tileSize, frameY + height - gp.tileSize * 2);
		}
	}
	
	public void drawSkillBar() {
		int slotSize = gp.tileSize;
		int slotGap = gp.tileSize / 3;

		// The skill bar will be horizontal: Q W E R | quick | flash
		int numSlots = 6;
		int barWidth = numSlots * slotSize + (numSlots - 1) * slotGap + 12;
		int barHeight = slotSize + 12;

		// Place bar at the bottom left corner
		int barX = 24; // 24px from left
		int barY = gp.baseHeight - barHeight - 24; // 24px from bottom

		// Draw connected background bar
		g2.setColor(new Color(30, 30, 60, 180));
		g2.fillRoundRect(barX - 6, barY - 6, barWidth, barHeight, 16, 16);
		g2.setColor(new Color(120, 120, 180, 120));
		g2.setStroke(new BasicStroke(4));
		g2.drawRoundRect(barX - 6, barY - 6, barWidth, barHeight, 16, 16);

		String[] keys = {gp.keyConfig.getKeyName(KeyConfig.SKILL1), gp.keyConfig.getKeyName(KeyConfig.SKILL2), gp.keyConfig.getKeyName(KeyConfig.SKILL3), gp.keyConfig.getKeyName(KeyConfig.SKILL4)};

		// --- Draw skill slots ---
		for (int i = 0; i < 4; i++) {
			int slotX = barX + i * (slotSize + slotGap);
			int slotY = barY;

			// Draw slot background
			g2.setColor(new Color(40, 40, 40, 220));
			g2.fillRoundRect(slotX, slotY, slotSize, slotSize, 16, 16);
			g2.setColor(Color.WHITE);
			g2.setStroke(new BasicStroke(4));
			g2.drawRect(slotX - 2, slotY - 2, slotSize + 4, slotSize + 4);
			g2.setStroke(new BasicStroke(1)); // Reset stroke

			// Draw skill icon
			Skill skill = gp.player.assignedSkills[i];
			if (skill != null && skill.getIcon() != null) {
				g2.drawImage(skill.getIcon(), slotX, slotY, slotSize, slotSize, null);
			}

			// Draw cooldown overlay if on cooldown
			if (skill != null && skill.getCooldown() > 0) {
				float percent = (float)skill.getCooldown() / skill.getCooldownMax();
				int overlayHeight = (int)(slotSize * percent);
				g2.setColor(new Color(0, 0, 0, 140));
				g2.fillRect(slotX, slotY, slotSize, overlayHeight);

				// Draw cooldown seconds
				g2.setColor(Color.WHITE);
				g2.setFont(currentFontBold.deriveFont(Font.BOLD, 22f));
				String cdText = String.format("%.1f", skill.getCooldown() / 60.0);
				int textWidth = g2.getFontMetrics().stringWidth(cdText);
				g2.drawString(cdText, slotX + (slotSize - textWidth) / 2, slotY + slotSize / 2 + 10);
			}

			// Draw key label at bottom left
			g2.setFont(currentFontBold.deriveFont(Font.BOLD, 22F));
			String keyLabel = keys[i];
			int labelWidth = g2.getFontMetrics().stringWidth(keyLabel);
			g2.setColor(Color.WHITE);
			g2.drawString(keyLabel, slotX + 4, slotY + slotSize - 6);
		}

		// --- Draw quick-use item slot (slot 4) ---
		int quickX = barX + 4 * (slotSize + slotGap);
		int quickY = barY;

		g2.setColor(new Color(40, 40, 40, 220));
		g2.fillRoundRect(quickX, quickY, slotSize, slotSize, 16, 16);
		g2.setColor(Color.WHITE);
		g2.setStroke(new BasicStroke(4));
		g2.drawRect(quickX - 2, quickY - 2, slotSize + 4, slotSize + 4);
		g2.setStroke(new BasicStroke(1));

		Entity quickItem = null;
		if (gp.player.quickUseItemClass != null) {
			for (Entity item : gp.player.inventory) {
				if (item != null
					&& item.getClass() == gp.player.quickUseItemClass
					&& (gp.player.quickUseItemName == null || gp.player.quickUseItemName.equals(item.name))
					&& item.quantity > 0) {
					quickItem = item;
					break;
				}
			}
		}
		if (quickItem != null && quickItem.down1 != null) {
			g2.drawImage(quickItem.down1, quickX, quickY, slotSize, slotSize, null);
			// Draw quantity if stackable
			if (quickItem.stackable && quickItem.quantity > 1) {
				g2.setFont(g2.getFont().deriveFont(Font.BOLD, 18F));
				String qtyText = "x" + quickItem.quantity;
				int qtyWidth = g2.getFontMetrics().stringWidth(qtyText);
				g2.setColor(Color.WHITE);
				g2.drawString(qtyText, quickX + slotSize - qtyWidth - 4, quickY + slotSize - 6);
			}
		} else {
			// Draw placeholder
			g2.setColor(new Color(200, 200, 200, 200));
			g2.fillRect(quickX, quickY, slotSize, slotSize);
			g2.setColor(Color.GRAY);
			String text = tr("quick_use.empty");
			int fontSize = 16;
			int maxWidth = slotSize - 8;
			java.util.List<String> lines = new java.util.ArrayList<>();
			String[] words = text.split(" ");
			StringBuilder line = new StringBuilder();
			for (String word : words) {
				String testLine = line.length() == 0 ? word : line + " " + word;
				g2.setFont(g2.getFont().deriveFont(Font.PLAIN, (float)fontSize));
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
			while (true) {
				boolean fits = true;
				for (String l : lines) {
					g2.setFont(g2.getFont().deriveFont(Font.BOLD, (float)fontSize));
					if (g2.getFontMetrics().stringWidth(l) > maxWidth) {
						fits = false;
						break;
					}
				}
				if (fits || fontSize <= 8) break;
				fontSize--;
			}
			int textHeight = g2.getFontMetrics().getAscent();
			int totalHeight = lines.size() * textHeight + (lines.size() - 1) * 2;
			int startY = quickY + (slotSize - totalHeight) / 2 + textHeight - 2;
			for (String l : lines) {
				int textWidth = g2.getFontMetrics().stringWidth(l);
				g2.drawString(l, quickX + (slotSize - textWidth) / 2, startY);
				startY += textHeight + 2;
			}
		}
		// Draw assigned key for quick-use
		String quickKey = gp.keyConfig.getKeyName(main.KeyConfig.QUICK_USE);
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 22F));
		g2.setColor(Color.WHITE);
		int quickKeyWidth = g2.getFontMetrics().stringWidth(quickKey);
		g2.drawString(quickKey, quickX + 4, quickY + slotSize - 4);

		// --- Draw flash slot (slot 5) ---
		int flashX = barX + 5 * (slotSize + slotGap);
		int flashY = barY;

		g2.setColor(new Color(40, 40, 40, 220));
		g2.fillRoundRect(flashX, flashY, slotSize, slotSize, 16, 16);
		g2.setColor(Color.WHITE);
		g2.setStroke(new BasicStroke(4));
		g2.drawRect(flashX - 2, flashY - 2, slotSize + 4, slotSize + 4);
		g2.setStroke(new BasicStroke(1));

		// Draw flash icon
		if (flash != null) {
			g2.drawImage(flash, flashX, flashY, slotSize, slotSize, null);
		} else {
			g2.setColor(Color.YELLOW);
			g2.fillRect(flashX, flashY, slotSize, slotSize);
		}

		// Draw cooldown overlay if on cooldown
		if (gp.player.flashCooldown > 0) {
			float percent = (float)gp.player.flashCooldown / gp.player.FLASH_COOLDOWN_MAX;
			int overlayHeight = (int)(slotSize * percent);
			g2.setColor(new Color(0, 0, 0, 120));
			g2.fillRect(flashX, flashY, slotSize, overlayHeight);

			// Draw cooldown seconds
			g2.setColor(Color.WHITE);
			g2.setFont(g2.getFont().deriveFont(Font.BOLD, 22f));
			String cdText = String.format("%.1f", gp.player.flashCooldown / 60.0);
			int textWidth = g2.getFontMetrics().stringWidth(cdText);
			g2.drawString(cdText, flashX + (slotSize - textWidth) / 2, flashY + slotSize / 2 + 12);
		}

		// Draw assigned key at bottom right corner
		String keyName = gp.keyConfig.getKeyName(main.KeyConfig.FLASH);
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 22F));
		g2.setColor(Color.WHITE);
		int keyTextWidth = g2.getFontMetrics().stringWidth(keyName);
		g2.drawString(keyName, flashX + 4, flashY + slotSize - 4);
	}

	public void drawSkillTreeScreen() {
	    // Background
	    g2.setColor(new Color(0, 0, 0, 230));
	    g2.fillRect(0, 0, gp.baseWidth, gp.baseHeight);

	    int centerX = gp.baseWidth / 2;
	    int startY = gp.tileSize * 2;
	    int nodeSize = gp.tileSize * 2;
	    int verticalGap = gp.tileSize * 3;
	    int horizontalGap = gp.tileSize * 3;

	    // Title
	    g2.setFont(currentFontBold.deriveFont(Font.BOLD, 48f));
	    String title = "Skill Tree";
	    int titleWidth = g2.getFontMetrics().stringWidth(title);
	    g2.setColor(Color.WHITE);
	    g2.drawString(title, centerX - titleWidth / 2, startY);

	    // Skill points
	    g2.setFont(currentFontBold.deriveFont(Font.BOLD, 28f));
	    String points = "Skill Points: " + gp.player.skillPoints;
	    int pointsWidth = g2.getFontMetrics().stringWidth(points);
	    g2.setColor(Color.YELLOW);
	    g2.drawString(points, centerX - pointsWidth / 2, startY + gp.tileSize);

	    // --- Draw the tree recursively ---
	    skill.SkillTreeNode root = gp.player.skillTreeRoot;
	    int treeDepth = getSkillTreeDepth(root);
	    int[] maxWidths = new int[treeDepth];
	    getMaxWidths(root, 0, maxWidths);

	    int rootY = startY + gp.tileSize * 2;
	    int rootX = centerX;

	    // Find selected node
	    int selectedLevel = gp.keyH.selectedSkillTreeLevel;
	    int selectedIndex = gp.keyH.selectedSkillTreeIndex;
	    skill.SkillTreeNode selected = getSelectedSkillTreeNode(root, selectedLevel, selectedIndex);

	    // Draw recursively
	    drawSkillTreeNodeRecursive(root, rootX, rootY, nodeSize, verticalGap, horizontalGap, 0, 0, selected);

	    // Instructions
	    g2.setFont(currentFont.deriveFont(Font.PLAIN, 24f));
	    g2.setColor(Color.LIGHT_GRAY);
	    String instr = "ARROWS: Move   ENTER: Unlock   ESC: Close";
	    int instrWidth = g2.getFontMetrics().stringWidth(instr);
	    g2.drawString(instr, centerX - instrWidth / 2, gp.baseHeight - gp.tileSize);
	}

	// Recursively draw the skill tree
	private void drawSkillTreeNodeRecursive(skill.SkillTreeNode node, int x, int y, int size, int vGap, int hGap, int level, int index, skill.SkillTreeNode selected) {
	    // Draw children first (so lines go under nodes)
	    int numChildren = node.children.size();
	    if (numChildren > 0) {
	        int totalWidth = (numChildren - 1) * hGap;
	        int childX = x - totalWidth / 2;
	        int childY = y + vGap;
	        for (int i = 0; i < numChildren; i++) {
	            skill.SkillTreeNode child = node.children.get(i);
	            // Draw line from parent to child
	            g2.setColor(Color.WHITE);
	            g2.setStroke(new BasicStroke(3));
	            g2.drawLine(x, y + size / 2, childX, childY - size / 2);
	            // Draw child recursively
	            drawSkillTreeNodeRecursive(child, childX, childY, size, vGap, hGap, level + 1, i, selected);
	            childX += hGap;
	        }
	    }
	    // Draw this node
	    boolean isSelected = (node == selected);
	    drawSkillNode(node, x, y, size, isSelected);
	}

	// Helper to draw a skill node with highlight, icon, name, and lock
	private void drawSkillNode(skill.SkillTreeNode node, int x, int y, int size, boolean selected) {
		if (selected) {
			g2.setColor(new Color(255, 255, 100, 200));
			g2.fillOval(x - size / 2 - 8, y - size / 2 - 8, size + 16, size + 16);
		}
		g2.setColor(node.unlocked ? new Color(100, 255, 100) : new Color(120, 120, 120));
		g2.fillOval(x - size / 2, y - size / 2, size, size);
		g2.setColor(Color.BLACK);
		g2.setStroke(new BasicStroke(1f));
		g2.drawOval(x - size / 2, y - size / 2, size, size);

		// Draw icon as a circle
		if (node.skill != null && node.skill.getIcon() != null) {
			int iconMargin = Math.max(2, size / 20); // minimal margin
			int iconSize = size - iconMargin * 2;
			int iconX = x - iconSize / 2;
			int iconY = y - iconSize / 2;

			java.awt.Shape oldClip = g2.getClip();
			g2.setClip(new java.awt.geom.Ellipse2D.Float(iconX, iconY, iconSize, iconSize));
			g2.drawImage(node.skill.getIcon(), iconX, iconY, iconSize, iconSize, null);
			g2.setClip(oldClip);
		}

		// Name
		g2.setColor(Color.WHITE);
		g2.setFont(currentFontBold.deriveFont(Font.BOLD, 18f));
		String name = node.skill != null ? node.skill.getName(gp) : "???";
		int nameWidth = g2.getFontMetrics().stringWidth(name);
		g2.drawString(name, x - nameWidth / 2, y + size / 2 + 22);

		// Lock status
		if (!node.unlocked) {
			if (node.parent != null && !node.parent.unlocked) {
				g2.setColor(new Color(255, 80, 80, 180));
				g2.setFont(currentFontBold.deriveFont(Font.BOLD, 16f));
				String lock = "Locked";
				int lockWidth = g2.getFontMetrics().stringWidth(lock);
				g2.drawString(lock, x - lockWidth / 2, y + size / 2 + 40);
			} else {
				g2.setColor(Color.YELLOW);
				g2.setFont(currentFontBold.deriveFont(Font.BOLD, 16f));
				String unlock = "Unlockable";
				int unlockWidth = g2.getFontMetrics().stringWidth(unlock);
				g2.drawString(unlock, x - unlockWidth / 2, y + size / 2 + 40);
			}
		}
	}

	// Utility: get tree depth
	private int getSkillTreeDepth(skill.SkillTreeNode node) {
	    if (node.children.isEmpty()) return 1;
	    int max = 0;
	    for (skill.SkillTreeNode child : node.children) {
	        max = Math.max(max, getSkillTreeDepth(child));
	    }
	    return max + 1;
	}

	// Utility: get max width at each level (for future expansion)
	private void getMaxWidths(skill.SkillTreeNode node, int level, int[] maxWidths) {
	    if (maxWidths.length > level) maxWidths[level]++;
	    for (skill.SkillTreeNode child : node.children) {
	        getMaxWidths(child, level + 1, maxWidths);
	    }
	}

	// Utility: get selected node by level/index
	private skill.SkillTreeNode getSelectedSkillTreeNode(skill.SkillTreeNode root, int level, int index) {
	    skill.SkillTreeNode node = root;
	    for (int l = 0; l < level; l++) {
	        if (node.children.size() > index) node = node.children.get(index);
	        else if (!node.children.isEmpty()) node = node.children.get(0);
	        else break;
	    }
	    return node;
	}
}