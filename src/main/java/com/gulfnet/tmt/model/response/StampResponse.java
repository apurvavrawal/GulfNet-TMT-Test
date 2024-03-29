package com.gulfnet.tmt.model.response;

import com.gulfnet.tmt.util.ImageUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
public class StampResponse {

    private UUID id;
    private String stampFile;
    private String status;

    public String getStampFile() {
        return  ImageUtil.getB64EncodedStringFromImagePathOrURL(stampFile);
    }
}