package hu.aut.android.todorecyclerviewdemo.touch

interface ItemTochHelperAdapter {
    fun onDismissed(position: Int)
    fun onItemMoved(fromPosition: Int, toPosition: Int)
}