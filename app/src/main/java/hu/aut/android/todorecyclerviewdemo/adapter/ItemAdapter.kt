package hu.aut.android.todorecyclerviewdemo.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import hu.aut.android.todorecyclerviewdemo.R
import hu.aut.android.todorecyclerviewdemo.ScrollingActivity
import hu.aut.android.todorecyclerviewdemo.data.AppDatabase
import hu.aut.android.todorecyclerviewdemo.data.Lists
import hu.aut.android.todorecyclerviewdemo.touch.ItemTochHelperAdapter
import kotlinx.android.synthetic.main.item_row.view.*
import java.util.*


class ItemAdapter : RecyclerView.Adapter<ItemAdapter.ViewHolder>, ItemTochHelperAdapter {


    var Items = mutableListOf<Lists>()


    val context : Context

    constructor(context: Context, itemList: List<Lists>) : super() {
        this.context = context
        this.Items.addAll(itemList)
    }

    constructor(context: Context) : super() {
        this.context = context
    }


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(
            R.layout.item_row, parent, false
        )
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return Items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val items = Items[position]


        holder.item_name.text = items.item_name
        holder.item_price.text = items.price
        holder.is_checkbox.isChecked = items.purchased
        holder.details.text = items.details
        holder.btnDelete.setOnClickListener {
            deleteItem(holder.adapterPosition)
        }

        holder.edit.setOnClickListener {
            (context as ScrollingActivity).showEditItemDialog(
                    items, holder.adapterPosition
            )
        }

        holder.is_checkbox.setOnClickListener {
            items.purchased = holder.is_checkbox.isChecked
            Thread {
            AppDatabase.getInstance(
                    context).itemDao().updateItem(items)
        }.start() }

        if (items.category == context.getString(R.string.food)) {
            holder.ivIcon.setImageResource(R.drawable.food)
        }
        else if (items.category == context.getString(R.string.electon)){
            holder.ivIcon.setImageResource(R.drawable.electronics)
        }
        else{
            holder.ivIcon.setImageResource(R.drawable.books)
        }


    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val item_name = itemView.item_name
        val item_price = itemView.item_price
        val btnDelete = itemView.btnDelete
        val is_checkbox = itemView.dis_checkbox
        val ivIcon = itemView.ivIcon
        val edit = itemView.edit
        val details = itemView.description


    }


    private fun deleteItem(adapterPosition: Int) {
        Thread {
            AppDatabase.getInstance(
                context).itemDao().deleteItem(Items[adapterPosition])

            Items.removeAt(adapterPosition)

            (context as ScrollingActivity).runOnUiThread {
                notifyItemRemoved(adapterPosition)
            }
        }.start()
    }


    fun addItem(additonal_items: Lists) {
        Items.add(0, additonal_items)
        notifyItemInserted(0)
    }


    override fun onDismissed(position: Int) {
        deleteItem(position)
    }

    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
        Collections.swap(Items, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }

    fun updateItem(item: Lists, editIndex: Int) {
        Items[editIndex] = item
        notifyItemChanged(editIndex)
    }

    fun removeAll() {
        Items.clear()
        notifyDataSetChanged()
    }

}