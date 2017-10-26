package com.pqt.server.tools.security;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5HashTool extends AbstractSimpleHashTool{
    public MD5HashTool() {
        setInstanceCode("MD5");
    }
}
