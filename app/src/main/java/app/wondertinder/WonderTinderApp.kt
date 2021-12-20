package app.wondertinder

import android.app.Application
import app.wondertinder.data.AppDatabase
import app.wondertinder.data.repositories.DatastoreRepository
import app.wondertinder.data.repositories.HeroRepository

class WonderTinderApp : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    val heroRepository by lazy { HeroRepository(database.heroDao()) }
    val datastoreRepository by lazy { DatastoreRepository(this) }
}