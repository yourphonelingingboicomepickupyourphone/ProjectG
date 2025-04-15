package character;

public class playerClass extends character {
    public String playerClassName;

    public playerClass(float maxHealth, float health, float maxMana, float mana, int AP, int MP,
                       float CritRate, float CritDamage, int defense, float agility) {
        super(maxHealth, health, maxMana, mana, AP, MP, CritRate, CritDamage, defense, agility);
    }
}

class melee extends playerClass {
    public melee() {
        super(2000.00f, 2000.00f, 500.00f, 500.00f, 200, 100, 0.1000f, 1.2000f, 50, 20.00f);
        this.playerClassName = "Melee";
    }
}

class ranged extends playerClass {
    public ranged() {
        super(1500.00f, 1500.00f, 700.00f, 700.00f, 200, 100, 0.2000f, 1.2000f, 50, 30.00f);
        this.playerClassName = "Ranged";
    }
}