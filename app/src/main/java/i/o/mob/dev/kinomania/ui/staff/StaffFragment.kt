package i.o.mob.dev.kinomania.ui.staff

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import i.o.mob.dev.kinomania.R
import i.o.mob.dev.kinomania.adapters.FactsAdapter
import i.o.mob.dev.kinomania.adapters.FilmsAdapter
import i.o.mob.dev.kinomania.adapters.FilmsAdapterDelegate
import i.o.mob.dev.kinomania.data.StaffFilm
import i.o.mob.dev.kinomania.utils.Utils.Companion.transparentStatusBar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_staff.*

class StaffFragment : Fragment(R.layout.fragment_staff), FilmsAdapterDelegate {

    private lateinit var viewModel: StaffViewModel
    private lateinit var filmAdapter: FilmsAdapter
    private lateinit var factsAdapter: FactsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(StaffViewModel::class.java)

        filmAdapter = FilmsAdapter()
        filmAdapter.setDelegate(this)
        filmsRv.adapter = filmAdapter

        factsAdapter = FactsAdapter()
        factsRv.adapter = factsAdapter

        requireActivity().bottomNavigation.visibility = View.GONE
        transparentStatusBar(false)

        arguments?.getInt("staffId")?.let { staffId ->
            lifecycleScope.launchWhenCreated {
                val staff = viewModel.getStaff(staffId)
                staff?.let {
                    if (it.nameRu.isNullOrEmpty()) {
                        toolbarTitle.text = it.nameEn
                        name.text = it.nameEn
                    } else {
                        toolbarTitle.text = it.nameRu
                        name.text = it.nameRu
                    }
                    profession.text = it.profession
                    if (it.birthday == null) age.visibility = View.GONE
                    val birth = String.format(
                        resources.getString(R.string.data_and_age),
                        it.birthday?.substring(8..9),
                        it.birthday?.substring(5..6),
                        it.birthday?.substring(0..3),
                        it.age.toString()
                    )
                    if (it.death != null){
                        age.text = String.format(resources.getString(R.string.data_and_death_age),
                            it.birthday?.substring(8..9),
                            it.birthday?.substring(5..6),
                            it.birthday?.substring(0..3),
                            it.death.substring(8..9),
                            it.death.substring(5..6),
                            it.death.substring(0..3),
                            it.age.toString())
                    }else{
                        age.text = birth
                    }

                    if (!it.films.isNullOrEmpty()) {
                        it.films?.let { it1 -> filmAdapter.submitList(it1.filter { (it.nameEn != null || it.nameRu != null) }) }
                    } else {
                        filmTitle.visibility = View.GONE
                    }
                    if (!it.facts.isNullOrEmpty()) {
                        Log.e("sdfsdfsdfsfdsf", "${it.facts.size}")
                        factsAdapter.submitList(it.facts)
                    } else {
                        factsTitle.visibility = View.GONE
                    }


                    Glide
                        .with(requireContext())
                        .load(it.posterUrl)
                        .override(600, 800)
                        .into(avatar)
                }
            }
        }

        back.setOnClickListener { requireActivity().onBackPressed() }


    }

    override fun filmClicked(film: StaffFilm) {
        val bundle = bundleOf("filmId" to film.filmId)
        findNavController().navigate(R.id.action_staffFragment_to_movieFragment, bundle)
    }

}