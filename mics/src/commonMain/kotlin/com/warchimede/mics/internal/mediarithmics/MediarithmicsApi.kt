package com.warchimede.mics.internal.mediarithmics

import com.warchimede.mics.internal.mediarithmics.model.*
import com.warchimede.mics.internal.mediarithmics.model.AssociationRequest
import com.warchimede.mics.internal.mediarithmics.model.SegmentResponse
import com.warchimede.mics.internal.mediarithmics.model.SerializableAccountIdentifier
import com.warchimede.mics.internal.mediarithmics.model.SerializableUserAgentIdentifier
import com.warchimede.mics.internal.mediarithmics.model.UserActivity
import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.http.*

internal class MediarithmicsApi(
    private val token: String,
    private val datamart: UInt
) {

    private val httpClient = createHttpClient()
    private val baseURL = "$ENDPOINT/$datamart"

    suspend fun getSegmentsForUserAgentId(userAgent: SerializableUserAgentIdentifier): SegmentResponse =
        httpClient.get("$baseURL/user_segments/user_agent_id=${userAgent.user_agent_id}") {
            headers(this, token)
        }

    suspend fun getSegmentsForUserAccount(account: SerializableAccountIdentifier): SegmentResponse =
        httpClient.get("$baseURL/user_segments/compartment_id=${account.compartment_id}/user_account_id=${account.user_account_id}")
        {
            headers(this, token)
        }

    suspend fun post(userActivity: UserActivity): SerializableResponse =
        httpClient.post("$baseURL/user_activities") {
            headers(this, token)
            body = userActivity
        }

    suspend fun associateUserIdentifiers(associationRequest: AssociationRequest): SerializableResponse =
        httpClient.post("$baseURL/user_identifiers_association_declarations") {
            headers(this, token)
            body = associationRequest
        }

    private fun headers(requestBuilder: HttpRequestBuilder, token: String) = requestBuilder.headers {
        append(HttpHeaders.Authorization, "api:$token")
        append(HttpHeaders.ContentType, "application/json")
    }

    private fun createHttpClient() = HttpClient {
        install(JsonFeature) {
            val json = kotlinx.serialization.json.Json {
                encodeDefaults = true
                explicitNulls = false
                ignoreUnknownKeys = true
                isLenient = true
                prettyPrint = true
            }
            serializer = KotlinxSerializer(json)
        }

        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
    }

    companion object {
        private const val ENDPOINT = "https://api.mediarithmics.com/v1/datamarts"
    }
}