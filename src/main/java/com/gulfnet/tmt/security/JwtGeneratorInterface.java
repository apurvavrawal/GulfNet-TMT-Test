package com.gulfnet.tmt.security;

import com.gulfnet.tmt.model.User;

import java.util.Map;

    public interface JwtGeneratorInterface {
        Map<String, String> generateToken(User user);
    }

