package com.mobileinsights.httpclientlibrary

import com.google.gson.Gson

inline fun <reified T>parseJson(text: String): T =
    Gson().fromJson(text, T::class.java)