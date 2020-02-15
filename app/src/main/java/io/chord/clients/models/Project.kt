package io.chord.clients.models


import com.squareup.moshi.Json
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable

/**
    * @property id
    * @property name
    * @property tempo
    * @property visibility
    * @property tracks
    * @property themes
*/

open class Project(
    @Json(name = "id") @field:Json(name = "id") var id: String,
    @Json(name = "name") @field:Json(name = "name") var name: String,
    @Json(name = "tempo") @field:Json(name = "tempo") var tempo: Int,
    @Json(name = "visibility") @field:Json(name = "visibility") var visibility: Visibility,
    @Json(name = "tracks") @field:Json(name = "tracks") var tracks: List<Track>,
    @Json(name = "themes") @field:Json(name = "themes") var themes: List<Theme>
): Serializable
{
    open fun copy(): Project
    {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
        objectOutputStream.writeObject(this)
        objectOutputStream.close()
        val byteArrayInputStream = ByteArrayInputStream(byteArrayOutputStream.toByteArray())
        val objectInputStream = ObjectInputStream(byteArrayInputStream)
        return objectInputStream.readObject() as Project
    }
}
