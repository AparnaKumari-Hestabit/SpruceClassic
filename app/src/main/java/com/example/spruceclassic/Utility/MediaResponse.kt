package com.example.spruceclassic.Utility

import com.google.gson.annotations.SerializedName

data class MediaResponse(
    @SerializedName("source_url")
    val url: String
)