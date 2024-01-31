package com.gulfnet.tmt.model.request;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
public class TokenPostRequest {

    private String fcmToken ;

}
