package i.o.mob.dev.kinomania.ui.search

import android.os.Bundle
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
import i.o.mob.dev.kinomania.utils.DynamicSearchViewWatcher
import i.o.mob.dev.kinomania.utils.Utils.Companion.transparentStatusBar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.coroutines.Job

class SearchFragment : Fragment(R.layout.fragment_search),
    FilmsLandAdapterDelegate {

    private lateinit var viewModel: SearchViewModel
    lateinit var filmAdapter: FilmsLandAdapter

    private var searchingScope: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)

        requireActivity().transparentStatusBar(false)
        requireActivity().bottomNavigation.visibility = View.GONE

        filmAdapter = FilmsLandAdapter()
        filmAdapter.setDelegate(this)
        filmsRv.adapter = filmAdapter


        requireActivity().bottomNavigation.visibility = View.GONE
        focusSearchView()

        searchView.setOnQueryTextListener(DynamicSearchViewWatcher(
            onTextChange = {
                if (searchingScope != null) {
                    searchingScope?.cancel()
                    nothingToShow.visibility = View.GONE
                }
                progress.visibility = View.VISIBLE
                searchingScope = lifecycleScope.launchWhenCreated {
                    val films = viewModel.querySearch(it.toString())
                    films?.let {
                        progress.visibility = View.GONE
                        if (films.films.isNullOrEmpty()) nothingToShow.visibility = View.VISIBLE
                        filmAdapter.isLoadMoreAvailable(true)
                        filmAdapter.submitList(films.films)
                    }
                    if (it == "") nothingToShow.visibility = View.GONE
                }
            },
            onTextSubmit = {

            }
        ))

        back.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun focusSearchView() {
        searchView.isIconified = true
        searchView.isIconified = false
    }

    override fun filmClicked(film: FilmByKeyword) {
        val bundle = bundleOf("filmId" to film.filmId)
        findNavController().navigate(R.id.action_searchFragment_to_movieFragment, bundle)
    }

    override fun loadMoreContent() {
        lifecycleScope.launchWhenCreated {
            val films = viewModel.loadMore(searchView.query.toString())
            filmAdapter.isLoadMoreAvailable(films.second)
            films.first?.let { filmAdapter.submitList(it) }
        }
    }
}