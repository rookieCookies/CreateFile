# What is this
This is a file manager that is designed to keep things simple and allow you to organize your files with ease

# The Basics
### Starting
- First of all, import FileManager.class and ManagedFile.class to your project
- After that create a global field with type FileManager in your main class  
- On enable, create a new instance of the FileManager class, and assign it to the field you created
- Adding saveAll() function on disable is recommended to make sure all the files on the Hard Drive is updated
```java
private static FileManager fileManager;
```
```java
@Override
public void onEnable() {
    fileManager = new FileManager(this);

}
```
```java
@Override
public void onDisable() {
    fileManager.saveAll();
    
}
```

### Creating a file
##### Creating a file is extremely simple!
- After assigning your FileManager variable, call the create() function from the FileManager variable
- The first parameter you will use is used to get the file name from the config
- When creating a file, if the file already exists it will copy the existing settings from the old file to the new one so no data will be lost!
```java
fileManager.create("example_config_path", "example");
```

### Adding a file to the FileManager
- After you created your File assign it to a variable
- While referancing your FileManager variable call the function addFile(), this function will require 2 parameters.
- - The first parameter is used for the ID. You will use this ID to referance this file later on
- - The second parameter is for your file, place your file variable there
```java
File file = fileManager.create("example_config_path", "example");
```
```java
fileManager.addFile("example_file", file);
```

### Removing a file from the FileManager
##### This is extremely simple
- While referancing your FileManager variable call the function removeFile(), the first parameter is for the ID you entered while creating the file.
```java
fileManager.removeFile("example_file");
```

### Getting a file from the FileManager
- While referancing your FileManager variable call the function getFile(), the first parameter is for the ID you entered while creating the file.
- This function will **NOT** return a File, it will return a ManagedFile.
- Register the returned value to a variable and call the function getFile() function while referancing this variable
```java
MFile managedFile = fileManager.getFile("example_file");
File actualFile = managedFile.getFile();
```

# ManagedFiles

## What is an ManagedFile
- An MFile is an Object that contains a few utility tools to keep everything nice and clean
- It contains the File, the FileConfiguration of the File, and getter/setter methods for these which makes it cleaner to use

### How to create an ManagedFile
- Most of the times the file manager will create the MFile for you and you don't need to create the MFile yourself
- You can just create a new instance of the MFile class and add your file as a parameter or you can enter no parameters and enter in the values manually! (Not Recommended)

### How to get a file from the ManagedFile
- Simple! Just use the getFile() function!

### How to get the file configuration that is inside an ManagedFile
- Use this getFileConfiguration() function!

### What is updateFileConfiguration() function?
- This function is used to update the file configuration after giving it a value, though this function may seem useless at first, since you can manually set the file configuration without being dependant on the file inside of the MFile itself, it might help you in a way!
