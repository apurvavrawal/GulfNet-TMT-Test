package com.gulfnet.tmt.model.request;

import lombok.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
public class UserContactsRequest {
    private List<UUID> newContactIds;
    private List<UUID> removeContactIds;
}
