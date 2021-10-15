package ar.com.myanotator.myanotator.data

import androidx.room.TypeConverter
import ar.com.myanotator.myanotator.data.models.Priority

class Converter {

    @TypeConverter
    fun fromPriority(priority: Priority):String{
        return priority.name
    }

    @TypeConverter
    fun toPriority(priority: String): Priority {
        return Priority.valueOf(priority)
    }

}