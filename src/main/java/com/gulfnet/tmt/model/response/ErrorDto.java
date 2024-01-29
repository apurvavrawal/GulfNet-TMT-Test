package com.gulfnet.tmt.model.response;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ErrorDto {

    private String errorCode;
    private String errorMessage;
}
