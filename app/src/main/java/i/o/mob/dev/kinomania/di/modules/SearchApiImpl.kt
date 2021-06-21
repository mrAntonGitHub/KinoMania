package i.o.mob.dev.kinomania.di.modules

import dagger.Module
import dagger.Provides
import i.o.mob.dev.kinomania.api.KinoSearchApi
import i.o.mob.dev.kinomania.data.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Singleton

@Module
class SearchApiImpl @Inject constructor() {

    private val kinoSearchApi = KinoSearchApi()

    @Provides
    fun provideFilm(id: Int): Deferred<FilmWrapper> {
        return kinoSearchApi.getFilm(id)
    }

    @Provides
    fun provideFilmFiltered(
        @Query(value = "country") country: String?,
        @Query(value = "genre") genre: String?,
        @Query(value = "type") type: String?,
        @Query(value = "ratingFrom") ratingFrom: String,
        @Query(value = "ratingTo") ratingTo: String,
        @Query(value = "yearFrom") yearFrom: String,
        @Query(value = "yearTo") yearTo: String,
        @Query(value = "order") order: String?,
        @Query(value = "page") page: Int
    ): Deferred<FilmFiltered> {
        return kinoSearchApi.getFilm(country, genre, type, ratingFrom, ratingTo, yearFrom, yearTo, order, page)
    }

    @Provides
    fun provideSearchByKeyword(keyword: String, page: Int): Deferred<Keyword> {
        return kinoSearchApi.searchByKeyword(keyword, page)
    }

    @Provides
    fun provideTopFilms(type: TopFilmsType, page: Int): Deferred<TopFilms> {
        return kinoSearchApi.getTopFilms(type, page)
    }

    @Provides
    fun provideFilmReview(filmId: Int, page: Int): Deferred<FilmReview> {
        return kinoSearchApi.getFilmReview(filmId, page)
    }

    @Provides
    fun provideFilmReviewById(reviewId: Int): Deferred<Review> {
        return  kinoSearchApi.getFilmReview(reviewId)
    }

    @Provides
    fun provideFilmVideos(filmId: Int): Deferred<FilmVideos> {
        return kinoSearchApi.getFilmVideos(filmId)
    }

    @Provides
    fun provideFilmStaff(filmId: Int): Deferred<ArrayList<FilmStaff>> {
        return kinoSearchApi.getFilmStaff(filmId)
    }

    @Provides
    fun provideStaff(id: Int): Deferred<Staff> {
        return kinoSearchApi.getStaff(id)
    }

    @Provides
    fun provideFilters(): Deferred<Filter> {
        return kinoSearchApi.getFilters()
    }

    @Provides
    fun provideFilmFrames(id: Int): Deferred<FilmFrame> {
        return kinoSearchApi.getFilmFrames(id)
    }

}