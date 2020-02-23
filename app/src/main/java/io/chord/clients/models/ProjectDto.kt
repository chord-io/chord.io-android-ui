package io.chord.clients.models


import io.chord.clients.models.Track
import io.chord.clients.models.Visibility
import org.threeten.bp.LocalDateTime
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable

/**
    * @property name
    * @property tempo
    * @property visibility
    * @property tracks
*/

open class ProjectDto(
    @Json(name = "name") @field:Json(name = "name") var name: String,
    @Json(name = "tempo") @field:Json(name = "tempo") var tempo: Int,
    @Json(name = "visibility") @field:Json(name = "visibility") var visibility: Visibility,
    @Json(name = "tracks") @field:Json(name = "tracks") var tracks: List<Track>
): Serializable
{
    open fun copy(): ProjectDto
    {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
        objectOutputStream.writeObject(this)
        objectOutputStream.close()
        val byteArrayInputStream = ByteArrayInputStream(byteArrayOutputStream.toByteArray())
        val objectInputStream = ObjectInputStream(byteArrayInputStream)
        return objectInputStream.readObject() as ProjectDto
    }
}
