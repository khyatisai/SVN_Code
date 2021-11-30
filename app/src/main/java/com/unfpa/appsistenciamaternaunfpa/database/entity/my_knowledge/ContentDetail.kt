package com.unfpa.appsistenciamaternaunfpa.database.entity.my_knowledge

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "content_detail")
data class ContentDetail(
    @PrimaryKey
    var nid: String,
    var title: String?,
    var field_image: String?,
    var field_short_description: String?,
    var field_description: String?,
    var field_age_group: String?,
    var field_objective: String?,
    var field_tags: String?

) {
}