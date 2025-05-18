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
			if (code == KeyEvent.VK_ENTER) {
				gp.gameState = gp.playState;
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
					gp.player.health = gp.player.maxHealth;
					break;
				case 1: 
					gp.player.maxMana += 50; 
					gp.player.mana = gp.player.maxMana;
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
			// Default stat values (adjust if your defaults change)
			int defaultHealth = 1800;
			int defaultMana = 400;
			int defaultAttack = 50;
			int defaultDefense = 10;

			// Subtract equipment bonuses if equipped
			int weaponHealthBonus = gp.player.currentWeapon != null ? gp.player.currentWeapon.healthBonus : 0;
			int weaponManaBonus = gp.player.currentWeapon != null ? gp.player.currentWeapon.manaBonus : 0;
			int weaponAttackBonus = gp.player.currentWeapon != null ? gp.player.currentWeapon.attackBonus : 0;
			int weaponDefenseBonus = gp.player.currentWeapon != null ? gp.player.currentWeapon.defenseBonus : 0;

			int spentHealth = Math.max(0, ((int)gp.player.maxHealth - weaponHealthBonus - defaultHealth) / 100);
			int spentMana = Math.max(0, ((int)gp.player.maxMana - weaponManaBonus - defaultMana) / 50);
			int spentAttack = Math.max(0, ((int)gp.player.attack - weaponAttackBonus - defaultAttack) / 10);
			int spentDefense = Math.max(0, ((int)gp.player.defense - weaponDefenseBonus - defaultDefense) / 10);
			int totalSpent = spentHealth + spentMana + spentAttack + spentDefense;

			// Only reset if any stat is above default (excluding equipment bonuses)
			boolean spent = gp.player.maxHealth > defaultHealth ||
							gp.player.maxMana > defaultMana ||
							(gp.player.attack - weaponAttackBonus) > defaultAttack ||
							(gp.player.defense - weaponDefenseBonus) > defaultDefense;

			if (spent) {
				gp.player.maxHealth = defaultHealth;
				gp.player.health = gp.player.maxHealth;
				gp.player.maxMana = defaultMana;
				gp.player.mana = gp.player.maxMana;
				gp.player.attack = defaultAttack + weaponAttackBonus;
				gp.player.defense = defaultDefense + weaponDefenseBonus;
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
