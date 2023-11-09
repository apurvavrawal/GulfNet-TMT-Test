package com.josh.gulfnet.model;

import java.util.Map;

    public interface JwtGeneratorInterface {
        Map<String, String> generateToken(User user);
    }

