package br.com.soluevo.cobrei.service.local.session

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.annotation.NonNull
import br.com.soluevo.cobrei.domain.response.AuthResponse
import com.google.gson.Gson


class SessionLocalDataSourceImpl(private val mContext: Context): SessionLocalDataSource {

    private val preferenceShareNameIdentifier = "authResponse"

    companion object {

        @Volatile
        private var INSTANCE: SessionLocalDataSourceImpl? = null

        fun getInstance(@NonNull mContext: Context): SessionLocalDataSourceImpl =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: SessionLocalDataSourceImpl(mContext).also {
                    INSTANCE = it
                }
            }
    }

    override fun saveAuthInSession(authResponse: AuthResponse) {
        val sharedPreferences = this.mContext.getSharedPreferences(preferenceShareNameIdentifier, MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val gson = Gson()
        val authResponseToJson = gson.toJson(authResponse)

        editor.putString(preferenceShareNameIdentifier, authResponseToJson)
        editor.apply()
    }

    override fun getAuthResponseInSession(): AuthResponse {
        val gson = Gson()
        val sharedPreferences = this.mContext.getSharedPreferences(preferenceShareNameIdentifier, MODE_PRIVATE)

        val authResponseToJson = sharedPreferences.getString(preferenceShareNameIdentifier, "")
        return gson.fromJson<AuthResponse>(authResponseToJson, AuthResponse::class.java)
    }

    override fun destroySession(): Boolean {
        val sharedPreferences = this.mContext.getSharedPreferences(preferenceShareNameIdentifier, MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.clear()
        editor.apply()

       val hasValue = sharedPreferences.getString(preferenceShareNameIdentifier, "")
       return hasValue.isEmpty()
    }

    override fun isLogged(): Boolean {
        val sharedPreferences = this.mContext.getSharedPreferences(preferenceShareNameIdentifier, MODE_PRIVATE)

        val hasValue = sharedPreferences.getString(preferenceShareNameIdentifier, "")
        return hasValue.isNotEmpty()
    }

}