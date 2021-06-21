package i.o.mob.dev.kinomania.ui.search.parametersSearch

import android.util.Log
import androidx.lifecycle.ViewModel
import i.o.mob.dev.kinomania.Application
import i.o.mob.dev.kinomania.data.Filter
import i.o.mob.dev.kinomania.data.Keyword
import i.o.mob.dev.kinomania.repository.RepositoryDelegate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SetParametersViewModel : ViewModel() {

    var selectedTypes = mutableMapOf<String, String>()
    var selectedGenres = mutableMapOf<String, String>()
    var selectedCountries = mutableMapOf<String, String>()

    @Inject lateinit var repositoryDelegate: RepositoryDelegate

    init {
        Application.application.appComponent.inject(this)
    }

    private var filters: Filter? = null

    suspend fun getFilters(): Filter? {
        return if (filters != null){
            filters
        }else{
            filters = repositoryDelegate.getFilters()
            filters
        }
    }


}