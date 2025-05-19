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
					//options
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
				} else if (key.equals("OK")) {
					gp.ui.typingName = false;
					gp.gameState = gp.playState;
					gp.player.name = gp.player.name.trim();
					// Proceed to next screen or save name
				} else if (gp.player.name.length() < 12) { // Limit name length
					gp.player.name += key;
				}
			}
			return; // Prevent further processing if on name input
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
					break;
				case 1: 
					gp.player.maxMana += 50; 
					gp.player.mana += 50;
					break;
				case 2: gp.player.attack += 10; break;
				case 3: gp.player.defense += 10; break;
			}
			gp.player.progressionPoints--;
		}
		if (code == KeyEvent.VK_C) {
			gp.gameState = gp.playState;
		}
		if (code == KeyEvent.VK_R) {
			int defaultHealth = 1800;
			int defaultMana = 400;
			int defaultAttack = 50;
			int defaultDefense = 10;

			int healthBonus = 0, manaBonus = 0, attackBonus = 0, defenseBonus = 0;
			if (gp.player.currentWeapon != null) {
				healthBonus += gp.player.currentWeapon.healthBonus;
				manaBonus += gp.player.currentWeapon.manaBonus;
				attackBonus += gp.player.currentWeapon.attackBonus;
				defenseBonus += gp.player.currentWeapon.defenseBonus;
			}
			if (gp.player.currentArmor != null) {
				healthBonus += gp.player.currentArmor.healthBonus;
				manaBonus += gp.player.currentArmor.manaBonus;
				attackBonus += gp.player.currentArmor.attackBonus;
				defenseBonus += gp.player.currentArmor.defenseBonus;
			}
			if (gp.player.currentHat != null) {
				healthBonus += gp.player.currentHat.healthBonus;
				manaBonus += gp.player.currentHat.manaBonus;
				attackBonus += gp.player.currentHat.attackBonus;
				defenseBonus += gp.player.currentHat.defenseBonus;
			}
			if (gp.player.currentBoots != null) {
				healthBonus += gp.player.currentBoots.healthBonus;
				manaBonus += gp.player.currentBoots.manaBonus;
				attackBonus += gp.player.currentBoots.attackBonus;
				defenseBonus += gp.player.currentBoots.defenseBonus;
			}

			int spentHealth = Math.max(0, ((int)gp.player.maxHealth - healthBonus - defaultHealth) / 100);
			int spentMana = Math.max(0, ((int)gp.player.maxMana - manaBonus - defaultMana) / 50);
			int spentAttack = Math.max(0, ((int)gp.player.attack - attackBonus - defaultAttack) / 10);
			int spentDefense = Math.max(0, ((int)gp.player.defense - defenseBonus - defaultDefense) / 10);

			boolean spent = (gp.player.maxHealth - healthBonus) > defaultHealth ||
							(gp.player.maxMana - manaBonus) > defaultMana ||
							(gp.player.attack - attackBonus) > defaultAttack ||
							(gp.player.defense - defenseBonus) > defaultDefense;

			if (spent) {
				int totalSpent = 0;
				if (gp.player.health - spentHealth * 100 > 0) {
					totalSpent += (gp.player.maxHealth - defaultHealth - healthBonus) / 100;
					gp.player.maxHealth = defaultHealth + healthBonus;
					gp.player.health -= spentHealth * 100;
				}
				if (gp.player.mana - spentMana * 50 > 0) {
					totalSpent += (gp.player.maxMana - defaultMana - manaBonus) / 50;
					gp.player.maxMana = defaultMana + manaBonus;
					gp.player.mana -= spentMana * 50;
				}
				totalSpent += (gp.player.attack - defaultAttack - attackBonus) / 10;
				gp.player.attack = defaultAttack + attackBonus;
				totalSpent += (gp.player.defense - defaultDefense - defenseBonus) / 10;
				gp.player.defense = defaultDefense + defenseBonus;
				gp.player.progressionPoints += totalSpent;
			}
		}
	}

	public void optionsState(int code){

	}

	public void inventoryState(int code){
		if (code == KeyEvent.VK_I){
			gp.gameState = gp.playState;
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

}
