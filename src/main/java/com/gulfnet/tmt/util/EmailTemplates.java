package com.gulfnet.tmt.util;

public class EmailTemplates {

    public static String RESET_PASSWORD_SUBJECT = "Reset Password OTP";
    public static String RESET_PASSWORD_REQUEST = "<h4>Hi {0},</h4>" +
            "<p style=\"margin-left: 20px;\">Your OTP is <b>{1}</b>.</p>" +
            "<p style=\"margin-left: 20px;\">Please do-not share the OTP with any person.</p>" +
            "<p style=\"margin-left: 20px;\">This OTP is valid for 10 minutes only.</p>" +
            "<p style=\"margin-left: 20px;\">This is an auto-generated message, please do not reply to this message."+
            "For any queries, please email us at support@gulfnet.com</p>"+
            "<p style=\"margin-bottom: 10px;\"> Regards,</p>" +
            "<p style=\"margin-bottom: 10px;\"> GulfNet Team </p>";
    public static String PASSWORD_CHANGE_SUBJECT = "Password Change Confirmation";
    public static String CHANGE_PASSWORD_SUCCESS = "<h4>Hi {0},</h4>" +
            "<p style=\"margin-left: 20px;\">Password Changed Successfully.</p>" +
            "<p style=\"margin-left: 20px;\">This is an auto-generated message, please do not reply to this message."+
            "For any queries, please email us at support@gulfnet.com</p>"+
            "<p style=\"margin-bottom: 10px;\"> Regards,</p>" +
            "<p style=\"margin-bottom: 10px;\"> GulfNet Team </p>";


    public static String USER_ONBOARDING_SUCCESS = "<h4>Hi {0} {1},</h4>" +
            "<p style=\"margin-left: 20px;\">User Onboarded  Successfully.</p>" +
            "<p style=\"margin-left: 20px;\">UserName: {2}</p>" +
            "<p style=\"margin-left: 20px;\">Password: {3}</p>" +
            "<p style=\"margin-left: 20px;\">This is an auto-generated message, please do not reply to this message."+
            "For any queries, please email us at support@gulfnet.com</p>"+
            "<p style=\"margin-bottom: 10px;\"> Regards,</p>" +
            "<p style=\"margin-bottom: 10px;\"> GulfNet Team </p>";
}
