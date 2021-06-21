package i.o.mob.dev.kinomania.ui.home

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import i.o.mob.dev.kinomania.R
import i.o.mob.dev.kinomania.adapters.TopFilmsAdapter
import i.o.mob.dev.kinomania.adapters.TopFilmsAdapterDelegate
import i.o.mob.dev.kinomania.data.TopFilmItem
import i.o.mob.dev.kinomania.data.TopFilmsType
import i.o.mob.dev.kinomania.utils.State
import i.o.mob.dev.kinomania.utils.Utils.Companion.hideBottomNavigation
import i.o.mob.dev.kinomania.utils.Utils.Companion.hideKeyboard
import i.o.mob.dev.kinomania.utils.Utils.Companion.transparentStatusBar
import i.o.mob.dev.kinomania.utils.VerticalSpaceItemDecorator
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HomeFragment : Fragment(R.layout.fragment_home), TopFilmsAdapterDelegate {

    private lateinit var viewModel: HomeViewModel

    private var top100Adapter = TopFilmsAdapter()
    private var topAwaitAdapter = TopFilmsAdapter()
    private var top250Adapter = TopFilmsAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        AppCompatDelegate.getDefaultNightMode()

        transparentStatusBar(false)
        hideBottomNavigation(false)
        hideKeyboard()
        contentIsLoading(true)

        initAdapters()

        lifecycleScope.launchWhenStarted {
            viewModel.top100.collect{state ->
                Log.e("HomeFragment", "$state")
                when(state){
                    is State.Loading -> { }
                    is State.Success -> {
                        withContext(Main) {
                            top100Adapter.submitList(state.data)
                            contentIsLoading(false)
                            noConnection(false)
                        }
                    }
                    is State.Error -> {
                        noConnection(true)
                    }
                    is State.NoMoreData -> {
                        withContext(Main) {
                            state.data.let { top100Adapter.submitList(it) }
                            top100Adapter.isLoadMoreAvailable(false)
                            top100Adapter.notifyItemRemoved(top100Adapter.itemCount - 1)
                            contentIsLoading(false)
                        }
                    }
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.top250.collect{state ->
                when(state){
                    is State.Loading -> { }
                    is State.Success -> {
                        withContext(Main) {
                            top250Adapter.submitList(state.data)
                            contentIsLoading(false)
                            noConnection(false)
                        }
                    }
                    is State.Error -> {
                        noConnection(true)
                    }
                    is State.NoMoreData -> {
                        withContext(Main) {
                            state.data.let { top250Adapter.submitList(it) }
                            top250Adapter.isLoadMoreAvailable(false)
                            top250Adapter.notifyItemRemoved(top250Adapter.itemCount - 1)
                            contentIsLoading(false)
                        }
                    }
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.topAwait.collect{state ->
                Log.e("HomeFragment", "$state")
                when(state){
                    is State.Loading -> { }
                    is State.Success -> {
                        withContext(Main) {
                            topAwaitAdapter.submitList(state.data)
                            contentIsLoading(false)
                            noConnection(false)
                        }
                    }
                    is State.Error -> {
                        noConnection(true)
                    }
                    is State.NoMoreData -> {
                        withContext(Main) {
                            state.data.let { topAwaitAdapter.submitList(it) }
                            topAwaitAdapter.isLoadMoreAvailable(false)
                            topAwaitAdapter.notifyItemRemoved(topAwaitAdapter.itemCount - 1)
                            contentIsLoading(false)
                        }
                    }
                }
            }
        }

        search.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_searchFragment)
        }

        parameters.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_setParametersFragment)
        }

        reload?.setOnClickListener {
            reload.visibility = View.GONE
            progress.visibility = View.VISIBLE
            loadMoreContent(TopFilmsType.TOP_100_POPULAR_FILMS)
            loadMoreContent(TopFilmsType.TOP_250_BEST_FILMS)
            loadMoreContent(TopFilmsType.TOP_AWAIT_FILMS)
        }

        top100Show.setOnClickListener {
            val bundle = bundleOf("type" to TopFilmsType.TOP_100_POPULAR_FILMS.toString())
            findNavController().navigate(R.id.action_navigation_home_to_filmsGridFragment, bundle)
        }

        top250Show.setOnClickListener {
            val bundle = bundleOf("type" to TopFilmsType.TOP_250_BEST_FILMS.toString())
            findNavController().navigate(R.id.action_navigation_home_to_filmsGridFragment, bundle)
        }

        topAwaitShow.setOnClickListener {
            val bundle = bundleOf("type" to TopFilmsType.TOP_AWAIT_FILMS.toString())
            findNavController().navigate(R.id.action_navigation_home_to_filmsGridFragment, bundle)
        }

    }

    private fun contentIsLoading(isLoading: Boolean){
        CoroutineScope(Main).launch {
            when (isLoading) {
                true -> {
                    loadingItemsPb?.visibility = View.VISIBLE
                    nestedScroll?.visibility = View.GONE
                }
                false -> {
                    loadingItemsPb?.visibility = View.GONE
                    nestedScroll?.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun initAdapters() {
        top100Adapter = TopFilmsAdapter()
        top100Adapter.setDelegate(this)
        top100Adapter.setAdapterType(TopFilmsType.TOP_100_POPULAR_FILMS)
        top100Rv.addItemDecoration(VerticalSpaceItemDecorator())
        top100Adapter.isLoadMoreAvailable(true)
        top100Rv.adapter = top100Adapter

        top250Adapter = TopFilmsAdapter()
        top250Adapter.setDelegate(this)
        top250Adapter.setAdapterType(TopFilmsType.TOP_250_BEST_FILMS)
        top250Rv.addItemDecoration(VerticalSpaceItemDecorator())
        top250Rv.adapter = top250Adapter

        topAwaitAdapter = TopFilmsAdapter()
        topAwaitAdapter.setDelegate(this)
        topAwaitAdapter.setAdapterType(TopFilmsType.TOP_AWAIT_FILMS)
        topAwaitRv.addItemDecoration(VerticalSpaceItemDecorator())
        topAwaitRv.adapter = topAwaitAdapter
    }

    private fun noConnection(boolean: Boolean) {
        if (boolean) {
            appBar.visibility = View.GONE
            nestedScroll.visibility = View.GONE
            noConnection.visibility = View.VISIBLE
            reload.visibility = View.VISIBLE
            progress.visibility = View.GONE
            loadingItemsPb?.visibility = View.GONE
            nestedScroll?.visibility = View.GONE
        } else {
            appBar.visibility = View.VISIBLE
            nestedScroll.visibility = View.VISIBLE
            noConnection.visibility = View.GONE
        }
    }

    override fun filmClicked(film: TopFilmItem) {
        val bundle = bundleOf("filmId" to film.filmId)
        findNavController().navigate(R.id.action_navigation_home_to_movieFragment, bundle)
    }

    override fun loadMoreContent(type: TopFilmsType) {
        lifecycleScope.launchWhenCreated {
            viewModel.loadMoreContent(type)
        }
    }

}