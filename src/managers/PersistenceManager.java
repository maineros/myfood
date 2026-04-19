package managers;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class PersistenceManager {

    public static void salvar(Object objeto, String arquivo) {
        try {
            File file = new File(arquivo);
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs(); 
            }

            XMLEncoder encoder = new XMLEncoder(
                new BufferedOutputStream(new FileOutputStream(file))
            );
            encoder.writeObject(objeto);
            encoder.close();
        } catch (Exception e) {
            System.out.println("Erro ao salvar o arquivo " + arquivo + ": " + e.getMessage());
        }
    }

    public static Object carregar(String arquivo) {
        try {
            XMLDecoder decoder = new XMLDecoder(
                new BufferedInputStream(new FileInputStream(arquivo))
            );
            Object objeto = decoder.readObject();
            decoder.close();
            return objeto;
        } catch (Exception e) {
            return null;
        }
    }
}