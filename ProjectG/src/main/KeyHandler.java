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
					gp.player.maxHealth = gp.player.maxHealth + 100;
					gp.player.health = gp.player.maxHealth;
					break;
				case 1: 
					gp.player.maxMana = gp.player.maxMana + 50; 
					gp.player.mana = gp.player.maxMana;
					break;
				case 2: gp.player.attack = gp.player.attack + 10; break;
				case 3: gp.player.defense = gp.player.defense + 10; break;
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

            if (gp.player.totalProgressionPoints > 0) {
                gp.player.maxHealth = defaultHealth;
                gp.player.health = gp.player.maxHealth;
                gp.player.maxMana = defaultMana;
                gp.player.mana = gp.player.maxMana;
                gp.player.attack = defaultAttack;
                gp.player.defense = defaultDefense;
                gp.player.progressionPoints += gp.player.totalProgressionPoints;
            }
        }
	}

	public void optionsState(int code){

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
	}

}
