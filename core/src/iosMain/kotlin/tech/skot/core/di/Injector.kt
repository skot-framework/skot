package tech.skot.core.di

actual class BaseInjector(modules: List<Module<BaseInjector>>) :
        Injector(modules) {
    override val context = this
}
