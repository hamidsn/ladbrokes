package com.example.ladbrokes.util

import android.text.format.DateUtils

object Utility {

    fun Long.formatSeconds(): String = if (this < 60) {
        this.toString()
    } else {
        DateUtils.formatElapsedTime(this)
    }

}