/**
 * NOTE: This class is auto generated by the Swagger Gradle Codegen for the following API: chord.io
 *
 * More info on this tool is available on https://github.com/Yelp/swagger-gradle-codegen
 */

package io.chord.client.models

import com.squareup.moshi.Json

/**
 * 
 * @property quality 
 * @property degree 
 * @property semitones 
 */
data class DegreeAndSemitonesIntervalDto (
        @Json(name = "quality") @field:Json(name = "quality") var quality: IntervalQuality,
        @Json(name = "degree") @field:Json(name = "degree") var degree: Int,
        @Json(name = "semitones") @field:Json(name = "semitones") var semitones: Int
)

