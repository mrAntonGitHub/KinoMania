package i.o.mob.dev.kinomania.ui.movie

import androidx.lifecycle.ViewModel
import i.o.mob.dev.kinomania.Application
import i.o.mob.dev.kinomania.data.*
import i.o.mob.dev.kinomania.repository.RepositoryDelegate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieViewModel : ViewModel() {

    @Inject
    lateinit var repositoryDelegate: RepositoryDelegate

    var film: FilmWrapper? = null
    var filmFrame: FilmFrame? = null
    var filmStaff: List<FilmStaff>? = null
    var filmReview: FilmReview? = null
    var filmVideos: FilmVideos? = null

    init {
        Application.application.appComponent.inject(this)
    }

    suspend fun getFilm(filmId: Int): FilmWrapper? {
        if (film == null) film = repositoryDelegate.getFilm(filmId)
        return film
    }

    suspend fun getFilmFrame(filmId: Int): FilmFrame? {
        if (filmFrame == null) filmFrame = repositoryDelegate.getFilmFrames(filmId)
        return filmFrame
    }

    suspend fun getFilmStaff(filmId: Int): List<FilmStaff>? {
        if (filmStaff == null) filmStaff = repositoryDelegate.getFilmStaff(filmId)
        return filmStaff
    }

    suspend fun getFilmReview(filmId: Int, page: Int = 1): FilmReview? {
        if (filmReview == null) filmReview = repositoryDelegate.getReview(filmId, page)
        return filmReview
    }

    suspend fun getFilmVideos(filmId: Int): FilmVideos? {
        if (filmVideos == null) filmVideos = repositoryDelegate.getFilmVideos(filmId)
        return filmVideos
    }
}