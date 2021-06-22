package i.o.mob.dev.kinomania.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import i.o.mob.dev.kinomania.R
import i.o.mob.dev.kinomania.ui.parameteredSearch.selectItem.SimpleListItem
import kotlinx.android.synthetic.main.fragment_item.view.*

interface SimpleItemDelegate {
    fun itemSelected(simpleListItem: SimpleListItem, isSelected: Boolean)
}

class MyItemRecyclerViewAdapter(
    private val values: List<SimpleListItem>,
    private val simpleItemDelegate: SimpleItemDelegate
) : RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder>() {

    override fun getItemCount(): Int = values.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.fragment_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            simpleListItem = values[position]
        }
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        private val content = view.findViewById<TextView>(R.id.content)
        private val checkbox = view.findViewById<CheckBox>(R.id.checkbox)
        var simpleListItem: SimpleListItem? = null
            set(value) {
                value?.let { newValue ->
                    field = newValue
                    content.text = newValue.content
                    checkbox.isChecked = newValue.isSelected
                }
                view.setOnClickListener { onItemSelected() }
            }

        private fun onItemSelected() {
            simpleItemDelegate.itemSelected(
                values[absoluteAdapterPosition],
                !view.checkbox.isChecked
            )
            view.checkbox.isChecked = !view.checkbox.isChecked
        }

    }
}