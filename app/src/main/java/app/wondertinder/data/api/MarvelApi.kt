package app.wondertinder.data.api

import app.wondertinder.data.response.Characters
import app.wondertinder.utils.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface MarvelApi {
    @GET("characters?limit=${API_LIMIT}&ts=${API_TS}&apikey=${API_KEY}&hash=${API_HASH}")
    suspend fun getCharacters(
        @Query("offset") offset: Int? = 0
    ): Characters

    companion object {
        operator fun invoke(): MarvelApi {
            return Retrofit.Builder()
                .baseUrl(BASE_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MarvelApi::class.java)
        }
    }
}