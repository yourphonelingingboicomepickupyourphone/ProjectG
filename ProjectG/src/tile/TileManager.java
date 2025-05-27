package tile;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;

public class TileManager {

	GamePanel gp;
	public Tile[] tile;
	public int mapTileNum[][][];
	public boolean drawPath = false; // For debugging purposes, to draw the pathfinding path
	
	public TileManager(GamePanel gp) {
		
		this.gp = gp;
		
		tile = new Tile[50];	//number of tiles going to be created
		mapTileNum = new int [gp.maxMap][gp.maxWorldCol][gp.maxWorldRow];
		
		getTileImage();
		loadMap("/maps/world01.txt", 0);
		loadMap("/maps/world02.txt", 1);
	}
	
	public void getTileImage() {
		
		try {

			setup(0, "grass", false);
			setup(1, "grass_1", false);
			setup(2, "grass_2", false);
			setup(3, "wall", true);
			setup(4, "water", true);
			setup(5, "stone", false);
			setup(6, "rail_horizontal", false);
			setup(7, "rail_horizontal_1", false);
			setup(8, "rail_vertical", false);	
			setup(9, "rail_vertical_1", false);
			setup(10, "rail_corner_NE", false);
			setup(11, "rail_corner_NW", false);	
			setup(12, "rail_corner_SE", false);
			setup(13, "rail_corner_SW", false);
			setup(14, "rail_corner_NE_1", false);
			setup(15, "rail_corner_NW_1", false);
			setup(16, "rail_corner_SE_1", false);	
			setup(17, "rail_corner_SW_1", false);
			setup(18, "rail_horizontal_broken", false);	
			setup(19, "rail_vertical_broken", false);
			setup(20, "wood_horizontal", false);
			setup(21, "wood_vertical", false);
			setup(22, "lava", false);
			setup(23, "tree_1", true);
			setup(24, "water_edge_horizontal_up", true);
			setup(25, "water_edge_horizontal_down", true);
			setup(26, "water_edge_vertical_left", true);
			setup(27, "water_edge_vertical_right", true);
			setup(28, "water_edge_horizontal_up_right", true);
			setup(29, "water_edge_horizontal_down_right", true);
			setup(30, "water_edge_horizontal_up_left", true);
			setup(31, "water_edge_horizontal_down_left", true);
			setup(32, "blank", true);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setup(int index, String imagePath, boolean collision) {
		UtilityTool uTool = new UtilityTool();

		try {

			tile[index] = new Tile();
			tile[index].image = ImageIO.read(getClass().getResourceAsStream("/tiles/" + imagePath + ".png"));
			tile[index].image = uTool.scaleImage(tile[index].image, gp.tileSize, gp.tileSize);
			tile[index].collision = collision;

		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public void loadMap(String filePath, int map) {
		
		try {
			InputStream is = getClass().getResourceAsStream(filePath);
			BufferedReader br = new  BufferedReader(new InputStreamReader(is));
			
			int col = 0;
			int row = 0;
			
			while(col < gp.maxWorldCol && row < gp.maxWorldRow) {
				
				String line = br.readLine();
				
				while(col < gp.maxWorldCol) {
					
					String numbers[] = line.split(" ");
					
					int num = Integer.parseInt(numbers[col]);
					
					mapTileNum[map][col][row] = num;
					col++;
				}
				if(col == gp.maxWorldCol) {
					col = 0;
					row++;
				}
			}
			br.close();
			
		}catch(Exception e) {
			
		}
	}
	
	public void draw(Graphics2D g2) {
		
		int worldCol = 0;
		int worldRow = 0;
		
		while (worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {
			
			int tileNum = mapTileNum[gp.currentMap][worldCol][worldRow];
			
			int worldX = worldCol * gp.tileSize;
			int worldY = worldRow * gp.tileSize;
			int screenX = worldX - gp.player.worldX + gp.player.screenX;
			int screenY = worldY - gp.player.worldY + gp.player.screenY;
			

			//Stop camera when tile is outside the screen
			if(gp.player.screenX > gp.player.worldX) {
				screenX = worldX;
			}
			if(gp.player.screenY > gp.player.worldY) {
				screenY = worldY;
			}
			int rightOffset = gp.baseWidth - gp.player.screenX;
			if(rightOffset > gp.worldWidth - gp.player.worldX) {
				screenX = gp.baseWidth - (gp.worldWidth - worldX);
			}
			int bottomOffset = gp.baseHeight - gp.player.screenY;
			if(bottomOffset > gp.worldHeight - gp.player.worldY) {
				screenY = gp.baseHeight - (gp.worldHeight - worldY);
			}


			if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
				worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
				worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
				worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
				g2.drawImage(tile[tileNum].image, screenX, screenY, null);
			}
			else if (gp.player.screenX > gp.player.worldX || 
					gp.player.screenY > gp.player.worldY || 
					rightOffset > gp.worldWidth - gp.player.worldX || 
					bottomOffset > gp.worldHeight - gp.player.worldY) {

				g2.drawImage(tile[tileNum].image, screenX, screenY, null);
			}
			worldCol++;
			
			if(worldCol == gp.maxWorldCol) {
				worldCol = 0;
				worldRow++;
			}
		}

		if (drawPath) {
			for (int i = 0; i < gp.pFinder.pathList.size(); i++) {
				int worldX = gp.pFinder.pathList.get(i).col * gp.tileSize;
				int worldY = gp.pFinder.pathList.get(i).row * gp.tileSize;
				int screenX = worldX - gp.player.worldX + gp.player.screenX;
				int screenY = worldY - gp.player.worldY + gp.player.screenY;
				g2.setColor(new Color(255, 0, 0, 128)); // Semi-transparent red for path
				g2.fillRect(screenX, screenY, gp.tileSize, gp.tileSize); // Draw blank tile for path
			}
		}
	}

}
