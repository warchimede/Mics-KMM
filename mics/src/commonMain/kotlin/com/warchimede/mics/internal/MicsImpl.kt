package com.warchimede.mics.internal

import com.warchimede.mics.Mics
import com.warchimede.mics.internal.mapping.RequestMapper
import com.warchimede.mics.internal.mapping.ResponseMapper
import com.warchimede.mics.internal.mediarithmics.MediarithmicsApi
import com.warchimede.mics.internal.mediarithmics.model.SerializableAccountIdentifier
import com.warchimede.mics.internal.mediarithmics.model.SerializableIdentifier
import com.warchimede.mics.internal.mediarithmics.model.SerializableUserAgentIdentifier
import com.warchimede.mics.model.Event
import com.warchimede.mics.model.Identifiers
import com.warchimede.mics.model.Response
import com.warchimede.mics.model.Segment

internal class MicsImpl internal constructor(
    private val mediarithmicsApi: MediarithmicsApi,
    private val identifiers: Identifiers,
    private val requestMapper: RequestMapper,
    private val responseMapper: ResponseMapper
) : Mics {

    suspend fun fetchSegments(): Result<List<Segment>> =
        runCatching { requestMapper.createSegmentRequest(identifiers) }
            .mapCatching { getSegments(it) }
            .mapCatching { responseMapper.mapSegments(it.data) }

    override suspend fun getSegments(): List<Segment>? = fetchSegments().getOrNull()

    suspend fun sendPost(event: Event): Result<Response> =
        runCatching { requestMapper.createUserActivityRequest(event, identifiers) }
            .mapCatching { mediarithmicsApi.post(it) }
            .mapCatching { responseMapper.mapResponse(it) }

    override suspend fun post(event: Event): Response? = sendPost(event).getOrNull()

    suspend fun sendAssociateUserIdentifiers(): Result<Response> =
        runCatching { requestMapper.createAssociationRequest(identifiers) }
            .mapCatching { mediarithmicsApi.associateUserIdentifiers(it) }
            .mapCatching { responseMapper.mapResponse(it) }

    override suspend fun associateUserIdentifiers(): Response? = sendAssociateUserIdentifiers().getOrNull()

    private suspend fun getSegments(id: SerializableIdentifier) = when (id) {
        is SerializableAccountIdentifier -> mediarithmicsApi.getSegmentsForUserAccount(id)
        is SerializableUserAgentIdentifier -> mediarithmicsApi.getSegmentsForUserAgentId(id)
    }
}