package tech.skot.tools.starter

fun StarterGenerator.skotModule(){
    ModuleGenerator("skot", configuration, rootDir).apply {
        buildGradle {
            plugin(BuildGradleGenerator.Plugin.Id("skot-tools"))
            manual = """
skot {
    app = tech.skot.tools.gradle.App(
            startScreen = "${configuration.appPackage}.screens.SplashVC",
            packageName = "${configuration.appPackage}",
            baseActivity = "${configuration.appPackage}.android.BaseActivity")
}                
            """.trimIndent()
        }



    }.generate()
    modules.add("skot")
}