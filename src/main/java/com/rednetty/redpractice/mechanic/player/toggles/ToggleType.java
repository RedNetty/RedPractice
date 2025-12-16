package com.rednetty.redpractice.mechanic.player.toggles;

public enum ToggleType {
    DEBUG("Chat Debug", true),
    CHAOTIC("Anti-Chaotic", false),
    PVP("Anti-PVP", true),
    FRIENDLYFIRE("Friendly Fire", false);


    private boolean onByDefault;
    private String displayName;


    ToggleType(String displayName, boolean onByDefault) {
        this.onByDefault = onByDefault;
        this.displayName = displayName;
    }

    public boolean isOnByDefault() {
        return onByDefault;
    }

    public String getDisplayName() {
        return displayName;
    }
}
