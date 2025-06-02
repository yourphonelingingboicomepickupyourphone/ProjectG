package entity;

import main.GamePanel;

public class Projectile extends Entity {

    Entity user; //who fired the projectile

    public Projectile(GamePanel gp) {
        super(gp);
        
    }

    public void set(int worldX, int worldY, String direction, boolean alive, Entity user) {
        this.worldX = worldX;
        this.worldY = worldY;
        this.direction = direction;
        this.alive = alive;
        this.user = user;
        this.attackRange = 4 * gp.tileSize; //attack range in tiles
    }

    public void update(){
        System.out.println("Projectile update: " + this.getClass().getName() + " alive=" + alive + " pos=" + worldX + "," + worldY);
        if (user == gp.player){
            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            if (monsterIndex != -999) {
                gp.player.damageMonster(monsterIndex);
                alive = false;
            }
        } else if (user != gp.player) {

        }
        switch (direction) {
            case "up":
                worldY -= speed;
                break;
            case "down":
                worldY += speed;
                break;
            case "left":
                worldX -= speed;
                break;
            case "right":
                worldX += speed;
                break;
        }

        attackRange--;
        if (attackRange <= 0) {
            alive = false;
        }
        spriteCounter++;
        if (spriteCounter > 12) {
            if (spriteNum == 1) {
                spriteNum = 2;
            } else if (spriteNum == 2) {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }
    }



}
