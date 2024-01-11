package com.gulfnet.tmt.util.enums;

public enum ChatStatus {
    ONLINE("online"), OFFLINE("offline");

    private final String chatStatus;

    ChatStatus(String chatStatus) {
        this.chatStatus = chatStatus;
    }
}
