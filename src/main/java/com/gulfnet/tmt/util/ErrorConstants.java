package com.gulfnet.tmt.util;

public final class ErrorConstants {

    public static String ENCRYPTION_ERROR_CODE = "710";
    public static String ENCRYPTION_ERROR_MESSAGE = "Error in encryption";
    public static String DECRYPTION_ERROR_CODE = "711";
    public static String DECRYPTION_ERROR_MESSAGE = "Error in decryption";

    //Login Exception
    public static String LOGIN_USER_NOT_FOUND_ERROR_CODE = "701";
    public static String LOGIN_USER_NOT_FOUND_ERROR_MESSAGE = "User Not Found, Please provide the correct UserName or Password";

    public static String NOT_FOUND_ERROR_CODE = "721";
    public static String NOT_FOUND_ERROR_MESSAGE = "{0} Not Found";
    public static String NOT_VALID_ERROR_CODE = "722";
    public static String NOT_VALID_ERROR_MESSAGE = "{0} is invalid.";
    public static String NOT_VALID_ERROR_MESSAGE_DESC = "{0} is invalid. {1}";
    public static String NOT_VALID_ERROR_MESSAGE_PASSWORD = "Password is not valid, It should have at least One Capital/Number/Special character and minimum length is 8.";
    public static String NOT_VALID_ERROR_MESSAGE_CHANGE_PASSWORD = "Password is not valid, It should not be same as previous one.";
    public static String NOT_MATCH_ERROR_CODE = "723";
    public static String NOT_MATCH_ERROR_MESSAGE = "{0} and {1} is not matching.";
    public static String MANDATORY_ERROR_CODE = "724";
    public static String MANDATORY_ERROR_MESSAGE = "{0} is mandatory.";
    public static String ALREADY_PRESENT_ERROR_CODE = "725";
    public static String ALREADY_PRESENT_ERROR_MESSAGE = "{0} : {1} is already present.";
    public static String OTP_EXPIRED = "Otp is expired";

    public static String SYSTEM_ERROR_CODE = "500";
    public static String ALREADY_LOGGED_IN_ERROR_CODE = "601";
    public static String ALREADY_LOGGED_IN_ERROR_MESSAGE = "You are already logged in from another device," +
            " Do you still want to continue to login here?";
    public static String JWT_TOKEN_EXPIRED_ERROR_CODE = "401";
    public static String JWT_TOKEN_EXPIRED_ERROR_MESSAGE = "JWT Token is expired.";
}
