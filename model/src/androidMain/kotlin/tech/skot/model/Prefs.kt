package tech.skot.model

import android.content.Context

class AndroidPrefs(context: Context) : Prefs {

    private val sharedPrefs = context.getSharedPreferences("GLOBAL_PREFS", Context.MODE_PRIVATE)
    override fun getString(key: String) = sharedPrefs.getString(key, null)


    override fun putString(key: String, value: String?) {
        sharedPrefs.edit().apply {
            putString(key, value)
            apply()
        }
    }

    override fun getInt(key: String) = if (sharedPrefs.contains(key)) sharedPrefs.getInt(key, -1) else null

    override fun putInt(key: String, value: Int?) {
        if (value != null) {
            sharedPrefs.edit().apply {
                putInt(key, value)
                apply()
            }
        }
    }

    override fun getLong(key: String) = if (sharedPrefs.contains(key)) sharedPrefs.getLong(key, -1) else null

    override fun putLong(key: String, value: Long?) {
        if (value != null) {
            sharedPrefs.edit().apply {
                putLong(key, value)
                apply()
            }
        }
    }

    override fun remove(key: String) {
        sharedPrefs.edit().apply {
            remove(key)
            apply()
        }
    }


}