package com.quickemojis.quickemojis

import android.content.Context
import android.speech.tts.TextToSpeech
import org.json.JSONArray
import java.util.Locale

data class EmojiItem(val emoji: String, val name: String)

object EmojiData {
    var emojis: List<EmojiItem> = emptyList()
    private var tts: TextToSpeech? = null
    
    fun init(context: Context) {
        val jsonString = context.assets.open("emojis.json").bufferedReader().use { it.readText() }
        val array = JSONArray(jsonString)
        val list = mutableListOf<EmojiItem>()
        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            list.add(EmojiItem(obj.getString("emoji"), obj.getString("name")))
        }
        emojis = list
        
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.US
            }
        }
    }
    
    fun speak(text: String) {
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }
}
