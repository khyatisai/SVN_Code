package com.unfpa.appsistenciamaternaunfpa.adapters.introductory

import com.google.gson.annotations.SerializedName

//TypeForCountry(val itemName: String, val itemCode: String,  var isChecked: Boolean = false){
data class TypeForCountry(

    @SerializedName("Name")
    var name: String,
    @SerializedName("Code")
    var code: String,
    var isChecked: Boolean = false
) {
}
