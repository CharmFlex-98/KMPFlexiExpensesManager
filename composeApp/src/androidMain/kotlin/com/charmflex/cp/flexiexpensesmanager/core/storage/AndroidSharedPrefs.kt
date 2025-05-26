package com.charmflex.cp.flexiexpensesmanager.core.storage

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

private const val FEM_SHARED_PREFS_FILE_PATH = "FLEXI-EXPENSES-MANAGER-SHARED-PREFERENCES-PATH"

// Secure SharedPreferences wrapper
internal class AndroidSharedPrefs constructor(
    private val appContext: Context,
) : SharedPrefs {

    private val sharedPrefs: SharedPreferences by lazy { initSharedPreferences() }

    @Synchronized
    private fun initSharedPreferences(): SharedPreferences {
        return EncryptedSharedPreferences.create(
            FEM_SHARED_PREFS_FILE_PATH,
            generateMainKeyAlias(),
            appContext,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    private fun generateMainKeyAlias(): String {
        val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
        return MasterKeys.getOrCreate(keyGenParameterSpec)
    }

    override fun setInt(key: String, value: Int) {
        sharedPrefs.edit().putInt(key, value).apply()
    }

    override fun getInt(key: String, default: Int): Int {
        return sharedPrefs.getInt(key, default) ?: default
    }

    override fun setString(key: String, value: String) {
        sharedPrefs.edit().putString(key, value).apply()
    }

    override fun getString(key: String, default: String): String {
        return sharedPrefs.getString(key, default) ?: default
    }

    override fun setFloat(key: String, value: Float) {
        sharedPrefs.edit().putFloat(key, value).apply()
    }

    override fun getFloat(key: String, default: Float): Float {
        return sharedPrefs.getFloat(key, default)
    }

    override fun setBoolean(key: String, value: Boolean) {
        sharedPrefs.edit().putBoolean(key, value).apply()
    }

    override fun getBoolean(key: String, default: Boolean): Boolean {
        return sharedPrefs.getBoolean(key, default)
    }

    override fun setStringSet(key: String, value: Set<String>) {
        sharedPrefs.edit().putStringSet(key, value).apply()
    }

    override fun getStringSet(key: String): Set<String> {
        return sharedPrefs.getStringSet(key, setOf()) ?: setOf()
    }

    override fun clearAllData() {
        sharedPrefs.edit().clear().apply()
    }
}