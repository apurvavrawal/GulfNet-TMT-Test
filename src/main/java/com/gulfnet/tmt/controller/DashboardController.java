package com.gulfnet.tmt.controller;

import com.gulfnet.tmt.model.response.DashboardResponse;
import com.gulfnet.tmt.model.response.ResponseDto;
import com.gulfnet.tmt.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    @Operation(summary = "Dashboard API")
    public ResponseDto<DashboardResponse> getDashboardData() {
        return dashboardService.getDashboardData();
    }
}
