package data;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class DataStorage implements Serializable{

    private static final long serialVersionUID = 1L;
    //Player Stats
    public String name;
	public float maxHealth;
	public float health;
	public int maxMana;
	public int mana = maxMana;
	public int level;
	public int attack;
	public int defense;
	public int exp;
	public int speed;
	public int totalProgressionPoints;
	public int progressionPoints;
	public int progressionHealthUpgrades;
	public int progressionManaUpgrades;
	public int progressionAttackUpgrades;
	public int progressionDefenseUpgrades;
	public int ATTACK_COOLDOWN_MAX;
	public int FLASH_COOLDOWN_MAX;
    public int flashCooldown = 0;

    public ArrayList<EntitySaveData> inventory = new ArrayList<>();
    public String quickUseItemClass;
    public String quickUseItemName;

    public int playerWorldX;
    public int playerWorldY;
    public int currentMap;

    // Add these fields for saving map entities and inventory
    public ArrayList<ArrayList<EntitySaveData>> savedObjects = new ArrayList<>();
    public ArrayList<ArrayList<EntitySaveData>> savedMonsters = new ArrayList<>();
    public ArrayList<ArrayList<EntitySaveData>> savedNpcs = new ArrayList<>();


    // If you want to save equipped items as EntitySaveData:
    public EntitySaveData currentWeapon;
    public EntitySaveData currentArmor;
    public EntitySaveData currentHat;
    public EntitySaveData currentBoots;

    public ArrayList<String> skills = new ArrayList<>();
    public ArrayList<String> unlockedSkillClassNames = new ArrayList<>();
    public ArrayList<String> assignedSkillClassNames = new ArrayList<>();
    public ArrayList<Integer> assignedSkillCooldowns = new ArrayList<>();

    
    public void savePlayerData(DataStorage data) {
        try (FileOutputStream fos = new FileOutputStream("save.dat");
            ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DataStorage loadPlayerData() {
        try (FileInputStream fis = new FileInputStream("save.dat");
            ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (DataStorage) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
