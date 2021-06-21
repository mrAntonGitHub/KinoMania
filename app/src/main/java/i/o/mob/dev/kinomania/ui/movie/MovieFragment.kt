package i.o.mob.dev.kinomania.ui.movie

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import i.o.mob.dev.kinomania.R
import i.o.mob.dev.kinomania.adapters.ReviewAdapter
import i.o.mob.dev.kinomania.adapters.ReviewAdapterDelegate
import i.o.mob.dev.kinomania.adapters.StaffAdapter
import i.o.mob.dev.kinomania.adapters.StaffAdapterDelegate
import i.o.mob.dev.kinomania.data.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_movie.*
import kotlinx.coroutines.delay


class MovieFragment : Fragment(R.layout.fragment_movie), StaffAdapterDelegate,
    ReviewAdapterDelegate {

    private lateinit var viewModel: MovieViewModel

    private lateinit var actorsAdapter: StaffAdapter
    private lateinit var directorsAdapter: StaffAdapter
    private lateinit var serviceAdapter: StaffAdapter
    private lateinit var reviewAdapter: ReviewAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MovieViewModel::class.java)

        transparentStatusBar()
        requireActivity().bottomNavigation?.visibility = View.GONE
        isLoading(true)

        arguments?.getInt("filmId")?.let { filmId ->
            lifecycleScope.launchWhenStarted {
                val film = viewModel.getFilm(filmId)
                Log.e("MovieFragment", "1")
                val filmFrames = viewModel.getFilmFrame(filmId)?.frames
                Log.e("MovieFragment", "2")
                val staff = viewModel.getFilmStaff(filmId)
                Log.e("MovieFragment", "3")
                val reviews = viewModel.getFilmReview(filmId)?.reviews
                Log.e("MovieFragment", "4")
                val videos = viewModel.getFilmVideos(filmId)
                Log.e("MovieFragment", "5")
                isLoading(false)
                film?.let { setupItems(it, filmFrames, videos) }
                setupStaffList(staff)
                setupReviewsList(reviews)
            }
        }
    }

    private fun transparentStatusBar() {
        val window: Window = requireActivity().window
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }

    private fun isLoading(isLoading: Boolean) {
        if (isLoading) {
            progress.visibility = View.VISIBLE
            appBar.visibility = View.GONE
            nestedScroll.visibility = View.GONE
        } else {
            progress.visibility = View.GONE
            appBar.visibility = View.VISIBLE
            nestedScroll.visibility = View.VISIBLE
        }
    }

    private fun setupItems(film: FilmWrapper, filmFrames: List<Frame>?, filmVideos: FilmVideos?) {
        filmName.showDataOrHide(film.data.nameRu)
        rate.showDataOrHide(film.rating.rating.toString())
        val time = film.data.filmLength?.split(':')
        Log.e("MovieFragment", "${time?.get(0)}")
        if (time?.get(0) == "00" && time[1] == "00"){
            length.visibility = View.GONE
        }
        else if (time?.get(0) != "00") {
            length.showDataOrHide("${time?.get(0)} ч. ${time?.get(1)} мин.")
        } else if (time[1] != "00"){
            length.showDataOrHide("${time[1]} мин.")
        }
        genre.showDataOrHide(
            film.data.genres.map { "${it.genre}  | " }.toString().drop(1).dropLast(3)
                .filter { it != ',' })

        filmSlogan.showDataOrHide("\u00A9 ${film.data.slogan}")
        releaseData.showDataOrHide(
            String.format(
                resources.getString(R.string.release_data),
                film.data.year
            )
        )
        country.showDataOrHide(
            String.format(
                resources.getString(R.string.country),
                film.data.countries.map { it.country }.toString().drop(1).dropLast(1)
            )
        )
        imdb_rate.showDataOrHide(
            String.format(
                resources.getString(R.string.imdb_rate),
                film.rating.ratingImdb
            )
        )
        critics_rate.showDataOrHide(
            String.format(
                resources.getString(R.string.critics_rate),
                film.rating.ratingFilmCritics
            )
        )
        budget.showDataOrHide(
            String.format(
                resources.getString(R.string.budget),
                film.budget.budget
            )
        )
        description.showDataOrHide(film.data.description)

        image.loadImage(film.data.posterUrl, duration = 0)
        imageSlider(filmFrames?.map { it.image })

        var intent: Intent? = null
        if (filmVideos?.trailers != null) {
            intent = Intent(Intent.ACTION_VIEW, Uri.parse(filmVideos.trailers[0].url))
        } else {
            showTrailer.visibility = View.GONE
        }

        showTrailer.setOnClickListener {
            intent?.let { startActivity(intent) }
        }

        back.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun setupStaffList(staff: List<FilmStaff>?) {
        actorsAdapter = StaffAdapter()
        directorsAdapter = StaffAdapter()
        serviceAdapter = StaffAdapter()
        serviceAdapter.setDelegate(this)
        directorsAdapter.setDelegate(this)
        actorsAdapter.setDelegate(this)
        Log.e("dsfsdfsdfdsfs", "${staff}")
        if (staff != null){
            staff.let {
                val actors = it.filter { it.professionText == "Актеры" }
                val directors = it.filter { it.professionText == "Режиссеры" }
                val service = it - actors - directors

                actorsAdapter.submitList(actors)
                directorsAdapter.submitList(directors)
                serviceAdapter.submitList(service)

                if (directors.size > 4) directorsRv.layoutManager = GridLayoutManager(
                    requireContext(),
                    4,
                    LinearLayoutManager.HORIZONTAL,
                    false
                ) else directorsRv.layoutManager =
                    GridLayoutManager(requireContext(), 1, LinearLayoutManager.HORIZONTAL, false)
                directorsRv.adapter = directorsAdapter

                if (actors.size > 4) actorsRv.layoutManager = GridLayoutManager(
                    requireContext(),
                    4,
                    LinearLayoutManager.HORIZONTAL,
                    false
                ) else actorsRv.layoutManager =
                    GridLayoutManager(requireContext(), 1, LinearLayoutManager.HORIZONTAL, false)
                actorsRv.adapter = actorsAdapter

                if (service.size > 4) {
                    serviceRv.layoutManager = GridLayoutManager(
                        requireContext(),
                        4,
                        LinearLayoutManager.HORIZONTAL,
                        false)
                }
                else {
                    serviceRv.layoutManager =
                        GridLayoutManager(requireContext(), 1, LinearLayoutManager.HORIZONTAL, false)
                    serviceRv.adapter = serviceAdapter
                }
                serviceRv.setHasFixedSize(false)

                if (actors.isNullOrEmpty()) actorsTitle.visibility = View.GONE
                if (directors.isNullOrEmpty()) directorsTitle.visibility = View.GONE
                if (service.isNullOrEmpty()) serviceTitle.visibility = View.GONE
            }
        }else{
            actorsTitle.visibility = View.GONE
            directorsTitle.visibility = View.GONE
            serviceTitle.visibility = View.GONE
        }

    }

    private fun setupReviewsList(reviews: List<Review>?) {
        reviewAdapter = ReviewAdapter()
        reviewAdapter.setDelegate(this)
        if (!reviews.isNullOrEmpty()) {
            reviewAdapter.submitList(reviews)
            reviewRv.adapter = reviewAdapter
        } else {
            reviewTitle.visibility = View.GONE
        }
    }

    private fun ImageView.loadImage(url: String, duration: Int = 2_500) {
        Glide
            .with(requireContext())
            .load(url)
            .transition(DrawableTransitionOptions.withCrossFade(duration))
            .override(800, 700)
            .into(this)
    }

    private fun imageSlider(imageUrls: List<String>?) {
        imageUrls?.let { urls ->
            if (urls.isNotEmpty()) {
                lifecycleScope.launchWhenStarted {
                    urls.forEach {
                        previewImage.loadImage(it)
                        delay(5_000)
                    }
                    imageSlider(imageUrls)
                }
            }
        }
    }

    private fun TextView.showDataOrHide(data: String?) {
        val regexNull = Regex("null")
        val regex00 = Regex("0.0")
        if (!data.isNullOrEmpty() && !data.contains(regexNull) && !data.contains(regex00) && data != "0" && data != "0.0" && data != "") {
            this.text = data
        } else {
            this.visibility = View.GONE
        }
    }

    override fun staffClicked(staff: FilmStaff) {
        val bundle = bundleOf("staffId" to staff.staffId)
        findNavController().navigate(R.id.action_movieFragment_to_staffFragment, bundle)
    }

    override fun reviewClicked(review: Review) {
        val bundle = bundleOf("id" to "${review.reviewId}")
        findNavController().navigate(R.id.action_movieFragment_to_reviewFragment, bundle)
    }
}