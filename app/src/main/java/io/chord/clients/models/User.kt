/**
 * NOTE: This class is auto generated by the Swagger Gradle Codegen for the following API: chord.io
 *
 * More info on this tool is available on https://github.com/Yelp/swagger-gradle-codegen
 */

package io.chord.clients.models

import com.squareup.moshi.Json

/**
 * 
 * @property id 
 * @property username 
 * @property email 
 */
data class User (
        @Json(name = "id") @field:Json(name = "id") var id: String,
        @Json(name = "username") @field:Json(name = "username") var username: String,
        @Json(name = "email") @field:Json(name = "email") var email: String
)
