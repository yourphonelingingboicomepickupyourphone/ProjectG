package item;
import entity.Entity;
import entity.Player;
import main.GamePanel;

public class ITEM_Mini_Health_Potion  extends Entity {
    public ITEM_Mini_Health_Potion(GamePanel gp) {
        super(gp);
        
        this.gp = gp;

        name = gp.ui.tr("item.mini_health_potion.name");
        level = 1;
        down1 = setup("/items/heal_potion");
        healthBonus = 100;
        rarity = 0; //0 = common, 1 = rare, 2 = epic, 3 = legendary
        levelRequirement = 0; //level requirement to use the item
        description = gp.ui.tr("item.mini_health_potion.description");
        healthHeal = 100; //health heal of the item
        
        type = 3; 
        itemType = 6; //6 = potion
        pickable = true;
        stackable = true;
    }

    public void getImage() {
        down1 = setup("/items/heal_potion");
    }

    public void use(Player player) {
        if (player.health < player.getTotalMaxHealth()) {
            player.health += healthHeal;
            if (player.health > player.getTotalMaxHealth()) {
                player.health = player.getTotalMaxHealth();
            }
            gp.ui.addMessage(gp.ui.tr("message.use_item", name));
            gp.ui.addMessage(gp.ui.tr("message.restore_health", healthHeal));
        } else {
            gp.ui.addMessage(gp.ui.tr("message.full_health"));
        }
    }

}
