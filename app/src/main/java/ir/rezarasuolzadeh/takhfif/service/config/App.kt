package ir.rezarasuolzadeh.takhfif.service.config

import android.app.Application
import ir.rezarasuolzadeh.takhfif.di.infoPageModule
import ir.rezarasuolzadeh.takhfif.di.mapPageModule
import ir.rezarasuolzadeh.takhfif.di.networkModule
import ir.rezarasuolzadeh.takhfif.service.utils.UserPreferences
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        UserPreferences.init(this)
        startKoin {
            androidContext(this@App)
            modules(
                networkModule,
                mapPageModule,
                infoPageModule
            )
        }
    }

}