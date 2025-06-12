package entity;

import java.awt.Graphics2D;

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
        this.attackRange = gp.tileSize / 2; //attack range in tiles
    }

    public void update(){
        System.out.println("Projectile update: " + this.getClass().getName() + " alive=" + alive + " pos=" + worldX + "," + worldY);
        if (user == gp.player){
            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            if (monsterIndex != 999) {
                Entity target = gp.monster[gp.currentMap][monsterIndex];
                int dmg = (this.attackBonus > 0 ? this.attackBonus : 1); // Use projectile's attackBonus or 1

                // --- FIX: Call boss takeDamage() directly ---
                if (target instanceof monster.BOSS_Skeleking) {
                    ((monster.BOSS_Skeleking)target).takeDamage(dmg);
                } else {
                    gp.player.damageMonster(monsterIndex);
                }

                // Show HP bar on hit (for both melee and projectile)
                target.showHpBar = true;
                target.hpBarDisplayCounter = 150;
                alive = false;
                return;
            }
            // Check collision with NPCs
            int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
            if (npcIndex != 999) {
                alive = false;
                return;
            }

            // Check collision with collidable objects (like chests, pillars, etc)
            int objIndex = gp.cChecker.checkObject(this, false);
            if (objIndex != 999 && gp.obj[gp.currentMap][objIndex].collision) {
                alive = false;
                return;
            }

            // Check collision with tiles (walls, obstacles)
            gp.cChecker.checkTile(this);
            if (collisionOn) {
                alive = false;
                return;
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

    @Override
    public void draw(Graphics2D g2) {
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;
        System.out.println("Drawing projectile at screen: " + screenX + "," + screenY + " alive=" + alive);

        g2.setColor(java.awt.Color.RED);
        g2.fillRect(screenX, screenY, gp.tileSize, gp.tileSize);
    }

}
