package com.socialbeat.influencer.app;

import com.socialbeat.influencer.R;

/**
 * Created by karthik.
 */
public class Config {
    // server URL configuration
    public static final String baseurl = String.valueOf(R.string.base_url);
    public static final String URL_REQUEST_SMS_LOG = baseurl+"updatemobile.php";
    public static final String URL_VERIFY_OTP = baseurl+"verify_otp.php";
    public static final String URL_SMS_OTP = baseurl+"doSendOTP";
    public static final String URL_REGISTER = baseurl+"registerNewUser";
    // SMS provider identification
    // It should match with your SMS gateway origin
    // You can use  MSGIND, TESTER and ALERTS as sender ID
    // If you want custom sender Id, approve MSG91 to get one
    public static final String SMS_ORIGIN = "INFLUE";
    // special character to prefix the otp. Make sure this character appears only once in the sms
    public static final String OTP_DELIMITER = ":";
//    // File upload url (replace the ip with your server address)
//    public static final String FILE_UPLOAD_URL = baseurl+"fileUpload.php";

//    // Directory name to store captured images and videos
//    public static final String IMAGE_DIRECTORY_NAME = "Influencer";
}
