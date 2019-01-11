package hu.aut.android.todorecyclerviewdemo.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable



@Entity(tableName = "items")

data class Lists(
    @PrimaryKey(autoGenerate = true) var itemId: Long?,
    @ColumnInfo(name = "item name") var item_name: String,
    @ColumnInfo(name = "purchased") var purchased: Boolean,
    @ColumnInfo(name = "price") var price: String,
    @ColumnInfo(name = "details") var details: String,
    @ColumnInfo(name = "category") var category: String
) : Serializable
