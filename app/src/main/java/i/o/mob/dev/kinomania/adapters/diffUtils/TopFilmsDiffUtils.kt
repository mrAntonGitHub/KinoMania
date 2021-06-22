package i.o.mob.dev.kinomania.adapters.diffUtils

import androidx.recyclerview.widget.DiffUtil
import i.o.mob.dev.kinomania.data.TopFilmItem

class TopFilmsDiffUtils(
    private val newList: List<TopFilmItem>,
    private val oldList: List<TopFilmItem>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return newList[newItemPosition].filmId == oldList[oldItemPosition].filmId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return newList[newItemPosition].nameRu == oldList[oldItemPosition].nameRu
                && newList[newItemPosition].genres == oldList[oldItemPosition].genres
                && newList[newItemPosition].posterUrl == oldList[oldItemPosition].posterUrl
    }

}