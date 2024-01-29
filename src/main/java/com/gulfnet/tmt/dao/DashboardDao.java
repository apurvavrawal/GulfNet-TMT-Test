package com.gulfnet.tmt.dao;

import com.gulfnet.tmt.model.response.DashboardResponse;
import com.gulfnet.tmt.model.response.DashboardUserCountByGroup;
import com.gulfnet.tmt.model.response.ResponseDto;
import com.gulfnet.tmt.repository.sql.GroupRepository;
import com.gulfnet.tmt.repository.sql.UserRepository;
import com.gulfnet.tmt.util.AppConstants;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DashboardDao {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    public ResponseDto<DashboardResponse> getDashboardData() {
        DashboardResponse dashboardResponse = new DashboardResponse();
        dashboardResponse.setTotalGroups(groupRepository.count());
        setUserCountByAppType(dashboardResponse);
        setMobileUserCountByAppRole(dashboardResponse);
        setGroupsByUserCount(dashboardResponse);
        return ResponseDto.<DashboardResponse>builder().data(List.of(dashboardResponse)).build();
    }
    private void setUserCountByAppType(DashboardResponse dashboardResponse) {
        List<Object[]> userCountByAppType = userRepository.countUsersByAppType();
        for(Object[] objects : userCountByAppType) {
            if (AppConstants.APP_TYPE_MOBILE.get(0).equalsIgnoreCase((String) objects[1])) {
                dashboardResponse.setTotalAdminUsers((Long) objects[0]);
            } else if (AppConstants.APP_TYPE_MOBILE.get(1).equalsIgnoreCase((String) objects[1])) {
                dashboardResponse.setTotalMobileUsers((Long) objects[0]);
            }
        }
    }
    private void setMobileUserCountByAppRole(DashboardResponse dashboardResponse) {
        List<Object[]> mobileUserCountByAppRole = userRepository.countMobileUsersByRole();
        for(Object[] objects : mobileUserCountByAppRole) {
            if (AppConstants.MOBILE_USER_ROLE_VALUE.get(0).equalsIgnoreCase((String) objects[1])) {
                dashboardResponse.setTotalBasicMobileUsers((Long) objects[0]);
            } else if (AppConstants.MOBILE_USER_ROLE_VALUE.get(1).equalsIgnoreCase((String) objects[1])) {
                dashboardResponse.setTotalHQMobileUsers((Long) objects[0]);
            } else if (AppConstants.MOBILE_USER_ROLE_VALUE.get(2).equalsIgnoreCase((String) objects[1])) {
                dashboardResponse.setTotalDeleteMobileUsers((Long) objects[0]);
            }
        }
    }
    private void setGroupsByUserCount(DashboardResponse dashboardResponse) {
        DashboardUserCountByGroup dashboardUserCountByGroup;
        List<Object[]> countMobileUsersByGroup = userRepository.countMobileUsersByGroup();
        HashMap<String, DashboardUserCountByGroup> userCountByGroups = new HashMap<>();
        for (Object[] objects : countMobileUsersByGroup) {
            if (userCountByGroups.containsKey(objects[2])) {
                dashboardUserCountByGroup = userCountByGroups.get(objects[2]);
            } else {
                dashboardUserCountByGroup = new DashboardUserCountByGroup();
                dashboardUserCountByGroup.setGroupName((String) objects[2]);
                userCountByGroups.put((String) objects[2], dashboardUserCountByGroup);

            }
            if (AppConstants.MOBILE_USER_ROLE_VALUE.get(0).equalsIgnoreCase((String) objects[1])) {
                dashboardUserCountByGroup.setTotalBasicMobileUsers((Long) objects[0]);
            } else if (AppConstants.MOBILE_USER_ROLE_VALUE.get(1).equalsIgnoreCase((String) objects[1])) {
                dashboardUserCountByGroup.setTotalHQMobileUsers((Long) objects[0]);
            } else if (AppConstants.MOBILE_USER_ROLE_VALUE.get(2).equalsIgnoreCase((String) objects[1])) {
                dashboardUserCountByGroup.setTotalDeleteMobileUsers((Long) objects[0]);
            }
            userCountByGroups.put((String) objects[2], dashboardUserCountByGroup);
        }
        dashboardResponse.setGroups(userCountByGroups.keySet().stream().filter(StringUtils::isNotEmpty).map(userCountByGroups::get).collect(Collectors.toList()));
    }

}
