package com.gulfnet.tmt.model.response;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@Builder
@Getter
@Setter
@NoArgsConstructor
public class DashboardResponse {
    private long totalHQMobileUsers;
    private long totalAdminUsers;
    private long totalDeleteMobileUsers;
    private long totalMobileUsers;
    private long totalGroups;
    private long totalBasicMobileUsers;
    private List<DashboardUserCountByGroup> groups;
}
