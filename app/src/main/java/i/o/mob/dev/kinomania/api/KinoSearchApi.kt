package i.o.mob.dev.kinomania.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import i.o.mob.dev.kinomania.BuildConfig
import i.o.mob.dev.kinomania.data.*
import kotlinx.coroutines.Deferred
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import kotlin.collections.ArrayList

const val baseUrl = "https://kinopoiskapiunofficial.tech"
const val kinoPoiskKey = BuildConfig.KINO_POISK_API_KEY
const val APPEND_TO_RESPONSE_KEY = "append_to_response"

interface KinoSearchApi {
    // Get film data by kinopoisk film id
    @GET("/api/v2.1/films/{id}?$APPEND_TO_RESPONSE_KEY=BUDGET&$APPEND_TO_RESPONSE_KEY=RATING")
    fun getFilm(@Path("id") id: Int): Deferred<FilmWrapper>

    // Get film by keyword
    @GET("/api/v2.1/films/search-by-keyword")
    fun searchByKeyword(
        @Query("keyword") keyword: String,
        @Query("page") page: Int = 1
    ): Deferred<Keyword>

    // Get TOP_100, TOP_250, TOP_AWAIT films
    @GET("/api/v2.2/films/top")
    fun getTopFilms(
        @Query("type") type: TopFilmsType,
        @Query("page") page: Int = 1
    ): Deferred<TopFilms>

    // Get review on the film
    @GET("/api/v1/reviews")
    fun getFilmReview(
        @Query("filmId") filmId: Int,
        @Query("page") page: Int = 1
    ): Deferred<FilmReview>

    // Get information in details about review
    @GET("/api/v1/reviews/details")
    fun getFilmReview(@Query("reviewId") reviewId: Int): Deferred<Review>

    // Get film trailer
    @GET("/api/v2.1/films/{filmId}/videos")
    fun getFilmVideos(@Path("filmId") filmId: Int): Deferred<FilmVideos>

    // Get film frames
    @GET("/api/v2.1/films/{id}/frames")
    fun getFilmFrames(@Path("id") id: Int): Deferred<FilmFrame>

    // Get film staff
    @GET("/api/v1/staff")
    fun getFilmStaff(@Query("filmId") filmId: Int): Deferred<ArrayList<FilmStaff>>

    // Get staff info in details
    @GET("/api/v1/staff/{id}")
    fun getStaff(@Path("id") id: Int): Deferred<Staff>

    // Get films by filters
    @GET("/api/v2.1/films/search-by-filters")
    fun getFilm(
        @Query("country") country: String?,
        @Query("genre") genre: String?,
        @Query("type") type: String?,
        @Query("ratingFrom") ratingFrom: String,
        @Query("ratingTo") ratingTo: String,
        @Query("yearFrom") yearFrom: String,
        @Query("yearTo") yearTo: String,
        @Query("order") order: String?,
        @Query("page") page: Int = 1
    ): Deferred<FilmFiltered>

    // Get all filters
    @GET("/api/v2.1/films/filters")
    fun getFilters(): Deferred<Filter>

    companion object {
        operator fun invoke(): KinoSearchApi {

            val requestInterceptor = Interceptor { chain ->
                val url: HttpUrl = chain.request()
                    .url()
                    .newBuilder()
                    .build()
                val request: Request = chain.request()
                    .newBuilder()
                    .header("X-API-KEY", kinoPoiskKey)
                    .url(url)
                    .build()
                return@Interceptor chain.proceed(request)
            }

            val loggingInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .addInterceptor(loggingInterceptor)
                .build()

            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build()
                .create(KinoSearchApi::class.java)
        }
    }
}