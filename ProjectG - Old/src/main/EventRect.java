package main;

import java.awt.Rectangle;

public class EventRect extends Rectangle{

    int eventRectDefaultX, eventRectDefaultY;
    boolean eventDone = false; // Flag to check if the event has been triggered
    int eventCooldown = 0; // Cooldown for the event trigger
}
