package com.pqt.server.tools.security;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5HashTool implements IHashTool{
    @Override
    public String hashAndSalt(String input, String salt) {
        String md5 = null;

        if(input == null || salt == null) return null;

        try {
            String str = salt+input;
            //Create MessageDigest object for MD5
            MessageDigest digest = MessageDigest.getInstance("MD5");

            //Update input string in message digest
            digest.update(str.getBytes(), 0, str.length());

            //Converts message digest value in base 16 (hex)
            md5 = new BigInteger(1, digest.digest()).toString(16);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return md5;
    }
}
