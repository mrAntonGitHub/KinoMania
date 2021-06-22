package i.o.mob.dev.kinomania.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import i.o.mob.dev.kinomania.R
import i.o.mob.dev.kinomania.data.StaffFilm
import i.o.mob.dev.kinomania.utils.Utils

interface FilmsAdapterDelegate {
    fun filmClicked(film: StaffFilm)
}

class FilmsAdapter : RecyclerView.Adapter<FilmsAdapter.ViewHolder>() {

    private var filmsAdapterDelegate: FilmsAdapterDelegate? = null
    private var films: List<StaffFilm> = listOf()

    fun setDelegate(filmsAdapterDelegate: FilmsAdapterDelegate) {
        this.filmsAdapterDelegate = filmsAdapterDelegate
    }

    fun submitList(films: List<StaffFilm>) {
        this.films = films
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = films.count()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_staff_film, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            film = this@FilmsAdapter.films[position]
            item.setOnClickListener { onItemClicked() }
        }
    }

    inner class ViewHolder(val item: View) : RecyclerView.ViewHolder(item) {
        private val rating = item.findViewById<TextView>(R.id.rating)
        private val title = item.findViewById<TextView>(R.id.title)
        var film: StaffFilm? = null
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

                        if (rating == "0.0" || rating == "null" || rating.isNullOrEmpty()) {
                            this@ViewHolder.rating.visibility = View.GONE
                        } else {
                            this@ViewHolder.rating.visibility = View.VISIBLE
                            this@ViewHolder.rating.text = rating
                        }


                        if (nameRu.isNullOrEmpty()) {
                            item.visibility = View.VISIBLE
                            this@ViewHolder.title.text = nameEn
                        } else {
                            item.visibility = View.VISIBLE
                            this@ViewHolder.title.text = nameRu
                        }

                    }
                }
            }

        fun onItemClicked() {
            filmsAdapterDelegate?.filmClicked(this@FilmsAdapter.films[absoluteAdapterPosition])
        }
    }

}