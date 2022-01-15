package test.test;

import org.bukkit.plugin.java.JavaPlugin;

public final class Test extends JavaPlugin {
    private static Test instance;
    public ManagedFile configFile;
    public ManagedFile exampleFile;

    @Override
    public void onEnable() {
        instance = this;
        var createFiles = new CreateFiles(this);

        configFile = new ManagedFile(createFiles.create("file_management.example_file_name", "test"));
        /* OR */
        exampleFile = new ManagedFile();
        exampleFile.setFile(createFiles.create("file_management.example_file_name", "test"));
        exampleFile.updateFileConfiguration();
    }
    public static Test getInstance() { return instance; }
}
