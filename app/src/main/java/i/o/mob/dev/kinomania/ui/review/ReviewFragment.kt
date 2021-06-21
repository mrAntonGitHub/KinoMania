package i.o.mob.dev.kinomania.ui.review

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import i.o.mob.dev.kinomania.R
import i.o.mob.dev.kinomania.utils.Utils.Companion.hideBottomNavigation
import i.o.mob.dev.kinomania.utils.Utils.Companion.hideKeyboard
import i.o.mob.dev.kinomania.utils.Utils.Companion.reviewDataToReadable
import i.o.mob.dev.kinomania.utils.Utils.Companion.transparentStatusBar
import kotlinx.android.synthetic.main.fragment_movie.*
import kotlinx.android.synthetic.main.review_fragment.*
import kotlinx.android.synthetic.main.review_fragment.back
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.withContext

class ReviewFragment : Fragment(R.layout.review_fragment) {

    private lateinit var viewModel: ReviewViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ReviewViewModel::class.java)

        transparentStatusBar(false)
        hideBottomNavigation(true)

        arguments?.get("id")?.let {
            lifecycleScope.launchWhenCreated {
                val review = viewModel.getReview((it as String).toInt())
                withContext(Main){
                    when(review?.reviewType){
                        "NEGATIVE" -> {
                            val red = resources.getColor(android.R.color.holo_red_light, null)
                            reviewType.setBackgroundColor(red)
                        }
                        "NEUTRAL" -> {
                            val green = resources.getColor(android.R.color.holo_green_light, null)
                            reviewType.setBackgroundColor(green)
                        }
                        else -> {
                            val green = resources.getColor(android.R.color.holo_green_light, null)
                            reviewType.setBackgroundColor(green)
                        }
                    }
                    toolbarTitle.text = review?.reviewAutor
                    text.text = review?.reviewDescription
                    data.text = review?.reviewData?.reviewDataToReadable()
                }
            }

        }

        back.setOnClickListener { requireActivity().onBackPressed() }

    }
}