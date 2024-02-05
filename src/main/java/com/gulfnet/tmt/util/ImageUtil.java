package com.gulfnet.tmt.util;

import com.gulfnet.tmt.config.GulfNetTMTServiceConfig;
import com.gulfnet.tmt.exceptions.GulfNetTMTException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Base64;

@RequiredArgsConstructor
@Slf4j
public class ImageUtil {
    private final static boolean isImageDataStoredOnpremises = true;

    public static String getB64EncodedStringFromImagePathOrURL(String imagePathURL) {
        if(StringUtils.isEmpty(imagePathURL)) {
            return StringUtils.EMPTY;
        }
        return isImageDataStoredOnpremises ?
                    getImageFromPathAsBase64(imagePathURL) : getImageFromURLAsBase64(imagePathURL);
    }

    private static String getImageFromPathAsBase64(String imagePath) {
        try {
            byte[] fileContent = FileUtils.readFileToByteArray(new File(imagePath));
            return Base64.getEncoder().encodeToString(fileContent);
        } catch (IOException ex) {
            log.error("Error in download image {} ", imagePath , ex);
            return StringUtils.EMPTY;
        }
    }

    private static String getImageFromURLAsBase64(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            try (InputStream inputStream = url.openStream()) {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                byte[] imageBytes = outputStream.toByteArray();
                return Base64.getEncoder().encodeToString(imageBytes);
            }
        } catch (IOException e) {
            log.error("Error in download image {} ", imageUrl , e);
            return StringUtils.EMPTY;
        }
    }

}
