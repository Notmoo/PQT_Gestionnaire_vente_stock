package com.pqt.server.tools.security;

public interface IHashTool {
    String hashAndSalt(String str, String salt);
}
