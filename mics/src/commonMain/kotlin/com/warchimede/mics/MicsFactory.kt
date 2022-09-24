package com.warchimede.mics

import com.warchimede.mics.internal.MicsImpl
import com.warchimede.mics.internal.mapping.RequestMapper
import com.warchimede.mics.internal.mapping.ResponseMapper
import com.warchimede.mics.internal.mediarithmics.MediarithmicsApi
import com.warchimede.mics.internal.platformId
import com.warchimede.mics.model.Identifiers

class MicsFactory(private val appId: String, private val apiToken: String, private val datamart: UInt) {

    fun create(identifiers: Identifiers): Mics =
        MicsImpl(createApi(), identifiers, createRequestFactory(), ResponseMapper())

    private fun createRequestFactory() = RequestMapper(platformId(), appId)

    private fun createApi() = MediarithmicsApi(apiToken, datamart)
}