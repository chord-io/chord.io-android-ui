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
    name: kotlin.String,
    color: kotlin.Int,
    channel: kotlin.Int,
    @Json(name = "drum_map") @field:Json(name = "drum_map") var drumMap: kotlin.String
): Serializable, MidiTrack(
    name,
    color,
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
