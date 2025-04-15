package character;

public abstract class character{
    public String name;
    public boolean isAlive = true;
    public int level;
    public float maxHealth; 
    public float health;
    public float maxMana;
    public float mana;
    public int AP;
    public int MP;
    public float CritRate;
    public float CritDamage;
    public int defense;
    public float agility;
    public String status = "Normal";
    public int coordinateX;
    public int coordinateY;

    public float Attack(String name) {
        int damage = (int) (AP);
        if (Math.random() < CritRate) {
            damage *= CritDamage;
            System.out.println("Critical!");
        }
        String Target = name;
        return damage;
    }

    public float TakeDamage(int damage) {
        if (isAlive) {
            health -= (damage - defense);
            if (health <= 0) {
                isAlive = false;
                System.out.println(name + " has died.");
            } else {
                System.out.println(name + " took " + damage + " damage. Remaining health: " + health);
            }
        } else {
            System.out.println(name + " is already dead.");
        }
    }

    public void Heal(int healAmount) {
        if (isAlive) {
            health += healAmount;
            if (health > maxHealth) {
                health = maxHealth;
            }
            System.out.println(name + " healed for " + healAmount + ". Current health: " + health);
        } else {
            System.out.println(name + " cannot be healed because they are dead.");
        }
    }

    public void Move(int x, int y) {
        if (isAlive) {
            coordinateX += x;
            coordinateY += y;
            System.out.println(name + " moved to (" + coordinateX + ", " + coordinateY + ").");
        } else {
            System.out.println(name + " cannot move because they are dead.");
        }
    }

    public void StatusChange(String newStatus) {
        if (isAlive) {
            status = newStatus;
            System.out.println(name + " is now " + status + ".");
        } else {
            System.out.println(name + " cannot change status because they are dead.");
        }
    }
}