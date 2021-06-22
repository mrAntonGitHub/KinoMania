package i.o.mob.dev.kinomania.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import i.o.mob.dev.kinomania.R
import i.o.mob.dev.kinomania.data.FilmStaff

interface StaffAdapterDelegate {
    fun staffClicked(staff: FilmStaff)
}

class StaffAdapter : RecyclerView.Adapter<StaffAdapter.ViewHolder>() {

    private var staffAdapterDelegate: StaffAdapterDelegate? = null
    private val staff: MutableList<FilmStaff> = mutableListOf()

    fun setDelegate(staffAdapterDelegate: StaffAdapterDelegate) {
        this.staffAdapterDelegate = staffAdapterDelegate
    }

    fun submitList(staff: List<FilmStaff>) {
        val diffResult: DiffUtil.DiffResult =
            DiffUtil.calculateDiff(ItemsDiffCallback(this.staff, staff))
        diffResult.dispatchUpdatesTo(this)
        this.staff.clear()
        this.staff.addAll(staff)
    }

    class ItemsDiffCallback(var oldList: List<FilmStaff>, var newList: List<FilmStaff>) :
        DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return (oldList[oldItemPosition].staffId == newList[newItemPosition].staffId)
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].nameEn == newList[newItemPosition].nameEn && oldList[oldItemPosition].professionKey == newList[newItemPosition].professionKey
        }
    }

    override fun getItemCount(): Int = staff.count()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_film_staff, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            staff = this@StaffAdapter.staff[position]
            item.setOnClickListener { onItemClicked() }
        }
    }

    inner class ViewHolder(val item: View) : RecyclerView.ViewHolder(item) {
        private val icon = item.findViewById<CircleImageView>(R.id.icon)
        private val name = item.findViewById<TextView>(R.id.name)
        private val job = item.findViewById<TextView>(R.id.job)
        var staff: FilmStaff? = null
            set(value) {
                value?.let { newValue ->
                    field = newValue
                    newValue.apply {
                        if (this.nameRu.isNullOrEmpty()) name.text = this.nameEn else name.text =
                            this.nameRu
                        this@ViewHolder.job.text = this.professionText
                        Glide
                            .with(icon.context)
                            .load(this.posterUrl)
                            .override(160, 160)
                            .into(icon)
                    }
                }
            }

        fun onItemClicked() {
            staffAdapterDelegate?.staffClicked(this@StaffAdapter.staff[absoluteAdapterPosition])
        }
    }

}