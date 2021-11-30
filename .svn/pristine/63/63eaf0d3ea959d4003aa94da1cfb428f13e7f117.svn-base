package com.unfpa.appsistenciamaternaunfpa.database

import androidx.room.TypeConverter
import java.util.*

/**
 * Created by KhyatiShah on 10/1/2019.
 */
class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }
}