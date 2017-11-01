package com.pqt.server.tools.io;

import java.nio.file.Path;
import java.nio.file.Paths;

public class SimpleSerialFileManagerFactory {
    protected SimpleSerialFileManagerFactory(){}

    public static <T> ISerialFileManager<T> getFileManager(Class<T> clazz, String filePath){
        return SimpleSerialFileManagerFactory.getFileManager(clazz, Paths.get(filePath));
    }

    public static <T> ISerialFileManager<T> getFileManager(Class<T> clazz, Path filePath){
        return new SimpleSerialFileManager<>(filePath, clazz);
    }
}
