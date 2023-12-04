package com.gulfnet.tmt.util;

import com.gulfnet.tmt.config.GulfNetTMTServiceConfig;
import com.gulfnet.tmt.exceptions.GulfNetTMTException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Base64;

@RequiredArgsConstructor
public class ImageUtil {
    private final static boolean isImageDataStoredOnpremises = true;

    public static String getB64EncodedStringFromImagePathOrURL(String imagePathURL) {
        return isImageDataStoredOnpremises?
                getImageFromPathAsBase64(imagePathURL) : getImageFromURLAsBase64(imagePathURL);
    }

    private static String getImageFromPathAsBase64(String imagePath) {
        try {
            byte[] fileContent = FileUtils.readFileToByteArray(new File(imagePath));
            return Base64.getEncoder().encodeToString(fileContent);
        } catch (IOException ex) {
            throw new GulfNetTMTException(ErrorConstants.SYSTEM_ERROR_CODE, "Unable to decode the image for image path:" + imagePath);
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
            throw new GulfNetTMTException(ErrorConstants.SYSTEM_ERROR_CODE, "Unable to decode the image for image URL:" + imageUrl);
        }
    }

}
