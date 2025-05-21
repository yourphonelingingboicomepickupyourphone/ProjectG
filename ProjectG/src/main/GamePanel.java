package main;

import java.awt.Color;

import java.awt.Dimension;
import java.awt.Graphics;
// import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JPanel;

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
	public final int screenWidth = tileSize * maxScreenCol;
	public final int screenHeight = tileSize * maxScreenRow;	//1920x1040

	public int screenWidth2 = screenWidth;
	public int screenHeight2 = screenHeight;
	BufferedImage tempScreen;
	Graphics2D g2;
	
	//World Settings
	public final int maxWorldCol = 100;
	public final int maxWorldRow = 100;
	public final int worldWidth = maxWorldCol * tileSize;
	public final int worldHeight = maxWorldRow * tileSize;
	
	
	//System Settings
	TileManager tileM = new TileManager(this);
	public KeyHandler keyH = new KeyHandler(this);
	public CollisionChecker cChecker = new CollisionChecker(this);
	public AssetSetter aSetter = new AssetSetter(this);
	public UI ui = new UI(this);
	public EventHandler eHandler = new EventHandler(this);
	Thread gameThread;
	
	//Entity & Object & NPC
	public Player player = new Player(this, keyH);
	public Entity obj[] = new Entity[15];	//number of objects
	public Entity npc[] = new Entity[10];	//number of NPCs 
	public Entity monster[] = new Entity[75];	//number of monsters	
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

	public GamePanel() {
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);	//all the drawing will be done in an offscreen painting buffer, improve performance
		this.addKeyListener(keyH);
		this.setFocusable(true);	//GamePanel can be focused to receive key input
	}
	
	
	public void setupGame() {
		
		aSetter.setObject();
		aSetter.setNPC();
		aSetter.setMonster();
	
		player.renderLayer = 2; // Player in the middle layer

		gameState = titleState;	//start with title screen

		tempScreen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_RGB);	//create a buffered image
		g2 = (Graphics2D)tempScreen.getGraphics();	//create a graphics context for the buffered image

		setFullScreen();	//set the window to full screen
		
	}

	public void setFullScreen(){
		GraphicsEnvironment gEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();	//get the local graphics environment
		GraphicsDevice gDevice = gEnv.getDefaultScreenDevice();	//get the default screen device
		gDevice.setFullScreenWindow(Main.window);	//set the window to full screen
		Main.window.setResizable(false);	//set the window to be not resizable

		screenWidth2 = Main.window.getWidth();	//get the width of the window
		screenHeight2 = Main.window.getHeight();	//get the height of the window
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
				drawToTempScreen();
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
				if(npc[i] != null) {
					npc[i].update();
				}
			}	//NPC update

			for(int i = 0; i < monster.length; i++) {
				if(monster[i] != null) {
					if(monster[i].alive == true && monster[i].dying == false) {
						monster[i].update();
					}
					if (monster[i].alive == false) {
						monster[i].checkDrop();
						monster[i] = null;
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
					}
				}
			}	//Projectile update

		}

		else if (gameState == pauseState) {
			// do nothing

		}
		
	}
	public void drawToTempScreen() {
		//title screen
		if (gameState == titleState) {
			ui.draw(g2);	//title screen
		}
		//others 
		else {
			tileM.draw(g2);	//tile draw

			entityList.add(player);	//add player to entity list

			for(int i = 0; i < obj.length; i++) {
				if(obj[i] != null) {
					entityList.add(obj[i]);	//add object to entity list
				}
			}

			for(int i = 0; i < npc.length; i++) {
				if(npc[i] != null) {
					entityList.add(npc[i]);	//add NPC to entity list
				}
			}

			for(int i = 0; i < monster.length; i++) {
				if(monster[i] != null) {
					entityList.add(monster[i]);	//add monster to entity list
				}
			}

			for (int i = 0; i < projectileList.size(); i++) {
				if (projectileList.get(i) != null) {
					entityList.add(projectileList.get(i));	//add projectile to entity list
				}
			}

			//Sort the entity list
			Collections.sort(entityList, new Comparator<Entity>() {
				@Override
				public int compare(Entity e1, Entity e2) {
					if (e1.renderLayer != e2.renderLayer) {	//compare the render layer of two entities
						return Integer.compare(e1.renderLayer, e2.renderLayer);	//compare the render layer of two entities
					}
					return Integer.compare(e1.worldY, e2.worldY);	//compare the Y coordinate of two entities
				}
			});

			//Draw entities
			for(int i = 0; i < entityList.size(); i++) {
				entityList.get(i).draw(g2);	//draw each entity in the list
			}

			entityList.clear();	//clear the entity list for next time
			
			ui.draw(g2);	//ui
		}
		
	}

	@Override
	protected void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    if (tempScreen != null) {
	        // Calculate scale to fit while preserving aspect ratio
	        double panelAspect = (double) getWidth() / getHeight();
	        double gameAspect = (double) screenWidth / screenHeight;
	        int drawWidth, drawHeight, drawX, drawY;

	        if (panelAspect > gameAspect) {
	            // Panel is wider than game: fit height
	            drawHeight = getHeight();
	            drawWidth = (int) (drawHeight * gameAspect);
	        } else {
	            // Panel is taller than game: fit width
	            drawWidth = getWidth();
	            drawHeight = (int) (drawWidth / gameAspect);
	        }
	        drawX = (getWidth() - drawWidth) / 2;
	        drawY = (getHeight() - drawHeight) / 2;

	        // Fill background black to hide borders
	        g.setColor(Color.BLACK);
	        g.fillRect(0, 0, getWidth(), getHeight());

	        // Draw the game buffer scaled, centered
	        g.drawImage(tempScreen, drawX, drawY, drawWidth, drawHeight, null);
	    }
	}
	
	public void drawToScreen() {
		Graphics g = getGraphics();	//get the graphics context of the screen
		g.drawImage(tempScreen, 0, 0, screenWidth2, screenHeight2, null);	//draw the buffered image to the screen
		g.dispose();	//dispose the graphics context
	}
		
}
