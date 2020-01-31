package tech.skot.core.di


import android.app.Application

actual class Injector(val androidApplication: Application, modules: List<Module>) :
        BaseInjector(modules) {
    override val injector = this
}
