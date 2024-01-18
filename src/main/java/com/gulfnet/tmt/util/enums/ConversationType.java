package com.gulfnet.tmt.util.enums;

import lombok.Getter;
import lombok.ToString;

import java.util.Arrays;
import java.util.Optional;

@Getter
@ToString
public enum ConversationType {
    PRIVATE("private"),
    GROUP("group");

    private final String conversationType;
    ConversationType(String type){
        this.conversationType= type;
    }
    public static Optional<ConversationType> get(String convType) {
        return Arrays.stream(ConversationType.values())
                .filter(env -> env.getConversationType().equalsIgnoreCase(convType))
                .findFirst();
    }
}
