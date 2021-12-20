package app.wondertinder.data.daos

import androidx.room.*
import app.wondertinder.data.entities.Hero
import kotlinx.coroutines.flow.Flow

@Dao
interface HeroDao {
    @Query("SELECT * FROM hero")
    fun getHeroes(): Flow<List<Hero>>

    @Query("SELECT * FROM hero WHERE id = :id LIMIT 1")
    fun getHeroById(id: Int): Hero?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHero(hero: Hero)

    @Delete
    fun deleteHero(heroes: Hero)
}