package i.o.mob.dev.kinomania.ui.review

import androidx.lifecycle.ViewModel
import i.o.mob.dev.kinomania.Application
import i.o.mob.dev.kinomania.data.FilmReview
import i.o.mob.dev.kinomania.data.Review
import i.o.mob.dev.kinomania.repository.RepositoryDelegate
import javax.inject.Inject

class ReviewViewModel : ViewModel() {

    @Inject lateinit var repository: RepositoryDelegate

    init {
        Application.application.appComponent.inject(this)
    }

    suspend fun getReview(id: Int): Review? {
        return repository.getReview(id)
    }

}