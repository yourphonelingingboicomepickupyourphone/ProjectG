package main;

public class EventHandler {

    GamePanel gp;
    EventRect eventRect[][][];

    int previousEventX, previousEventY;
    boolean canTouchEvent = true;
    int tempMap, tempCol, tempRow;

    public EventHandler(GamePanel gp) {
        this.gp = gp;

        eventRect = new EventRect[gp.maxMap][gp.maxWorldCol][gp.maxWorldRow];

        int map = 0;
        int col = 0;
        int row = 0;

        while (map < gp.maxMap && col < gp.maxWorldCol && row < gp.maxWorldRow) {
            eventRect[map][col][row] = new EventRect();
            eventRect[map][col][row].x = col * gp.tileSize;
            eventRect[map][col][row].y = row * gp.tileSize;
            eventRect[map][col][row].width = gp.tileSize;
            eventRect[map][col][row].height = gp.tileSize;
            eventRect[map][col][row].eventRectDefaultX = eventRect[map][col][row].x;
            eventRect[map][col][row].eventRectDefaultY = eventRect[map][col][row].y;

            col++;
            if (col == gp.maxWorldCol) {
                col = 0;
                row++;

                if (row == gp.maxWorldRow) {
                    row = 0;
                    map++;
                }
            }
        }

    }

    public void checkEvent() {

        // Check if the player is in a different position than the previous event
        int xDistance = Math.abs(gp.player.worldX - previousEventX);
        int yDistance = Math.abs(gp.player.worldY - previousEventY);    
        int distance = Math.max(xDistance, yDistance);
        if (distance > gp.tileSize) {
            canTouchEvent = true; // Reset the event touch flag if the player has moved far enough
        }

        if(canTouchEvent){
            // if (hit(1, 20, 28, "any") == true) {
            //     triggerTrap(20, 18, gp.dialogueState);

            // }

            if (hit(0, 50, 57, "any")) {
                teleport(1, 50, 50); // Teleport to map 1 at coordinates (50, 50)
            }
        }
    }

    public boolean hit(int map, int col, int row, String reqDirection) {
        boolean hit = false;
        if (gp.currentMap == map) {
            // Reset eventRect to its default position before checking
            eventRect[map][col][row].x = eventRect[map][col][row].eventRectDefaultX;
            eventRect[map][col][row].y = eventRect[map][col][row].eventRectDefaultY;

            // Set player's solid area for collision check
            gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
            gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;

            if (gp.player.solidArea.intersects(eventRect[map][col][row]) && !eventRect[map][col][row].eventDone) {
                if (gp.player.direction.contentEquals(reqDirection) || reqDirection.equals("any")) {
                    hit = true;
                    previousEventX = gp.player.worldX;
                    previousEventY = gp.player.worldY;
                }
            }

            // Reset the player's solid area to its default position
            gp.player.solidArea.x = gp.player.solidAreaDefaultX;
            gp.player.solidArea.y = gp.player.solidAreaDefaultY;
        }
        return hit;

    }

    public void triggerTrap(int gameState) {
        gp.gameState = gameState;
        gp.ui.currentDialogue = "You stepped on a trap!";
        gp.player.health -= 50; // Decrease player's health by 10
        gp.keyH.enterPressed = false; // Reset the enter key press
        canTouchEvent = false; // Prevent further event triggering until the player moves
    }

    public void teleport(int map, int col, int row) {

        gp.gameState = gp.transitionState; // Set the game state to transition  
        tempMap = map;
        tempCol = col;
        tempRow = row;
        canTouchEvent = true; // Prevent further event triggering until the player moves
    }

    public void finishTeleport() {
        gp.currentMap = tempMap;
        gp.player.worldX = tempCol * gp.tileSize - gp.tileSize / 2;
        gp.player.worldY = tempRow * gp.tileSize - gp.tileSize / 2;
        previousEventX = gp.player.worldX;
        previousEventY = gp.player.worldY;
    }
        
}
