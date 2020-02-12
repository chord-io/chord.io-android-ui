package io.chord.clients.models


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

/**
    * @property channel
*/
@JsonClass(generateAdapter = true)
open class MidiTrack(
    name: kotlin.String,
    color: kotlin.Int,
    @Json(name = "channel") @field:Json(name = "channel") var channel: kotlin.Int
) : Track(
    name,
    color) 
{
    override fun copy(): MidiTrack
    {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
        objectOutputStream.writeObject(this)
        objectOutputStream.close()
        val byteArrayInputStream = ByteArrayInputStream(byteArrayOutputStream.toByteArray())
        val objectInputStream = ObjectInputStream(byteArrayInputStream)
        return objectInputStream.readObject() as MidiTrack
    }
}

