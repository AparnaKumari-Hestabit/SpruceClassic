package com.example.spruceclassic.Utility

data class DataModel(
    val id: Int,
    val date: String,
    val date_gmt: String,
    val guid: Guid,
    val modified: String,
    val modified_gmt: String,
    val slug: String,
    val status: String,
    val type: String,
    val link: String
)

data class Guid(
    val rendered: String
)


//typealias DataModelList = ArrayList<DataModel>
