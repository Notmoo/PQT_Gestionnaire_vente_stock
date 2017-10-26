package com.pqt.server.tools.security;

public interface IHashTool {
    String hashAndSalt(String input, String salt);
}
