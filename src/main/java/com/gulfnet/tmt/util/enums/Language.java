package com.gulfnet.tmt.util.enums;

import java.util.Arrays;
import java.util.Optional;

public enum Language {
    ENGLISH("ENG"), JAPANESE("JPN"), THAI("THAI");
    private final String languageCode;

    Language(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getLanguageCode() {
        return languageCode;
    }


    public static Optional<Language> get(String lngCode) {
        return Arrays.stream(Language.values())
                .filter(env -> env.getLanguageCode().equalsIgnoreCase(lngCode))
                .findFirst();
    }

}
