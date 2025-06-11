package main;

import java.awt.Color;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JPanel;

import ai.PathFinder;
// import data.DataStorage;
import entity.Entity;
import entity.Player;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable{
	private static final int FPS = 60;
	//Settings
	final int originalTileSize = 16;	//1 tile originally has size of 16x16
	final int scale = 5;	//The scale by which a tile is enlarged
	
	public final int tileSize = originalTileSize * scale;	//The tile on the game screen will be displayed by 80x80 pixels
	public final int maxScreenCol = 24;
	public final int maxScreenRow = 13;
	public int screenWidth = tileSize * maxScreenCol;
	public int screenHeight = tileSize * maxScreenRow;	//1920x1040
	
	//World Settings
	public final int maxWorldCol = 100;
	public final int maxWorldRow = 100;
	public final int maxMap = 10;	//maximum number of maps
	public int currentMap = 0;	//current map index, start from 0
	public final int worldWidth = maxWorldCol * tileSize;
	public final int worldHeight = maxWorldRow * tileSize;

	public KeyConfig keyConfig = new KeyConfig();	//Key configuration
	
	
	//System Settings
	public TileManager tileM = new TileManager(this);
	public KeyHandler keyH = new KeyHandler(this);
	public CollisionChecker cChecker = new CollisionChecker(this);
	public AssetSetter aSetter = new AssetSetter(this);
	public UI ui = new UI(this);
	public EventHandler eHandler = new EventHandler(this);
	public Config config;
	public PathFinder pFinder = new PathFinder(this);

	Thread gameThread;
	
	//Entity & Object & NPC
	public Player player = new Player(this, keyH);
	public Entity obj[][] = new Entity[maxMap][15];	//number of objects
	public Entity npc[][] = new Entity[maxMap][10];	//number of NPCs 
	public Entity monster[][] = new Entity[maxMap][75];	//number of monsters	
	public ArrayList<Entity> entityList = new ArrayList<>();
	public ArrayList<Entity> projectileList = new ArrayList<>();
	
	//Game State, indicate pause or play
	public int gameState;
	public final int titleState = 0;	
	public final int playState = 1;
	public final int pauseState = 2;
	public final int dialogueState = 3;
	public final int characterState = 4;
	public final int distributionState = 5;
	public final int inventoryState = 6;
	public final int optionsState = 7;
	public final int chestState = 8;
	public final int gameOverState = 9;
	public final int deathState = 10;
	public final int transitionState = 11;	//Transition state for map change
	public final int skillsState = 12;	
	public final int skillTreeState = 13; //Skill tree state

	public boolean debugMode = false;	//Debug mode, true to enable debug features

	public final int baseWidth = 1920;
	public final int baseHeight = 1040;

	public GamePanel() {
		config = new Config(this);
		config.loadConfig();
		ui.loadLanguage(); // Load language settings

		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);	//all the drawing will be done in an offscreen painting buffer, improve performance
		this.addKeyListener(keyH);
		this.setFocusable(true);	//GamePanel can be focused to receive key input
	}
	
	
	public void setupGame() {

		// Initialize all sub-arrays for all maps
		for (int map = 0; map < maxMap; map++) {
			npc[map] = new Entity[10];      // or your actual NPC array size
			monster[map] = new Entity[75];  // or your actual monster array size
			obj[map] = new Entity[15];      // or your actual object array size
		}

		// Now safe to clear and set entities
		for (int i = 0; i < npc.length; i++) {
			for (int j = 0; j < npc[i].length; j++) {
				npc[i][j] = null;
			}
		}
		for (int i = 0; i < monster[currentMap].length; i++) {
			Entity m = monster[currentMap][i];
			if (m != null) {
				m.update();
				if (!m.alive) {
					monster[currentMap][i] = null;
				}
			}
		}
		for (int i = 0; i < obj.length; i++) {
			for (int j = 0; j < obj[i].length; j++) {
				obj[i][j] = null;
			}
		}
		
		aSetter.setObject();
		aSetter.setNPC();
		aSetter.setMonster();
	
		player.renderLayer = 2; // Player in the middle layer

		gameState = titleState;	//start with title screen
		
	}

	public void startGameThread() {
		gameThread = new Thread(this);	//passing GamePanel to this thread
		gameThread.start();
	}
	
	@Override
	public void run() {
		
		double drawInterval = 1000000000/FPS;
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		long timer = 0;
		while (gameThread != null) {
			
			
			currentTime = System.nanoTime();
			delta += (currentTime - lastTime) / drawInterval;
			timer += currentTime - lastTime;
			lastTime = currentTime;
			
			if (delta >= 1) {
				update();
				repaint();
				delta--;
			}
			
			
			if (timer > 1000000000) {
				timer = 0;
			}
		}
	}
	public void update() {
		
		if (gameState == playState) {
			player.update();	//player update

			for(int i = 0; i < npc.length; i++) {
				if(npc[currentMap][i] != null) {
					npc[currentMap][i].update();
				}
			}	//NPC update

			for(int i = 0; i < monster.length; i++) {
				if(monster[currentMap][i] != null) {
					if(monster[currentMap][i].alive == true && monster[currentMap][i].dying == false) {
						monster[currentMap][i].update();
					}
					if (monster[currentMap][i].alive == false) {
						monster[currentMap][i] = null;
					}
				}
			}	//Monster update

			for (int i = 0; i < projectileList.size(); i++) {
				if (projectileList.get(i) != null) {
					if (projectileList.get(i).alive == true) {
						projectileList.get(i).update();
					}
					if (projectileList.get(i).alive == false) {
						projectileList.remove(i);
						i--;
					}
				}
			}	//Projectile update

		}

		else if (gameState == deathState) {
			player.deathAnimCounter++;
			if (player.deathAnimCounter >= player.DEATH_ANIM_DURATION) {
				gameState = gameOverState;	//Switch to game over state after death animation
			}

		}
		
		if (aSetter.weaponSpawnAnimating) {
            aSetter.weaponSpawnAnimTimer++;
            if (aSetter.weaponSpawnAnimTimer >= aSetter.WEAPON_SPAWN_ANIM_DELAY) {
                aSetter.weaponSpawnAnimTimer = 0;
                if (aSetter.weaponSpawnAnimIndex < aSetter.pendingWeaponSpawns.size()) {
                    // Find first empty slot
                    int map = 0;
                    int i = 0;
                    while (i < obj[map].length && obj[map][i] != null) i++;
                    if (i < obj[map].length) {
                        obj[map][i] = aSetter.pendingWeaponSpawns.get(aSetter.weaponSpawnAnimIndex);
                    }
                    aSetter.weaponSpawnAnimIndex++;
                } else {
                    aSetter.weaponSpawnAnimating = false;
                }
            }
        }
	}
	
	public void paintComponent(Graphics g) {	//draw objects onscreen
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		// Calculate scale factors
		double scaleX = (double) getWidth() / baseWidth;
		double scaleY = (double) getHeight() / baseHeight;

		// Save the original transform
		java.awt.geom.AffineTransform oldTransform = g2.getTransform();

		// Scale the graphics context
		g2.scale(scaleX, scaleY);

		// Draw everything as if at base resolution
		if (gameState == titleState) {
			ui.draw(g2);
		} else {
			tileM.draw(g2);	//tile draw

			// Add all entities and projectiles to entityList
			entityList.add(player);

			for(int i = 0; i < obj[currentMap].length; i++) {
				if(obj[currentMap][i] != null) {
					entityList.add(obj[currentMap][i]);
				}
			}
			for(int i = 0; i < npc[currentMap].length; i++) {
				if(npc[currentMap][i] != null) {
					entityList.add(npc[currentMap][i]);
				}
			}
			for(int i = 0; i < monster[currentMap].length; i++) {
				if(monster[currentMap][i] != null) {
					entityList.add(monster[currentMap][i]);
				}
			}
			for (int i = 0; i < projectileList.size(); i++) {
				if (projectileList.get(i) != null) {
					entityList.add(projectileList.get(i));
				}
			}

			//Sort the entity list
			Collections.sort(entityList, new Comparator<Entity>() {
				@Override
				public int compare(Entity e1, Entity e2) {
					if (e1.renderLayer != e2.renderLayer) {
						return Integer.compare(e1.renderLayer, e2.renderLayer);
					}
					return Integer.compare(e1.worldY, e2.worldY);
				}
			});

			entityList.removeIf(e -> e instanceof monster.BOSS_Skeleking && !e.alive);
			
			//Draw entities (including projectiles)
			for(int i = 0; i < entityList.size(); i++) {
				entityList.get(i).draw(g2);
			}

			entityList.clear(); //clear the entity list for next time
			
			//Draw boss arena border BEFORE UI
			boolean bossAlive = false;
			for (Entity e : monster[currentMap]) {
				if (e instanceof monster.BOSS_Skeleking && e.alive) {
					bossAlive = true;
					break;
				}
			}
			if (!bossAlive) {
				player.bossArenaActive = false; // Defensive: always turn off if boss is gone
			}
			if (player.bossArenaActive && bossAlive) {
			    int sx = player.bossArenaCenterX - player.worldX + player.screenX;
			    int sy = player.bossArenaCenterY - player.worldY + player.screenY;
			    int r = player.bossArenaRadius;
			    g2.setColor(java.awt.Color.YELLOW);
			    g2.setStroke(new java.awt.BasicStroke(4));
			    g2.drawOval(sx - r, sy - r, r * 2, r * 2);
			    g2.setStroke(new java.awt.BasicStroke(1));
			}

			//Draw UI (HP bar, mana bar, minimap, etc.)
			ui.draw(g2);
		}

		if (debugMode) {
			g2.setColor(Color.YELLOW);
			g2.drawString("DEBUG MODE ON", 10, 20);
			for (int map = 0; map < monster.length; map++) {
				for (int i = 0; i < monster[map].length; i++) {
					Entity m = monster[map][i];
					if (m != null && m.onPath && m.pathList != null) {
						g2.setColor(java.awt.Color.RED);
						for (ai.Node node : m.pathList) {
							int screenX = node.col * tileSize - player.worldX + player.screenX;
							int screenY = node.row * tileSize - player.worldY + player.screenY;
							g2.drawRect(screenX, screenY, tileSize, tileSize);
						}
					}
				}
			}
		}

		// Restore the original transform
		g2.setTransform(oldTransform);

		g2.dispose();	//dispose of graphic context & free system resource
	}
	public void resetEntities() {
	    // Initialize all sub-arrays for all maps
	    for (int map = 0; map < maxMap; map++) {
	        npc[map] = new Entity[10];      // or your actual NPC array size
	        monster[map] = new Entity[75];  // or your actual monster array size
	        obj[map] = new Entity[15];      // or your actual object array size
	    }

	    // Now safe to clear and set entities
	    for (int i = 0; i < npc.length; i++) {
	        for (int j = 0; j < npc[i].length; j++) {
	            npc[i][j] = null;
	        }
	    }
	    for (int i = 0; i < monster.length; i++) {
	        for (int j = 0; j < monster[i].length; j++) {
	            monster[i][j] = null;
	        }
	    }
	    for (int i = 0; i < obj.length; i++) {
	        for (int j = 0; j < obj[i].length; j++) {
	            obj[i][j] = null;
	        }
	    }

	    // Re-add your starting NPCs, monsters, and objects
	    aSetter.setNPC();
	    aSetter.setMonster();
	    aSetter.setObject();
	}

	public void resetToFirstMap() {
	    currentMap = 0;
	    for (int i = 0; i < obj[currentMap].length; i++) {
	        obj[currentMap][i] = null;
	    }
	    // If you have an assetSetter, re-place objects for the first map
	    if (aSetter != null) {
	        aSetter.setObject();
	        aSetter.setMonster();
	    }
	    // Reset player position to the starting point of the first map
	    if (player != null) {
	        player.worldX = player.defaultWorldX;
	        player.worldY = player.defaultWorldY;
	    }
		resetNpcDialogIndex();
	}
	public void resetNpcDialogIndex() {
	    for (int i = 0; i < npc[currentMap].length; i++) {
	        if (npc[currentMap][i] != null) {
	            npc[currentMap][i].dialogIndex = 0;
	        }
	    }
	}
	
}
