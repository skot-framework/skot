package tech.skot.core.di

actual class BaseInjector(modules: List<Module<in BaseInjector>>) :
        Injector<BaseInjector>(modules) {
    override val context = this
}
