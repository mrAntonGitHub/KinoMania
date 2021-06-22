package i.o.mob.dev.kinomania.utils

import android.widget.SearchView

class DynamicSearchViewWatcher(
    private val onTextSubmit: ((String?) -> Unit) = { _ -> },
    private val onTextChange: ((CharSequence?) -> Unit) = { _ -> }
) : SearchView.OnQueryTextListener {
    override fun onQueryTextSubmit(query: String?): Boolean {
        onTextSubmit(query)
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        onTextChange(newText)
        return true
    }
}