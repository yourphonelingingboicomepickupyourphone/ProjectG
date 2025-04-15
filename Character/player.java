package character;

//import character.playerClass.melee;
//import character.playerClass.ranged;

public class player extends character{
    public int experience = 0;
    public int ProgressionPoint = 0;
    public String className;

//--------------------------------------------------------Create new player--------------------------------------------------------------------------------------------
public player(String name, String className) {
    this.name = name;
    this.className = className;
    this.level = 1;
    this.experience = 0;
    this.ProgressionPoint = 0;

    playerClass baseStats;
    switch (className.toLowerCase()) {
        case "melee":
            baseStats = new melee();
            break;
        case "ranged":
            baseStats = new ranged();
            break;
        default:
            throw new IllegalArgumentException("Unknown class name: " + className);
    }

    this.maxHealth = baseStats.maxHealth;
    this.health = baseStats.health;
    this.maxMana = baseStats.maxMana;
    this.mana = baseStats.mana;
    this.AP = baseStats.AP;
    this.MP = baseStats.MP;
    this.CritRate = baseStats.CritRate;
    this.CritDamage = baseStats.CritDamage;
    this.defense = baseStats.defense;
    this.agility = baseStats.agility;
}
//--------------------------------------------------------Stats - Player stats----------------------------------------------------------------------------------
   
    public void DisplayPlayerInfo() {
            System.out.println("Player Name: " + name);
            System.out.println("Class: " + className);
            System.out.println("Level: " + level);
            System.out.println("Experience: " + experience);
            System.out.println("Health: " + health);
            System.out.println("Mana: " + mana);
            System.out.println("Attack Power: " + AP);
            System.out.println("Magic Power: " + MP);
            System.out.println("Critical Rate: " + CritRate * 100 + "%");
            System.out.println("Critical Damage: " + CritDamage * 100 + "%");
            System.out.println("Defense: " + defense);
            System.out.println("Agility: " + agility);
        }

//-----------------------------------Respawn - Return to last checkpoint with full health and mana----------------------------------------------------------------
    public void Respawn(int x, int y){
        System.out.println("You have respawned at the last checkpoint.");
        coordinateX = x;
        coordinateY = y;
        health = maxHealth; 
        mana = maxMana; 
    }

//------------------------------------Level Up - Increase level, gain points and distribute them-------------------------------------------------------------------
    public int LevelUp(){
        double exp = (level * 100 + Math.pow(10, (Math.floor(level / 10) + 1)));
        if (experience >= exp){
            experience -= exp;
            level++;
            int points = level * 5 + 5;
            return points;
        }
        else {
            return 0;
        }
    }

    public void AddProgressionPoint(int points){
        ProgressionPoint += points;
    }

    public void AddExperience(int exp){
        experience += exp;
        LevelUp();
    }

    public void distributePP(int points, String stat){
        if (points > ProgressionPoint) {
            System.out.println("You don't have enough points to distribute.");
            return;
        }
        else {
            ProgressionPoint -= points;
        }
        switch(stat){
            case "health":
                maxHealth += points * 100.0f;
                health += points * 100.0f;
                break;
            case "mana":
                maxMana += points * 20.0f;
                mana += points * 20.0f;
                break;
            case "AP":
                AP += points * 20;
                break;
            case "MP":
                MP += points * 20;
                break;
            case "Defense":
                defense += points * 20;
                break;
            case "agility":
                agility += points * 2.0f;
                break;
            default:
                System.out.println("Invalid stat name.");
        }
    }


}   

