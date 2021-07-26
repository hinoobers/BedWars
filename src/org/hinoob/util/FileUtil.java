package org.hinoob.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.io.*;
import java.lang.reflect.Field;

@UtilityClass
public class FileUtil {

    @SneakyThrows
    public void copy(File toCopy, File destination){
        if(toCopy.isDirectory()){
            if(!destination.exists()){
                destination.mkdir();
            }

            String[] files = toCopy.list();
            if(files == null) return;
            for(String file : files){
                File newSource = new File(toCopy, file);
                File newDestination = new File(destination, file);
                copy(newSource, newDestination);
            }
        }else{
            InputStream in = new FileInputStream(toCopy);
            OutputStream out = new FileOutputStream(destination);

            byte[] buffer = new byte[1024];

            int length;

            while((length = in.read(buffer)) > 0){
                out.write(buffer, 0, length);
            }

            in.close();
            out.close();
        }
    }

    public void delete(File file){
        if(file.isDirectory()){
            File[] files = file.listFiles();
            if(files == null) return;
            for(File child : files){
                delete(child);
            }
        }

        file.delete();
    }
}
