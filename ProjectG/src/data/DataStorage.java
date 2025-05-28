package data;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import entity.Entity;

public class DataStorage implements Serializable{

    //Player Stats
    public String name;
	public float maxHealth;
	public float health;
	public int maxMana = 400;
	public int mana = maxMana;
	public int level = 1;
	public int attack = 50;
	public int defense = 10;
	public int exp = 0;
	public int speed = 5;
	public int totalProgressionPoints = 0;
	public int progressionPoints = 0;
	public int progressionHealthUpgrades = 0;
	public int progressionManaUpgrades = 0;
	public int progressionAttackUpgrades = 0;
	public int progressionDefenseUpgrades = 0;
	public int ATTACK_COOLDOWN_MAX = 30; // 30 frames = 0.5s at 60fps
	public int FLASH_COOLDOWN_MAX = 3600; // 60 seconds at 60fps

	public Entity currentWeapon;
	public Entity currentArmor = null;
	public Entity currentHat = null;
	public Entity currentBoots = null;
    public ArrayList<Entity> inventory = new ArrayList<>();
    public String quickUseItemClass;
    public String quickUseItemName;

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
