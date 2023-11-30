package com.gulfnet.tmt.util.enums;

public enum ResponseMessage {
    FILE_UPLOAD_SUCCESSFULLY("File upload successfully"),
    FILE_DELETE_SUCCESSFULLY("File delete successfully"),
    FILE_NOT_FOUND("File not found");
    private final String name;
    ResponseMessage(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
