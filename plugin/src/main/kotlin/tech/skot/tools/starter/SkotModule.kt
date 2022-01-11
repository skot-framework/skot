package tech.skot.tools.starter

fun StarterGenerator.skotModule(){
    ModuleGenerator("skot", configuration, rootDir).apply {
        buildGradle {
            plugin(BuildGradleGenerator.Plugin.Id("tech.skot.tools"))
            manual = """
skot {
    app = tech.skot.tools.gradle.App(
            startScreen = ".screens.SplashVC",
            packageName = "${configuration.appPackage}",
            baseActivity = ".android.BaseActivity")
}                
            """.trimIndent()
        }



    }.generate()
    modules.add("skot")
}