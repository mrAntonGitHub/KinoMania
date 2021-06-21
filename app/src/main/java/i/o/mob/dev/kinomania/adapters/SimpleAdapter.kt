package i.o.mob.dev.kinomania.adapters

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import i.o.mob.dev.kinomania.R
import kotlin.random.Random


interface SimpleAdapterDelegate{
    fun categoryClicked(position: Int)
}

class SimpleAdapter : RecyclerView.Adapter<SimpleAdapter.ViewHolder>() {

    private var simpleAdapterDelegate : SimpleAdapterDelegate? = null
    private var categories : List<String> = listOf()

    fun setDelegate(simpleAdapterDelegate: SimpleAdapterDelegate){
        this.simpleAdapterDelegate = simpleAdapterDelegate
    }

    fun submitList(categories : List<String>) {
        this.categories = categories
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = categories.count()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            category = categories[position]
            item.setOnClickListener { onItemClicked() }
        }
    }

    inner class ViewHolder(val item: View): RecyclerView.ViewHolder(item){
        private val categoryName = item.findViewById<TextView>(android.R.id.text1)

        var category : String? = null
            set(value) {
                value?.let {newValue ->
                    field = newValue
                    newValue.apply {
                        categoryName.text = newValue
                    }
                }
            }

        fun onItemClicked(){
            simpleAdapterDelegate?.categoryClicked(absoluteAdapterPosition)
        }
    }

}