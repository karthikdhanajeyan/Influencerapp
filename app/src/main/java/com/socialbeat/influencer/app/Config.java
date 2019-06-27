package com.socialbeat.influencer.app;

import com.socialbeat.influencer.R;

/**
 * Created by karthik.
 */
public class Config {
    // server URL configuration
    public static final String baseurl = String.valueOf(R.string.base_url);
    public static final String baseurl_v6 = String.valueOf(R.string.base_url_v6);

    public static final String URL_CHECK_EMAIL = baseurl_v6 + R.string.userexit_register_url;

    public static final String URL_REQUEST_SMS_LOG = baseurl+R.string.userexit_register_url;
    public static final String URL_VERIFY_OTP = baseurl+"verify_otp.php";
    public static final String URL_SMS_OTP = baseurl+"doSendOTP";
    public static final String URL_REGISTER = baseurl+"registerNewUser";




    public static final String SMS_ORIGIN = "INFLUE";
    public static final String OTP_DELIMITER = ":";

}
