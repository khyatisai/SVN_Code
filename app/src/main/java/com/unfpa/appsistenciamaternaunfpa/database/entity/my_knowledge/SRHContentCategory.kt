package com.unfpa.appsistenciamaternaunfpa.database.entity.my_knowledge

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Created by KhyatiShah on 8/16/2019.
 */
@Entity(tableName = "content_category")

class SRHContentCategory(
    @SerializedName("tid")
    @PrimaryKey var category_id: String,

    @SerializedName("name")
    var name: String,

    @SerializedName("description__value_export")
    var description_guide: String,
    
    @SerializedName("field_quiz_description_export")
    var description_class: String
) {

}