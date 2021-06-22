package i.o.mob.dev.kinomania.ui.categories

import androidx.lifecycle.ViewModel
import i.o.mob.dev.kinomania.Application
import i.o.mob.dev.kinomania.data.GenreFilter
import i.o.mob.dev.kinomania.repository.RepositoryDelegate
import javax.inject.Inject

class CategoryViewModel : ViewModel() {

    @Inject
    lateinit var repositoryDelegate: RepositoryDelegate

    init {
        Application.application.appComponent.inject(this)
    }

    private var genres: List<GenreFilter>? = null

    suspend fun getCategories(): List<GenreFilter>? {
        return if (genres != null) {
            genres
        } else {
            genres = repositoryDelegate.getFilters()?.genres
            genres
        }
    }


}