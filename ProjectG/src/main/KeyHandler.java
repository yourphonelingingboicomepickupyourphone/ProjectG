package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener{
	
	GamePanel gp;
	public KeyHandler(GamePanel gp) {
		this.gp = gp;

	}
	
	public boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed, spacePressed, escPressed;

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();

		if (gp.gameState == gp.titleState) {
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


	}

	public void titleState(int code){
		if (gp.ui.titleScreenState == 0) {
			if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
				gp.ui.commandNum--;
				if (gp.ui.commandNum < 0) {
					gp.ui.commandNum = 3;
				}
			}
				
			if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
				gp.ui.commandNum++;
				if (gp.ui.commandNum > 3) {
					gp.ui.commandNum = 0;
				}
			}
		
			if (code == KeyEvent.VK_ENTER) {
				if (gp.ui.commandNum == 0) {
					gp.ui.titleScreenState = 1; 
				}
				if (gp.ui.commandNum == 1) {
					//load game

				}
				if (gp.ui.commandNum == 2) {
					gp.ui.commandNum = 0; // Reset cursor to first option
					gp.ui.titleScreenState = 3; // Show settings menu
				}	
				if (gp.ui.commandNum == 3) {
					System.exit(0);
				}
			}
			
		}
			
		else if (gp.ui.titleScreenState == 1) {
			if (code == KeyEvent.VK_UP) {
				gp.ui.kbRow = Math.max(0, gp.ui.kbRow - 1);
				gp.ui.kbCol = Math.min(gp.ui.kbCol, gp.ui.keyboard[gp.ui.kbRow].length - 1);
			} else if (code == KeyEvent.VK_DOWN) {
				gp.ui.kbRow = Math.min(gp.ui.keyboard.length - 1, gp.ui.kbRow + 1);
				gp.ui.kbCol = Math.min(gp.ui.kbCol, gp.ui.keyboard[gp.ui.kbRow].length - 1);
			} else if (code == KeyEvent.VK_LEFT) {
				gp.ui.kbCol = Math.max(0, gp.ui.kbCol - 1);
			} else if (code == KeyEvent.VK_RIGHT) {
				gp.ui.kbCol = Math.min(gp.ui.keyboard[gp.ui.kbRow].length - 1, gp.ui.kbCol + 1);
			} else if (code == KeyEvent.VK_ENTER) {
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
			} else if (code == KeyEvent.VK_ESCAPE) {
				gp.ui.typingName = false;
				gp.ui.titleScreenState = 0; // Go back to main menu
			}
			return; // Prevent further processing if on name input
		}
		else if (gp.ui.titleScreenState == 3) {
			if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
				gp.ui.commandNum--;
				if (gp.ui.commandNum < 0) gp.ui.commandNum = 5; // 6 options: 0-5
			}
			if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
				gp.ui.commandNum++;
				if (gp.ui.commandNum > 5) gp.ui.commandNum = 0;
			}
			if (code == KeyEvent.VK_ENTER) {
				// Handle selection based on gp.ui.commandNum
				// 0: Music Volume, 1: SFX Volume, 2: Fullscreen, 3: Back
				if (gp.ui.commandNum == 5) {
					gp.ui.titleScreenState = 0; // Go back to main menu
				}
				// Add your logic for other options here
			}
		}
		
		
		else if (gp.ui.titleScreenState == 4) {
			if (code == KeyEvent.VK_ENTER) {
				gp.gameState = gp.playState;
				gp.ui.titleScreenState = 0; // Go back to main menu
			}
		}
		
		else if (gp.ui.titleScreenState == 5) {
			if (code == KeyEvent.VK_ENTER) {
				gp.gameState = gp.playState;
				gp.ui.titleScreenState = 0; // Go back to main menu
			}
		}
		
		else if (gp.ui.titleScreenState == 6) {
			if (code == KeyEvent.VK_ENTER) {
				gp.gameState = gp.playState;
				gp.ui.titleScreenState = 0; // Go back to main menu
			}
		}

	}

	public void playState(int code){
		if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
			upPressed = true;
		}
		
		if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
			downPressed = true;
		}

		if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
			leftPressed = true;
		}
	
		if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
			rightPressed = true;
		}
	
		if (code == KeyEvent.VK_ESCAPE) {
			gp.gameState = gp.pauseState;
		}

		if (code == KeyEvent.VK_ENTER) {
			enterPressed = true;
		}
		if (code == KeyEvent.VK_SPACE) {
			spacePressed = true;
		}
		if (code == KeyEvent.VK_C) {
			gp.gameState = gp.characterState;
		}

		if (code == KeyEvent.VK_I){
			gp.gameState = gp.inventoryState;
		}
	}


	public void pauseState(int code){
			
		if (code == KeyEvent.VK_ESCAPE) {
			gp.gameState = gp.playState;
		}
		
		if (KeyEvent.VK_W == code || KeyEvent.VK_UP == code) {
			gp.ui.pauseCommandNum--;
			if (gp.ui.pauseCommandNum < 0) gp.ui.pauseCommandNum = 3;
		}
		if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
			gp.ui.pauseCommandNum++;
			if (gp.ui.pauseCommandNum > 3) gp.ui.pauseCommandNum = 0;
		}
		if (code == KeyEvent.VK_ENTER) {
			switch (gp.ui.pauseCommandNum) {
				case 0: gp.gameState = gp.playState; break; // Continue
				case 1: gp.gameState = gp.optionsState; break; // Settings
				// case 2: saveGame(); break; // Save
				case 3: System.exit(0); break; // Exit
			}
		}
	}

	public void dialogueState(int code){
		if (code == KeyEvent.VK_ENTER) {
			gp.gameState = gp.playState;
		}
	}
	
	public void characterState(int code){
		if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
			UI.progressionSelectIndex--;
			if (UI.progressionSelectIndex < 0) UI.progressionSelectIndex = 3;
		}
		if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
			UI.progressionSelectIndex++;
			if (UI.progressionSelectIndex > 3) UI.progressionSelectIndex = 0;
		}
		if (code == KeyEvent.VK_ENTER && gp.player.progressionPoints > 0) {
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
		if (code == KeyEvent.VK_C) {
			gp.gameState = gp.playState;
		}
		if (code == KeyEvent.VK_R) {
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
		if (code == KeyEvent.VK_I) {
			gp.gameState = gp.inventoryState;
		}
	}

	public void optionsState(int code){
		if (code == KeyEvent.VK_ESCAPE) {
			gp.gameState = gp.pauseState;
		}
		
		if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
			gp.ui.commandNum--;
			if (gp.ui.commandNum < 0) gp.ui.commandNum = 3; // 4 options: 0-3
		}
		if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
			gp.ui.commandNum++;
			if (gp.ui.commandNum > 3) gp.ui.commandNum = 0;
		}
		if (code == KeyEvent.VK_ENTER) {
			switch (gp.ui.commandNum) {
				case 0: // BGM
					break;
				case 1: // SFX
					break;
				case 2: // Controls
					break;
				case 3: // Back
					gp.gameState = gp.pauseState; 
					break;
			}
		}

	}

	public void inventoryState(int code){
		if (code == KeyEvent.VK_I){
			gp.gameState = gp.playState;
		}
		if (code == KeyEvent.VK_C) {
			gp.gameState = gp.characterState;
		}
		if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
			if (gp.ui.slotCol == 0) {
				gp.ui.slotCol = gp.ui.maxInventoryCol - 1;
			} else {
				gp.ui.slotCol--;
			}
		}
		if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
			if (gp.ui.slotCol == gp.ui.maxInventoryCol - 1) {
				gp.ui.slotCol = 0;
			} else {
				gp.ui.slotCol++;
			}
		}
		if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
			if (gp.ui.slotRow == 0) {
				gp.ui.slotRow = gp.ui.maxInventoryRow - 1;
			} else {
				gp.ui.slotRow--;
			}
		}
		if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
			if (gp.ui.slotRow == gp.ui.maxInventoryRow - 1) {
				gp.ui.slotRow = 0;
			} else {
				gp.ui.slotRow++;
				
			}
		}
		if (code == KeyEvent.VK_ENTER) {
			gp.player.selectItem(0); // The argument is not used, so 0 is fine
		}
		if (code == KeyEvent.VK_R){
			gp.player.disposeSelectedItem();
		}
	}
	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		
		if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
			upPressed = false;
		}
		
		if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
			downPressed = false;
		}

		if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
			leftPressed = false;
		}

		if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
			rightPressed = false;
		}

		if (code == KeyEvent.VK_ENTER) {
			enterPressed = false;
		}
		if (code == KeyEvent.VK_SPACE) {
			spacePressed = false;
		}
		if (code == KeyEvent.VK_ESCAPE) {
			escPressed = false;
		}
	}

	private String generateRandomName() {
	    String[] names = {"Alex", "Riley", "Morgan", "Casey", "Jordan", "Taylor", "Skyler", "Jamie", "Avery", "Quinn"};
	    return names[(int)(Math.random() * names.length)];
	}
}
