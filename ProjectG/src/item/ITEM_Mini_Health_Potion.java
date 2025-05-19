package item;
import entity.Entity;
import main.GamePanel;

public class ITEM_Mini_Health_Potion  extends Entity {
    public ITEM_Mini_Health_Potion(GamePanel gp) {
        super(gp);
        
        this.gp = gp;

        name = "Mini Health Potion";
        level = 1;
        down1 = setup("/items/heal_potion");
        healthBonus = 50;
        rarity = 0; //0 = common, 1 = rare, 2 = epic, 3 = legendary
        levelRequirement = 0; //level requirement to use the item
        description = "A potion that restores health. It is not very powerful, but it is better than nothing.";
        healthHeal = 100; //health heal of the item
        
        type = 6; //6 = potion
        pickable = true;
        stackable = true;
    }

    public void use(Entity user) {
        if (user.health < user.maxHealth) {
            user.health += healthHeal;
            if (user.health > user.maxHealth) {
                user.health = user.maxHealth;
            }
            gp.ui.addMessage("You used a " + name + "!");
            gp.ui.addMessage("You restored " + healthHeal + " health!");
        } else {
            gp.ui.addMessage("You are already at full health!");
        }
    }

}
