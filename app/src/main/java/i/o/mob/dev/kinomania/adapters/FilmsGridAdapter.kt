package i.o.mob.dev.kinomania.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.content.res.AppCompatResources.getColorStateList
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import i.o.mob.dev.kinomania.R
import i.o.mob.dev.kinomania.adapters.DiffUtils.TopFilmsDiffUtils
import i.o.mob.dev.kinomania.data.TopFilmItem
import i.o.mob.dev.kinomania.data.TopFilmsType
import i.o.mob.dev.kinomania.utils.Utils.Companion.getFilmLabelColor

interface FilmsGridAdapterDelegate{
    fun filmClicked(film: TopFilmItem)
    fun loadMoreContent(type: TopFilmsType)
}

class FilmsGridAdapter : RecyclerView.Adapter<FilmsGridAdapter.ViewHolder>() {

    private var filmsGridAdapterDelegate : FilmsGridAdapterDelegate? = null
    private var films : MutableList<TopFilmItem> = mutableListOf()
    private var isLoadMoreAvailable : Boolean = true
    private var adapterType : TopFilmsType? = null

    fun setDelegate(filmsGridAdapterDelegate: FilmsGridAdapterDelegate){
        this.filmsGridAdapterDelegate = filmsGridAdapterDelegate
    }

    fun setAdapterType(type: TopFilmsType){
        this.adapterType = type
    }

    fun submitList(films : List<TopFilmItem>) {
        val diffResult = DiffUtil.calculateDiff(TopFilmsDiffUtils(films, this.films))
        diffResult.dispatchUpdatesTo(this)
        this.films.clear()
        this.films.addAll(films)
    }

    fun isLoadMoreAvailable(boolean: Boolean){
        this.isLoadMoreAvailable = boolean
        if (!boolean){
            this.notifyItemRemoved(films.count())
        }
    }

    override fun getItemCount(): Int {
        return when(films.count()){
            0 -> 0
            else -> {
                if(isLoadMoreAvailable) films.count() + 1
                else films.count()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(position){
            in 0 until (films.count()) -> { TYPE_CONTENT }
            else -> { TYPE_LOAD_MORE }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when(viewType){
            TYPE_CONTENT -> { ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_grid_movie_card, parent, false)) }
            TYPE_LOAD_MORE -> { ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_load_more_card, parent, false)) }
            else -> { ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_grid_movie_card, parent, false)) }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when(getItemViewType(position)){
            TYPE_CONTENT -> {
                holder.apply {
                    topFilmItem = this@FilmsGridAdapter.films[position]
                    item.setOnClickListener { onItemClicked() }
                }
            }
            TYPE_LOAD_MORE -> {
                holder.apply {
                    this.item.layoutParams.width = this.itemView.context.resources.getDimension(R.dimen.movie_card_width_grid).toInt()
                    this.item.layoutParams.height = this.itemView.context.resources.getDimension(R.dimen.movie_card_height_grid).toInt()
                    loadMore?.visibility = View.VISIBLE
                    loadMoreProgress?.visibility = View.GONE
                    item.setOnClickListener {
                        loadMore?.visibility = View.GONE
                        loadMoreProgress?.visibility = View.VISIBLE
                        loadMoreContent()
                    }
                }
            }
        }

    }

    inner class ViewHolder(val item: View): RecyclerView.ViewHolder(item){
        private val image = item.findViewById<ImageView>(R.id.image)
        private val rating = item.findViewById<TextView>(R.id.rating)
        private val title = item.findViewById<TextView>(R.id.title)
        private val genre = item.findViewById<TextView>(R.id.type)

        val loadMore: TextView? = item.findViewById(R.id.loadMore)
        val loadMoreProgress: ProgressBar? = item.findViewById(R.id.progress)

        var topFilmItem : TopFilmItem? = null
            set(value) {
                value?.let {newValue ->
                    field = newValue
                    newValue.apply {
                        try {
                            this@ViewHolder.rating.text = rating
                            this@ViewHolder.rating.backgroundTintList = getColorStateList(item.context, getFilmLabelColor(rating?.toFloat() ?: -1f))
                            Log.e("TopFilmsAdapter", "${this@ViewHolder.rating.backgroundTintList}")
                        }catch (e: Exception){
                            this@ViewHolder.rating.text = "Ожидается"
                            this@ViewHolder.rating.backgroundTintList = getColorStateList(item.context, R.color.blue)
                        }
                        if (rating.isNullOrBlank()){
                            this@ViewHolder.rating.visibility = View.GONE
                        }else{
                            this@ViewHolder.rating.visibility = View.VISIBLE
                        }
                        this@ViewHolder.title.text = nameRu
                        this@ViewHolder.genre.text = genres?.map { it.genre }.toString().drop(1).dropLast(1)

                        Glide
                            .with(item.context)
                            .load(this.posterUrl)
                            .override(600,800)
                            .into(image)
                    }
                }
            }
        fun onItemClicked(){
            filmsGridAdapterDelegate?.filmClicked(this@FilmsGridAdapter.films[absoluteAdapterPosition])
        }
        fun loadMoreContent(){
            adapterType?.let { filmsGridAdapterDelegate?.loadMoreContent(it) }
        }
    }

}