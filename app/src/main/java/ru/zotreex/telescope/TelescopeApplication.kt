package ru.zotreex.telescope

import android.app.Application
import org.drinkless.tdlib.TdApi.Message
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.withOptions
import org.koin.dsl.module
import ru.zotreex.telescope.auth.code.CodeAuthModel
import ru.zotreex.telescope.auth.phone.PhoneAuthModel
import ru.zotreex.telescope.auth.qr.QrAuthScreenModel
import ru.zotreex.telescope.auth.two_factor.TwoFactorModel
import ru.zotreex.telescope.core.AuthorizationHandler
import ru.zotreex.telescope.core.TdClient
import ru.zotreex.telescope.core.TdClientResultHandler
import ru.zotreex.telescope.experenets.DataScreenModel
import ru.zotreex.telescope.home.HomeScreenModel
import ru.zotreex.telescope.player.PlayerScreenModel

class TelescopeApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()

            androidContext(this@TelescopeApplication)

            modules(homeModule)
        }
    }
}

val homeModule = module {
    factory { parameters -> DataScreenModel(id = parameters.get()) }
    factory { QrAuthScreenModel(get(), get(), get()) }
    factory { PhoneAuthModel(get(), get(), get()) }
    factory { StartRoute(get()) }
    factory { CodeAuthModel(get(), get(), get()) }
    factory { TwoFactorModel(get(), get()) }
    factory { HomeScreenModel(get()) }
    factory { PlayerScreenModel(get(), get()) }

    single { GlobalRouter() }
    single { AuthorizationHandler() }
    single { TdClientResultHandler(get()) }
    single { TdClient(get(), get(), get()) } withOptions {
        createdAtStart()
    }
}