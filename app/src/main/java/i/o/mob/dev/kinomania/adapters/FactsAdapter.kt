package i.o.mob.dev.kinomania.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import i.o.mob.dev.kinomania.R
import i.o.mob.dev.kinomania.data.StaffFilm

class FactsAdapter : RecyclerView.Adapter<FactsAdapter.ViewHolder>() {

    private var facts : List<String> = listOf()

    fun submitList(facts: List<String>){
        this.facts = facts
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = facts.count()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_fact, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            fact = facts[position]
        }
    }

    inner class ViewHolder(val item: View): RecyclerView.ViewHolder(item){
        private val factText = item.findViewById<TextView>(R.id.fact)
        var fact : String? = null
            set(value) {
                value?.let {newValue ->
                    field = newValue
                    newValue.apply {
                        factText.text = newValue
                    }
                }
            }
    }

}