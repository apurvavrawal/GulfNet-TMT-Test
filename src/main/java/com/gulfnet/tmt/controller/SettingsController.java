package com.gulfnet.tmt.controller;

import com.gulfnet.tmt.model.request.SettingsRequest;
import com.gulfnet.tmt.model.response.ResponseDto;
import com.gulfnet.tmt.model.response.SettingsResponse;
import com.gulfnet.tmt.service.SettingsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/settings")
public class SettingsController {

    private final SettingsService settingsService;

    @GetMapping
    public ResponseDto<SettingsResponse> getSettings() {
        return settingsService.getSettings();
    }

    @PutMapping
    public ResponseDto<SettingsResponse> updateSettings(@RequestBody SettingsRequest settingsRequest) {
        return settingsService.updateSettings(settingsRequest);
    }
}
