package com.twilio.accountsecurity.config;

import java.util.Arrays;
import java.util.List;

public class Settings {

    public static List<String> loginRequiredPaths = Arrays.asList("/2fa");
    public static List<String> twoFaRequiredPaths = Arrays.asList("/protected");

    public static String authyId(){
        return System.getenv("ACCOUNT_SECURITY_API_KEY");
    }
}
