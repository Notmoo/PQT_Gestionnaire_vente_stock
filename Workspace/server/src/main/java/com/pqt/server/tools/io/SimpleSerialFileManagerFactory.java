package com.pqt.server.tools.io;

import java.nio.file.Path;

public class SimpleSerialFileManagerFactory {
    protected SimpleSerialFileManagerFactory(){}

    public static <T> ISerialFileManager<T> getFileManager(Class<T> clazz, String filePath){
        return new SimpleSerialFileManager<>(filePath, clazz);
    }

    public static <T> ISerialFileManager<T> getFileManager(Class<T> clazz, Path filePath){
        return new SimpleSerialFileManager<>(filePath, clazz);
    }
}
