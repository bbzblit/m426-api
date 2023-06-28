package dev.bbzblit.m426.service;

import dev.bbzblit.m426.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.security.spec.KeySpec;


public class HashService {

    /**
     * Method to convert a byte array to a hex string
     * @param bytes the byte array
     * @return the resulting hex string
     */
    public static String toHexString(byte[] bytes)
    {
        StringBuilder result = new StringBuilder();

        for (byte i : bytes) {
            int decimal = (int)i & 0XFF;
            String hex = Integer.toHexString(decimal);

            if (hex.length() % 2 == 1) {
                hex = "0" + hex;
            }

            result.append(hex);
        }
        return result.toString();
    }

    /**
     * Method to verify if the hased password matches the plain text password
     * @param password plain text password
     * @param salt the salt value to hash
     * @param expectedPassword the password that is expected to be returned
     * @return true if it matches otherwise false
     */
    public static boolean verifyPassword(String password, byte[] salt, String expectedPassword){

        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        SecretKeyFactory factory;
        try{
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = factory.generateSecret(spec).getEncoded();
            if(expectedPassword.equals(toHexString(hash))){
                return true;
            }

        }catch (Exception ex){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unable currently to Login");
        }

        return false;
    }

    /**
     * Method to hash the password of a user and create a s secure random salt
     * @param user the providet user
     * @return user with hashed password.
     */
    public static User hashPassword(User user){

        //Generate Salt
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        user.setSaltValue(salt);

        KeySpec spec = new PBEKeySpec(user.getPassword().toCharArray(), salt, 65536, 128);
        SecretKeyFactory factory;
        try{
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = factory.generateSecret(spec).getEncoded();
            user.setPassword(toHexString(hash));

        }catch (Exception ex){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unable to currently create new Users");
        }

        return user;
    }
}
