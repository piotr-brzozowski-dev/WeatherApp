package com.example.weatherapp.error

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ErrorDialogData(
    val message: String,
    val action: (() -> Unit)? = null,
    val tag: String = ErrorDialogData::class.java.simpleName
) : Parcelable