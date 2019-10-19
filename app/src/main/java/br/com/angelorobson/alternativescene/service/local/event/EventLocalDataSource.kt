package br.com.angelorobson.alternativescene.service.local.event

import android.content.Context
import br.com.angelorobson.alternativescene.domain.EventState
import com.google.gson.Gson

class EventLocalDataSource(private val mContext: Context) {


    private val eventStateIdentifier = "eventStateIdentifier"

    fun saveEventState(eventState: EventState) {
        val sharedPreferences = this.mContext.getSharedPreferences(
            eventStateIdentifier,
            Context.MODE_PRIVATE
        )
        val editor = sharedPreferences.edit()

        val gson = Gson()
        val authResponseToJson = gson.toJson(eventState)

        editor.putString(eventStateIdentifier, authResponseToJson)
        editor.apply()
    }

    fun getEventState(): EventState {
        val gson = Gson()
        val sharedPreferences = this.mContext.getSharedPreferences(
            eventStateIdentifier,
            Context.MODE_PRIVATE
        )

        val authResponseToJson = sharedPreferences.getString(eventStateIdentifier, "")
        return gson.fromJson<EventState>(authResponseToJson, EventState::class.java)
    }
}