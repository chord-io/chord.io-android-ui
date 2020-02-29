package io.chord.clients.models

import io.chord.clients.models.MidiTrack
import org.threeten.bp.LocalDateTime
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable
import io.chord.clients.BaseModel

/**
    * @property drumMap
*/

open class DrumTrack(
    name: String,
    color: Int,
    themes: List<Theme>,
    entries: List<ThemeEntry>,
    channel: Int,
    @Json(name = "drum_map") @field:Json(name = "drum_map") var drumMap: String
): Serializable, MidiTrack(
    name,
    color,
    themes,
    entries,
    channel)
{
    override fun copy(regenerate: Boolean): DrumTrack
    {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
        objectOutputStream.writeObject(this)
        objectOutputStream.close()
        val byteArrayInputStream = ByteArrayInputStream(byteArrayOutputStream.toByteArray())
        val objectInputStream = ObjectInputStream(byteArrayInputStream)
        val obj = objectInputStream.readObject() as DrumTrack

        if(regenerate)
        {
            obj.regenerate()
        }

        return obj
    }
}
