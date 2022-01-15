package test.test;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;

public class CreateFiles {
    private final Test instance = Test.getInstance();

    public CreateFiles() {
        final long start = System.currentTimeMillis();
        
        createLanguageFile();
        
        final long fin = System.currentTimeMillis() - start;
        String logMessage = "All files have been generated and registered successfully! (" + fin + "ms)";
        instance.getLogger().log(Level.INFO, logMessage);
    }

    void createLanguageFile() {
        var languageFile = createFile("language_file", "default");
        instance.setLanguageFileConfiguration(new YamlConfiguration());
        try {
            instance.getLanguageFile().load(languageFile);
            instance.getLogger().log(Level.INFO, "Successfully loaded the language file!");
        } catch (IOException | InvalidConfigurationException e) { e.printStackTrace(); }
    }

    File createFile(String pathInConfig, String fileName) {
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
            System.out.println("File does not exist");
            // If the existing file does not exist, create a new file from the JAR and rename it to the value in config
            instance.saveResource(fileName + ".yml", false);
            var defaultFile = new File(instance.getDataFolder(), fileName + ".yml");
            boolean f = defaultFile.renameTo(existingFile);
            if (!f)
                returnValue = createFile(pathInConfig, fileName); // If it fails, try again
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
