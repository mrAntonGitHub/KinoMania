package i.o.mob.dev.kinomania.ui.filmsGrid

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import i.o.mob.dev.kinomania.R
import i.o.mob.dev.kinomania.adapters.FilmsGridAdapter
import i.o.mob.dev.kinomania.adapters.FilmsGridAdapterDelegate
import i.o.mob.dev.kinomania.data.TopFilmItem
import i.o.mob.dev.kinomania.data.TopFilmsType
import i.o.mob.dev.kinomania.utils.State
import i.o.mob.dev.kinomania.utils.Utils.Companion.hideBottomNavigation
import i.o.mob.dev.kinomania.utils.Utils.Companion.transparentStatusBar
import i.o.mob.dev.kinomania.utils.VerticalSpaceItemDecorator
import kotlinx.android.synthetic.main.fragment_grid_films.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext

class FilmsGridFragment : Fragment(R.layout.fragment_grid_films), FilmsGridAdapterDelegate {

    private lateinit var viewModel: FilmsGridViewModel

    private val adapter = FilmsGridAdapter()

    override fun onResume() {
        super.onResume()
        hideBottomNavigation(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(FilmsGridViewModel::class.java)

        transparentStatusBar(false)

        adapter.setDelegate(this)
        filmsRv.addItemDecoration(VerticalSpaceItemDecorator())
        filmsRv.layoutManager = GridLayoutManager(requireActivity(), resources.getInteger(R.integer.films_raw_count))
        filmsRv.adapter = adapter

        arguments?.getString("type")?.let {
            lifecycleScope.launchWhenCreated {
                viewModel.films.collect { state ->
                    when(state){
                        is State.Loading -> { }
                        is State.Success -> {
                            withContext(Dispatchers.Main) {
                                adapter.submitList(state.data)
                            }
                        }
                        is State.Error -> {

                        }
                        is State.NoMoreData -> {
                            withContext(Dispatchers.Main) {
                                state.data.let { adapter.submitList(it) }
                                adapter.isLoadMoreAvailable(false)
                                adapter.notifyItemRemoved(adapter.itemCount - 1)
                            }
                        }
                    }
                }
            }
            when(it){
                TopFilmsType.TOP_100_POPULAR_FILMS.toString() -> {
                    toolbarTitle.text = "Топ 100"
                    adapter.setAdapterType(TopFilmsType.TOP_100_POPULAR_FILMS)
                    viewModel.initialListLoad(TopFilmsType.TOP_100_POPULAR_FILMS)
                }
                TopFilmsType.TOP_250_BEST_FILMS.toString() -> {
                    toolbarTitle.text = "Топ 250"
                    adapter.setAdapterType(TopFilmsType.TOP_250_BEST_FILMS)
                    viewModel.initialListLoad(TopFilmsType.TOP_250_BEST_FILMS)
                }
                TopFilmsType.TOP_AWAIT_FILMS.toString() -> {
                    toolbarTitle.text = "Топ Ожидаемых"
                    adapter.setAdapterType(TopFilmsType.TOP_AWAIT_FILMS)
                    viewModel.initialListLoad(TopFilmsType.TOP_AWAIT_FILMS)
                }
                else -> { }
            }
        }

        back.setOnClickListener {
            requireActivity().onBackPressed()
        }

    }

    override fun filmClicked(film: TopFilmItem) {
        val bundle = bundleOf("filmId" to film.filmId)
        findNavController().navigate(R.id.action_filmsGridFragment_to_movieFragment, bundle)
    }

    override fun loadMoreContent(type: TopFilmsType) {
        lifecycleScope.launchWhenCreated {
            viewModel.loadMoreContent(type)
        }
    }

}