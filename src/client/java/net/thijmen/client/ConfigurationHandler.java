package net.thijmen.client;

import net.fabricmc.loader.api.FabricLoader;

import javax.crypto.SecretKey;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class ConfigurationHandler {
    public static Path configFile = FabricLoader.getInstance()
            .getConfigDir()
            .resolve("secure_msg.txt");

    public static void readConfig() {
        System.out.println(configFile.toString());
        try {
            if (Files.notExists(configFile)) {
                Files.createFile(configFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        File file = new File(configFile.toUri());
        try (Scanner myReader = new Scanner(file)) {
            int i = 0;
            SecureMsgClient.getPlayerKeyEntries().clear();
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] splitData = data.split(",");
                System.out.println(data);
                SecureMsgClient.getPlayerKeyEntries().add(new PlayerKeyEntry(splitData[0].trim(), SecureMsgEncryptionHelper.getKeyFromString(splitData[1].trim())));
            }
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void writeConfig() {
        try {
            deleteFile();
            createFile();
            FileWriter fileWriter = new FileWriter(configFile.toFile());
            int size = SecureMsgClient.getPlayerKeyEntries().size();
            for (int i = 0; i < size; i++) {
                String playerName = SecureMsgClient.getPlayerKeyEntries().get(i).getPlayerName();
                String key = SecureMsgEncryptionHelper.getString(SecureMsgClient.getPlayerKeyEntries().get(i).getKey());
                fileWriter.write(playerName + ", " + key + "\n");
                System.out.println("writing: " + playerName + ", " + key);
            }
            fileWriter.close();
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void deleteFile() {
        File file = new File(configFile.toUri());
        file.delete();
    }

    private static void createFile() {
        try {
            if (Files.notExists(configFile)) {
                Files.createFile(configFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
