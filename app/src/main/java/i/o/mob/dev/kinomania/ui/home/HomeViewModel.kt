package i.o.mob.dev.kinomania.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import i.o.mob.dev.kinomania.Application
import i.o.mob.dev.kinomania.data.TopFilmItem
import i.o.mob.dev.kinomania.data.TopFilmsType
import i.o.mob.dev.kinomania.repository.RepositoryDelegate
import i.o.mob.dev.kinomania.utils.State
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeViewModel : ViewModel() {

    @Inject
    lateinit var repository: RepositoryDelegate

    private val _top100 = MutableStateFlow<State<List<TopFilmItem>>>(State.loading())
    val top100 = _top100 as StateFlow<State<List<TopFilmItem>>>

    private val _top250 = MutableStateFlow<State<List<TopFilmItem>>>(State.loading())
    val top250 = _top250 as StateFlow<State<List<TopFilmItem>>>

    private val _topAwait = MutableStateFlow<State<List<TopFilmItem>>>(State.loading())
    val topAwait = _topAwait as StateFlow<State<List<TopFilmItem>>>

    init {
        Application.application.appComponent.inject(this)
        initialListLoad()
    }

    private fun initialListLoad() {
        viewModelScope.launch(IO) {
            repository.top100Films(false).collect {
                _top100.value = it
                when (it) {
                    is State.Success -> {
                        if (it.data.isEmpty()) {
                            loadMoreContent(TopFilmsType.TOP_100_POPULAR_FILMS)
                        }
                    }
                    is State.NoMoreData -> {
                        if (it.data.isEmpty()) {
                            loadMoreContent(TopFilmsType.TOP_100_POPULAR_FILMS)
                        }
                    }
                    else -> {
                    }
                }
            }

            repository.top250Films(false).collect {
                _top250.value = it
                when (it) {
                    is State.Success -> {
                        if (it.data.isEmpty()) {
                            loadMoreContent(TopFilmsType.TOP_250_BEST_FILMS)
                        }
                    }
                    is State.NoMoreData -> {
                        if (it.data.isEmpty()) {
                            loadMoreContent(TopFilmsType.TOP_250_BEST_FILMS)
                        }
                    }
                    else -> {
                    }
                }
            }

            repository.topAwaitFilms(false).collect {
                _topAwait.value = it
                when (it) {
                    is State.Success -> {
                        if (it.data.isEmpty()) {
                            loadMoreContent(TopFilmsType.TOP_AWAIT_FILMS)
                        }
                    }
                    is State.NoMoreData -> {
                        if (it.data.isEmpty()) {
                            loadMoreContent(TopFilmsType.TOP_AWAIT_FILMS)
                        }
                    }
                    else -> {
                    }
                }
            }
        }
    }


    suspend fun loadMoreContent(type: TopFilmsType) {
        when (type) {
            TopFilmsType.TOP_100_POPULAR_FILMS -> {
                _top100.value = State.loading()
                repository.top100Films(true).collect {
                    _top100.value = it
                }
            }
            TopFilmsType.TOP_250_BEST_FILMS -> {
                _top250.value = State.loading()
                repository.top250Films(true).collect {
                    _top250.value = it
                }
            }
            TopFilmsType.TOP_AWAIT_FILMS -> {
                _topAwait.value = State.loading()
                repository.topAwaitFilms(true).collect {
                    _topAwait.value = it
                }
            }
        }
    }
}