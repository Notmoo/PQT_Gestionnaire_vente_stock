package com.pqt.server.tools.io;

import java.util.List;
import java.util.Set;

//TODO Issue #5 : écrire javadoc
public interface ISerialFileManager<T> {
    List<T> loadListFromFile();
    Set<T> loadSetFromFile();
    void saveListToFile(List<T> list);
    void saveSetToFile(Set<T> set);
}
