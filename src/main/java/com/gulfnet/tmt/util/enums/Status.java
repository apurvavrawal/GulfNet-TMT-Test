package com.gulfnet.tmt.util.enums;



public enum Status {

    ACTIVE("Active", "1"), INACTIVE("InActive", "2"),
    PENDING("Pending", "3"), EXPIRED("Expired", "4"),
    COMPLETED("Completed", "5");

    private final String name;
    private final String value;

    Status(String name, String value) {
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

