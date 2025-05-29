package data;

import java.io.Serializable;
import java.util.ArrayList;

public class EntitySaveData implements Serializable {
    private static final long serialVersionUID = 1L;

    // Basic info
    public String className;
    public String name;
    public int type;
    public int itemType;
    public int worldX, worldY;
    public int renderLayer;
    public int speed;
    public String direction;
    public int spriteCounter;
    public int spriteNum;
    public boolean collision;
    public boolean attacking;
    public boolean alive;
    public boolean dying;
    public int dyingCounter;
    public boolean onPath;
    public boolean invincible;
    public int invincibleCounter;
    public boolean showHpBar;
    public int hpBarDisplayCounter;
    public int knockbackCounter;
    public int knockbackDuration;
    public int knockbackDX;
    public int knockbackDY;
    public int actionLockCounter;
    public int dialogIndex;
    public int attackAreaDefaultX;
    public int attackAreaDefaultY;
    public boolean collisionOn;
    public int quantity;
    public boolean pickable;
    public boolean stackable;

    // Stats
    public float maxHealth;
    public float health;
    public int maxMana;
    public int mana;
    public int level;
    public int attack;
    public int defense;
    public int progressionHealthUpgrades;
    public int progressionManaUpgrades;
    public int progressionAttackUpgrades;
    public int progressionDefenseUpgrades;
    public int exp;
    public int nextLevelExp;
    public int totalProgressionPoints;
    public int progressionPoints;
    public int expReward;
    public int timeSinceLastHit;
    public int healDelay;

    // Equipment/Bonuses
    public int healthBonus;
    public int manaBonus;
    public int attackBonus;
    public int defenseBonus;
    public int speedBonus;
    public int attackRange;
    public int weaponType;
    public int rarity;
    public int levelRequirement;
    public String description;
    public int cooldownBonus;

    // Item-specific
    public int healthHeal;
    public int manaHeal;
    public float healthCost;
    public int manaCost;

    // For chests, keys, etc.
    // Add more as needed for your objects

    // For inventory/equipment, you may want to store a list of EntitySaveData for nested items
    public ArrayList<EntitySaveData> inventory = new ArrayList<>();
}
