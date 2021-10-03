# Create a screen

## View contract
The first step is to create the view contract of the screen.
1. Create an interface ScreenNameVC.kt in the your.package.name.screens package.
This interface must inherit from SKScreenVC.

Example : 
```
interface ScreenNameVC : SkScreenVC {
	var welcomeText : String
	var btStartApp : SkButtonVC
}
```
In this sample, we have a screen with a textview displayint a welcome text, and a button to start App. We use a Skbutton available in the Framework, and for the textview, we used a string.

2. Add a SKOpens annotation on the view contract of the screen that will launch your new screen and pass it the class of your view contract.
Example : 
```
@SKOpens([ScreenNameVC::class])
interface SplashVC : SkScreenVC {
	var welcomeText : String
	var btStartApp : SkButtonVC
}
```

3. launch the generation with the gradle command ***skGenerate***. The framework has generated code for you, and especially a viewModel and a view class

