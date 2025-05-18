package main;

import java.awt.Color;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
	ArrayList<Entity> entityList = new ArrayList<>();
	
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
						monster[i] = null;
					}
				}
			}	//Monster update

		}

		else if (gameState == pauseState) {
			// do nothing

		}
		
	}
	
	public void paintComponent(Graphics g) {	//draw objects onscreen
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D)g;	//extend Graphics to provide more sophisticated control over geometry
		

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
			
			g2.dispose();	//dispose of graphic context & free system resource
		}
		
		
	}
}
