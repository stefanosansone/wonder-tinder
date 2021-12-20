package app.wondertinder.data.repositories

import androidx.annotation.WorkerThread
import app.wondertinder.data.daos.HeroDao
import app.wondertinder.data.entities.Hero
import kotlinx.coroutines.flow.Flow

class HeroRepository(private val heroDao: HeroDao) {

    val allHeros: Flow<List<Hero>> = heroDao.getHeroes()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(hero: Hero) {
        heroDao.insertHero(hero)
    }
}