package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.reflect.Array;
import java.util.ArrayList;

import entity.Entity;
import skill.Skill;

public class KeyHandler implements KeyListener{
	
	GamePanel gp;
	public KeyHandler(GamePanel gp) {
		this.gp = gp;

	}
	
	public boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed, spacePressed, escPressed, flashPressed;
	public int selectedSkillTreeLevel = 0;
	public int selectedSkillTreeIndex = 0;

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
    	int code = e.getKeyCode();

		// Block menu keys in boss arena
		if (gp.player != null && gp.player.bossArenaActive) {
			if (code == gp.keyConfig.getKey(KeyConfig.INVENTORY) ||
				code == gp.keyConfig.getKey(KeyConfig.CHARACTER) ||
				code == gp.keyConfig.getKey(KeyConfig.SKILLS) ||
				code == gp.keyConfig.getKey(KeyConfig.ESCAPE)) {
				gp.ui.addMessage("You can't open menus in the boss arena!");
				return;
			}
		}

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
		else if (gp.gameState == gp.skillTreeState) {
			skillTreeState(code);
		}
		else if (gp.gameState == gp.saveLoadState) {
			saveLoadState(code);
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
					// Reset all save slots
				    for (int i = 1; i <= gp.ui.maxSaveSlots; i++) {
				        java.io.File saveFile = new java.io.File("save_slot" + i + ".dat");
				        if (saveFile.exists()) saveFile.delete();
				        java.io.File metaFile = new java.io.File("save_slot" + i + ".meta");
				        if (metaFile.exists()) metaFile.delete();
				    }
					gp.keyConfig.resetToDefault(); // Reset key bindings to default
					gp.config.saveConfig(); // Load the config to apply changes
					gp.ui.resetKeyboardCursor();
					gp.player.reset(); // Reset player state
					gp.resetEntities();
					gp.ui.titleScreenState = 1; 
				}
				if (gp.ui.commandNum == 1) {
					gp.ui.isSaving = false;
					gp.ui.saveSlotIndex = 0;
					gp.gameState = gp.saveLoadState;
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
					// Only accept if name is not null and not just spaces
					if (gp.player.name == null || gp.player.name.trim().isEmpty()) {
						gp.player.name = generateRandomName();
						return;
					}
					gp.player.name = gp.player.name.trim();
					gp.ui.typingName = false;
					gp.gameState = gp.playState;
					gp.ui.kbCol = 0; // Reset cursor position
					gp.ui.kbRow = 0; // Reset cursor position
				}
			}
			// Handle ESC key to go back to main menu
			if (code == gp.keyConfig.getKey(KeyConfig.ESCAPE)) {
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
		if (code == gp.keyConfig.getKey(KeyConfig.SKILL1)) {
		    gp.player.useAssignedSkill(0); 
		}
		if (code == gp.keyConfig.getKey(KeyConfig.SKILL2)) {
		    gp.player.useAssignedSkill(1);
		}
		if (code == gp.keyConfig.getKey(KeyConfig.SKILL3)) {
		    gp.player.useAssignedSkill(2);
		}
		if (code == gp.keyConfig.getKey(KeyConfig.SKILL4)) {
		    gp.player.useAssignedSkill(3);
		}
		if (code == gp.keyConfig.getKey(KeyConfig.SKILLS)){
			gp.gameState = gp.skillsState;
			gp.ui.skillsCommandNum = 0;
			gp.ui.assigningSkill = false;
			gp.ui.skillListScroll = 0; 
		}
		if (code == KeyEvent.VK_T) {
			gp.gameState = gp.skillTreeState;
		}
	}


	public void pauseState(int code){
		if (blockMenusInBossArena(code)) return;
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
					gp.ui.isSaving = true;
					gp.gameState = gp.saveLoadState;
					gp.ui.saveSlotIndex = 0;
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

	public void dialogueState(int code) {
		if (code == gp.keyConfig.getKey(KeyConfig.CHOOSE)) {
			for (Entity npc : gp.npc[gp.currentMap]) {
				if (npc != null && npc.dialogues != null && npc.dialogues.length > 0) {
					// If the next line is the last line, spawn weapons now
					if (npc.dialogues[gp.currentMap][npc.dialogIndex + 1] == null
						&& gp.currentMap == 0
						&& (!gp.player.hasTalkedToWeaponNPC || !areStartingWeaponsPresent())) {
						gp.player.hasTalkedToWeaponNPC = true;
						gp.aSetter.spawnStartingWeaponsAnimated();
					}
					if (npc.dialogues[gp.currentMap][npc.dialogIndex] != null) {
						npc.speak();
					} else {
						gp.gameState = gp.playState;
						npc.dialogIndex = 0;
						if (gp.player.pendingTeleport) {
							gp.eHandler.teleport(gp.player.pendingTeleportMap, gp.player.pendingTeleportX, gp.player.pendingTeleportY);
							gp.player.pendingTeleport = false;
						}
					}
					break;
				}
			}
		}
	}
	
	public void characterState(int code){
		if (blockMenusInBossArena(code)) return;
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

		int total = gp.ui.controlActions.length;
		int cols = 3;
		int rows = (int)Math.ceil((double)total / cols);

		int index = gp.ui.controlsCommandNum;
		int row = index % rows;
		int col = index / rows;

	    // Navigation for grid
	    if (gp.ui.controlsCommandNum < total) {
	        if (code == gp.keyConfig.getKey(KeyConfig.UP)) {
	            row--;
	            if (row < 0) {
	                // Move to "Back"
	                gp.ui.controlsCommandNum = total;
	                return;
	            }
	        }
	        if (code == gp.keyConfig.getKey(KeyConfig.DOWN)) {
	            row++;
	            if (row >= rows || (col * rows + row) >= total) {
	                // Move to "Back"
	                gp.ui.controlsCommandNum = total;
	                return;
	            }
	        }
	        if (code == gp.keyConfig.getKey(KeyConfig.LEFT)) {
	            col--;
	            if (col < 0) col = cols - 1;
	            if (col * rows + row >= total) col = (total - 1) / rows;
	        }
	        if (code == gp.keyConfig.getKey(KeyConfig.RIGHT)) {
	            col++;
	            if (col >= cols || (col * rows + row) >= total) col = 0;
	        }
	        int newIndex = col * rows + row;
	        if (newIndex >= total) newIndex = total; // If out of bounds, select "Back"
	        if (code == gp.keyConfig.getKey(KeyConfig.UP) ||
	            code == gp.keyConfig.getKey(KeyConfig.DOWN) ||
	            code == gp.keyConfig.getKey(KeyConfig.LEFT) ||
	            code == gp.keyConfig.getKey(KeyConfig.RIGHT)) {
	            gp.ui.controlsCommandNum = newIndex;
	        }
	    } else {
	        // Navigation for Back and Reset
	        if (code == gp.keyConfig.getKey(KeyConfig.LEFT) || code == gp.keyConfig.getKey(KeyConfig.RIGHT)) {
	            // Toggle between Back and Reset
	            gp.ui.controlsCommandNum = (gp.ui.controlsCommandNum == total) ? total + 1 : total;
	            return;
	        }
	        if (code == gp.keyConfig.getKey(KeyConfig.UP)) {
	            // Move to last row, first or last column
	            gp.ui.controlsCommandNum = (gp.ui.controlsCommandNum == total)
	                ? (rows - 1)
	                : (rows * 2 - 1);
	            if (gp.ui.controlsCommandNum >= total) gp.ui.controlsCommandNum = total - 1;
	            return;
	        }
	    }

	    // Handle Enter/Choose for rebinding, back, or reset
	    if (code == gp.keyConfig.getKey(KeyConfig.CHOOSE)) {
	        if (gp.ui.controlsCommandNum == total) {
	            // "Back" selected
	            gp.config.saveConfig();
	            gp.ui.subState = 0;
	        } else if (gp.ui.controlsCommandNum == total + 1) {
	            // "Reset" selected
	            gp.keyConfig.resetToDefault();
	            gp.config.saveConfig();
	            gp.ui.addMessage(gp.ui.tr("controls.reset_success"));
	        } else {
	            // Start rebinding
	            gp.ui.waitingForKey = true;
	            gp.ui.waitingAction = gp.ui.controlActions[gp.ui.controlsCommandNum];
	        }
	    }
	}

	public void inventoryState(int code){
		if (blockMenusInBossArena(code)) return;
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

		if (code == gp.keyConfig.getKey(KeyConfig.QUICK_USE)) {
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
		// 	"Alex", "Riley", "Morgan", "Casey", "Jordan", "Taylor", "Skyler", "Jamie", "Avery", "Quinn", "TrungTT",
        // "Harper", "Rowan", "Dakota", "Emerson", "Finley", "Oner", "Peyton", "Reese", "Sawyer", "Sage",
        // "Charlie", "Elliot", "Jules", "Kai", "Logan", "Milan", "Noel", "Phoenix", "River", "Shiloh",
        // "Blake", "Cameron", "Drew", "Frankie", "Jesse", "Kendall", "Lane", "Doran", "Parker", "Remy",
        // "Sam", "Tatum", "Val", "Wren", "Zion", "Aspen", "Briar", "Cory", "Devon", "Keria", "Gray",
        // "Indigo", "Jaden", "Kieran", "Faker", "Micah", "Oakley", "Perry", "Quincy", "Robin", "Sasha"
		"TrungTT"
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
		int cols = 3;
		int rows = (int)Math.ceil((double)total / cols);

		int index = gp.ui.menuControlsCommandNum;
		int row = index % rows;
		int col = index / rows;

	    // Navigation for grid
	    if (gp.ui.menuControlsCommandNum < total) {
	        if (code == gp.keyConfig.getKey(KeyConfig.UP)) {
	            row--;
	            if (row < 0) {
	                // Move to "Back"
	                gp.ui.menuControlsCommandNum = total;
	                return;
	            }
	        }
	        if (code == gp.keyConfig.getKey(KeyConfig.DOWN)) {
	            row++;
	            if (row >= rows || (col * rows + row) >= total) {
	                // Move to "Back"
	                gp.ui.menuControlsCommandNum = total;
	                return;
	            }
	        }
	        if (code == gp.keyConfig.getKey(KeyConfig.LEFT)) {
	            col--;
	            if (col < 0) col = cols - 1;
	            if (col * rows + row >= total) col = (total - 1) / rows;
	        }
	        if (code == gp.keyConfig.getKey(KeyConfig.RIGHT)) {
	            col++;
	            if (col >= cols || (col * rows + row) >= total) col = 0;
	        }
	        int newIndex = col * rows + row;
	        if (newIndex >= total) newIndex = total; // If out of bounds, select "Back"
	        if (code == gp.keyConfig.getKey(KeyConfig.UP) ||
	            code == gp.keyConfig.getKey(KeyConfig.DOWN) ||
	            code == gp.keyConfig.getKey(KeyConfig.LEFT) ||
	            code == gp.keyConfig.getKey(KeyConfig.RIGHT)) {
	            gp.ui.menuControlsCommandNum = newIndex;
	        }
	    } else {
	        // Navigation for Back and Reset
	        if (code == gp.keyConfig.getKey(KeyConfig.LEFT) || code == gp.keyConfig.getKey(KeyConfig.RIGHT)) {
	            // Toggle between Back and Reset
	            gp.ui.menuControlsCommandNum = (gp.ui.menuControlsCommandNum == total) ? total + 1 : total;
	            return;
	        }
	        if (code == gp.keyConfig.getKey(KeyConfig.UP)) {
	            // Move to last row, first or last column
	            gp.ui.menuControlsCommandNum = (gp.ui.menuControlsCommandNum == total)
	                ? (rows - 1)
	                : (rows * 2 - 1);
	            if (gp.ui.menuControlsCommandNum >= total) gp.ui.menuControlsCommandNum = total - 1;
	            return;
	        }
	    }

	    // Handle Enter/Choose for rebinding, back, or reset
	    if (code == gp.keyConfig.getKey(KeyConfig.CHOOSE)) {
	        if (gp.ui.menuControlsCommandNum == total) {
	            // "Back" selected
	            gp.config.saveConfig();
	            gp.ui.titleScreenState = 3;
	            gp.ui.menuControlsCommandNum = 0;
	        } else if (gp.ui.menuControlsCommandNum == total + 1) {
	            // "Reset" selected
	            gp.keyConfig.resetToDefault();
	            gp.config.saveConfig();
	            gp.ui.addMessage(gp.ui.tr("controls.reset_success"));
	        } else {
	            // Start rebinding
	            gp.ui.waitingForKey = true;
	            gp.ui.waitingAction = gp.ui.controlActions[gp.ui.menuControlsCommandNum];
	        }
	    }
	    if (code == gp.keyConfig.getKey(KeyConfig.ESCAPE)) {
	        gp.config.saveConfig();
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
		if (blockMenusInBossArena(code)) return;
		ArrayList<skill.Skill> unlockedSkills = (ArrayList<Skill>) gp.player.getAllUnlockedSkills();
		int unlockedCount = unlockedSkills.size();

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
			if (code == gp.keyConfig.getKey(KeyConfig.SKILLSTREE)){
				gp.gameState = gp.skillTreeState;
				gp.ui.assigningSkill = false; // Reset assigning state
				gp.ui.skillsCommandNum = 0; // Reset command number
			}
		} else {
			// Navigating unlocked skills
			if (code == gp.keyConfig.getKey(KeyConfig.UP)) {
				gp.ui.skillsCommandNum--;
				if (gp.ui.skillsCommandNum < 4) gp.ui.skillsCommandNum = 4 + unlockedCount - 1;
				// Scroll up if needed
				if (gp.ui.skillsCommandNum - 4 < gp.ui.skillListScroll) gp.ui.skillListScroll = gp.ui.skillsCommandNum - 4;
			}
			if (code == gp.keyConfig.getKey(KeyConfig.DOWN)) {
				gp.ui.skillsCommandNum++;
				if (gp.ui.skillsCommandNum >= 4 + unlockedCount) gp.ui.skillsCommandNum = 4;
				// Scroll down if needed
				if (gp.ui.skillsCommandNum - 4 >= gp.ui.skillListScroll + gp.ui.maxVisibleSkills)
					gp.ui.skillListScroll = gp.ui.skillsCommandNum - 4 - gp.ui.maxVisibleSkills + 1;
			}
			if (code == gp.keyConfig.getKey(KeyConfig.LEFT) || code == gp.keyConfig.getKey(KeyConfig.RIGHT)) {
				gp.ui.assigningSkill = false;
				gp.ui.skillsCommandNum = gp.ui.skillAssignSlotIndex; // Restore slot selection
			}
			if (code == gp.keyConfig.getKey(KeyConfig.CHOOSE)) {
				int skillIndex = gp.ui.skillsCommandNum - 4;
				if (skillIndex >= 0 && skillIndex < unlockedCount) {
					int slotIndex = gp.ui.skillAssignSlotIndex;
					gp.player.assignSkillToKey(slotIndex, unlockedSkills.get(skillIndex));
					gp.ui.assigningSkill = false;
					gp.ui.skillsCommandNum = slotIndex;
					gp.ui.addMessage(gp.ui.tr("skills.assigned", unlockedSkills.get(skillIndex).getName(this.gp), "1234".charAt(slotIndex) + ""));
				}
			}
			if (code == gp.keyConfig.getKey(KeyConfig.ESCAPE)) {
				gp.ui.assigningSkill = false;
				gp.ui.skillsCommandNum = gp.ui.skillAssignSlotIndex; // Restore slot selection
			}
		}
	}

	public void skillTreeState(int code) {
	    // Example navigation: up/down to move, enter to unlock
	    if (code == gp.keyConfig.getKey(KeyConfig.UP)) selectedSkillTreeLevel--;
	    if (code == gp.keyConfig.getKey(KeyConfig.DOWN)) selectedSkillTreeLevel++;
	    if (code == gp.keyConfig.getKey(KeyConfig.CHOOSE)) {
	        // Find selected node and unlock if possible
	        skill.SkillTreeNode node = getSelectedSkillTreeNode();
	        if (node != null && !node.unlocked && node.parent != null && node.parent.unlocked && gp.player.skillPoints >= node.requiredPoints) {
	            node.unlocked = true;
	            gp.player.skillPoints -= node.requiredPoints;
	            gp.player.unlockedSkills.add(node.skill);
	            gp.ui.addMessage("Unlocked: " + node.skill.getName(gp));
	        }
	    }
		if (code == gp.keyConfig.getKey(KeyConfig.SKILLS)) {
			gp.gameState = gp.skillsState; // Exit skill tree to skills menu
			gp.ui.assigningSkill = false; // Reset assigning state
			gp.ui.skillsCommandNum = 0; // Reset command number
			gp.ui.skillListScroll = 0; // Reset scroll position
			selectedSkillTreeLevel = 0; // Reset skill tree level
		}
		if (code == gp.keyConfig.getKey(KeyConfig.ESCAPE)) {
	        gp.gameState = gp.playState; // Exit skill tree
	    }	
	}

	private skill.SkillTreeNode getSelectedSkillTreeNode() {
	    // Traverse tree based on selectedSkillTreeLevel/Index (implement as needed)
	    // For a simple vertical tree:
	    skill.SkillTreeNode node = gp.player.skillTreeRoot;
	    for (int i = 0; i < selectedSkillTreeLevel; i++) {
	        if (node.children.size() > 0) node = node.children.get(0);
	        else return null;
	    }
	    return node;
	}

	private boolean areStartingWeaponsPresent() {
		int map = 0;
		for (Entity obj : gp.obj[map]) {
			if (obj != null && (
				obj instanceof item.ITEM_Sword_Normal ||
				obj instanceof item.ITEM_Axe_Normal ||
				obj instanceof item.ITEM_Bow_Normal ||
				obj instanceof item.ITEM_Staff_Normal ||
				obj instanceof item.ITEM_Spear_Normal
			)) {
				return true;
			}
		}
		return false;
	}

	private boolean blockMenusInBossArena(int code) {
        if (gp.player != null && gp.player.bossArenaActive) {
            // Block inventory, character, pause, escape, skills, etc.
            if (
                code == gp.keyConfig.getKey(KeyConfig.INVENTORY) ||
                code == gp.keyConfig.getKey(KeyConfig.CHARACTER) ||
                code == gp.keyConfig.getKey(KeyConfig.ESCAPE) ||
                code == gp.keyConfig.getKey(KeyConfig.SKILLS)
            ) {
                gp.ui.addMessage("You can't open menus in the boss arena!");
                return true;
            }
        }
        return false;
    }

	public void saveLoadState(int code) {
	    if (code == KeyEvent.VK_UP) {
	        gp.ui.saveSlotIndex = (gp.ui.saveSlotIndex + gp.ui.maxSaveSlots - 1) % gp.ui.maxSaveSlots;
	    }
	    if (code == KeyEvent.VK_DOWN) {
	        gp.ui.saveSlotIndex = (gp.ui.saveSlotIndex + 1) % gp.ui.maxSaveSlots;
	    }
	    if (code == KeyEvent.VK_ENTER) {
	        if (gp.ui.isSaving) {
	            gp.config.savePlayer(gp.player, gp.ui.saveSlotIndex + 1);
	            gp.ui.addMessage("Game saved to slot " + (gp.ui.saveSlotIndex + 1));
	        } else {
	            gp.config.loadPlayer(gp.player, gp.ui.saveSlotIndex + 1);
	            gp.ui.addMessage("Game loaded from slot " + (gp.ui.saveSlotIndex + 1));
	        }
	        gp.gameState = gp.playState;
	    }
	    if (code == KeyEvent.VK_ESCAPE) {
	        gp.gameState = gp.playState;
	    }
	}
	
	public void bossDeadState(int code) {
	    if (code == gp.keyConfig.getKey(KeyConfig.CHOOSE) || code == KeyEvent.VK_ENTER) {
	        // Reset player state
	        gp.player.reset();
	        // Reset all entities (monsters, NPCs, objects, etc.)
	        gp.resetEntities();
	        // Reset save/load slot selection and menu state
	        gp.ui.saveSlotIndex = 0;
	        gp.ui.commandNum = 0;
	        gp.ui.titleScreenState = 0;
	        // Optionally reset other game variables here

	        // Return to main menu
	        gp.gameState = gp.titleState;
	    }
	}
}
