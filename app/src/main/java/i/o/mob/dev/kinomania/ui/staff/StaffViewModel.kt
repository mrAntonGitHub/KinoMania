package i.o.mob.dev.kinomania.ui.staff

import androidx.lifecycle.ViewModel
import i.o.mob.dev.kinomania.Application
import i.o.mob.dev.kinomania.data.Staff
import i.o.mob.dev.kinomania.repository.RepositoryDelegate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StaffViewModel : ViewModel() {

    @Inject
    lateinit var repositoryDelegate: RepositoryDelegate

    init {
        Application.application.appComponent.inject(this)
    }

    private var staff: Staff? = null

    suspend fun getStaff(staffId: Int): Staff? {
        return if (staff != null) {
            staff
        } else {
            val staff: Staff? = repositoryDelegate.getStaff(staffId)
            val sortedFilms =
                repositoryDelegate.getStaff(staffId)?.films?.sortedByDescending { it.rating }
            staff?.films = sortedFilms?.distinctBy { it.filmId }
            this.staff = staff
            this.staff
        }
    }

}