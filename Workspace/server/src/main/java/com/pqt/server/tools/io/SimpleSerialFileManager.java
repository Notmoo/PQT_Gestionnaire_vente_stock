package com.pqt.server.tools.io;

import com.pqt.server.tools.FileUtil;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

//TODO vérifier que le save écrase bien le contenu précédent du fichier
public class SimpleSerialFileManager<T> implements ISerialFileManager<T> {

    private Path filePath;
    private Class<T> clazz;

    SimpleSerialFileManager(String filePath, Class<T> clazz){
        this(Paths.get(filePath), clazz);
    }

    SimpleSerialFileManager(Path filePath, Class<T> clazz){
        this.filePath = filePath;
        this.clazz = clazz;
        try{
            FileUtil.createFileIfNotExist(filePath);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public List<T> loadListFromFile() {
        try{
            if(!FileUtil.createFileIfNotExist(filePath)){
                List<T> loadedEntries = new ArrayList<>();
                fillCollection(loadedEntries);
                return loadedEntries;
            }
        }catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public Set<T> loadSetFromFile() {
        try{
            if(!FileUtil.createFileIfNotExist(filePath)){
                Set<T> loadedEntries = new HashSet<>();
                fillCollection(loadedEntries);
                return loadedEntries;
            }
        }catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return new HashSet<>();
    }

    private void fillCollection(Collection<T> collection) throws IOException, ClassNotFoundException {
        if(collection==null) return;
        try(FileInputStream fis = new FileInputStream(filePath.toString());
            ObjectInputStream ois = new ObjectInputStream(fis)){
            boolean end = false;
            do{
                try{
                    Object obj = ois.readObject();
                    if(clazz.isInstance(obj)){
                        T ae = clazz.cast(obj);
                        collection.add(ae);
                    }
                }catch (EOFException e){
                    end = true;
                }
            }while(!end);
        }
    }

    @Override
    public void saveListToFile(List<T> list) {
        save(list);
    }

    @Override
    public void saveSetToFile(Set<T> set) {
        save(set);
    }

    private void save(Collection<T> collection){
        try{
            FileUtil.createFileIfNotExist(filePath);
        }catch (IOException e){
            e.printStackTrace();
            return;
        }
        try(FileOutputStream fos = new FileOutputStream(filePath.toString());
            ObjectOutputStream oos = new ObjectOutputStream(fos)){

            collection.forEach(p -> {
                try {
                    oos.writeObject(p);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
