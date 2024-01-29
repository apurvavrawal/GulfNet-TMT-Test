package com.gulfnet.tmt.util;

import com.gulfnet.tmt.entity.sql.LoginAudit;
import com.gulfnet.tmt.exceptions.GulfNetTMTException;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConversionUtil {

    public static Date conversion(LoginAudit loginAudit) {
        try {
            SimpleDateFormat outputFormat = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy");
            String formattedDate = outputFormat.format(loginAudit.getLoginExpiryDate());
            return outputFormat.parse(formattedDate);
        } catch (Exception ex) {
            throw new GulfNetTMTException(ErrorConstants.SYSTEM_ERROR_CODE, ex.getMessage());
        }
    }
}
