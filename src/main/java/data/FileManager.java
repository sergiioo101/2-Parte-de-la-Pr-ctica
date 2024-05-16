package data;

import model.Experimento;
import java.io.*;

public class FileManager {
    public static final String DIRECTORY = "experimentos/";

    public static void saveExperiment(Experimento experimento, String filename) {
        try {
            File dir = new File(DIRECTORY);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, filename);
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
                oos.writeObject(experimento);
                System.out.println("Experimento guardado exitosamente en: " + file.getAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("Error al guardar el experimento: " + e.getMessage());
        }
    }

    public static Experimento loadExperiment(String filename) {
        try {
            File file = new File(DIRECTORY, filename);
            if (!file.exists()) {
                System.err.println("El archivo no existe: " + file.getAbsolutePath());
                return null;
            }
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                return (Experimento) ois.readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al cargar el experimento: " + e.getMessage());
            return null;
        }
    }
}







