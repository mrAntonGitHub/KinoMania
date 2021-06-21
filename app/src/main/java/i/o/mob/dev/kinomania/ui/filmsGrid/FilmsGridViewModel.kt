package i.o.mob.dev.kinomania.ui.filmsGrid

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import i.o.mob.dev.kinomania.Application
import i.o.mob.dev.kinomania.data.TopFilmItem
import i.o.mob.dev.kinomania.data.TopFilmsType
import i.o.mob.dev.kinomania.repository.RepositoryDelegate
import i.o.mob.dev.kinomania.utils.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class FilmsGridViewModel : ViewModel() {
    @Inject
    lateinit var repository: RepositoryDelegate

    private val _films = MutableStateFlow<State<List<TopFilmItem>>>(State.loading())
    val films = _films as StateFlow<State<List<TopFilmItem>>>

    init {
        Application.application.appComponent.inject(this)
    }

    fun initialListLoad(type: TopFilmsType) {
        viewModelScope.launch(Dispatchers.IO) {
            when (type) {
                TopFilmsType.TOP_100_POPULAR_FILMS -> {
                    repository.top100Films(false).collect {
                        _films.value = it
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
                }
                TopFilmsType.TOP_250_BEST_FILMS -> {
                    repository.top250Films(false).collect {
                        _films.value = it
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
                }
                TopFilmsType.TOP_AWAIT_FILMS -> {
                    repository.topAwaitFilms(false).collect {
                        _films.value = it
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
                }
            }
        }
    }


    suspend fun loadMoreContent(type: TopFilmsType) {
        when (type) {
            TopFilmsType.TOP_100_POPULAR_FILMS -> {
                _films.value = State.loading()
                repository.top100Films(true).collect {
                    _films.value = it
                }
            }
            TopFilmsType.TOP_250_BEST_FILMS -> {
                _films.value = State.loading()
                repository.top250Films(true).collect {
                    _films.value = it
                }
            }
            TopFilmsType.TOP_AWAIT_FILMS -> {
                _films.value = State.loading()
                repository.topAwaitFilms(true).collect {
                    _films.value = it
                }
            }
        }
    }

}