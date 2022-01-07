package tech.skot.core.di

class InjectorMock(modules:List<Module<InjectorMock>>): Injector<InjectorMock>(
    modules
) {
    override val context: InjectorMock = this
}