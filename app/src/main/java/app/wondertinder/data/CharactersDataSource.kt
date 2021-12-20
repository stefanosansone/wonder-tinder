package app.wondertinder.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import app.wondertinder.data.api.MarvelApi
import app.wondertinder.data.response.Result
import retrofit2.HttpException
import java.io.IOException

class CharactersDataSource(private val marvelApi: MarvelApi, private val heroesRated: Int) :
    PagingSource<Int, Result>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Result> {
        val page = params.key ?: 1
        val offset = (page - 1) * 10
        return try {
            val response = marvelApi.getCharacters(offset + heroesRated)
            val characters = response.data.results
            LoadResult.Page(
                data = characters,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.data.offset + response.data.count >= response.data.total) null else page + 1
            )

        } catch (exception: IOException) {
            val error = IOException("Please Check Internet Connection")
            LoadResult.Error(error)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }

    }

    override fun getRefreshKey(state: PagingState<Int, Result>): Int? {
        return state.anchorPosition
    }
}