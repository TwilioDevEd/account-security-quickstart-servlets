package com.twilio.accountsecurity.services;

public class Settings {

    public static String authyId(){
        return System.getenv("ACCOUNT_SECURITY_API_KEY");
    }
}
