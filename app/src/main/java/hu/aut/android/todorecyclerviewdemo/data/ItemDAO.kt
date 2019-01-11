package hu.aut.android.todorecyclerviewdemo.data

import android.arch.persistence.room.*

@Dao
interface ItemDAO {

    @Query("SELECT * FROM items")
    fun findAllItems(): List<Lists>

    @Insert
    fun insertItem(item: Lists) : Long

    @Delete
    fun deleteItem(item: Lists)

    @Update
    fun updateItem(item: Lists)

    @Query("DELETE FROM [items]")
    fun deleteAll()

}
