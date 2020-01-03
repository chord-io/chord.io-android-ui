/**
 * NOTE: This class is auto generated by the Swagger Gradle Codegen for the following API: chord.io
 *
 * More info on this tool is available on https://github.com/Yelp/swagger-gradle-codegen
 */

package io.chord.client.models

import com.squareup.moshi.Json

/**
 * 
 * @property username 
 * @property email 
 * @property password 
 */
data class UserDto (
        @Json(name = "username") @field:Json(name = "username") var username: String,
        @Json(name = "email") @field:Json(name = "email") var email: String,
        @Json(name = "password") @field:Json(name = "password") var password: String
)
