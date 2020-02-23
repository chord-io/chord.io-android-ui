package io.chord.clients.models


import com.squareup.moshi.Json
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
