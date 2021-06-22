package i.o.mob.dev.kinomania.repository

import android.util.Log
import i.o.mob.dev.kinomania.data.*
import i.o.mob.dev.kinomania.di.modules.SearchApiImpl
import i.o.mob.dev.kinomania.utils.State
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

interface RepositoryDelegate {

    fun top100Films(shouldLoadMore: Boolean): Flow<State<List<TopFilmItem>>>

    fun top250Films(shouldLoadMore: Boolean): Flow<State<List<TopFilmItem>>>

    fun topAwaitFilms(shouldLoadMore: Boolean): Flow<State<List<TopFilmItem>>>

    suspend fun getFilm(id: Int): FilmWrapper?

    suspend fun searchByKeyword(keyword: String, page: Int = 1): Keyword?

    suspend fun getReview(filmId: Int, page: Int = 1): FilmReview?

    suspend fun getReview(filmId: Int): Review?

    suspend fun getFilmStaff(filmId: Int): ArrayList<FilmStaff>?

    suspend fun getStaff(id: Int): Staff?

    suspend fun getFilm(parameters: Map<String, String>, page: Int = 1): FilmFiltered?

    suspend fun getFilters(): Filter?

    suspend fun getFilmFrames(id: Int): FilmFrame?

    suspend fun getFilmVideos(filmId: Int): FilmVideos?
}

@Singleton
class Repository @Inject constructor(private val searchApiImpl: SearchApiImpl) :
    RepositoryDelegate {

    private var top100CurrentPage = 0
    private val top100FilmsList = mutableListOf<TopFilmItem>()
    private var top100MaxPages = 1

    private var top250CurrentPage = 0
    private val top250FilmsList = mutableListOf<TopFilmItem>()
    private var top250MaxPages = 1

    private var topAwaitCurrentPage = 0
    private val topAwaitFilmsList = mutableListOf<TopFilmItem>()
    private var topAwaitMaxPages = 1

    private var filters: Filter? = null

    override fun top100Films(shouldLoadMore: Boolean): Flow<State<List<TopFilmItem>>> =
        flow<State<List<TopFilmItem>>> {
            emit(State.loading())
            if (shouldLoadMore) {
                if (top100CurrentPage < top100MaxPages) {
                    try {
                        val newList = searchApiImpl.provideTopFilms(
                            TopFilmsType.TOP_100_POPULAR_FILMS,
                            ++top100CurrentPage
                        ).await()
                        val topFilms = newList.films
                        top100MaxPages = newList.pagesCount
                        top100FilmsList.addAll(topFilms)
                        if (top100CurrentPage == top100MaxPages) {
                            emit(State.noMoreData(top100FilmsList))
                        } else {
                            emit(State.success(top100FilmsList))
                        }
                    } catch (e: Exception) {
                        top100CurrentPage--
                        emit(State.error(e, "Ошибка загрузки"))
                    }
                } else {
                    if (top100FilmsList.isEmpty()) {
                        emit(State.error(null))
                    } else {
                        emit(State.noMoreData(top100FilmsList))
                    }
                }
            } else {
                if (top100CurrentPage >= top100MaxPages) {
                    emit(State.noMoreData(top100FilmsList))
                } else {
                    emit(State.success(top100FilmsList))
                }
            }
        }

    override fun top250Films(shouldLoadMore: Boolean): Flow<State<List<TopFilmItem>>> =
        flow<State<List<TopFilmItem>>> {
            emit(State.loading())
            if (shouldLoadMore) {
                if (top250CurrentPage < top250MaxPages) {
                    try {
                        val newList = searchApiImpl.provideTopFilms(
                            TopFilmsType.TOP_250_BEST_FILMS,
                            ++top250CurrentPage
                        ).await()
                        val topFilms = newList.films
                        top250MaxPages = newList.pagesCount
                        top250FilmsList.addAll(topFilms)
                        if (top250CurrentPage == top250MaxPages) {
                            emit(State.noMoreData(top250FilmsList))
                        } else {
                            emit(State.success(top250FilmsList))
                        }
                    } catch (e: Exception) {
                        top250CurrentPage--
                        emit(State.error(e, "Ошибка загрузки"))
                    }
                } else {
                    if (top100FilmsList.isEmpty()) {
                        emit(State.error(null))
                    } else {
                        emit(State.noMoreData(top250FilmsList))
                    }
                }
            } else {
                if (top250CurrentPage >= top250MaxPages) {
                    emit(State.noMoreData(top250FilmsList))
                } else {
                    emit(State.success(top250FilmsList))
                }
            }
        }

    override fun topAwaitFilms(shouldLoadMore: Boolean): Flow<State<List<TopFilmItem>>> =
        flow<State<List<TopFilmItem>>> {
            emit(State.loading())
            if (shouldLoadMore) {
                if (topAwaitCurrentPage < topAwaitMaxPages) {
                    try {
                        val newList = searchApiImpl.provideTopFilms(
                            TopFilmsType.TOP_AWAIT_FILMS,
                            ++topAwaitCurrentPage
                        ).await()
                        val topFilms = newList.films
                        topAwaitMaxPages = newList.pagesCount
                        topAwaitFilmsList.addAll(topFilms)
                        if (topAwaitCurrentPage == topAwaitMaxPages) {
                            emit(State.noMoreData(topAwaitFilmsList))
                        } else {
                            emit(State.success(topAwaitFilmsList))
                        }
                    } catch (e: Exception) {
                        topAwaitCurrentPage--
                        emit(State.error(e, "Ошибка загрузки"))
                    }
                } else {
                    if (top100FilmsList.isEmpty()) {
                        emit(State.error(null))
                    } else {
                        emit(State.noMoreData(topAwaitFilmsList))
                    }
                }
            } else {
                if (topAwaitCurrentPage >= topAwaitMaxPages) {
                    emit(State.noMoreData(topAwaitFilmsList))
                } else {
                    emit(State.success(topAwaitFilmsList))
                }
            }
        }

    override suspend fun getFilm(id: Int): FilmWrapper? {
        return try {
            searchApiImpl.provideFilm(id).await()
        } catch (e: Exception) {
            Log.e("Exception", "$e")
            null
        }
    }

    override suspend fun getFilm(parameters: Map<String, String>, page: Int): FilmFiltered? {
        return try {
            searchApiImpl.provideFilmFiltered(
                parameters.getOrElse("country", { "" }).toString(),
                parameters.getOrElse("genre", { "" }).toString(),
                parameters.getOrElse("type", { "ALL" }).toString(),
                parameters.getOrElse("ratingFrom", { "0" }).toString(),
                parameters.getOrElse("ratingTo", { "10" }).toString(),
                parameters.getOrElse("yearFrom", { "1888" }).toString(),
                parameters.getOrElse(
                    "yearTo",
                    { Calendar.getInstance().get(Calendar.YEAR).toString() }).toString(),
                parameters.getOrElse("order", { "" }).toString(),
                page
            ).await()
        } catch (e: Exception) {
            Log.e("Exception", "$e")
            null
        }
    }

    override suspend fun searchByKeyword(keyword: String, page: Int): Keyword? {
        return try {
            searchApiImpl.provideSearchByKeyword(keyword, page).await()
        } catch (e: Exception) {
            Log.e("Exception", "$e")
            null
        }
    }

    override suspend fun getReview(filmId: Int, page: Int): FilmReview? {
        return try {
            searchApiImpl.provideFilmReview(filmId, page).await()
        } catch (e: Exception) {
            Log.e("Exception", "$e")
            null
        }
    }

    override suspend fun getReview(filmId: Int): Review? {
        return try {
            searchApiImpl.provideFilmReviewById(filmId).await()
        } catch (e: Exception) {
            Log.e("Exception", "$e")
            null
        }
    }

    override suspend fun getFilmStaff(filmId: Int): ArrayList<FilmStaff>? {
        return try {
            searchApiImpl.provideFilmStaff(filmId).await()
        } catch (e: Exception) {
            Log.e("Exception", "$e")
            null
        }
    }

    override suspend fun getStaff(id: Int): Staff? {
        return try {
            searchApiImpl.provideStaff(id).await()
        } catch (e: Exception) {
            Log.e("Exception", "$e")
            null
        }
    }

    override suspend fun getFilters(): Filter? {
        return filters
            ?: return try {
                filters = searchApiImpl.provideFilters().await()
                filters
            } catch (e: Exception) {
                Log.e("Exception", "$e")
                null
            }
    }

    override suspend fun getFilmFrames(id: Int): FilmFrame? {
        return try {
            searchApiImpl.provideFilmFrames(id).await()
        } catch (e: Exception) {
            Log.e("Exception", "$e")
            null
        }

    }

    override suspend fun getFilmVideos(filmId: Int): FilmVideos? {
        return try {
            searchApiImpl.provideFilmVideos(filmId).await()
        } catch (e: Exception) {
            Log.e("Exception", "$e")
            null
        }
    }

}