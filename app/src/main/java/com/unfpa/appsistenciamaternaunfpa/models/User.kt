package com.unfpa.appsistenciamaternaunfpa.models

import com.google.firebase.database.IgnoreExtraProperties

/**
 * Created by KhyatiShah on 11/25/2019.
 */
@IgnoreExtraProperties
data class User(
    var uId: String? = "",
    var username: String? = "",
    var countryCode: String? = "",
    var gender: String? = "",
    var ageGroup: String? = "",
    var areaOfInt: String? = "",
    var education: String? = ""
)