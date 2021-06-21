package i.o.mob.dev.kinomania.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import de.hdodenhof.circleimageview.CircleImageView
import i.o.mob.dev.kinomania.R
import i.o.mob.dev.kinomania.data.Review
import i.o.mob.dev.kinomania.utils.Utils.Companion.reviewDataToReadable

interface ReviewAdapterDelegate{
    fun reviewClicked(review: Review)
}

class ReviewAdapter : RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {

    private var reviewAdapterDelegate : ReviewAdapterDelegate? = null
    private var reviews : List<Review> = listOf()

    fun setDelegate(reviewAdapterDelegate: ReviewAdapterDelegate){
        this.reviewAdapterDelegate = reviewAdapterDelegate
    }

    fun submitList(reviews : List<Review>) {
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(ItemsDiffCallback(this.reviews, reviews))
        this.reviews = reviews
        diffResult.dispatchUpdatesTo(this)
    }

    class ItemsDiffCallback(var oldList: List<Review>, var newList: List<Review>) :
        DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return (oldList[oldItemPosition].reviewId == newList[newItemPosition].reviewId)
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].reviewAutor == newList[newItemPosition].reviewAutor && oldList[oldItemPosition].reviewType == newList[newItemPosition].reviewType
        }
    }


    override fun getItemCount(): Int = reviews.count()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_film_review, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            review = this@ReviewAdapter.reviews[position]
            item.setOnClickListener { onItemClicked() }
        }
    }

    inner class ViewHolder(val item: View): RecyclerView.ViewHolder(item){
        private val type = item.findViewById<View>(R.id.reviewType)
        private val thumb = item.findViewById<ImageView>(R.id.thumb)
        private val name = item.findViewById<TextView>(R.id.name)
        private val data = item.findViewById<TextView>(R.id.data)
        private val title = item.findViewById<TextView>(R.id.reviewTitle)
        private val text = item.findViewById<TextView>(R.id.reviewText)

        var review: Review? = null
        set(value) {
            value?.let {newValue ->
                field = newValue
                newValue.apply {
                    name.text = this.reviewAutor
                    data.text = this.reviewData.reviewDataToReadable()
                    title.text = this.reviewTitle
                    text.text = this.reviewDescription
                    when(this.reviewType){
                        "NEGATIVE" -> {
                            val red = item.resources.getColor(android.R.color.holo_red_light, null)
                            type.setBackgroundColor(red)
                            thumb.setImageResource(R.drawable.ic_thumb_down)
                            thumb.setColorFilter(red)
                        }
                        "NEUTRAL" -> {
                            val green = item.resources.getColor(android.R.color.holo_green_light, null)
                            type.setBackgroundColor(green)
                            thumb.setImageResource(R.drawable.ic_thumb_up)
                            thumb.setColorFilter(green)
                        }
                        else -> {
                            val green = item.resources.getColor(android.R.color.holo_green_light, null)
                            type.setBackgroundColor(green)
                            thumb.setImageResource(R.drawable.ic_thumb_up)
                            thumb.setColorFilter(green)
                        }
                    }
                }
            }
        }
        fun onItemClicked(){
          reviewAdapterDelegate?.reviewClicked(this@ReviewAdapter.reviews[absoluteAdapterPosition])
        }
    }

}