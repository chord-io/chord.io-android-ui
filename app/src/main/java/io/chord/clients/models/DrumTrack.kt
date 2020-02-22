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

/**
    * @property drumMap
*/

open class DrumTrack(
    name: String,
    color: Int,
    themes: List<ThemeEntry>,
    channel: Int,
    @Json(name = "drum_map") @field:Json(name = "drum_map") var drumMap: String
): Serializable, MidiTrack(
    name,
    color,
    themes,
    channel)
{
    override fun copy(): DrumTrack
    {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
        objectOutputStream.writeObject(this)
        objectOutputStream.close()
        val byteArrayInputStream = ByteArrayInputStream(byteArrayOutputStream.toByteArray())
        val objectInputStream = ObjectInputStream(byteArrayInputStream)
        return objectInputStream.readObject() as DrumTrack
    }
}
