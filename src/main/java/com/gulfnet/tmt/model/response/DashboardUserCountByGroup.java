package com.gulfnet.tmt.model.response;

import lombok.*;

@AllArgsConstructor
@Builder
@Getter
@Setter
@NoArgsConstructor
public class DashboardUserCountByGroup {
    private String groupName;
    private long totalHQMobileUsers;
    private long totalDeleteMobileUsers;
    private long totalBasicMobileUsers;
}
