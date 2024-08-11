package ru.zotreex.telescope

import android.app.Application
import org.koin.core.context.startKoin
import org.koin.dsl.module
import ru.zotreex.telescope.experenets.DataScreenModel

class TelescopeApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            modules(homeModule)
        }
    }
}

val homeModule = module {
    factory { parameters -> DataScreenModel(id = parameters.get()) }
}