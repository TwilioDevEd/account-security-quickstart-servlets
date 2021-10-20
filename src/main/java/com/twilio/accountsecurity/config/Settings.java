package com.twilio.accountsecurity.config;

import io.github.cdimascio.dotenv.Dotenv;

import java.util.Arrays;
import java.util.List;

public class Settings {

    public static List<String> loginRequiredPaths = Arrays.asList("/2fa");
    public static List<String> twoFaRequiredPaths = Arrays.asList("/protected");

    private static final Dotenv dotenv = Dotenv.load();

    public static String authyId() {
        return dotenv.get("ACCOUNT_SECURITY_API_KEY");
    }

    public static Dotenv getDotenv() {
        return dotenv;
    }
}
