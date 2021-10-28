# Create project (cli)
## 1. Setting up
1. Create a folder  with the name of your project and go inside
2. Create a file ***build.gradle.kts*** with content: 
```
buildscript {
    repositories {
        google()
    }
}
plugins {
	id("tech.skot.starter") version "1.+"
}
skot {
	appPackage = "your.package.name"
	appName = "Your app name"
}
```
These file will be overrided after initialization.

## 2. Initialization
In a terminal : 
1. Move to the folder of your project
2. Enter the command `gradle wrapper`
3. Enter the command `./gradlew start`
4. Enter the command `./gradlew skGenerate`
5. Launch Android Studio

You can start coding!
