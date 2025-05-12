package entity;


import main.GamePanel;

import java.util.Random;

public class NPC_Wizard extends Entity {

    public NPC_Wizard(GamePanel gp) {
        super(gp);
        
        direction = "down";
        name = "Wizard";
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
        dialogues[0] = "Wassup nigga?";
        dialogues[1] = "You looking for a job?";
        dialogues[2] = "Hah you useless blyat";
        dialogues[3] = "Lucky I have something for you to do";
    }

    public void setAction() {
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
    public void speak() {
        super.speak();
    }
}
