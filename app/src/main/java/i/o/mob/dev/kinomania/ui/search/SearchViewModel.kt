package i.o.mob.dev.kinomania.ui.search

import android.util.Log
import androidx.lifecycle.ViewModel
import i.o.mob.dev.kinomania.Application
import i.o.mob.dev.kinomania.data.FilmByKeyword
import i.o.mob.dev.kinomania.data.Filter
import i.o.mob.dev.kinomania.data.Keyword
import i.o.mob.dev.kinomania.repository.RepositoryDelegate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchViewModel : ViewModel() {

    @Inject lateinit var repositoryDelegate: RepositoryDelegate

    init {
        Application.application.appComponent.inject(this)
    }

    private var currentPage = 1
    private var keyword: Keyword? = null

    suspend fun querySearch(query: String) : Keyword?{
        return if (query == keyword?.keyword){
            keyword
        }else{
            keyword = repositoryDelegate.searchByKeyword(query)
            currentPage = 1
            keyword
        }
    }

    suspend fun loadMore(query: String): Pair<List<FilmByKeyword>?, Boolean> {
        // boolean show if data available (could load more)
        currentPage += 1
        val nextPage = repositoryDelegate.searchByKeyword(query, currentPage)
        return if (nextPage != null){
            return if (!nextPage.films.isNullOrEmpty()) {
                return if (keyword?.films != nextPage.films){
                    keyword?.films?.addAll(nextPage.films)
                    keyword?.films to true
                }else{
                    keyword?.films to false
                }
            }else{
                keyword?.films to false
            }
        }else{
            keyword?.films to false
        }
    }

}