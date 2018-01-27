package com.pqt.server.tools.io;

import com.pqt.server.tools.FileUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

//TODO vérifier que le save écrase bien le contenu précédent du fichier
public class SimpleSerialFileManager<T> implements ISerialFileManager<T> {

    private static Logger LOGGER = LogManager.getLogger(SimpleSerialFileManager.class);

    private Path filePath;
    private Class<T> clazz;

    SimpleSerialFileManager(String filePath, Class<T> clazz){
        this(Paths.get(filePath), clazz);
    }

    SimpleSerialFileManager(Path filePath, Class<T> clazz){
        LOGGER.info("Gestionnaire de fichiers créé pour le fichier '{}'", filePath.toAbsolutePath().toString());
        this.filePath = filePath;
        this.clazz = clazz;
        try{
            FileUtil.createFileIfNotExist(filePath);
        }catch (IOException e){
            LOGGER.error("IOException durant la création d'un fichier : {}", e);
            e.printStackTrace();
        }
    }

    @Override
    public List<T> loadListFromFile() {
        try{
            if(!FileUtil.createFileIfNotExist(filePath)){
                LOGGER.debug("Chargement des données (liste) du fichier '{}'", filePath.toAbsolutePath().toString());
                List<T> loadedEntries = new ArrayList<>();
                fillCollection(loadedEntries);
                return loadedEntries;
            }
        }catch(IOException | ClassNotFoundException e){
            onLoadError(e);
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public Set<T> loadSetFromFile() {
        try{
            if(!FileUtil.createFileIfNotExist(filePath)){
                LOGGER.debug("Chargement des données (set) du fichier '{}'", filePath.toAbsolutePath().toString());
                Set<T> loadedEntries = new HashSet<>();
                fillCollection(loadedEntries);
                return loadedEntries;
            }
        }catch(IOException | ClassNotFoundException e){
            onLoadError(e);
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
            onSaveError(e);
            e.printStackTrace();
            return;
        }
        try(FileOutputStream fos = new FileOutputStream(filePath.toString());
            ObjectOutputStream oos = new ObjectOutputStream(fos)){

            LOGGER.debug("Sauvegarde de données vers le fichier '{}'", filePath.toAbsolutePath().toString());
            collection.forEach(p -> {
                try {
                    oos.writeObject(p);
                } catch (IOException e) {
                    onSaveError(e);
                    e.printStackTrace();
                }
            });
        }catch(IOException e){
            onSaveError(e);
            e.printStackTrace();
        }
    }

    private void onLoadError(Throwable e){
        LOGGER.error("Exception durant le chargement des données du fichier '{}' : {} --> {}",
                filePath.toAbsolutePath().toString(),
                e.getClass().getName(),
                e.getMessage());
    }

    private void onSaveError(Throwable e){
        LOGGER.error("Exception durant la sauvegarde de données vers le fichier '{}' : {} --> {}",
                filePath.toAbsolutePath().toString(),
                e.getClass().getName(),
                e.getMessage());
    }
}
