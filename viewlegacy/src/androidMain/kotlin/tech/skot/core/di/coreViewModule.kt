package tech.skot.core.di

val coreViewModule = module<BaseInjector> {
    single { CoreViewInjectorImpl() as CoreViewInjector }
}


val coreViewInjector: CoreViewInjector by lazy {
    get<CoreViewInjector>()
}

//val rootStack by lazy { RootStack() }