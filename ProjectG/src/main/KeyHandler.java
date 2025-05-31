package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import entity.Entity;

public class KeyHandler implements KeyListener{
	
	GamePanel gp;
	public KeyHandler(GamePanel gp) {
		this.gp = gp;

	}
	
	public boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed, spacePressed, escPressed, flashPressed;

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
    	int code = e.getKeyCode();

		if (gp.gameState == gp.titleState) {
			// Handle all title screen substates here!
			titleState(code);
		}

		else if (gp.gameState == gp.playState) {
			playState(code);
		}

		else if (gp.gameState == gp.pauseState) {
			pauseState(code);
		}

		else if (gp.gameState == gp.dialogueState) {
			dialogueState(code);
		}

		else if (gp.gameState == gp.characterState) {
			characterState(code);
		}
		else if (gp.gameState == gp.optionsState) {
			optionsState(code);
		}

		else if (gp.gameState == gp.inventoryState) {
			inventoryState(code);
		}
		else if (gp.gameState == gp.gameOverState) {
			gameOverState(code);
		}
		else if (gp.gameState == gp.chestState) {
			chestState(code);
		}
		else if (gp.gameState == gp.skillsState) {
			skillsState(code);
		}
		

	}

	public void titleState(int code){
		if (gp.ui.titleScreenState == 0) {
			if (code == gp.keyConfig.getKey(KeyConfig.UP)) {
				gp.ui.commandNum--;
				if (gp.ui.commandNum < 0) {
					gp.ui.commandNum = 3;
				}
			}
				
			if (code == gp.keyConfig.getKey(KeyConfig.DOWN)) {
				gp.ui.commandNum++;
				if (gp.ui.commandNum > 3) {
					gp.ui.commandNum = 0;
				}
			}
		
			if (code == gp.keyConfig.getKey(KeyConfig.CHOOSE)) {
				if (gp.ui.commandNum == 0) {
					gp.keyConfig.resetToDefault(); // Reset key bindings to default
					gp.config.saveConfig(); // Load the config to apply changes
					gp.ui.resetKeyboardCursor();
					gp.player.reset(); // Reset player state
					gp.resetEntities();
					gp.ui.titleScreenState = 1; 
				}
				if (gp.ui.commandNum == 1) {
					gp.config.loadPlayer(gp.player);
				}
				if (gp.ui.commandNum == 2) {
					gp.ui.commandNum = 0; // Reset cursor to first option
					gp.ui.titleScreenState = 3; // Show settings menu
				}	
				if (gp.ui.commandNum == 3) {
					gp.config.saveConfig();
					System.exit(0);
				}

			}
			
		}
			
		else if (gp.ui.titleScreenState == 1) {
			if (code == gp.keyConfig.getKey(KeyConfig.UP)) {
				gp.ui.kbRow = Math.max(0, gp.ui.kbRow - 1);
				gp.ui.kbCol = Math.min(gp.ui.kbCol, gp.ui.keyboard[gp.ui.kbRow].length - 1);
			} else if (code == gp.keyConfig.getKey(KeyConfig.DOWN)) {
				gp.ui.kbRow = Math.min(gp.ui.keyboard.length - 1, gp.ui.kbRow + 1);
				gp.ui.kbCol = Math.min(gp.ui.kbCol, gp.ui.keyboard[gp.ui.kbRow].length - 1);
			} else if (code == gp.keyConfig.getKey(KeyConfig.LEFT)) {
				gp.ui.kbCol = Math.max(0, gp.ui.kbCol - 1);
			} else if (code == gp.keyConfig.getKey(KeyConfig.RIGHT)) {
				gp.ui.kbCol = Math.min(gp.ui.keyboard[gp.ui.kbRow].length - 1, gp.ui.kbCol + 1);
			} else if (code == gp.keyConfig.getKey(KeyConfig.CHOOSE)) {
				String key = gp.ui.keyboard[gp.ui.kbRow][gp.ui.kbCol];
				if (key.equals("<-")) {
					if (!gp.player.name.isEmpty()) {
						gp.player.name = gp.player.name.substring(0, gp.player.name.length() - 1);
					}
				} else if (key.equals("SPACE")) {
					gp.player.name += " ";
				} else if (key.equals("OK") && !gp.player.name.isEmpty()) {
					gp.ui.typingName = false;
					gp.gameState = gp.playState;
					gp.player.name = gp.player.name.trim();
					gp.ui.kbCol = 0; // Reset cursor position
					gp.ui.kbRow = 0; // Reset cursor position
					// Proceed to next screen or save name
				} else if (key.equals("OK")) {
					if (gp.player.name.isEmpty()) {
						gp.player.name = generateRandomName();
						// Stay on the name input screen so the player can see and edit the name
					} else {
						gp.ui.typingName = false;
						gp.gameState = gp.playState;
						gp.player.name = gp.player.name.trim();
						// Proceed to next screen or save name
					}
				} else if (gp.player.name.length() < 12) // Limit name length
					gp.player.name += key;
			} else if (code == gp.keyConfig.getKey(KeyConfig.ESCAPE)) {
				gp.ui.typingName = false;
				gp.ui.titleScreenState = 0; // Go back to main menu
				gp.ui.commandNum = 0; // Reset command number
			}
			return; // Prevent further processing if on name input
		}
		else if (gp.ui.titleScreenState == 3) {
			if (code == gp.keyConfig.getKey(KeyConfig.UP)) {
				gp.ui.commandNum--;
				if (gp.ui.commandNum < 0) gp.ui.commandNum = 5; // e.g., 6 options: 0-5
			}
			if (code == gp.keyConfig.getKey(KeyConfig.DOWN)) {
				gp.ui.commandNum++;
				if (gp.ui.commandNum > 5) gp.ui.commandNum = 0;
			}
			if (code == gp.keyConfig.getKey(KeyConfig.CHOOSE)) {
				// Handle selection based on gp.ui.commandNum
				if (gp.ui.commandNum == 2) {
					gp.ui.commandNum = 0; 
					gp.ui.titleScreenState = 4;
				}
				if (gp.ui.commandNum == 3) {
					gp.ui.commandNum = 0; // Reset cursor to first option
					gp.ui.titleScreenState = 5; // Show language
				}
				if (gp.ui.commandNum == 4) {
					gp.ui.commandNum = 0; // Reset cursor to first option
					gp.ui.titleScreenState = 6; // Show controls
				}
				if (gp.ui.commandNum == 5) {
					gp.ui.titleScreenState = 0; // Go back to main menu
					gp.ui.commandNum = 0; // Reset command number
				}
				// Add your logic for other options here
			}
		}
		
		
		else if (gp.ui.titleScreenState == 4) {
			if (code == gp.keyConfig.getKey(KeyConfig.UP)) {
				gp.ui.graphicsCommandNum--;
				if (gp.ui.graphicsCommandNum < 0) gp.ui.graphicsCommandNum = 4; // 5 options: 0-4
			}
			if (code == gp.keyConfig.getKey(KeyConfig.DOWN)) {
				gp.ui.graphicsCommandNum++;
				if (gp.ui.graphicsCommandNum > 4) gp.ui.graphicsCommandNum = 0;
			}
			if (code == gp.keyConfig.getKey(KeyConfig.CHOOSE)) {
				switch (gp.ui.graphicsCommandNum) {
					case 0: // Resolution
						gp.ui.resolutionIndex = (gp.ui.resolutionIndex + 1) % gp.ui.resolutions.length;
						String res = gp.ui.resolutions[gp.ui.resolutionIndex];
						String[] parts = res.split("x");
						int width = Integer.parseInt(parts[0]);
						int height = Integer.parseInt(parts[1]);
						gp.screenWidth = width;
						gp.screenHeight = height;
						gp.setPreferredSize(new java.awt.Dimension(width, height));
						gp.revalidate();
						gp.getParent().revalidate();
						java.awt.Window window = javax.swing.SwingUtilities.getWindowAncestor(gp);
						if (window != null) window.pack();
						break;
					case 1: // VSync
						gp.ui.vsyncOn = !gp.ui.vsyncOn;
						break;
					case 2: // Quality
						gp.ui.qualityIndex = (gp.ui.qualityIndex + 1) % gp.ui.qualities.length;
						break;
					case 3: // Fullscreen
						gp.ui.fullscreenOn = !gp.ui.fullscreenOn;
						java.awt.Window win = javax.swing.SwingUtilities.getWindowAncestor(gp);
						java.awt.GraphicsDevice gd = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
						if (win instanceof javax.swing.JFrame) {
						 javax.swing.JFrame frame = (javax.swing.JFrame) win;
						 if (gp.ui.fullscreenOn) {
						 frame.dispose();
						 frame.setUndecorated(true);
						 frame.setResizable(false);
						 frame.setVisible(true);
						 gd.setFullScreenWindow(frame);
						 } else {
						 gd.setFullScreenWindow(null);
						 frame.dispose();
						 frame.setUndecorated(false);
						 frame.setResizable(true);
						 frame.setVisible(true);
						 }
						}
						break;
					case 4: // Back
						gp.ui.titleScreenState = 3;
						gp.ui.graphicsCommandNum = 0;
						break;
				}
			}
			if (code == gp.keyConfig.getKey(KeyConfig.ESCAPE)) {
				gp.ui.titleScreenState = 3;
				gp.ui.graphicsCommandNum = 0;
			}
		}
		
		else if (gp.ui.titleScreenState == 5) {
			languageSelectState(code);
		}
		
		else if (gp.ui.titleScreenState == 6) {
			menuControlsState(code);
		}

	}

	public void playState(int code){
		
		boolean canOpenMenus = !gp.player.monsterNearby && !gp.player.attacking && gp.player.health > 0;
		if (code == gp.keyConfig.getKey(KeyConfig.UP)) {
			upPressed = true;
		}
		
		if (code == gp.keyConfig.getKey(KeyConfig.DOWN)) {
			downPressed = true;
		}

		if (code == gp.keyConfig.getKey(KeyConfig.LEFT)) {
			leftPressed = true;
		}
	
		if (code == gp.keyConfig.getKey(KeyConfig.RIGHT)) {
			rightPressed = true;
		}
	
		if (code == gp.keyConfig.getKey(KeyConfig.ESCAPE)) {
			if (canOpenMenus) gp.gameState = gp.pauseState;
			else {
				gp.ui.addMessage(gp.ui.tr("cant_open_menu"));
			}
		}

		if (code == gp.keyConfig.getKey(KeyConfig.CHOOSE)) {
			enterPressed = true;
		}
		if (code == gp.keyConfig.getKey(KeyConfig.ATTACK)) {
			spacePressed = true;
		}
		if (code == gp.keyConfig.getKey(KeyConfig.FLASH)) {
			flashPressed = true;
		}
		if (code == gp.keyConfig.getKey(KeyConfig.CHARACTER)) {
			if (canOpenMenus) gp.gameState = gp.characterState;
			else {
				gp.ui.addMessage(gp.ui.tr("cant_open_menu"));
			}
		}

		if (code == gp.keyConfig.getKey(KeyConfig.INVENTORY)) {
			if (canOpenMenus) gp.gameState = gp.inventoryState;
			else {
				gp.ui.addMessage(gp.ui.tr("cant_open_menu"));
			}
		}

		if (code == gp.keyConfig.getKey(KeyConfig.QUICK_USE)) {
			Entity quickItem = null;
			for (Entity item : gp.player.inventory) {
				if (item != null
					&& gp.player.quickUseItemClass != null
					&& item.getClass() == gp.player.quickUseItemClass
					&& (gp.player.quickUseItemName == null || gp.player.quickUseItemName.equals(item.name))
					&& item.quantity > 0) {
					quickItem = item;
					break;
				}
			}
			if (quickItem != null) {
				quickItem.use(gp.player);
				quickItem.quantity--;
				if (quickItem.quantity <= 0) {
					gp.player.inventory.remove(quickItem);
				}
				// If no more of this item, clear quick use slot
				boolean found = false;
				for (Entity item : gp.player.inventory) {
					if (item != null
						&& gp.player.quickUseItemClass != null
						&& item.getClass() == gp.player.quickUseItemClass
						&& (gp.player.quickUseItemName == null || gp.player.quickUseItemName.equals(item.name))
						&& item.quantity > 0) {
						found = true;
						break;
					}
				}
				if (!found) {
					gp.player.quickUseItemClass = null;
					gp.player.quickUseItemName = null;
				}
			}
		}

		if (code == KeyEvent.VK_F3) {
			gp.debugMode = !gp.debugMode; // Toggle debug mode
		}
		if (code == KeyEvent.VK_Q) {
		    gp.player.useAssignedSkill(0); // Q = skill 0
		}
		if (code == KeyEvent.VK_W) {
		    gp.player.useAssignedSkill(1); // W = skill 1
		}
		if (code == KeyEvent.VK_E) {
		    gp.player.useAssignedSkill(2); // E = skill 2
		}
		if (code == KeyEvent.VK_R) {
		    gp.player.useAssignedSkill(3); // R = skill 3
		}
		if (code == gp.keyConfig.getKey(KeyConfig.SKILLS)){
			gp.gameState = gp.skillsState;
			gp.ui.skillsCommandNum = 0;
			gp.ui.assigningSkill = false;
		}
	}


	public void pauseState(int code){
			
		if (code == gp.keyConfig.getKey(KeyConfig.ESCAPE)) {
			gp.gameState = gp.playState;
		}
		
		if (KeyEvent.VK_W == code || KeyEvent.VK_UP == code) {
			gp.ui.pauseCommandNum--;
			if (gp.ui.pauseCommandNum < 0) gp.ui.pauseCommandNum = 4;
		}
		if (code == gp.keyConfig.getKey(KeyConfig.DOWN)) {
			gp.ui.pauseCommandNum++;
			if (gp.ui.pauseCommandNum > 4) gp.ui.pauseCommandNum = 0;
		}
		if (code == gp.keyConfig.getKey(KeyConfig.CHOOSE)) {
			switch (gp.ui.pauseCommandNum) {
				case 0: gp.gameState = gp.playState; break; // Continue
				case 1: 
					gp.config.savePlayer(gp.player);
					gp.gameState = gp.playState;
					gp.ui.addMessage(gp.ui.tr("message.saved"));
					break; // Save
				case 2: gp.gameState = gp.optionsState; break; // Settings
				case 3: // Return to Main Menu
					gp.gameState = gp.titleState;
					gp.ui.titleScreenState = 0;
					gp.ui.commandNum = 0; // Reset command number
					break;
				case 4: 
					gp.config.saveConfig();
					System.exit(0); break; // Exit
			}
		}
	}

	public void dialogueState(int code){
		if (code == gp.keyConfig.getKey(KeyConfig.CHOOSE)) {
			gp.gameState = gp.playState;
		}
	}
	
	public void characterState(int code){
		if (code == gp.keyConfig.getKey(KeyConfig.UP)) {
			UI.progressionSelectIndex--;
			if (UI.progressionSelectIndex < 0) UI.progressionSelectIndex = 3;
		}
		if (code == gp.keyConfig.getKey(KeyConfig.DOWN)) {
			UI.progressionSelectIndex++;
			if (UI.progressionSelectIndex > 3) UI.progressionSelectIndex = 0;
		}
		if (code == gp.keyConfig.getKey(KeyConfig.CHOOSE) && gp.player.progressionPoints > 0) {
			switch (UI.progressionSelectIndex) {
				case 0: 
					gp.player.maxHealth += 100;
					gp.player.health += 100;
					gp.player.progressionHealthUpgrades++;
					break;
				case 1: 
					gp.player.maxMana += 50; 
					gp.player.mana += 50;
					gp.player.progressionManaUpgrades++;
					break;
				case 2: 
					gp.player.attack += 10; 
					gp.player.progressionAttackUpgrades++;
					break;
				case 3: 
					gp.player.defense += 10; 
					gp.player.progressionDefenseUpgrades++;
					break;
			}
			gp.player.progressionPoints--;
		}
		if (code == gp.keyConfig.getKey(KeyConfig.CHARACTER)) {
			gp.gameState = gp.playState;
		}
		if (code == gp.keyConfig.getKey(KeyConfig.RESET)) {
			int totalSpent = 0;

			// Health
			int healthReduction = gp.player.progressionHealthUpgrades * 100;
			if (gp.player.maxHealth - healthReduction > 0 && gp.player.health - healthReduction > 0) {
				gp.player.maxHealth -= healthReduction;
				gp.player.health -= healthReduction;
				totalSpent += gp.player.progressionHealthUpgrades;
				gp.player.progressionHealthUpgrades = 0;
			}

			// Mana
			int manaReduction = gp.player.progressionManaUpgrades * 50;
			if (gp.player.maxMana - manaReduction > 0 && gp.player.mana - manaReduction > 0) {
				gp.player.maxMana -= manaReduction;
				gp.player.mana -= manaReduction;
				totalSpent += gp.player.progressionManaUpgrades;
				gp.player.progressionManaUpgrades = 0;
			}

			// Attack
			gp.player.attack -= gp.player.progressionAttackUpgrades * 10;
			totalSpent += gp.player.progressionAttackUpgrades;
			gp.player.progressionAttackUpgrades = 0;

			// Defense
			gp.player.defense -= gp.player.progressionDefenseUpgrades * 10;
			totalSpent += gp.player.progressionDefenseUpgrades;
			gp.player.progressionDefenseUpgrades = 0;

			gp.player.progressionPoints += totalSpent;
		}
		if (code == gp.keyConfig.getKey(KeyConfig.INVENTORY)) {
			gp.gameState = gp.inventoryState;
		}
	}

	public void optionsState(int code){
		if (gp.ui.subState == 0) {
			if (code == gp.keyConfig.getKey(KeyConfig.ESCAPE)) {
				gp.gameState = gp.pauseState;
			}
			if (code == gp.keyConfig.getKey(KeyConfig.UP)) {
				gp.ui.commandNum--;
				if (gp.ui.commandNum < 0) gp.ui.commandNum = 4; 
			}
			if (code == gp.keyConfig.getKey(KeyConfig.DOWN)) {
				gp.ui.commandNum++;
				if (gp.ui.commandNum > 4) gp.ui.commandNum = 0;
			}
			// ADD THIS BLOCK for language selection
			if (gp.ui.commandNum == 3) { // Assuming language is option 2
				if (code == gp.keyConfig.getKey(KeyConfig.LEFT)) {
					gp.ui.languageIndex--;
					if (gp.ui.languageIndex < 0) gp.ui.languageIndex = gp.ui.languageCodes.length - 1;
					gp.ui.language = gp.ui.languageCodes[gp.ui.languageIndex];
					gp.ui.loadLanguage();
					gp.config.saveConfig();
				}
				if (code == gp.keyConfig.getKey(KeyConfig.RIGHT)) {
					gp.ui.languageIndex++;
					if (gp.ui.languageIndex >= gp.ui.languageCodes.length) gp.ui.languageIndex = 0;
					gp.ui.language = gp.ui.languageCodes[gp.ui.languageIndex];
					gp.ui.loadLanguage();
					gp.config.saveConfig();
				}
			}
			if (code == gp.keyConfig.getKey(KeyConfig.CHOOSE)) {
				switch (gp.ui.commandNum) {
					case 2: // Controls
						gp.ui.subState = 1;
						gp.ui.controlsCommandNum = 0;
						break;
					case 4: // Back
						gp.gameState = gp.pauseState;
						break;
				}
			}
		} else if (gp.ui.subState == 1) {
			controlsState(code);
		}
	}

	public void controlsState(int code) {
		if (gp.ui.waitingForKey) {
			// Check for duplicate key assignment
			boolean duplicate = false;
			for (String action : gp.ui.controlActions) {
				if (gp.keyConfig.getKey(action) == code) {
					duplicate = true;
					break;
				}
			}
			if (!duplicate) {
				gp.keyConfig.setKey(gp.ui.waitingAction, code);
			} else {
				gp.ui.keyBindWarning = true;
				gp.ui.keyBindWarningTime = System.currentTimeMillis();
			}
			gp.ui.waitingForKey = false;
			gp.ui.waitingAction = null;
			return;
		}

		// When in optionsState and controls subState
		if (gp.gameState == gp.optionsState && gp.ui.subState == 1 && !gp.ui.waitingForKey) {
		    if (code == gp.keyConfig.getKey(KeyConfig.UP)) {
		        gp.ui.controlsCommandNum--;
		        if (gp.ui.controlsCommandNum < 0) gp.ui.controlsCommandNum = gp.ui.controlActions.length; // wrap to "Back"
		    }
		    if (code == gp.keyConfig.getKey(KeyConfig.DOWN)) {
		        gp.ui.controlsCommandNum++;
		        if (gp.ui.controlsCommandNum > gp.ui.controlActions.length) gp.ui.controlsCommandNum = 0; // wrap to first
		    }
		    // Handle Enter/Choose for rebinding or going back
		    if (code == gp.keyConfig.getKey(KeyConfig.CHOOSE)) {
		        if (gp.ui.controlsCommandNum == gp.ui.controlActions.length) {
		            // "Back" selected
		            gp.ui.subState = 0;
		        } else {
		            // Start rebinding
		            gp.ui.waitingForKey = true;
		            gp.ui.waitingAction = gp.ui.controlActions[gp.ui.controlsCommandNum];
		        }
		    }
		}
	}

	public void inventoryState(int code){
		if (code == gp.keyConfig.getKey(KeyConfig.INVENTORY)){
			gp.gameState = gp.playState;
		}
		if (code == gp.keyConfig.getKey(KeyConfig.CHARACTER)) {
			gp.gameState = gp.characterState;
		}
		if (code == gp.keyConfig.getKey(KeyConfig.LEFT)) {
			if (gp.ui.slotCol == 0) {
				gp.ui.slotCol = gp.ui.maxInventoryCol - 1;
			} else {
				gp.ui.slotCol--;
			}
		}
		if (code == gp.keyConfig.getKey(KeyConfig.RIGHT)) {
			if (gp.ui.slotCol == gp.ui.maxInventoryCol - 1) {
				gp.ui.slotCol = 0;
			} else {
				gp.ui.slotCol++;
			}
		}
		if (code == gp.keyConfig.getKey(KeyConfig.UP)) {
			if (gp.ui.slotRow == 0) {
				gp.ui.slotRow = gp.ui.maxInventoryRow - 1;
			} else {
				gp.ui.slotRow--;
				
			}
		}
		if (code == gp.keyConfig.getKey(KeyConfig.DOWN)) {
			if (gp.ui.slotRow == gp.ui.maxInventoryRow - 1) {
				gp.ui.slotRow = 0;
			} else {
				gp.ui.slotRow++;
				
			}
		}
		if (code == gp.keyConfig.getKey(KeyConfig.CHOOSE)) {
			int itemIndex = gp.ui.getItemIndexOnSlot();
			if (itemIndex < gp.player.inventory.size()) {
				Entity selectedItem = gp.player.inventory.get(itemIndex);
				if (selectedItem != null) {
					if (selectedItem.itemType == 6) { // Potion or consumable
						// Use the item
						selectedItem.use(gp.player);
						// Decrease quantity or remove if zero
						selectedItem.quantity--;
						if (selectedItem.quantity <= 0) {
							gp.player.inventory.remove(itemIndex);
							// Adjust cursor if needed
							int totalItems = gp.player.inventory.size();
							int maxIndex = totalItems - 1;
							int currentIndex = gp.ui.slotRow * gp.ui.maxInventoryCol + gp.ui.slotCol;
							if (currentIndex > maxIndex) {
								if (gp.ui.slotCol > 0) {
									gp.ui.slotCol--;
								} else if (gp.ui.slotRow > 0) {
									gp.ui.slotRow--;
									gp.ui.slotCol = gp.ui.maxInventoryCol - 1;
									currentIndex = gp.ui.slotRow * gp.ui.maxInventoryCol + gp.ui.slotCol;
									if (currentIndex > maxIndex) {
										gp.ui.slotCol = maxIndex % gp.ui.maxInventoryCol;
									}
								}
							}
						}
					} else {
						// Equip the item as before
						gp.player.selectItem(0);
					}
				}
			}
		}

		if (code == gp.keyConfig.getKey(KeyConfig.ASSIGN_QUICK_USE)) {
			int itemIndex = gp.ui.getItemIndexOnSlot();
			if (itemIndex < gp.player.inventory.size()) {
				Entity selectedItem = gp.player.inventory.get(itemIndex);
				if (selectedItem != null && selectedItem.itemType == 6) { // Only allow potions/consumables
					gp.player.quickUseItemClass = selectedItem.getClass();
					gp.player.quickUseItemName = selectedItem.name;
					gp.ui.addMessage(gp.ui.tr("message.quick_use_assigned", selectedItem.name));
				} else {
					gp.ui.addMessage(gp.ui.tr("message.quick_use_invalid"));
				}
			}
		}

		if (code == gp.keyConfig.getKey(KeyConfig.RESET)){
			gp.player.disposeSelectedItem();
		}
	}
	public void gameOverState(int code) {
	    // Assume 2 options: 0 = Restart, 1 = Main Menu
	    if (code == gp.keyConfig.getKey(KeyConfig.UP)) {
	        gp.ui.commandNum--;
	        if (gp.ui.commandNum < 0) gp.ui.commandNum = 1;
	    }
	    if (code == gp.keyConfig.getKey(KeyConfig.DOWN)) {
	        gp.ui.commandNum++;
	        if (gp.ui.commandNum > 1) gp.ui.commandNum = 0;
	    }
	    if (code == gp.keyConfig.getKey(KeyConfig.CHOOSE)) {
	        if (gp.ui.commandNum == 0) {
	            // Restart the game
				String playerName = gp.player.name; // Save player name
	            gp.player.reset(); // Reset player state
				gp.player.name = playerName; // Restore player name
				gp.resetEntities(); // Reset entities
	            gp.gameState = gp.playState;
	        } else if (gp.ui.commandNum == 1) {
	            // Return to main menu
	            gp.player.reset(); // Reset player state
	            gp.gameState = gp.titleState;
	            gp.ui.titleScreenState = 0;
				gp.ui.commandNum = 0;
	        }
	    }
	    // Optional: ESCAPE always returns to main menu
	    if (code == gp.keyConfig.getKey(KeyConfig.ESCAPE)) {
	        gp.player.reset();
	        gp.gameState = gp.titleState;
	        gp.ui.titleScreenState = 0;
	    }
	}
	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		
		if (code == gp.keyConfig.getKey(KeyConfig.UP)) {
			upPressed = false;
		}
		
		if (code == gp.keyConfig.getKey(KeyConfig.DOWN)) {
			downPressed = false;
		}

		if (code == gp.keyConfig.getKey(KeyConfig.LEFT)) {
			leftPressed = false;
		}

		if (code == gp.keyConfig.getKey(KeyConfig.RIGHT)) {
			rightPressed = false;
		}

		if (code == gp.keyConfig.getKey(KeyConfig.CHOOSE)) {
			enterPressed = false;
		}
		if (code == gp.keyConfig.getKey(KeyConfig.ATTACK)) {
			spacePressed = false;
		}
		if (code == gp.keyConfig.getKey(KeyConfig.ESCAPE)) {
			escPressed = false;
		}
		if (code == gp.keyConfig.getKey(KeyConfig.FLASH)){
			flashPressed = false;
		}
	}

	private String generateRandomName() {
	    String[] names = {
			"Alex", "Riley", "Morgan", "Casey", "Jordan", "Taylor", "Skyler", "Jamie", "Avery", "Quinn", "TrungTT",
        "Harper", "Rowan", "Dakota", "Emerson", "Finley", "Oner", "Peyton", "Reese", "Sawyer", "Sage",
        "Charlie", "Elliot", "Jules", "Kai", "Logan", "Milan", "Noel", "Phoenix", "River", "Shiloh",
        "Blake", "Cameron", "Drew", "Frankie", "Jesse", "Kendall", "Lane", "Doran", "Parker", "Remy",
        "Sam", "Tatum", "Val", "Wren", "Zion", "Aspen", "Briar", "Cory", "Devon", "Keria", "Gray",
        "Indigo", "Jaden", "Kieran", "Faker", "Micah", "Oakley", "Perry", "Quincy", "Robin", "Sasha"
		};
	    return names[(int)(Math.random() * names.length)];
	}
	public void languageSelectState(int code) {
	    // Move up
	    if (code == gp.keyConfig.getKey(KeyConfig.UP)) {
	        gp.ui.languageIndex--;
	        if (gp.ui.languageIndex < 0) gp.ui.languageIndex = gp.ui.languageNames.length; // wrap to "Back"
	    }
	    // Move down
	    if (code == gp.keyConfig.getKey(KeyConfig.DOWN)) {
	        gp.ui.languageIndex++;
	        if (gp.ui.languageIndex > gp.ui.languageNames.length) gp.ui.languageIndex = 0; // wrap to first
	    }
	    // Select
	    if (code == gp.keyConfig.getKey(KeyConfig.CHOOSE)) {
	        if (gp.ui.languageIndex == gp.ui.languageNames.length) {
	            // "Back" selected
	            gp.ui.titleScreenState = 3; // Go back to options/settings
	        } else {
	            // Change language
	            gp.ui.language = gp.ui.languageCodes[gp.ui.languageIndex];
	            gp.ui.loadLanguage();
	            gp.config.saveConfig(); // If you want to save the language selection
	        }
	    }
	    // ESCAPE also goes back
	    if (code == gp.keyConfig.getKey(KeyConfig.ESCAPE)) {
	        gp.ui.titleScreenState = 3;
	    }
	}
	public void menuControlsState(int code) {
	    if (gp.ui.waitingForKey) {
	        // Check for duplicate key assignment
	        boolean duplicate = false;
	        for (String action : gp.ui.controlActions) {
	            if (gp.keyConfig.getKey(action) == code) {
	                duplicate = true;
	                break;
	            }
	        }
	        if (!duplicate) {
	            gp.keyConfig.setKey(gp.ui.waitingAction, code);
	        } else {
	            gp.ui.keyBindWarning = true;
	            gp.ui.keyBindWarningTime = System.currentTimeMillis();
	        }
	        gp.ui.waitingForKey = false;
	        gp.ui.waitingAction = null;
	        return;
	    }

	    int total = gp.ui.controlActions.length;
	    int cols = 2;
	    int rows = (total + 1) / 2;

	    int index = gp.ui.menuControlsCommandNum;
	    int row = index / cols;
	    int col = index % cols;

	    if (code == gp.keyConfig.getKey(KeyConfig.UP)) {
	        if (gp.ui.menuControlsCommandNum == total) {
	            row = rows - 1;
	            col = 0;
	        } else {
	            row--;
	            if (row < 0) {
	                gp.ui.menuControlsCommandNum = total;
	                return;
	            }
	        }
	    }
	    if (code == gp.keyConfig.getKey(KeyConfig.DOWN)) {
	        row++;
	        if (row >= rows || (row * cols + col) >= total) {
	            gp.ui.menuControlsCommandNum = total;
	            return;
	        }
	    }
	    if (code == gp.keyConfig.getKey(KeyConfig.LEFT)) {
	        if (gp.ui.menuControlsCommandNum == total) return;
	        col--;
	        if (col < 0) col = cols - 1;
	    }
	    if (code == gp.keyConfig.getKey(KeyConfig.RIGHT)) {
	        if (gp.ui.menuControlsCommandNum == total) return;
	        col++;
	        if (col >= cols) col = 0;
	    }

	    int newIndex = row * cols + col;
	    if (newIndex >= total) newIndex = row * cols;
	    if (code == gp.keyConfig.getKey(KeyConfig.UP) ||
	        code == gp.keyConfig.getKey(KeyConfig.DOWN) ||
	        code == gp.keyConfig.getKey(KeyConfig.LEFT) ||
	        code == gp.keyConfig.getKey(KeyConfig.RIGHT)) {
	        gp.ui.menuControlsCommandNum = newIndex;
	    }

	    if (code == gp.keyConfig.getKey(KeyConfig.CHOOSE)) {
	        if (gp.ui.menuControlsCommandNum == total) {
	            gp.ui.titleScreenState = 3; // Go back to options/settings
	            gp.ui.menuControlsCommandNum = 0;
	        } else {
	            gp.ui.waitingForKey = true;
	            gp.ui.waitingAction = gp.ui.controlActions[gp.ui.menuControlsCommandNum];
	        }
	    }
	    if (code == gp.keyConfig.getKey(KeyConfig.ESCAPE)) {
	        gp.ui.titleScreenState = 3;
	        gp.ui.menuControlsCommandNum = 0;
	    }
	}
	
	public void chestState(int code) {
		if (code == KeyEvent.VK_ESCAPE) {
			gp.gameState = gp.playState; // Close chest and return to play state
			gp.player.currentChest = null; // Clear current chest reference
		}
		if (code == KeyEvent.VK_ENTER) {
			gp.player.selectChestItem(gp.ui.chestRow, gp.ui.chestCol);
		}
		if (code == KeyEvent.VK_UP && gp.ui.chestRow > 0) {
			gp.ui.chestRow--;
		}
		if (code == KeyEvent.VK_DOWN && gp.ui.chestRow < gp.ui.maxChestRow - 1) {
			gp.ui.chestRow++;
		}
		if (code == KeyEvent.VK_LEFT && gp.ui.chestCol > 0) {
			gp.ui.chestCol--;
		}
		if (code == KeyEvent.VK_RIGHT && gp.ui.chestCol < gp.ui.maxChestCol - 1) {
			gp.ui.chestCol++;
		}

	}

	public void skillsState(int code) {
		int unlockedCount = gp.player.unlockedSkills.size();

		if (!gp.ui.assigningSkill) {
			// Navigating slots (Q/W/E/R)
			if (code == gp.keyConfig.getKey(KeyConfig.LEFT)) {
				gp.ui.skillsCommandNum--;
				if (gp.ui.skillsCommandNum < 0) gp.ui.skillsCommandNum = 3;
			}
			if (code == gp.keyConfig.getKey(KeyConfig.RIGHT)) {
				gp.ui.skillsCommandNum++;
				if (gp.ui.skillsCommandNum > 3) gp.ui.skillsCommandNum = 0;
			}
			if (code == gp.keyConfig.getKey(KeyConfig.UP)) {
				gp.ui.skillsCommandNum = 3;
			}
			if (code == gp.keyConfig.getKey(KeyConfig.DOWN)) {
				if (unlockedCount > 0) {
					gp.ui.skillAssignSlotIndex = gp.ui.skillsCommandNum; // <--- Save selected slot
					gp.ui.skillsCommandNum = 4;
					gp.ui.assigningSkill = true;
				}
			}
			if (code == gp.keyConfig.getKey(KeyConfig.CHOOSE)) {
				if (unlockedCount > 0) {
					gp.ui.skillAssignSlotIndex = gp.ui.skillsCommandNum; // <--- Save selected slot
					gp.ui.assigningSkill = true;
					gp.ui.skillsCommandNum = 4; // First unlocked skill
				}
			}
			if (code == gp.keyConfig.getKey(KeyConfig.ESCAPE)) {
				gp.gameState = gp.playState;
			}
		} else {
			// Navigating unlocked skills
			if (code == gp.keyConfig.getKey(KeyConfig.UP)) {
				gp.ui.skillsCommandNum--;
				if (gp.ui.skillsCommandNum < 4) gp.ui.skillsCommandNum = 4 + unlockedCount - 1;
			}
			if (code == gp.keyConfig.getKey(KeyConfig.DOWN)) {
				gp.ui.skillsCommandNum++;
				if (gp.ui.skillsCommandNum >= 4 + unlockedCount) gp.ui.skillsCommandNum = 4;
			}
			if (code == gp.keyConfig.getKey(KeyConfig.LEFT) || code == gp.keyConfig.getKey(KeyConfig.RIGHT)) {
				gp.ui.assigningSkill = false;
				gp.ui.skillsCommandNum = gp.ui.skillAssignSlotIndex; // Restore slot selection
			}
			if (code == gp.keyConfig.getKey(KeyConfig.CHOOSE)) {
				int skillIndex = gp.ui.skillsCommandNum - 4;
				if (skillIndex >= 0 && skillIndex < unlockedCount) {
					int slotIndex = gp.ui.skillAssignSlotIndex; // Use the saved slot index!
					gp.player.assignSkillToKey(slotIndex, gp.player.unlockedSkills.get(skillIndex));
					gp.ui.assigningSkill = false;
					gp.ui.skillsCommandNum = slotIndex;
					gp.ui.addMessage(gp.ui.tr("skills.assigned", gp.player.unlockedSkills.get(skillIndex).getName(), "QWER".charAt(slotIndex) + ""));
				}
			}
			if (code == gp.keyConfig.getKey(KeyConfig.ESCAPE)) {
				gp.ui.assigningSkill = false;
				gp.ui.skillsCommandNum = gp.ui.skillAssignSlotIndex; // Restore slot selection
			}
		}
	}
}
