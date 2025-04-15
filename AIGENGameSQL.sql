CREATE TABLE Player (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100),
    level INT,
    experience INT,
    gold INT,
    dead_count INT,
    max_level INT
);

CREATE TABLE Character (
    id SERIAL PRIMARY KEY,
    player_id INT REFERENCES Player(id),
    class VARCHAR(100),
    strength INT,
    agility INT,
    intelligence INT,
    defense INT,
    develop_point INT,
    mana INT,
    x_coordinate INT,
    y_coordinate INT,
    title VARCHAR(100)
);

CREATE TABLE Inventory (
    id SERIAL PRIMARY KEY,
    player_id INT REFERENCES Player(id),
    item_name VARCHAR(100),
    max_amount INT
);

CREATE TABLE Item (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL,
    description TEXT,
    item_type VARCHAR(50),
    rarity VARCHAR(50),
    type VARCHAR(50),
    effect TEXT,
    toughness INT,
    class_restriction VARCHAR(100)
);

CREATE TABLE Worn_Item (
    id SERIAL PRIMARY KEY,
    character_id INT REFERENCES Character(id),
    item_id INT REFERENCES Item(id),
    slot VARCHAR(50) NOT NULL
);

CREATE TABLE Level_Experience (
    level INT PRIMARY KEY,
    experience_required INT
);

CREATE TABLE Class_Skill (
    id SERIAL PRIMARY KEY,
    class VARCHAR(100),
    skill_name VARCHAR(100),
    required_level INT
);

CREATE TABLE Account (
    id SERIAL PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Login_Info (
    id SERIAL PRIMARY KEY,
    account_id INT REFERENCES Account(id),
    login_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ip_address VARCHAR(45)
);

CREATE TABLE Monster (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    health INT,
    attack INT,
    defense INT,
    speed INT,
    experience_reward INT,
    vulnerability TEXT,
    resistances TEXT
);

CREATE TABLE Boss (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    health INT,
    attack INT,
    defense INT,
    speed INT,
    experience_reward INT,
    special_ability TEXT,
    vulnerability TEXT,
    resistances TEXT,
    x_coordinate INT,
    y_coordinate INT
);

CREATE TABLE NPC (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    role VARCHAR(100),
    description TEXT,
    x_coordinate INT,
    y_coordinate INT
);

CREATE TABLE Quest (
    id SERIAL PRIMARY KEY,
    npc_id INT REFERENCES NPC(id),
    name VARCHAR(100) NOT NULL,
    description TEXT,
    reward_gold INT,
    reward_experience INT,
    reward_item_id INT REFERENCES Item(id)
);

CREATE TABLE Shop (
    id SERIAL PRIMARY KEY,
    npc_id INT REFERENCES NPC(id),
    name VARCHAR(100) NOT NULL,
    description TEXT,
    x_coordinate INT,
    y_coordinate INT
);

CREATE TABLE Shop_Item (
    id SERIAL PRIMARY KEY,
    shop_id INT REFERENCES Shop(id),
    item_id INT REFERENCES Item(id),
    price INT NOT NULL,
    stock INT
);

CREATE TABLE Map (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    difficulty_level INT
);

CREATE TABLE Mimic (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL,
    health INT NOT NULL,
    attack INT NOT NULL,
    defense INT NOT NULL,
    special_ability TEXT
);

CREATE TABLE Chest_Drop (
    id SERIAL PRIMARY KEY,
    chest_type VARCHAR(50) NOT NULL,
    item_id INT REFERENCES Item(id),
    drop_rate FLOAT NOT NULL,
    min_x INT,
    max_x INT,
    min_y INT,
    max_y INT,
    mimic_id INT REFERENCES Mimic(id),
    mimic_spawn_chance FLOAT NOT NULL
);

CREATE TABLE Mimic_Item_Drop (
    id SERIAL PRIMARY KEY,
    mimic_id INT REFERENCES Mimic(id),
    item_id INT REFERENCES Item(id),
    drop_rate FLOAT NOT NULL
);

CREATE TABLE Monster_Chest_Drop (
    id SERIAL PRIMARY KEY,
    monster_id INT REFERENCES Monster(id),
    chest_type VARCHAR(50) NOT NULL,
    drop_rate FLOAT NOT NULL
);

CREATE TABLE Boss_Chest_Drop (
    id SERIAL PRIMARY KEY,
    boss_id INT REFERENCES Boss(id),
    chest_type VARCHAR(50) NOT NULL,
    drop_rate FLOAT NOT NULL
);

CREATE TABLE Monster_Spawn (
    id SERIAL PRIMARY KEY,
    monster_id INT REFERENCES Monster(id),
    map_id INT REFERENCES Map(id),
    min_x INT,
    max_x INT,
    min_y INT,
    max_y INT
);

CREATE TABLE Title (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL,
    description TEXT,
    effect TEXT
);

CREATE TABLE Character_Title (
    id SERIAL PRIMARY KEY,
    character_id INT REFERENCES Character(id),
    title_id INT REFERENCES Title(id)
);

CREATE TABLE Save_Slot (
    id SERIAL PRIMARY KEY,
    player_id INT REFERENCES Player(id),
    slot_number INT NOT NULL,
    save_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    quick_save BOOLEAN DEFAULT FALSE,
    data JSONB NOT NULL
);

CREATE TABLE Trap (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    effect TEXT,
    damage INT,
    trigger_chance FLOAT,
    x_coordinate INT,
    y_coordinate INT
);
