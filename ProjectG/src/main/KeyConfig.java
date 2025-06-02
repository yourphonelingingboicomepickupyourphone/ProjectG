package main;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class KeyConfig {

    public static final String UP = "Up";
    public static final String DOWN = "Down";
    public static final String LEFT = "Left";
    public static final String RIGHT = "Right";
    public static final String CHOOSE = "Choose";
    public static final String ESCAPE = "Escape";
    public static final String ATTACK = "Attack";
    public static final String INVENTORY = "Inventory";
    public static final String CHARACTER = "Character";
    public static final String RESET = "Reset";
    public static final String FLASH = "Flash";
    public static final String QUICK_USE = "QuickUse";
    public static final String SKILLS = "Skills";
    public static final String SKILL1 = "Skill1"; // Added SKILLS1 binding
    public static final String SKILL2 = "Skill2"; // Added SKILLS2 binding
    public static final String SKILL3 = "Skill3"; // Added SKILLS3 binding
    public static final String SKILL4 = "Skill4"; // Added SKILLS4 binding

    private final Map<String, Integer> keyBindings = new HashMap<>();

    public KeyConfig(){
        // Default key bindings
        keyBindings.put(UP, KeyEvent.VK_UP);
        keyBindings.put(DOWN, KeyEvent.VK_DOWN);
        keyBindings.put(LEFT, KeyEvent.VK_LEFT);
        keyBindings.put(RIGHT, KeyEvent.VK_RIGHT);
        keyBindings.put(CHOOSE, KeyEvent.VK_ENTER);
        keyBindings.put(ESCAPE, KeyEvent.VK_ESCAPE);
        keyBindings.put(ATTACK, KeyEvent.VK_SPACE);
        keyBindings.put(INVENTORY, KeyEvent.VK_I);
        keyBindings.put(CHARACTER, KeyEvent.VK_C);
        keyBindings.put(RESET, KeyEvent.VK_R);
        keyBindings.put(FLASH, KeyEvent.VK_F); 
        keyBindings.put(QUICK_USE, KeyEvent.VK_D);
        keyBindings.put(SKILLS, KeyEvent.VK_S); // Added SKILLS binding
        keyBindings.put(SKILL1, KeyEvent.VK_1); // Default binding for SKILL1
        keyBindings.put(SKILL2, KeyEvent.VK_2); // Default binding for SKILL2
        keyBindings.put(SKILL3, KeyEvent.VK_3); // Default binding for SKILL3
        keyBindings.put(SKILL4, KeyEvent.VK_4); // Default binding for SKILL4
    }

    public int getKey(String action) {
        return keyBindings.getOrDefault(action, KeyEvent.VK_UNDEFINED);
    }

    public void setKey(String action, int keyCode) {
        keyBindings.put(action, keyCode);
    }

    public String getKeyName(String action) {
        return KeyEvent.getKeyText(getKey(action));
    }

    public Map<String, Integer> getAllBindings() {
        return keyBindings;
    }

    public void resetToDefault() {
        keyBindings.clear();
        keyBindings.put(UP, KeyEvent.VK_UP);
        keyBindings.put(DOWN, KeyEvent.VK_DOWN);
        keyBindings.put(LEFT, KeyEvent.VK_LEFT);
        keyBindings.put(RIGHT, KeyEvent.VK_RIGHT);
        keyBindings.put(CHOOSE, KeyEvent.VK_ENTER);
        keyBindings.put(ESCAPE, KeyEvent.VK_ESCAPE);
        keyBindings.put(ATTACK, KeyEvent.VK_SPACE);
        keyBindings.put(INVENTORY, KeyEvent.VK_I);
        keyBindings.put(CHARACTER, KeyEvent.VK_C);
        keyBindings.put(RESET, KeyEvent.VK_R);
        keyBindings.put(FLASH, KeyEvent.VK_F);
        keyBindings.put(QUICK_USE, KeyEvent.VK_D);
        keyBindings.put(SKILLS, KeyEvent.VK_S); // Reset SKILLS binding
        keyBindings.put(SKILL1, KeyEvent.VK_1); // Reset SKILL1 binding
        keyBindings.put(SKILL2, KeyEvent.VK_2); // Reset SKILL2 binding
        keyBindings.put(SKILL3, KeyEvent.VK_3); // Reset SKILL3 binding
        keyBindings.put(SKILL4, KeyEvent.VK_4); // Reset SKILL4 binding
    }
}
