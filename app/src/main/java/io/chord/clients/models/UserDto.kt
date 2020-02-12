package io.chord.clients.models

import org.threeten.bp.LocalDateTime
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

/**
    * @property username
    * @property email
    * @property password
*/
@JsonClass(generateAdapter = true)
open class UserDto(
    @Json(name = "username") @field:Json(name = "username") var username: kotlin.String,
    @Json(name = "email") @field:Json(name = "email") var email: kotlin.String,
    @Json(name = "password") @field:Json(name = "password") var password: kotlin.String
)
{
    open fun copy(): UserDto
    {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
        objectOutputStream.writeObject(this)
        objectOutputStream.close()
        val byteArrayInputStream = ByteArrayInputStream(byteArrayOutputStream.toByteArray())
        val objectInputStream = ObjectInputStream(byteArrayInputStream)
        return objectInputStream.readObject() as UserDto
    }
}

