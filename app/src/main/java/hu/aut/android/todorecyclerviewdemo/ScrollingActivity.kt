package hu.aut.android.todorecyclerviewdemo

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.Menu
import android.view.MenuItem
import hu.aut.android.todorecyclerviewdemo.adapter.ItemAdapter
import hu.aut.android.todorecyclerviewdemo.data.AppDatabase
import hu.aut.android.todorecyclerviewdemo.data.Lists
import hu.aut.android.todorecyclerviewdemo.touch.ItemTouchHelperCallback
import kotlinx.android.synthetic.main.activity_scrolling.*

class ScrollingActivity : AppCompatActivity(), ItemDialog.ItemHandler {

    private lateinit var itemAdapter: ItemAdapter

        companion object {
        val KEY_ITEM_TO_EDIT = "KEY_ITEM_TO_EDIT"
    }
    private var editIndex: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling)

        setSupportActionBar(toolbar)

        initRecyclerView()


    }

    private fun initRecyclerView() {
        Thread {
            val itemList = AppDatabase.getInstance(
                    this@ScrollingActivity
            ).itemDao().findAllItems()

            itemAdapter = ItemAdapter(
                    this@ScrollingActivity,
                    itemList
            )

            runOnUiThread {
                recyclerItem.adapter = itemAdapter

                val callback = ItemTouchHelperCallback(itemAdapter)
                val touchHelper = ItemTouchHelper(callback)
                touchHelper.attachToRecyclerView(recyclerItem)
            }
        }.start()
    }

    private fun showAddItemDialog() {
        ItemDialog().show(supportFragmentManager,
                getString(R.string.create_tag))
    }

    private fun deleteall(){
        Thread {
            AppDatabase.getInstance(this@ScrollingActivity).itemDao().deleteAll()
            runOnUiThread {
                itemAdapter.removeAll() }
        }.start()
    }

    public fun showEditItemDialog(itemToEdit: Lists, idx: Int) {
        editIndex = idx
        val editItemDialog = ItemDialog()

        val bundle = Bundle()
        bundle.putSerializable(KEY_ITEM_TO_EDIT, itemToEdit)
        editItemDialog.arguments = bundle

        editItemDialog.show(supportFragmentManager,
            getString(R.string.edit_dialog))
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_scrolling, menu)
        return true


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_item -> {
                showAddItemDialog()
            }
        }

        when (item.itemId) {
            R.id.delete_items -> {
                deleteall()
            }
        }

        when (item.itemId) {
            R.id.review -> {
                var intentStart = Intent()
                intentStart.setClass(ScrollingActivity@ this, Rating::class.java)
                startActivity(intentStart)
            }
        }

        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)

        }
    }

        override fun itemCreated(item: Lists) {
            Thread {
                val itemId = AppDatabase.getInstance(
                        this@ScrollingActivity).itemDao().insertItem(item)

                item.itemId = itemId

                runOnUiThread {
                    itemAdapter.addItem(item)
                }
            }.start()
        }

        override fun itemUpdated(item: Lists) {
            Thread {
                AppDatabase.getInstance(
                        this@ScrollingActivity).itemDao().updateItem(item)

                runOnUiThread {
                    itemAdapter.updateItem(item, editIndex)
                }
            }.start()
        }

    }
