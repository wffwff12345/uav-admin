package com.example.uavground.mavlink.menu;

public enum MessageMenu {
    VEHICLEINFO("vehicleInfo"),
    HEARTBEAT("heartbeat");
    private final String value;

    MessageMenu(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
