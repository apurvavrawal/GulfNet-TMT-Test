package com.gulfnet.tmt.util.enums;

public enum Action {

    CHANGE_PASSWORD("ChangePassword", "1"), RESET_PASSWORD("ResetPassword", "2");

    private final String name;
    private final String value;

    Action(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
