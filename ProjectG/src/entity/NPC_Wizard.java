package entity;


import main.GamePanel;

import java.util.Random;

public class NPC_Wizard extends Entity {

    public NPC_Wizard(GamePanel gp) {
        super(gp);
        
        direction = "down";
        name = "Wizard";
        type = 2;
        
        renderLayer = 1; 
        speed = 1;
        
        // Adjust solid area to match sprite
        solidArea.x = 7;
        solidArea.y = 14;
        solidArea.width = 66;
        solidArea.height = 66;

        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        
        getImage();
        setDialogue();

    }

    public void getImage() {
        up1 = setup("/npc/npc_wizard_up_1");
        up2 = setup("/npc/npc_wizard_up_2");
        down1 = setup("/npc/npc_wizard_down_1");
        down2 = setup("/npc/npc_wizard_down_2");
        left1 = setup("/npc/npc_wizard_left_1");
        left2 = setup("/npc/npc_wizard_left_2");
        right1 = setup("/npc/npc_wizard_right_1");
        right2 = setup("/npc/npc_wizard_right_2");
    }

    public void setDialogue() {
        dialogues[0][0] = "Ho, ho, ho";
        dialogues[0][1] = "Why you still here?";
        dialogues[0][2] = "You should be out there fighting";
        dialogues[0][3] = "Pick one of those weapons and go!";
    }

    public void setAction() {

        if (gp.currentMap == 0){
            speed = 0;
            direction = "down";
            spriteNum = 1;
            spriteCounter = 0;
            return;
        } else {
            if (onPath == true){
                int goalCol = (gp.player.worldX + gp.player.solidArea.x) / gp.tileSize;
                int goalRow = (gp.player.worldY + gp.player.solidArea.y) / gp.tileSize;

                searchPath(goalCol, goalRow);
            } else {
                actionLockCounter++;
                if(actionLockCounter == 240) //direction changes after 2 secs
                {
                    Random random = new Random();
                    int i = random.nextInt(100) + 1;  // pick up a number from 1 to 100
                    if(i <= 25)
                    {
                        direction = "up";
                    }
                    if(i>25 && i <= 50)
                    {
                        direction = "down";
                    }
                    if(i>50 && i <= 75)
                    {
                        direction = "left";
                    }
                    if(i>75 && i <= 100)
                    {
                        direction = "right";
                    }
                    actionLockCounter = 0; // reset
                }
            }
        }
    }
    public void speak() {
        super.speak();
        onPath = true;
    }
}
