package i.o.mob.dev.kinomania.ui.parameteredSearch

import androidx.lifecycle.ViewModel
import i.o.mob.dev.kinomania.Application
import i.o.mob.dev.kinomania.data.FilmFiltered
import i.o.mob.dev.kinomania.repository.RepositoryDelegate
import javax.inject.Inject

class ParameteredFilmsViewModel : ViewModel() {

    @Inject
    lateinit var repositoryDelegate: RepositoryDelegate

    init {
        Application.application.appComponent.inject(this)
    }

    private var currentPage = 1
    private var filmFiltered: FilmFiltered? = null

    suspend fun loadFilms(parameters: Map<String, String>): FilmFiltered? {
        return if (filmFiltered != null) {
            filmFiltered
        } else {
            filmFiltered = repositoryDelegate.getFilm(parameters)
            currentPage = 1
            filmFiltered
        }
    }

    suspend fun loadMore(parameters: Map<String, String>): Pair<FilmFiltered?, Boolean> {
        // boolean show if data available (could load more)
        currentPage++
        val nextPage = repositoryDelegate.getFilm(parameters, currentPage)
        return if (nextPage != null) {
            return if (!nextPage.films.isNullOrEmpty()) {
                return if (filmFiltered?.films?.containsAll(nextPage.films) == false) {
                    filmFiltered?.films?.addAll(nextPage.films)
                    filmFiltered to true
                } else {
                    filmFiltered to false
                }
            } else {
                filmFiltered to false
            }
        } else {
            filmFiltered to false
        }
    }

}