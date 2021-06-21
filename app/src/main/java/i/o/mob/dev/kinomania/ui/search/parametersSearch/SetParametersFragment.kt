package i.o.mob.dev.kinomania.ui.search.parametersSearch

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import i.o.mob.dev.kinomania.R
import i.o.mob.dev.kinomania.ui.search.parametersSearch.selectItem.LIST_COUNTRY
import i.o.mob.dev.kinomania.ui.search.parametersSearch.selectItem.LIST_GENRE
import i.o.mob.dev.kinomania.ui.search.parametersSearch.selectItem.LIST_TYPE
import i.o.mob.dev.kinomania.utils.Utils.Companion.hideBottomNavigation
import kotlinx.android.synthetic.main.fragment_set_parameters.*
import java.util.*

class SetParametersFragment : Fragment(R.layout.fragment_set_parameters) {

    private lateinit var viewModel: SetParametersViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(SetParametersViewModel::class.java)

        hideBottomNavigation(true)

        setSelectedCategories()
        observeSelectedItems()

        rateValue.setValues(5f, 10f)
        yearValue.setValues(1950f, Calendar.getInstance().get(Calendar.YEAR).toFloat())

        lifecycleScope.launchWhenStarted {
            val filters = viewModel.getFilters()

            val typeMap =
                listOf(Triple("FILM", "фильмы", false), Triple("TV_SHOW", "сериалы", false))
            val genreMap = filters?.genres?.map { Triple(it.id.toString(), it.genre, false) }
            val countriesMap =
                filters?.countries?.map { Triple(it.id.toString(), it.country, false) }

            typeCard.setOnClickListener {
                val list = typeMap.map {
                    Triple(it.first, it.second, viewModel.selectedTypes.containsKey(it.first))
                }

                val bundle = bundleOf(LIST_TYPE to list)
                findNavController().navigate(
                    R.id.action_setParametersFragment_to_itemFragment2,
                    bundle
                )
            }

            genreCard.setOnClickListener {
                val list = genreMap?.map {
                    Triple(it.first, it.second, viewModel.selectedGenres.containsKey(it.first))
                }
                val bundle = bundleOf(LIST_GENRE to list)
                findNavController().navigate(
                    R.id.action_setParametersFragment_to_itemFragment2,
                    bundle
                )
            }

            countryCard.setOnClickListener {
                val list = countriesMap?.map {
                    Triple(it.first, it.second, viewModel.selectedCountries.containsKey(it.first))
                }
                val bundle = bundleOf(LIST_COUNTRY to list)
                findNavController().navigate(
                    R.id.action_setParametersFragment_to_itemFragment2,
                    bundle
                )
            }


        }

        rateValue.addOnChangeListener { _, _, _ ->
            val values = rateValue.values
            rateText.text = String.format(
                resources.getString(R.string.rate_value),
                values[0].toInt(),
                values[1].toInt()
            )
        }

        yearValue.addOnChangeListener { _, _, _ ->
            val values = yearValue.values
            yearText.text = String.format(
                resources.getString(R.string.year_value),
                values[0].toInt(),
                values[1].toInt()
            )
        }

        search.setOnClickListener {
            val types = mutableListOf<String>()
            if (viewModel.selectedTypes.count() == 2){
                types.add("All")
            }else{
                viewModel.selectedTypes.forEach {
                    types.add(it.key)
                }
            }
            val genre = mutableListOf<String>()
            viewModel.selectedGenres.forEach {
                genre.add(it.key)
            }
            val country = mutableListOf<String>()
            viewModel.selectedCountries.forEach {
                country.add(it.key)
            }
            val yearFrom = yearValue.values[0].toInt()
            val yearTo = yearValue.values[1].toInt()
            val ratingFrom = rateValue.values[0].toInt()
            val ratingTo = rateValue.values[1].toInt()

            val bundle = bundleOf(
                "title" to "Поиск по параметрам",
                "key" to (mapOf(
                    "type" to types.toString().drop(1).dropLast(1),
                    "genre" to genre.toString().drop(1).dropLast(1),
                    "country" to country.toString().drop(1).dropLast(1),
                    "yearFrom" to yearFrom.toString(),
                    "yearTo" to yearTo.toString(),
                    "ratingFrom" to ratingFrom.toString(),
                    "ratingTo" to ratingTo.toString()
                ))
            )
            Log.e("dsfsdfsdfsdfsd", "$bundle")
            findNavController().navigate(
                R.id.action_setParametersFragment_to_parameteredFilmsFragment,
                bundle
            )
        }

        clear.setOnClickListener {
            viewModel.selectedTypes.clear()
            viewModel.selectedGenres.clear()
            viewModel.selectedCountries.clear()
            Log.e(
                "dsfsdfsdfsdf",
                "${viewModel.selectedTypes} ${viewModel.selectedGenres} ${viewModel.selectedCountries}"
            )
            typeSelected.text = "Любые"
            genreSelected.text = "Любые"
            countrySelected.text = "Любые"
            rateValue.setValues(5f, 10f)
            yearValue.setValues(1950f, Calendar.getInstance().get(Calendar.YEAR).toFloat())
        }

        back.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun setSelectedCategories() {
        if (viewModel.selectedTypes.isNullOrEmpty() || viewModel.selectedTypes.count() == 2) {
            typeSelected.text = "Любые"
        } else {
            typeSelected.text =
                viewModel.selectedTypes.map { it.value }.toString().drop(1).dropLast(1)
        }
        if (viewModel.selectedGenres.isNullOrEmpty()) {
            genreSelected.text = "Любые"
        } else {
            genreSelected.text =
                viewModel.selectedGenres.map { it.value }.toString().drop(1).dropLast(1)
        }
        if (viewModel.selectedCountries.isNullOrEmpty()) {
            countrySelected.text = "Любые"
        } else {
            countrySelected.text =
                viewModel.selectedCountries.map { it.value }.toString().drop(1).dropLast(1)
        }
    }

    private fun observeSelectedItems() {
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Pair<String, Map<String, String>>>(
            "key"
        )?.observe(viewLifecycleOwner, Observer<Pair<String, Map<String, String>>> {
            when (it.first) {
                LIST_TYPE -> {
                    viewModel.selectedTypes.clear()
                    viewModel.selectedTypes.putAll(it.second)
                    if (it.second.isNullOrEmpty()) {
                        typeSelected.text = "Любые"
                    } else {
                        typeSelected.text =
                            it.second.map { it.value }.toString().drop(1).dropLast(1)
                    }
                }
                LIST_GENRE -> {
                    viewModel.selectedGenres.clear()
                    viewModel.selectedGenres.putAll(it.second)
                    if (it.second.isNullOrEmpty()) {
                        genreSelected.text = "Любые"
                    } else {
                        genreSelected.text =
                            it.second.map { it.value }.toString().drop(1).dropLast(1)
                    }
                }
                LIST_COUNTRY -> {
                    viewModel.selectedCountries.clear()
                    viewModel.selectedCountries.putAll(it.second)
                    if (it.second.isNullOrEmpty()) {
                        countrySelected.text = "Любые"
                    } else {
                        countrySelected.text =
                            it.second.map { it.value }.toString().drop(1).dropLast(1)
                    }
                }
            }
        })
    }

}