package i.o.mob.dev.kinomania.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


interface SimpleAdapterDelegate {
    fun categoryClicked(position: Int)
}

class SimpleAdapter : RecyclerView.Adapter<SimpleAdapter.ViewHolder>() {

    private var simpleAdapterDelegate: SimpleAdapterDelegate? = null
    private val categories: MutableList<String> = mutableListOf()

    fun setDelegate(simpleAdapterDelegate: SimpleAdapterDelegate) {
        this.simpleAdapterDelegate = simpleAdapterDelegate
    }

    fun submitList(categories: List<String>) {
        this.categories.clear()
        this.categories.addAll(categories)
        // data sets only once, with all necessary data in pack
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = categories.count()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(android.R.layout.simple_list_item_1, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            category = categories[position]
            item.setOnClickListener { onItemClicked() }
        }
    }

    inner class ViewHolder(val item: View) : RecyclerView.ViewHolder(item) {
        private val categoryName = item.findViewById<TextView>(android.R.id.text1)

        var category: String? = null
            set(value) {
                value?.let { newValue ->
                    field = newValue
                    newValue.apply {
                        categoryName.text = newValue
                    }
                }
            }

        fun onItemClicked() {
            simpleAdapterDelegate?.categoryClicked(absoluteAdapterPosition)
        }
    }

}