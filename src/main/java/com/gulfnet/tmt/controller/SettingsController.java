package com.gulfnet.tmt.controller;

import com.gulfnet.tmt.model.request.SettingsRequest;
import com.gulfnet.tmt.model.response.ResponseDto;
import com.gulfnet.tmt.model.response.SettingsResponse;
import com.gulfnet.tmt.service.SettingsService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = " Retrieve current application settings.")
    public ResponseDto<SettingsResponse> getSettings() {
        return settingsService.getSettings();
    }

    @PutMapping
    @Operation(summary = "Modify application settings with new values.")
    public ResponseDto<SettingsResponse> updateSettings(@RequestBody SettingsRequest settingsRequest) {
        return settingsService.updateSettings(settingsRequest);
    }
}
