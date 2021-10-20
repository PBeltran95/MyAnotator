package ar.com.myanotator.myanotator.data

import android.content.Context
import androidx.room.*
import ar.com.myanotator.myanotator.data.models.ToDoData

@Database(entities = [ToDoData::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class ToDoDatabase:RoomDatabase() {

    abstract fun toDoDao():ToDoDao

}