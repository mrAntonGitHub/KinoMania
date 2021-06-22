package i.o.mob.dev.kinomania

import android.app.Application
import i.o.mob.dev.kinomania.di.component.AppComponent
import i.o.mob.dev.kinomania.di.component.DaggerAppComponent
import i.o.mob.dev.kinomania.di.modules.AppModule

class Application : Application() {

    companion object {
        lateinit var application: i.o.mob.dev.kinomania.Application
    }

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        application = this
        setupDagger()
    }

    private fun setupDagger() {
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(applicationContext))
            .build()
    }

}