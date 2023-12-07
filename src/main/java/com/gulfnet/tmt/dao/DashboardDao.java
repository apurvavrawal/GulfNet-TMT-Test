package com.gulfnet.tmt.dao;

import com.gulfnet.tmt.model.response.DashboardResponse;
import com.gulfnet.tmt.model.response.GroupResponse;
import com.gulfnet.tmt.model.response.ResponseDto;
import com.gulfnet.tmt.repository.sql.GroupRepository;
import com.gulfnet.tmt.repository.sql.UserRepository;
import com.gulfnet.tmt.util.AppConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DashboardDao {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    public ResponseDto<DashboardResponse> getDashboardData() {
        DashboardResponse dashboardResponse = new DashboardResponse();

        Page<GroupResponse> allGroups = groupRepository.findAllGroups(PageRequest.of(0, 200));
        dashboardResponse.setGroups(allGroups.getContent());

        List<Object[]> userCountByAppType = userRepository.countUsersByAppType();
        for(Object[] objects : userCountByAppType) {
          if (AppConstants.APP_TYPE_MOBILE.get(0).equalsIgnoreCase((String) objects[1])) {
              dashboardResponse.setTotalAdminUsers((Long) objects[0]);
          } else if (AppConstants.APP_TYPE_MOBILE.get(1).equalsIgnoreCase((String) objects[1])) {
              dashboardResponse.setTotalMobileUsers((Long) objects[0]);
          }
        }

        List<Object[]> mobileUserCountByAppRole = userRepository.countMobileUsersByRole();
        for(Object[] objects : mobileUserCountByAppRole) {
            if (AppConstants.MOBILE_USER_ROLE_VALUE.get(0).equalsIgnoreCase((String) objects[1])) {
                dashboardResponse.setTotalMobileUsers((Long) objects[0]);
            } else if (AppConstants.MOBILE_USER_ROLE_VALUE.get(1).equalsIgnoreCase((String) objects[1])) {
                dashboardResponse.setTotalHQMobileUsers((Long) objects[0]);
            } else if (AppConstants.MOBILE_USER_ROLE_VALUE.get(2).equalsIgnoreCase((String) objects[1])) {
                dashboardResponse.setTotalDeleteMobileUsers((Long) objects[0]);
            }
        }

        return ResponseDto.<DashboardResponse>builder().data(List.of(dashboardResponse)).build();
    }
}
