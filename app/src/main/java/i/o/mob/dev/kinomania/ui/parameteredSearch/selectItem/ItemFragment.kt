package i.o.mob.dev.kinomania.ui.parameteredSearch.selectItem

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import i.o.mob.dev.kinomania.R
import i.o.mob.dev.kinomania.adapters.MyItemRecyclerViewAdapter
import i.o.mob.dev.kinomania.adapters.SimpleItemDelegate
import i.o.mob.dev.kinomania.utils.Utils.Companion.hideBottomNavigation
import kotlinx.android.synthetic.main.fragment_item_list.*

const val LIST_TYPE = "LIST_TYPE"
const val LIST_GENRE = "LIST_GENRE"
const val LIST_COUNTRY = "LIST_COUNTRY"

class ItemFragment : Fragment(R.layout.fragment_item_list), SimpleItemDelegate {

    private val selectedItemsMap = mutableMapOf<String, String>()

    private var currentList = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideBottomNavigation(true)

        lifecycleScope.launchWhenCreated {

            toolbarTitle.text = "Выберите параметры"

            arguments?.get("LIST_TYPE")?.let { map ->
                currentList = LIST_TYPE
                try {
                    map as List<Triple<String, String, Boolean>>
                    val simpleListItems = map.map {
                        if (it.third != false) {
                            selectedItemsMap[it.first] = it.second
                        }
                        SimpleListItem(it.first, it.second, it.third)
                    }
                    list.adapter = MyItemRecyclerViewAdapter(simpleListItems, this@ItemFragment)

                } catch (e: Exception) {
                    // todo handle error
                }
            }

            arguments?.get("LIST_GENRE")?.let { map ->
                currentList = LIST_GENRE
                try {
                    map as List<Triple<String, String, Boolean>>
                    val simpleListItems = map.map {
                        if (it.third != false) {
                            selectedItemsMap[it.first] = it.second
                        }
                        SimpleListItem(it.first, it.second, it.third)
                    }
                    list.adapter = MyItemRecyclerViewAdapter(simpleListItems, this@ItemFragment)

                } catch (e: Exception) {
                    // todo handle error
                }
            }

            arguments?.get("LIST_COUNTRY")?.let { map ->
                currentList = LIST_COUNTRY
                try {
                    map as List<Triple<String, String, Boolean>>
                    val simpleListItems = map.map {
                        if (it.third != false) {
                            selectedItemsMap[it.first] = it.second
                        }
                        SimpleListItem(it.first, it.second, it.third)
                    }
                    list.adapter = MyItemRecyclerViewAdapter(simpleListItems, this@ItemFragment)

                } catch (e: Exception) {
                    Log.e("ItemFragment", "$e")
                }
            }
        }

        choose.setOnClickListener {
            when (currentList) {
                LIST_TYPE -> {
                    findNavController().previousBackStackEntry?.savedStateHandle?.set(
                        "key",
                        LIST_TYPE to selectedItemsMap
                    )
                    requireActivity().onBackPressed()
                }
                LIST_GENRE -> {
                    findNavController().previousBackStackEntry?.savedStateHandle?.set(
                        "key",
                        LIST_GENRE to selectedItemsMap
                    )
                    requireActivity().onBackPressed()
                }
                LIST_COUNTRY -> {
                    findNavController().previousBackStackEntry?.savedStateHandle?.set(
                        "key",
                        LIST_COUNTRY to selectedItemsMap
                    )
                    requireActivity().onBackPressed()
                }
            }
        }

        back.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    override fun itemSelected(simpleListItem: SimpleListItem, isSelected: Boolean) {
        if (isSelected) {
            selectedItemsMap[simpleListItem.id] = simpleListItem.content
        } else {
            selectedItemsMap.remove(simpleListItem.id)
        }
    }


}