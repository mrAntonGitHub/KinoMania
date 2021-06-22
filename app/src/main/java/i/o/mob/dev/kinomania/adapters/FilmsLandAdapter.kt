package i.o.mob.dev.kinomania.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import i.o.mob.dev.kinomania.R
import i.o.mob.dev.kinomania.data.FilmByKeyword
import i.o.mob.dev.kinomania.utils.Utils

interface FilmsLandAdapterDelegate {
    fun filmClicked(film: FilmByKeyword)
    fun loadMoreContent()
}

class FilmsLandAdapter : RecyclerView.Adapter<FilmsLandAdapter.ViewHolder>() {

    private var filmsLandAdapterDelegate: FilmsLandAdapterDelegate? = null
    private val films: MutableList<FilmByKeyword> = mutableListOf()
    private var isLoadMoreAvailable: Boolean = true

    fun setDelegate(filmsLandAdapterDelegate: FilmsLandAdapterDelegate) {
        this.filmsLandAdapterDelegate = filmsLandAdapterDelegate
    }

    fun submitList(films: List<FilmByKeyword>) {
        this.films.clear()
        this.films.addAll(films)
        notifyDataSetChanged()
    }

    fun isLoadMoreAvailable(boolean: Boolean) {
        this.isLoadMoreAvailable = boolean
    }

    override fun getItemCount(): Int {
        return when (films.count()) {
            0 -> 0
            else -> {
                if (isLoadMoreAvailable) films.count() + 1
                else films.count()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            in 0 until (films.count()) -> {
                TYPE_CONTENT
            }
            else -> {
                TYPE_LOAD_MORE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            TYPE_CONTENT -> {
                ViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_movie_land, parent, false)
                )
            }
            TYPE_LOAD_MORE -> {
                ViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_load_more, parent, false)
                )
            }
            else -> {
                ViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_movie_land, parent, false)
                )
            }
        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            TYPE_CONTENT -> {
                holder.apply {
                    staff = this@FilmsLandAdapter.films[position]
                    item.setOnClickListener { onItemClicked() }
                }
            }
            TYPE_LOAD_MORE -> {
                holder.apply {
                    loadMoreButton?.visibility = View.VISIBLE
                    loadMoreProgress?.visibility = View.GONE
                    loadMoreButton?.setOnClickListener {
                        loadMoreButton.visibility = View.GONE
                        loadMoreProgress?.visibility = View.VISIBLE
                        loadMoreContent()
                    }
                }
            }
        }

    }

    inner class ViewHolder(val item: View) : RecyclerView.ViewHolder(item) {
        private val image = item.findViewById<ImageView>(R.id.image)
        private val rating = item.findViewById<TextView>(R.id.rating)
        private val title = item.findViewById<TextView>(R.id.title)
        private val genre = item.findViewById<TextView>(R.id.type)
        private val releaseData = item.findViewById<TextView>(R.id.releaseData)
        private val countries = item.findViewById<TextView>(R.id.countries)
        private val length = item.findViewById<TextView>(R.id.length)

        val loadMoreButton: Button? = item.findViewById(R.id.button)
        val loadMoreProgress: ProgressBar? = item.findViewById(R.id.progress)

        var staff: FilmByKeyword? = null
            set(value) {
                value?.let { newValue ->
                    field = newValue
                    newValue.apply {
                        try {
                            this@ViewHolder.rating.text = rating
                            this@ViewHolder.rating.backgroundTintList =
                                AppCompatResources.getColorStateList(
                                    item.context,
                                    Utils.getFilmLabelColor(rating?.toFloat() ?: -1f)
                                )
                        } catch (e: Exception) {
                            this@ViewHolder.rating.text = "Ожидается"
                            this@ViewHolder.rating.backgroundTintList =
                                AppCompatResources.getColorStateList(item.context, R.color.blue)
                        }
                        if (rating.isNullOrBlank()) {
                            this@ViewHolder.rating.visibility = View.GONE
                        } else {
                            this@ViewHolder.rating.visibility = View.VISIBLE
                        }
                        this@ViewHolder.title.text = nameRu
                        this@ViewHolder.genre.text =
                            genres?.map { it.genre }.toString().drop(1).dropLast(1)
                        releaseData.text = this.year
                        this@ViewHolder.countries.text =
                            countries?.map { it.country }.toString().drop(1).dropLast(1)
                        this.filmLength?.let {
                            val time = it.split(':')
                            when {
                                time[0] != "0" -> {
                                    length.text = String.format(
                                        itemView.resources.getString(R.string.film_length),
                                        time[0],
                                        time[1]
                                    )
                                }
                                time[1] != "0" -> {
                                    length.text = String.format(
                                        itemView.resources.getString(R.string.film_length_minutes),
                                        time[1]
                                    )
                                }
                                else -> {
                                    length.visibility = View.GONE
                                }
                            }
                        }
                        Glide
                            .with(item.context)
                            .load(this.posterUrl)
                            .override(600, 800)
                            .into(image)
                    }
                }
            }

        fun onItemClicked() {
            filmsLandAdapterDelegate?.filmClicked(this@FilmsLandAdapter.films[absoluteAdapterPosition])
        }

        fun loadMoreContent() {
            filmsLandAdapterDelegate?.loadMoreContent()
        }
    }

}