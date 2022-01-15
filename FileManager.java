package test.test;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileManager {
    private final Map<String, MFile> files;
    private final Plugin instance;
    FileManager(Plugin instance) {
        this.instance = instance;
        files = new HashMap<>();
    }

    /**
     * Add an item to the file manager
     * @param id The ID of the file, you will access the file using this ID
     * @param file The file that will be registered
     */
    public void addFile(String id, File file) {
        var managedFile = new MFile(file);
        files.put(id, managedFile);
    }

    /**
     * Remove a file from the file manager
     * @param id The ID you used to register this file
     */
    public void removeFile(String id) {
        files.remove(id);
    }

    /**
     * Get an existing item from the file manager, returns null if it does not exist
     * @param id The ID you used to register the file
     * @return The file, or null if it does not exist
     */
    public MFile getFile(String id) {
        if (!files.containsKey(id))
            return null;
        return files.get(id);
    }

    /**
     * Used to get the file manager's map variable. Is not recommended
     */
    public Map<String, MFile> getFilesMap() {
        return files;
    }
    /**
     * Creates a file, if the file already exists update the file saving the old configurations
     * @param pathInConfig The config path that contains the name of the file, generates the path if it does not exist
     * @param fileName The name of the file that is in the JAR itself
     * @return The updated/generated file
     */
    File create(String pathInConfig, String fileName) {
        var filePath = instance.getConfig().getString(pathInConfig);
        if (filePath == null) {
            instance.getConfig().set(pathInConfig, fileName);
            filePath = instance.getConfig().getString(pathInConfig);
            instance.saveConfig();
        }
        filePath += ".yml";
        var existingFile = new File(instance.getDataFolder(), filePath);
        if (!existingFile.exists()) {
            // If the existing file does not exist, create a new file from the JAR and rename it to the value in config
            instance.saveResource(fileName + ".yml", false);
            var defaultFile = new File(instance.getDataFolder(), fileName + ".yml");
            var i = 5;
            while (!defaultFile.renameTo(existingFile) && i > 0)
                i--;
            return existingFile;
        }
        // Loop through all the values to make sure they are updated
        var existingFileConfiguration = new YamlConfiguration();
        try {
            existingFileConfiguration.load(existingFile); // Loading the existing files values to the configuration
        } catch (IOException | InvalidConfigurationException e) { e.printStackTrace(); }
        instance.saveResource(fileName + ".yml", true);
        var newFile = new File(instance.getDataFolder(), fileName + ".yml"); // Create a file from the JAR
        var newFileConfiguration = new YamlConfiguration();
        try {
            newFileConfiguration.load(newFile); // Create a file from the JAR
        }
        catch (IOException | InvalidConfigurationException e) { e.printStackTrace(); }
        Map<String, Object> sec = existingFileConfiguration.getValues(true);
        for (Map.Entry<String, Object> entry : sec.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value.equals(newFileConfiguration.get(key)) || !newFileConfiguration.contains(key))
                continue;
            newFileConfiguration.set(key, value);
        }
        try {
            newFileConfiguration.save(newFile);
        } catch (IOException e) { e.printStackTrace(); }
        var i = 5;
        while (!newFile.renameTo(existingFile) && i > 0)
            i--;
        return existingFile;
    }
}
