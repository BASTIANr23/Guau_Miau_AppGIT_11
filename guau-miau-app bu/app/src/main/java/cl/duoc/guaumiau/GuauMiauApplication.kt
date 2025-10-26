package cl.duoc.guaumiau

import android.app.Application
import cl.duoc.guaumiau.data.local.AppDatabase


class GuauMiauApplication : Application() {
    
    // Instancia única de la base de datos (Singleton)
    val database: AppDatabase by lazy { 
        AppDatabase.getDatabase(this) 
    }
    
    override fun onCreate() {
        super.onCreate()
        instance = this
    }
    
    companion object {
        lateinit var instance: GuauMiauApplication
            private set
    }
}
