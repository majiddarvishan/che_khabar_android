package ir.rezarasuolzadeh.takhfif.service.utils

import android.content.Context
import android.content.SharedPreferences

object UserPreferences {

    private lateinit var sharedPreference: SharedPreferences

    fun init(context: Context){
        sharedPreference = context.getSharedPreferences("sharedPreference", Context.MODE_PRIVATE)
    }

    // KEYS
    private const val userRoleKey: String = "userRoleKey"
    private const val requestIPKey: String = "requestIPKey"
    private const val requestPortKey: String = "requestPortKey"

    // USER NAME
    fun setUserRole(token: String) {
        val editor: SharedPreferences.Editor = sharedPreference.edit()
        editor.putString(userRoleKey, token)
        editor.apply()
    }

    fun getUserRole(): String? {
        return sharedPreference.getString(userRoleKey, "")
    }

    fun removeUserRole() {
        val editor: SharedPreferences.Editor = sharedPreference.edit()
        editor.remove(userRoleKey)
        editor.apply()
    }

    // REQUEST IP
    fun setRequestIP(ip: String) {
        val editor: SharedPreferences.Editor = sharedPreference.edit()
        editor.putString(requestIPKey, ip)
        editor.apply()
    }

    fun getRequestIP(): String? {
        return sharedPreference.getString(requestIPKey, "")
    }

    fun removeRequestIP() {
        val editor: SharedPreferences.Editor = sharedPreference.edit()
        editor.remove(requestIPKey)
        editor.apply()
    }

    // REQUEST PORT
    fun setRequestPort(port: String) {
        val editor: SharedPreferences.Editor = sharedPreference.edit()
        editor.putString(requestPortKey, port)
        editor.apply()
    }

    fun getRequestPort(): String? {
        return sharedPreference.getString(requestPortKey, "")
    }

    fun removeRequestPort() {
        val editor: SharedPreferences.Editor = sharedPreference.edit()
        editor.remove(requestPortKey)
        editor.apply()
    }

}