package test.test;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class CreateFiles {
    private final Plugin instance;

    public CreateFiles(Plugin instance) { this.instance = instance; }

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
        File returnValue;
        if (!existingFile.exists()) {
            // If the existing file does not exist, create a new file from the JAR and rename it to the value in config
            instance.saveResource(fileName + ".yml", false);
            var defaultFile = new File(instance.getDataFolder(), fileName + ".yml");
            boolean f = defaultFile.renameTo(existingFile);
            if (!f)
                returnValue = create(pathInConfig, fileName); // If it fails, try again
            else returnValue = existingFile;
            // We don't need to update the values here since it will already get the values from the JAR
        } else {
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
            newFile.renameTo(existingFile);
            returnValue = existingFile;
        }
        return returnValue;
    }
}