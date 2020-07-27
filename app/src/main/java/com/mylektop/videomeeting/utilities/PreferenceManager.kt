package com.mylektop.videomeeting.utilities

import android.content.Context

/**
 * Created by iddangunawan on 27/07/20
 */
class PreferenceManager(context: Context) {

    private var sharedPreferences =
        context.getSharedPreferences(Constants.KEY_PREFERENCE_NAME, Context.MODE_PRIVATE)

    fun putBoolean(key: String, value: Boolean) {
        val editor = sharedPreferences?.edit()
        editor?.putBoolean(key, value)
        editor?.apply()
    }

    fun getBoolean(key: String): Boolean {
        return sharedPreferences?.getBoolean(key, false)!!
    }

    fun putString(key: String, value: String) {
        val editor = sharedPreferences?.edit()
        editor?.putString(key, value)
        editor?.apply()
    }

    fun getString(key: String): String {
        return sharedPreferences?.getString(key, null)!!
    }

    fun clearPreferences() {
        val editor = sharedPreferences?.edit()
        editor?.clear()
        editor?.apply()
    }

}