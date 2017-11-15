package com.twilio.accountsecurity.services;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Random;

public class PasswordEncoder {

    private Random random;

    public PasswordEncoder() {
        random = new Random();
    }
    public String encode(final String password, byte[] salt) {

        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance( "PBKDF2WithHmacSHA512" );
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 10, 128);
            SecretKey key = skf.generateSecret(spec);
            byte[] res = key.getEncoded();
            return new String(res, StandardCharsets.UTF_8);

        } catch( NoSuchAlgorithmException | InvalidKeySpecException e ) {
            throw new RuntimeException( e );
        }
    }

    public byte[] getSalt() {
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }
}
