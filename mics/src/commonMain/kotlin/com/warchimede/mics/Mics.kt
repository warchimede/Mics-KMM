package com.warchimede.mics

import com.warchimede.mics.model.Event
import com.warchimede.mics.model.Response
import com.warchimede.mics.model.Segment

interface Mics {
    suspend fun getSegments(): List<Segment>?

    suspend fun post(event: Event): Response?

    suspend fun associateUserIdentifiers(): Response?
}