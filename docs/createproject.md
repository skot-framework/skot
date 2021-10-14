# Create project (cli)
## 1. Setting up
1. Create a folder  with the name of your project and go inside
2. Create a file ***build.gradle.kts*** with content: 
```
buildscript {
 	repositories {
		google()
		jcenter()
		mavenCentral()
		mavenLocal()
	}
}
plugins {
	id("skot-starter").version("insert latest version")
}
skot {
	appPackage = "your.package.name"
	appName = "Your app name"
}
```
3. Create a file ***settings.gradle.kts*** with content:
```
pluginManagement {
	repositories {
		mavenLocal()
		maven {
			url = uri("https://raw.github.com/MathieuScotet/skot/repository")
		}
	}	
}
```
These files will be overrided after initialization.

## 2. Initialization
In a terminal : 
1. Move to the folder of your project
2. Enter the command `gradle wrapper`
3. Change gradle version to 7.0.2 in gradle.properties   
4. Enter the command `gradle start`
5. Enter the command `gradle skGenerate`
6. Launch Android Studio

You can start coding!
