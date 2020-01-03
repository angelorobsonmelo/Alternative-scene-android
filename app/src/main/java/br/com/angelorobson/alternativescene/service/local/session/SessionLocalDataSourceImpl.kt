package br.com.angelorobson.alternativescene.service.local.session

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.annotation.NonNull
import br.com.angelorobson.alternativescene.domain.response.AuthResponse
import com.google.gson.Gson


class SessionLocalDataSourceImpl(private val mContext: Context) : SessionLocalDataSource {

    private val preferenceShareNameIdentifier = "authResponse"
    private val preferenceShareTokenIdentifier = "token"

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
        val sharedPreferences =
            this.mContext.getSharedPreferences(preferenceShareNameIdentifier, MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val gson = Gson()
        val authResponseToJson = gson.toJson(authResponse)

        editor.putString(preferenceShareNameIdentifier, authResponseToJson)
        editor.apply()

        saveToken(authResponse.token)
    }

    override fun getAuthResponseInSession(): AuthResponse {
        val gson = Gson()
        val sharedPreferences =
            this.mContext.getSharedPreferences(preferenceShareNameIdentifier, MODE_PRIVATE)

        val authResponseToJson = sharedPreferences.getString(preferenceShareNameIdentifier, "")
        return gson.fromJson<AuthResponse>(authResponseToJson, AuthResponse::class.java)
    }

    override fun destroySession(): Boolean {
        val sharedPreferences =
            this.mContext.getSharedPreferences(preferenceShareNameIdentifier, MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.clear()
        editor.apply()

        val hasValue = sharedPreferences.getString(preferenceShareNameIdentifier, "")
        return hasValue.isNullOrEmpty()
    }

    override fun isLogged(): Boolean {
        var isUserLogged = false
        val sharedPreferences =
            this.mContext.getSharedPreferences(preferenceShareNameIdentifier, MODE_PRIVATE)

        val hasValue = sharedPreferences.getString(preferenceShareNameIdentifier, "")

        hasValue?.apply {
            when {
                isNotEmpty() -> isUserLogged = true
            }
        }

        return isUserLogged
    }

    override fun getToken(): String? {
        val sharedPreferences =
            this.mContext.getSharedPreferences(preferenceShareTokenIdentifier, MODE_PRIVATE)
        return sharedPreferences.getString(preferenceShareNameIdentifier, "")
    }

    override fun saveToken(token: String) {
        val sharedPreferences =
            this.mContext.getSharedPreferences(preferenceShareTokenIdentifier, MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString(preferenceShareNameIdentifier, token)
        editor.apply()
    }

}