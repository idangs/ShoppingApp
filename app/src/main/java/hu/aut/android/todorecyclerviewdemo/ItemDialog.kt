package hu.aut.android.todorecyclerviewdemo

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.*
import hu.aut.android.todorecyclerviewdemo.data.Lists
import kotlinx.android.synthetic.main.dialog_item.view.*
import kotlinx.android.synthetic.main.item_row.*
import kotlinx.android.synthetic.main.item_row.view.*
import java.lang.RuntimeException

class ItemDialog : DialogFragment() {

    interface ItemHandler {
        fun itemCreated(item: Lists)
        fun itemUpdated(item: Lists)
    }

    var spinner: Spinner? = null
    private lateinit var itemHandler: ItemHandler

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is ItemHandler) {
            itemHandler = context
        } else {
            throw RuntimeException(
                getString(R.string.errorhandler))
        }
    }

    private lateinit var item_name: EditText
    private lateinit var item_description: EditText
    private lateinit var item_price: EditText
    private lateinit var purchased: CheckBox
    private lateinit var my_spinner: AbsSpinner
    lateinit var list_of_items: Array<String>


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle(getString(R.string.add_item))

        val rootView = requireActivity().layoutInflater.inflate(
                R.layout.dialog_item, null
        )

        item_name = rootView.name
        item_description=rootView.descript
        item_price=rootView.price
        purchased = rootView.checkBox
        my_spinner= rootView.myspinner!!

        list_of_items = arrayOf(getString(R.string.food), getString(R.string.electronic), getString(R.string.book))


        my_spinner.adapter = ArrayAdapter(activity, android.R.layout.simple_spinner_dropdown_item, list_of_items)
        my_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            }
        }
        builder.setView(rootView)

        builder.setPositiveButton(getString(R.string.add_item)) {
            dialog, witch -> // empty
        }

        val arguments = this.arguments
        if (arguments != null && arguments.containsKey(
                        ScrollingActivity.KEY_ITEM_TO_EDIT
                )
        ) {

            val changeItem = arguments.getSerializable(
                    ScrollingActivity.KEY_ITEM_TO_EDIT
            ) as Lists
            item_name.setText(changeItem.item_name)
            item_price.setText(changeItem.price)
            item_description.setText(changeItem.details)
            purchased.isChecked = changeItem.purchased
            when {
                changeItem.category == getString(R.string.fooda) -> my_spinner.setSelection(0)
                changeItem.category == getString(R.string.electronic) -> my_spinner.setSelection(1)
                else -> my_spinner.setSelection(2)
            }
            builder.setTitle(getString(R.string.edit_item))
        }

        builder.setPositiveButton(getString(R.string.ok)) { dialog, witch ->
        }

        return builder.create()
    }

    override fun onResume() {
        super.onResume()

        val positiveButton = (dialog as AlertDialog).getButton(Dialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {
            if (item_name.text.isNotEmpty() && item_description.text.isNotEmpty() && item_price.text.isNotEmpty()) {

                val arguments = this.arguments
                if (arguments != null && arguments.containsKey(ScrollingActivity.KEY_ITEM_TO_EDIT)) {
                    handleItemEdit()
                } else {
                    handleItemCreate()
                }

                dialog.dismiss()
            } else {
                if (item_name.text.isEmpty()) {
                    item_name.error = getString(R.string.cannotempty)
                }
                if (item_description.text.isEmpty()) {
                    item_description.error = getString(R.string.cannotemptyt)
                }
                if (item_price.text.isEmpty()) {
                    item_price.error = getString(R.string.cannotemptyh)
                }
            }
        }
    }



    private fun handleItemCreate() {
        itemHandler.itemCreated(
            Lists(
                null,
                    item_name.text.toString(), false,
                item_price.text.toString(),
                    item_description.text.toString(),list_of_items[my_spinner.selectedItemPosition]
            )
        )
    }

    private fun handleItemEdit() {
        val itemToEdit = arguments?.getSerializable(
            ScrollingActivity.KEY_ITEM_TO_EDIT
        ) as Lists
        itemToEdit.item_name = item_name.text.toString()
        itemToEdit.price = item_price.text.toString()
        itemHandler.itemUpdated(itemToEdit)
        itemToEdit.details = item_description.text.toString()
        itemToEdit.category = list_of_items[my_spinner.selectedItemPosition]
        itemHandler.itemUpdated(itemToEdit)
    }

}