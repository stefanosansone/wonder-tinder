package app.wondertinder.data.repositories

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DatastoreRepository(
    private val context: Context
) {

    companion object {
        val HEROES_RATED = intPreferencesKey("HEROES_RATED")
    }

    suspend fun addHeroesRated() {
        context.dataStore.edit {
            val currentHeroesRated = it[HEROES_RATED] ?: 0
            it[HEROES_RATED] = currentHeroesRated + 1
        }
    }

    val heroesRatedFlow: Flow<Int?> = context.dataStore.data.map {
        it[HEROES_RATED]
    }

}