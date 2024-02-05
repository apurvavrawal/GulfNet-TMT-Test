package com.gulfnet.tmt.service;

import com.gulfnet.tmt.dao.DashboardDao;
import com.gulfnet.tmt.model.response.DashboardResponse;
import com.gulfnet.tmt.model.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final DashboardDao dashboardDao;

    public ResponseDto<DashboardResponse> getDashboardData() {
        return dashboardDao.getDashboardData();
    }
}
