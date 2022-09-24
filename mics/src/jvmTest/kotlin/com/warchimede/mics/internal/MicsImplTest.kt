package com.warchimede.mics.internal

import com.warchimede.mics.internal.mapping.RequestMapper
import com.warchimede.mics.internal.mapping.ResponseMapper
import com.warchimede.mics.internal.mediarithmics.MediarithmicsApi
import com.warchimede.mics.internal.mediarithmics.model.SegmentResponse
import com.warchimede.mics.internal.mediarithmics.model.SerializableAccountIdentifier
import com.warchimede.mics.internal.mediarithmics.model.SerializableUserAgentIdentifier
import com.warchimede.mics.model.Identifiers
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

class MicsImplTest {

    private val identifiers: Identifiers = mockk()
    private val mediarithmicsApi: MediarithmicsApi = mockk {
        coEvery { getSegmentsForUserAgentId(any()) } returns SegmentResponse("", emptyList())
        coEvery { getSegmentsForUserAccount(any()) } returns SegmentResponse("", emptyList())
        coEvery { associateUserIdentifiers(any()) } returns mockk()
        coEvery { post(any()) } returns mockk()
    }
    private val requestMapper: RequestMapper = mockk {
        every { createAssociationRequest(any()) } returns mockk()
        every { createUserActivityRequest(any(), any()) } returns mockk()
        every { createSegmentRequest(any()) } returns mockk()

    }
    private val responseMapper: ResponseMapper = mockk {
        every { mapResponse(any()) } returns mockk()
        every { mapSegments(any()) } returns mockk()
    }

    private lateinit var mics: MicsImpl

    @BeforeTest
    fun setUp() {
        mics = MicsImpl(mediarithmicsApi, identifiers, requestMapper, responseMapper)
    }

    @Test
    fun `getSegments, on request mapper error return Error`() = runTest {
        every { requestMapper.createSegmentRequest(any()) } throws NullPointerException()

        val result = mics.fetchSegments()
        assertTrue(result.isFailure)
    }

    @Test
    fun `getSegments, with user agent identifier call user agent api`() = runTest {
        every { requestMapper.createSegmentRequest(any()) } returns mockk<SerializableUserAgentIdentifier>()

        mics.getSegments()
        coVerify { mediarithmicsApi.getSegmentsForUserAgentId(any()) }
    }

    @Test
    fun `getSegments, with account identifier call account api`() = runTest {
        every { requestMapper.createSegmentRequest(any()) } returns mockk<SerializableAccountIdentifier>()

        mics.getSegments()
        coVerify { mediarithmicsApi.getSegmentsForUserAccount(any()) }
    }

    @Test
    fun `getSegments, when api error, return Error`() = runTest {
        coEvery { mediarithmicsApi.getSegmentsForUserAccount(any()) } throws NullPointerException()

        val result = mics.fetchSegments()
        assertTrue(result.isFailure)
    }

    @Test
    fun `getSegments, when response mapper error, return Error`() = runTest {
        every { responseMapper.mapResponse(any()) } throws NullPointerException()

        val result = mics.fetchSegments()
        assertTrue(result.isFailure)
    }

    @Test
    fun `getSegments, nominal success`() = runTest {
        every { requestMapper.createSegmentRequest(any()) } returns mockk<SerializableAccountIdentifier>()
        coEvery { mediarithmicsApi.getSegmentsForUserAccount(any()) } returns SegmentResponse("", emptyList())
        every { responseMapper.mapSegments(any()) } returns mockk()

        val result = mics.fetchSegments()
        assertTrue(result.isSuccess)
    }

    @Test
    fun `post, nominal success`() = runTest {
        val result = mics.sendPost(mockk())
        assertTrue(result.isSuccess)
    }

    @Test
    fun `post, on request mapper error, return Error`() = runTest {
        every { requestMapper.createUserActivityRequest(any(), any()) } throws NullPointerException()

        val result = mics.sendPost(mockk())
        assertTrue(result.isFailure)
    }

    @Test
    fun `post, on api error, return Error`() = runTest {
        coEvery { mediarithmicsApi.post(any()) } throws NullPointerException()

        val result = mics.sendPost(mockk())
        assertTrue(result.isFailure)
    }

    @Test
    fun `associateUserIdentifiers, nominal success`() = runTest {

        val result = mics.sendAssociateUserIdentifiers()
        assertTrue(result.isSuccess)
    }

    @Test
    fun `associateUserIdentifiers, on response mapper error returnsError`() = runTest {
        every { responseMapper.mapResponse(any()) } throws NullPointerException()

        val result = mics.sendAssociateUserIdentifiers()
        assertTrue(result.isFailure)
    }
}