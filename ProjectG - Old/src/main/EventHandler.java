package main;

public class EventHandler {

    GamePanel gp;
    EventRect eventRect[][];

    int previousEventX, previousEventY;
    boolean canTouchEvent = true;

    public EventHandler(GamePanel gp) {
        this.gp = gp;

        eventRect = new EventRect[gp.maxWorldCol][gp.maxWorldRow];

        int col = 0;
        int row = 0;

        while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
            eventRect[col][row] = new EventRect();
            eventRect[col][row].x = col * gp.tileSize;
            eventRect[col][row].y = row * gp.tileSize;
            eventRect[col][row].width = gp.tileSize;
            eventRect[col][row].height = gp.tileSize;
            eventRect[col][row].eventRectDefaultX = eventRect[col][row].x;
            eventRect[col][row].eventRectDefaultY = eventRect[col][row].y;

            col++;
            if (col == gp.maxWorldCol) {
                col = 0;
                row++;
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
            // if (hit(20, 28, "any") == true) {
            //     triggerTrap(20, 18, gp.dialogueState);

            // }

            // if (hit(23, 28, "any") == true) {
            //     useFountain(23, 28, gp.dialogueState);

            // }
        }
    }

    public boolean hit(int col, int row, String  reqDirection) {
        boolean hit = false;
        gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
        gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;
        eventRect[col][row].x = col * gp.tileSize + eventRect[col][row].x;
        eventRect[col][row].y = row * gp.tileSize + eventRect[col][row].y;
        if (gp.player.solidArea.intersects(eventRect[col][row]) && !eventRect[col][row].eventDone) {
            if (gp.player.direction.contentEquals(reqDirection) || reqDirection.equals("any")) {
                hit = true;

                previousEventX = gp.player.worldX;
                previousEventY = gp.player.worldY;
            }
        }

        // Reset the player's solid area to its default position
        gp.player.solidArea.x = gp.player.solidAreaDefaultX;
        gp.player.solidArea.y = gp.player.solidAreaDefaultY;
        // Reset the event rectangle to its default position
        eventRect[col][row].x = eventRect[col][row].eventRectDefaultX;
        eventRect[col][row].y = eventRect[col][row].eventRectDefaultY;

        return hit;
    }

    public void triggerTrap(int col, int row,int gameState) {
        gp.gameState = gameState;
        gp.ui.currentDialogue = "You stepped on a trap!";
        gp.player.health -= 50; // Decrease player's health by 10
        eventRect[col][row].eventDone = true; // Mark the event as done
        gp.keyH.enterPressed = false; // Reset the enter key press
        canTouchEvent = false; // Prevent further event triggering until the player moves
    }

    public void useFountain(int col, int row, int gameState) {
        gp.gameState = gameState;
        if (gp.keyH.enterPressed == true) {
            gp.gameState = gp.dialogueState;
            gp.ui.currentDialogue = "You feel refreshed!";
            gp.player.health = gp.player.maxHealth; // Heal the player to max health
            gp.player.mana = gp.player.maxMana; // Restore mana to max
            eventRect[col][row].eventDone = true; // Mark the event as done
            gp.keyH.enterPressed = false; // Reset the enter key press
            canTouchEvent = false; // Prevent further event triggering until the player moves
            System.out.println("Fountain used");
        }
        
        gp.keyH.enterPressed = false;
    }
        
}
