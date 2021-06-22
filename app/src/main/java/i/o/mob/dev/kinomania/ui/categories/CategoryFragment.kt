package i.o.mob.dev.kinomania.ui.categories

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import i.o.mob.dev.kinomania.R
import i.o.mob.dev.kinomania.adapters.SimpleAdapter
import i.o.mob.dev.kinomania.adapters.SimpleAdapterDelegate
import i.o.mob.dev.kinomania.utils.Utils.Companion.hideBottomNavigation
import i.o.mob.dev.kinomania.utils.Utils.Companion.transparentStatusBar
import kotlinx.android.synthetic.main.fragment_categories.*


class CategoryFragment : Fragment(R.layout.fragment_categories), SimpleAdapterDelegate {

    private lateinit var viewModel: CategoryViewModel
    lateinit var simpleAdapter: SimpleAdapter

    override fun onStart() {
        super.onStart()
        hideBottomNavigation(false)
        transparentStatusBar(false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(CategoryViewModel::class.java)

        simpleAdapter = SimpleAdapter()
        simpleAdapter.setDelegate(this)
        categoriesRv.addItemDecoration(
            DividerItemDecoration(
                requireActivity(),
                RecyclerView.VERTICAL
            )
        )
        categoriesRv.setHasFixedSize(true)
        categoriesRv.adapter = simpleAdapter

        lifecycleScope.launchWhenCreated {
            val categories = viewModel.getCategories()
            categories?.let {
                simpleAdapter.submitList(categories.map { it.genre })
            }

        }
    }

    override fun categoryClicked(position: Int) {
        lifecycleScope.launchWhenCreated {
            val genres = viewModel.getCategories()
            val id = genres?.get(position)?.id
            id.let {
                val bundle = bundleOf(
                    "title" to "${genres?.get(position)?.genre}",
                    "key" to (mapOf("genre" to id.toString()))
                )
                findNavController().navigate(
                    R.id.action_navigation_dashboard_to_parameteredFilmsFragment,
                    bundle
                )
            }
        }
    }
}