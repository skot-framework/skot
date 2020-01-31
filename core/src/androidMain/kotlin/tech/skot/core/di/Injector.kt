package tech.skot.core.di


import android.app.Application

actual class BaseInjector(val androidApplication: Application, modules: List<Module<in BaseInjector>>) :
        Injector<BaseInjector>(modules) {
    override val context = this
}
