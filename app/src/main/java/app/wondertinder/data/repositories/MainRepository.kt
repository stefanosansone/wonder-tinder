package app.wondertinder.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import app.wondertinder.data.CharactersDataSource
import app.wondertinder.data.api.MarvelApi
import app.wondertinder.data.response.Result
import kotlinx.coroutines.flow.Flow

class MainRepository constructor(
    private val marvelApi: MarvelApi
) {
    fun getCharacters(heroesRated: Int): Flow<PagingData<Result>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = true, pageSize = 10),
            pagingSourceFactory = {
                CharactersDataSource(marvelApi, heroesRated)
            }
        ).flow
    }
}