package com.pqt.server.tools;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtil {

    /**
     * @see #createFileIfNotExist(Path)
     */
    public static boolean createFileIfNotExist(String filePath) throws IOException {
        return createFileIfNotExist(Paths.get(filePath));
    }

    /**
     * Check if the given file path correspond to an existing file, and create it if it doesn't.
     *
     * @param filePath the file path to check
     *
     * @return {@code true} if the file has been created, {@code false} if it already existed.
     * @throws IOException if any IOException happend during this method's execution.
     */
    public static boolean createFileIfNotExist(Path filePath) throws IOException {
        if(!FileUtil.exist(filePath)){
            Files.createFile(filePath);
            return true;
        }
        return false;
    }

    public static boolean exist(Path path) {
        return Files.exists(path);
    }

    public static boolean exist(String path) {
        return Files.exists(Paths.get(path));
    }
}
