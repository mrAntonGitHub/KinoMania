package i.o.mob.dev.kinomania.ui.parameteredSearch

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import i.o.mob.dev.kinomania.R
import i.o.mob.dev.kinomania.adapters.FilmsLandAdapter
import i.o.mob.dev.kinomania.adapters.FilmsLandAdapterDelegate
import i.o.mob.dev.kinomania.data.FilmByKeyword
import i.o.mob.dev.kinomania.utils.Utils.Companion.hideBottomNavigation
import i.o.mob.dev.kinomania.utils.Utils.Companion.transparentStatusBar
import kotlinx.android.synthetic.main.fragment_parametered_films.*

class ParameteredFilmsFragment : Fragment(R.layout.fragment_parametered_films),
    FilmsLandAdapterDelegate {

    private lateinit var viewModel: ParameteredFilmsViewModel
    private lateinit var filmsLandAdapter: FilmsLandAdapter

    private var parameters = mutableMapOf<String, String>()

    override fun onStart() {
        super.onStart()
        hideBottomNavigation(true)
        transparentStatusBar(false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(ParameteredFilmsViewModel::class.java)

        filmsLandAdapter = FilmsLandAdapter()
        filmsLandAdapter.setDelegate(this)
        filmsLandAdapter.isLoadMoreAvailable(true)
        filmsRv.adapter = filmsLandAdapter

        arguments?.get("title").let {
            it as String
            toolbarTitle.text = it
        }

        arguments?.get("key")?.let {
            try {
                it as Map<String, String>
                parameters.clear()
                parameters.putAll(it)
                lifecycleScope.launchWhenCreated {
                    progress.visibility = View.VISIBLE
                    val films = viewModel.loadFilms(this@ParameteredFilmsFragment.parameters)
                    if (films != null) {
                        films.let {
                            it.films?.let {
                                it.map {
                                    FilmByKeyword(
                                        it.countries,
                                        null,
                                        it.filmId,
                                        null,
                                        it.genres,
                                        it.nameEn,
                                        it.nameRu,
                                        it.posterUrl,
                                        it.posterUrlPreview,
                                        it.rating,
                                        it.ratingVoteCount,
                                        it.type,
                                        it.year
                                    )
                                }
                            }?.let { it1 ->
                                filmsLandAdapter.submitList(it1)
                                progress.visibility = View.GONE
                                nothingToShow.visibility = View.GONE
                            }
                        }
                    } else {
                        nothingToShow.visibility = View.VISIBLE
                        progress.visibility = View.GONE
                    }
                }
            } catch (e: Exception) {
                Log.e("ParameteredFilmsFrg", e.toString())
            }

        }

        back.setOnClickListener { requireActivity().onBackPressed() }

    }

    override fun filmClicked(film: FilmByKeyword) {
        val bundle = bundleOf("filmId" to film.filmId)
        findNavController().navigate(R.id.action_parameteredFilmsFragment_to_movieFragment, bundle)
    }

    override fun loadMoreContent() {
        lifecycleScope.launchWhenStarted {
            val films = viewModel.loadMore(this@ParameteredFilmsFragment.parameters)
            filmsLandAdapter.isLoadMoreAvailable(films.second)
            films.first?.let {
                it.films?.let {
                    it.map {
                        FilmByKeyword(
                            it.countries,
                            null,
                            it.filmId,
                            null,
                            it.genres,
                            it.nameEn,
                            it.nameRu,
                            it.posterUrl,
                            it.posterUrlPreview,
                            it.rating,
                            it.ratingVoteCount,
                            it.type,
                            it.year
                        )
                    }
                }?.let { it1 -> filmsLandAdapter.submitList(it1) }
            }
        }
    }
}