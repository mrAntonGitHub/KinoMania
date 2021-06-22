package i.o.mob.dev.kinomania.utils

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import i.o.mob.dev.kinomania.Application
import i.o.mob.dev.kinomania.R
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.roundToInt

@Singleton
class Utils {

    @Inject
    lateinit var context: Context

    init {
        Application.application.appComponent.inject(this)
    }

    companion object {
        private val context = Utils().context

        fun Int.toDp(): Int {
            val displayMetrics: DisplayMetrics = context.resources.displayMetrics
            return (this / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
        }

        fun Int.toPx(): Int {
            val displayMetrics: DisplayMetrics = context.resources.displayMetrics
            return (this * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
        }

        fun String.reviewDataToReadable(): String {
            return "${this.substring(8..9)}.${this.substring(5..6)}.${this.substring(0..3)}"
        }

        fun Fragment.hideKeyboard() {
            view?.let { activity?.hideKeyboard(it) }
        }

        fun Activity.hideKeyboard() {
            hideKeyboard(currentFocus ?: View(this))
        }

        fun Fragment.hideBottomNavigation(boolean: Boolean) {
            if (boolean) {
                this.requireActivity().bottomNavigation?.visibility = View.GONE
            } else {
                this.requireActivity().bottomNavigation?.visibility = View.VISIBLE
            }
        }

        fun Activity.hideBottomNavigation(boolean: Boolean) {
            if (boolean) {
                this.bottomNavigation.visibility = View.GONE
            } else {
                this.bottomNavigation.visibility = View.VISIBLE
            }
        }

        private fun Context.hideKeyboard(view: View) {
            val inputMethodManager =
                getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }

        fun Activity.transparentStatusBar(isTransparent: Boolean) {
            val window: Window = this.window
            if (isTransparent) {
                window.setFlags(
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                )
            } else {
                window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
            }
        }

        fun Fragment.transparentStatusBar(isTransparent: Boolean) {
            val window: Window = this.requireActivity().window
            if (isTransparent) {
                window.setFlags(
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                )
            } else {
                window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
            }
        }

        fun getFilmLabelColor(rating: Float): Int {
            return when (rating) {
                in 0.0..4.0 -> {
                    R.color.red
                }
                in 4.0..7.0 -> {
                    R.color.yellow
                }
                in 7.0..10.0 -> {
                    R.color.green
                }
                else -> {
                    Log.e("Utils", "Unhandled value")
                    Color.TRANSPARENT
                }
            }
        }

    }

}