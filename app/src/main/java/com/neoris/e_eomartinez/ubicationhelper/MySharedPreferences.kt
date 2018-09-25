package com.neoris.e_eomartinez.ubicationhelper

import android.content.Context
import android.content.SharedPreferences

val MY_PREFERENCES = "MY_PREFERENCES"

class MySharedPreferences(mContext: Context) {

    private val sharedPreferences = mContext.getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()

    fun savePreferences(name: String, value: String) = editor.putString(name, value).commit()

    fun getString(name: String) = sharedPreferences.getString(name, "")


}