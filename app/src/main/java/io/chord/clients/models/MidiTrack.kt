package io.chord.clients.models

import com.squareup.moshi.Json
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable

/**
    * @property channel
*/

open class MidiTrack(
    name: String,
    color: Int,
    themes: List<Theme>,
    entries: List<ThemeEntry>,
    @Json(name = "channel") @field:Json(name = "channel") var channel: Int
): Serializable, Track(
    name,
    color,
    themes,
    entries)
{
    override fun copy(regenerate: Boolean): MidiTrack
    {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
        objectOutputStream.writeObject(this)
        objectOutputStream.close()
        val byteArrayInputStream = ByteArrayInputStream(byteArrayOutputStream.toByteArray())
        val objectInputStream = ObjectInputStream(byteArrayInputStream)
        val obj = objectInputStream.readObject() as MidiTrack

        if(regenerate)
        {
            obj.regenerate()
        }

        return obj
    }
}
