package com.example.androidproject.domain

import android.graphics.Color
import org.json.JSONObject
import java.util.UUID


data class Note(
    val uid: String = UUID.randomUUID().toString(),
    val title: String,
    val content: String,
    val color: Int = Color.WHITE,
    val importance: Importance = Importance.NORMAL
)
{
    companion object {
        fun parse(json: JSONObject): Note? {
            return try {
                val uid = json.optString("uid", UUID.randomUUID().toString())
                val title = json.getString("title")
                val content = json.getString("content")
                val color = json.optInt("color", Color.WHITE)
                val importance = Importance.valueOf(json.optString("importance", Importance.NORMAL.name))
                Note(uid, title, content, color, importance)
            } catch (e: Exception) {
                null
            }
        }
    }

    fun toJson(): JSONObject {
        val json = JSONObject()
        json.put("uid", uid)
        json.put("title", title)
        json.put("content", content)

        if (color != Color.WHITE) {
            json.put("color", color)
        }

        if (importance != Importance.NORMAL) {
            json.put("importance", importance.name)
        }

        return json
    }
}
