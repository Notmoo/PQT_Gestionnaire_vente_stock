package com.pqt.server.tools.security;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public abstract class AbstractSimpleHashTool implements IHashTool {

    private static final String DEFAULT_INSTANCE_CODE = "SHA-256";

    private String instanceCode;

    @Override
    public final String hashAndSalt(String input, String salt) {
        String encryptedInput = null;

        if(input == null || salt == null) return null;

        try {
            String str = salt+input;
            //Create MessageDigest object with the instance code
            MessageDigest digest = (instanceCode!=null?MessageDigest.getInstance(instanceCode):MessageDigest.getInstance(DEFAULT_INSTANCE_CODE));

            //Update input string in message digest
            digest.update(str.getBytes(), 0, str.length());

            //Converts message digest value in base 16 (hex)
            encryptedInput = new BigInteger(1, digest.digest()).toString(16);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return encryptedInput;
    }

    protected void setInstanceCode(String instanceCode){
        this.instanceCode = instanceCode;
    }
}
