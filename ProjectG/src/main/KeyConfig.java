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
    }
}
