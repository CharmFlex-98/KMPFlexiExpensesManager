package com.charmflex.cp.flexiexpensesmanager.core.storage


internal interface SharedPrefs {

    fun setInt(key: String, value: Int)
    fun getInt(key: String, default: Int): Int
    fun setString(key: String, value: String)

    fun getString(key: String, default: String): String

    fun setFloat(key: String, value: Float)

    fun getFloat(key: String, default: Float): Float

    fun setBoolean(key: String, value: Boolean)

    fun getBoolean(key: String, default: Boolean): Boolean

    fun setStringSet(key: String, value: Set<String>)
    fun getStringSet(key: String): Set<String>

    fun clearAllData()
}