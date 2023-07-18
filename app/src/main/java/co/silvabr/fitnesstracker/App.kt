package co.silvabr.fitnesstracker

import android.app.Application
import co.silvabr.fitnesstracker.model.AppDatabase

class App : Application() {

    lateinit var db: AppDatabase

    override fun onCreate() {
        super.onCreate()
        db = AppDatabase.getDatabase(this)
    }

}