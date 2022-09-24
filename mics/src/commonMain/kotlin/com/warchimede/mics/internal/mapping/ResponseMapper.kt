package com.warchimede.mics.internal.mapping

import com.warchimede.mics.internal.mediarithmics.model.SerializableResponse
import com.warchimede.mics.internal.mediarithmics.model.SerializableSegment
import com.warchimede.mics.model.Response
import com.warchimede.mics.model.Segment

internal class ResponseMapper {

    fun mapSegments(serializableSegments: List<SerializableSegment>) =
        serializableSegments.map(::mapSegment)

    fun mapResponse(serializableResponse: SerializableResponse) =
        Response(serializableResponse.status)

    private fun mapSegment(serializableSegment: SerializableSegment) =
        Segment(serializableSegment.segment_id)
}