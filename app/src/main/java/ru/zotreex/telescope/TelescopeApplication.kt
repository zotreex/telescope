package ru.zotreex.telescope

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.withOptions
import org.koin.dsl.module
import ru.zotreex.telescope.auth.code.CodeAuthModel
import ru.zotreex.telescope.auth.phone.PhoneAuthModel
import ru.zotreex.telescope.auth.qr.QrAuthScreenModel
import ru.zotreex.telescope.auth.two_factor.TwoFactorModel
import ru.zotreex.telescope.core.AuthorizationHandler
import ru.zotreex.telescope.core.TdClient
import ru.zotreex.telescope.core.TdClientResultHandler
import ru.zotreex.telescope.home.HomeScreenModel
import ru.zotreex.telescope.navigation.GlobalRouter
import ru.zotreex.telescope.navigation.StartRoute
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
    viewModel { QrAuthScreenModel(get(), get(), get()) }
    viewModel { PhoneAuthModel(get(), get(), get()) }
    factory { StartRoute(get()) }
    viewModel { CodeAuthModel(get(), get(), get()) }
    viewModel { TwoFactorModel(get(), get()) }
    viewModel { HomeScreenModel(get(), get()) }
    viewModel { PlayerScreenModel(get(), get()) }

    single { GlobalRouter() }
    single { AuthorizationHandler() }
    single { TdClientResultHandler(get()) }
    single { TdClient(get(), get(), get()) } withOptions {
        createdAtStart()
    }
}