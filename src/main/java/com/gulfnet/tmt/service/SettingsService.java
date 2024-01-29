package com.gulfnet.tmt.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gulfnet.tmt.entity.sql.Settings;
import com.gulfnet.tmt.model.request.SettingsRequest;
import com.gulfnet.tmt.model.response.ResponseDto;
import com.gulfnet.tmt.model.response.SettingsResponse;
import com.gulfnet.tmt.repository.sql.SettingsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SettingsService {
    private final ObjectMapper mapper;
    private final SettingsRepository settingsRepository;

    public SettingsService(ObjectMapper mapper, SettingsRepository settingsRepository) {
        this.mapper = mapper;
        this.settingsRepository = settingsRepository;
    }

    public ResponseDto<SettingsResponse> getSettings() {
        SettingsResponse settingsResponse = mapper.convertValue(settingsRepository.findAll().get(0), SettingsResponse.class);
        return ResponseDto.<SettingsResponse>builder()
                .data(List.of(settingsResponse))
                .build();
    }

    public ResponseDto<SettingsResponse> updateSettings(SettingsRequest settingsRequest) {
        Settings settings = settingsRepository.findAll().get(0);
        settings.setLanguagePreference(settingsRequest.getLanguagePreference());
        settings.setSupportEmail(settingsRequest.getSupportEmail());
        settings.setSupportUrl(settingsRequest.getSupportUrl());
        settings.setSupportPhoneNumber(settingsRequest.getSupportPhoneNumber());
        SettingsResponse settingsResponse = mapper.convertValue(settingsRepository.save(settings), SettingsResponse.class);
        return ResponseDto.<SettingsResponse>builder()
                .data(List.of(settingsResponse))
                .build();
    }
}