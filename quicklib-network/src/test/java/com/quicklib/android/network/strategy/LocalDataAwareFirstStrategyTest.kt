package com.quicklib.android.network.strategy

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import com.quicklib.android.network.DataStatus
import com.quicklib.android.network.DataWrapper
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.rules.TestRule
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.eq
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when` as _when
import org.mockito.MockitoAnnotations
import org.mockito.junit.jupiter.MockitoExtension
import java.lang.StringBuilder
import kotlin.reflect.full.isSubclassOf


@ExtendWith(MockitoExtension::class)
class LocalDataAwareFirstStrategyTest {

    companion object {
        const val TAG_NETWORK = "network"
        const val TAG_NETWORK_ERROR = "network_error"
        const val TAG_NETWORK_UNAVAILABLE = "Remote data is not available"
        const val TAG_LOCAL = "local"
        const val TAG_LOCAL_ERROR = "local_error"
        const val TAG_LOCAL_UNAVAILABLE = "Local data is not available"

        @BeforeAll
        @JvmStatic
        fun initAll() {
            MockitoAnnotations.initMocks(this)
        }

        @AfterAll
        @JvmStatic
        fun tearDownAll() {
        }
    }

    @Rule
    @JvmField
    var rule: TestRule = InstantTaskExecutorRule()

    @Captor
    lateinit var resultCaptor: ArgumentCaptor<DataWrapper<String>>
    @Mock
    lateinit var liveData: MediatorLiveData<DataWrapper<String>>
    @Mock
    lateinit var liveDataLocal: LiveData<String>
    @Mock
    lateinit var observer: Observer<String>
    @Captor
    lateinit var observerCaptor: ArgumentCaptor<String>
    @Mock
    lateinit var storage: StringBuilder
    @Mock
    lateinit var mockLifecycleOwner: LifecycleRegistry

    abstract inner class SutStrategy(liveData: MediatorLiveData<DataWrapper<String>>) : LocalDataAwareFirstStrategy<String>(mainScope = CoroutineScope(Dispatchers.Unconfined), localScope = CoroutineScope(Dispatchers.Unconfined), remoteScope = CoroutineScope(Dispatchers.Unconfined), liveData = liveData)

    @BeforeEach
    fun init() {

    }

    @AfterEach
    fun tearDown() {
    }

    @Test
    @DisplayName("happyPath => should return a local livedata")
    fun happyPath() {
        // Arrange /////////////////////////////////////////////////////////////////////////////////
        _when(liveData.addSource(liveDataLocal, observer)).then {
            observer.onChanged(TAG_LOCAL)
        }
        val mockNetworkState = true
        val mockLocalState = true
        val localValidity = true

        // Act /////////////////////////////////////////////////////////////////////////////////////
        mockLifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        runBlocking {
            val sut = object : SutStrategy(liveData = liveData) {
                override suspend fun isValid(data: String) = localValidity
                override fun isRemoteAvailable(): Boolean = mockNetworkState
                override fun isLocalAvailable(): Boolean = mockLocalState
                override suspend fun fetchData(): Deferred<String> = CompletableDeferred(TAG_NETWORK)
                override suspend fun readData(): Deferred<LiveData<String>> = CompletableDeferred(liveDataLocal)
                override suspend fun writeData(data: String) {
                    storage.append(data)
                }
            }
        }

        // Assert //////////////////////////////////////////////////////////////////////////////////
        val accessCount = 2
        verify(liveData, times(accessCount)).postValue(resultCaptor.capture())
//        verify(liveData).addSource(eq(liveDataLocal), eq(observer))
//        verify(observer).onChanged(observerCaptor.capture())
        val resultList = resultCaptor.allValues
        assert(resultList.size == accessCount)

        // Resource #1
        assert(resultList[0].value == null)
        assert(resultList[0].status == DataStatus.LOADING)
        assert(resultList[0].error == null)
        assert(resultList[0].localData)
        assert(resultList[0].warning == null)
        assert(resultList[0].timestamp > 0)
        assert(resultList[0].strategy.isSubclassOf(LocalDataAwareFirstStrategy::class))
        // Resource #2
        assert(resultList[1].value == observerCaptor.value)
        assert(resultList[1].status == DataStatus.SUCCESS)
        assert(resultList[1].error == null)
        assert(!resultList[1].localData)
        assert(resultList[1].warning == null)
        assert(resultList[1].timestamp > 0)
        assert(resultList[1].strategy.isSubclassOf(LocalDataAwareFirstStrategy::class))
        // Save
        verify(storage, times(1)).append(ArgumentMatchers.eq(TAG_NETWORK))
    }

    @Test
    fun `test start() - with network error - returns local wrapped data`() {
        // Arrange /////////////////////////////////////////////////////////////////////////////////
        val mockNetworkState = true
        val mockLocalState = true
        val localValidity = true

        // Act /////////////////////////////////////////////////////////////////////////////////////
        mockLifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        val sut = object : SutStrategy(liveData = liveData) {
            override suspend fun isValid(data: String) = localValidity
            override fun isRemoteAvailable(): Boolean = mockNetworkState
            override fun isLocalAvailable(): Boolean = mockLocalState
            override suspend fun fetchData(): Deferred<String> {
                throw UnsupportedOperationException(TAG_NETWORK_ERROR)
            }

            override suspend fun readData(): Deferred<LiveData<String>> = CompletableDeferred(liveDataLocal)
            override suspend fun writeData(data: String) {
                storage.append(data)
            }
        }


        // Assert //////////////////////////////////////////////////////////////////////////////////
        val accessCount = 3
        verify(liveData, times(accessCount)).postValue(resultCaptor.capture())
        val resultList = resultCaptor.allValues
        assert(resultList.size == accessCount)
        // Resource #1
        assert(resultList[0].value == null)
        assert(resultList[0].status == DataStatus.FETCHING)
        assert(resultList[0].error == null)
        assert(!resultList[0].localData)
        assert(resultList[0].warning == null)
        assert(resultList[0].timestamp > 0)
        assert(resultList[0].strategy.isSubclassOf(LocalDataAwareFirstStrategy::class))
        // Resource #2
        assert(resultList[1].value == null)
        assert(resultList[1].status == DataStatus.LOADING)
        assert(resultList[1].error == null)
        assert(resultList[1].localData)
        assert(resultList[1].warning is UnsupportedOperationException && resultList[1].warning?.message == TAG_NETWORK_ERROR)
        assert(resultList[1].timestamp > 0)
        assert(resultList[1].strategy.isSubclassOf(LocalDataAwareFirstStrategy::class))
        // Resource #3
        assert(resultList[2].value == TAG_LOCAL)
        assert(resultList[2].status == DataStatus.SUCCESS)
        assert(resultList[2].error == null)
        assert(resultList[2].localData)
        assert(resultList[2].warning is UnsupportedOperationException && resultList[2].warning?.message == TAG_NETWORK_ERROR)
        assert(resultList[2].timestamp > 0)
        assert(resultList[2].strategy.isSubclassOf(LocalDataAwareFirstStrategy::class))
        // Save
        verify(storage, times(0)).append(ArgumentMatchers.eq(TAG_NETWORK))
    }


    @Test
    fun `test start() - with network error and no storage - returns error`() {
        // Arrange /////////////////////////////////////////////////////////////////////////////////
        val mockNetworkState = true
        val mockLocalState = false
        val localValidity = true

        // Act /////////////////////////////////////////////////////////////////////////////////////
        mockLifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        val sut = object : SutStrategy(liveData = liveData) {
            override suspend fun isValid(data: String) = localValidity
            override fun isRemoteAvailable(): Boolean = mockNetworkState
            override fun isLocalAvailable(): Boolean = mockLocalState
            override suspend fun fetchData(): Deferred<String> {
                throw UnsupportedOperationException(TAG_NETWORK_ERROR)
            }

            override suspend fun readData(): Deferred<LiveData<String>> = CompletableDeferred(liveDataLocal)
            override suspend fun writeData(data: String) {
                storage.append(data)
            }
        }


        // Assert //////////////////////////////////////////////////////////////////////////////////
        val accessCount = 2
        verify(liveData, times(accessCount)).postValue(resultCaptor.capture())
        val resultList = resultCaptor.allValues
        assert(resultList.size == accessCount)
        // Resource #1
        assert(resultList[0].value == null)
        assert(resultList[0].status == DataStatus.FETCHING)
        assert(resultList[0].error == null)
        assert(!resultList[0].localData)
        assert(resultList[0].warning == null)
        assert(resultList[0].timestamp > 0)
        assert(resultList[0].strategy.isSubclassOf(LocalDataAwareFirstStrategy::class))
        // Resource #2
        assert(resultList[1].value == null)
        assert(resultList[1].status == DataStatus.INVALID)
        assert(resultList[1].error is IllegalStateException && resultList[1].error?.message == TAG_LOCAL_UNAVAILABLE)
        assert(resultList[1].localData)
        assert(resultList[1].warning is UnsupportedOperationException && resultList[1].warning?.message == TAG_NETWORK_ERROR)
        assert(resultList[1].timestamp > 0)
        assert(resultList[1].strategy.isSubclassOf(LocalDataAwareFirstStrategy::class))
        // Save
        verify(storage, times(0)).append(ArgumentMatchers.eq(TAG_NETWORK))
    }


    @Test
    fun `test start() - with network error and storage error - returns error`() {
        // Arrange /////////////////////////////////////////////////////////////////////////////////
        val mockNetworkState = true
        val mockLocalState = true
        val localValidity = true

        // Act /////////////////////////////////////////////////////////////////////////////////////
        mockLifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        val sut = object : SutStrategy(liveData = liveData) {
            override suspend fun isValid(data: String) = localValidity
            override fun isRemoteAvailable(): Boolean = mockNetworkState
            override fun isLocalAvailable(): Boolean = mockLocalState
            override suspend fun fetchData(): Deferred<String> {
                throw UnsupportedOperationException(TAG_NETWORK_ERROR)
            }

            override suspend fun readData(): Deferred<LiveData<String>> {
                throw UnsupportedOperationException(TAG_LOCAL_ERROR)
            }

            override suspend fun writeData(data: String) {
                storage.append(data)
            }
        }


        // Assert //////////////////////////////////////////////////////////////////////////////////
        val accessCount = 3
        verify(liveData, times(accessCount)).postValue(resultCaptor.capture())
        val resultList = resultCaptor.allValues
        assert(resultList.size == accessCount)
        // Resource #1
        assert(resultList[0].value == null)
        assert(resultList[0].status == DataStatus.FETCHING)
        assert(resultList[0].error == null)
        assert(!resultList[0].localData)
        assert(resultList[0].warning == null)
        assert(resultList[0].timestamp > 0)
        assert(resultList[0].strategy.isSubclassOf(LocalDataAwareFirstStrategy::class))
        // Resource #2
        assert(resultList[1].value == null)
        assert(resultList[1].status == DataStatus.LOADING)
        assert(resultList[1].error == null)
        assert(resultList[1].localData)
        assert(resultList[1].warning is UnsupportedOperationException && resultList[1].warning?.message == TAG_NETWORK_ERROR)
        assert(resultList[1].timestamp > 0)
        assert(resultList[1].strategy.isSubclassOf(LocalDataAwareFirstStrategy::class))
        // Resource #3
        assert(resultList[2].value == null)
        assert(resultList[2].status == DataStatus.ERROR)
        assert(resultList[2].error is UnsupportedOperationException && resultList[2].error?.message == TAG_LOCAL_ERROR)
        assert(resultList[2].localData)
        assert(resultList[2].warning is UnsupportedOperationException && resultList[2].warning?.message == TAG_NETWORK_ERROR)
        assert(resultList[2].timestamp > 0)
        assert(resultList[2].strategy.isSubclassOf(LocalDataAwareFirstStrategy::class))
        // Save
        verify(storage, times(0)).append(ArgumentMatchers.eq(TAG_NETWORK))
    }


    @Test
    fun `test start() - without network connexion - returns local wrapped data`() {
        // Arrange /////////////////////////////////////////////////////////////////////////////////
        val mockNetworkState = false
        val mockLocalState = true
        val localValidity = true

        // Act /////////////////////////////////////////////////////////////////////////////////////
        mockLifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        val sut = object : SutStrategy(liveData = liveData) {
            override suspend fun isValid(data: String) = localValidity
            override fun isRemoteAvailable(): Boolean = mockNetworkState
            override fun isLocalAvailable(): Boolean = mockLocalState
            override suspend fun fetchData(): Deferred<String> = CompletableDeferred(TAG_NETWORK)
            override suspend fun readData(): Deferred<LiveData<String>> = CompletableDeferred(liveDataLocal)
            override suspend fun writeData(data: String) {
                storage.append(data)
            }
        }


        // Assert //////////////////////////////////////////////////////////////////////////////////
        val accessCount = 2
        verify(liveData, times(accessCount)).postValue(resultCaptor.capture())
        val resultList = resultCaptor.allValues
        assert(resultList.size == accessCount)
        // Resource #1
        assert(resultList[0].value == null)
        assert(resultList[0].status == DataStatus.LOADING)
        assert(resultList[0].error == null)
        assert(resultList[0].localData)
        assert(resultList[0].warning is IllegalStateException && resultList[0].warning?.message == TAG_NETWORK_UNAVAILABLE)
        assert(resultList[0].timestamp > 0)
        assert(resultList[0].strategy.isSubclassOf(LocalDataAwareFirstStrategy::class))
        // Resource #2
        assert(resultList[1].value == TAG_LOCAL)
        assert(resultList[1].status == DataStatus.SUCCESS)
        assert(resultList[1].error == null)
        assert(resultList[1].localData)
        assert(resultList[1].warning is IllegalStateException && resultList[1].warning?.message == TAG_NETWORK_UNAVAILABLE)
        assert(resultList[1].timestamp > 0)
        assert(resultList[1].strategy.isSubclassOf(LocalDataAwareFirstStrategy::class))
        // Save
        verify(storage, times(0)).append(ArgumentMatchers.eq(TAG_NETWORK))
    }


    @Test
    fun `test start() - without network connexion nor storage - returns error`() {
        // Arrange /////////////////////////////////////////////////////////////////////////////////
        val mockNetworkState = false
        val mockLocalState = false
        val localValidity = true

        // Act /////////////////////////////////////////////////////////////////////////////////////
        mockLifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        val sut = object : SutStrategy(liveData = liveData) {
            override suspend fun isValid(data: String) = localValidity
            override fun isRemoteAvailable(): Boolean = mockNetworkState
            override fun isLocalAvailable(): Boolean = mockLocalState
            override suspend fun fetchData(): Deferred<String> = CompletableDeferred(TAG_NETWORK)
            override suspend fun readData(): Deferred<LiveData<String>> = CompletableDeferred(liveDataLocal)
            override suspend fun writeData(data: String) {
                storage.append(data)
            }
        }


        // Assert //////////////////////////////////////////////////////////////////////////////////
        val accessCount = 1
        verify(liveData, times(accessCount)).postValue(resultCaptor.capture())
        val resultList = resultCaptor.allValues
        assert(resultList.size == accessCount)
        // Resource #1
        assert(resultList[0].value == null)
        assert(resultList[0].status == DataStatus.INVALID)
        assert(resultList[0].error is IllegalStateException && resultList[0].error?.message == TAG_LOCAL_UNAVAILABLE)
        assert(resultList[0].localData)
        assert(resultList[0].warning is IllegalStateException && resultList[0].warning?.message == TAG_NETWORK_UNAVAILABLE)
        assert(resultList[0].timestamp > 0)
        assert(resultList[0].strategy.isSubclassOf(LocalDataAwareFirstStrategy::class))
        // Save
        verify(storage, times(0)).append(ArgumentMatchers.eq(TAG_NETWORK))
    }


    @Test
    fun `test start() - without network connexion and storage error - returns error`() {
        // Arrange /////////////////////////////////////////////////////////////////////////////////
        val mockNetworkState = false
        val mockLocalState = true
        val localValidity = true

        // Act /////////////////////////////////////////////////////////////////////////////////////
        mockLifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        val sut = object : SutStrategy(liveData = liveData) {
            override suspend fun isValid(data: String) = localValidity
            override fun isRemoteAvailable(): Boolean = mockNetworkState
            override fun isLocalAvailable(): Boolean = mockLocalState
            override suspend fun fetchData(): Deferred<String> = CompletableDeferred(TAG_NETWORK)
            override suspend fun readData(): Deferred<LiveData<String>> {
                throw UnsupportedOperationException(TAG_LOCAL_ERROR)
            }

            override suspend fun writeData(data: String) {
                storage.append(data)
            }
        }


        // Assert //////////////////////////////////////////////////////////////////////////////////
        val accessCount = 2
        verify(liveData, times(accessCount)).postValue(resultCaptor.capture())
        val resultList = resultCaptor.allValues
        assert(resultList.size == accessCount)
        // Resource #1
        assert(resultList[0].value == null)
        assert(resultList[0].status == DataStatus.LOADING)
        assert(resultList[0].error == null)
        assert(resultList[0].localData)
        assert(resultList[0].warning is IllegalStateException && resultList[0].warning?.message == TAG_NETWORK_UNAVAILABLE)
        assert(resultList[0].timestamp > 0)
        assert(resultList[0].strategy.isSubclassOf(LocalDataAwareFirstStrategy::class))
        // Resource #2
        assert(resultList[1].value == null)
        assert(resultList[1].status == DataStatus.ERROR)
        assert(resultList[1].error is UnsupportedOperationException && resultList[1].error?.message == TAG_LOCAL_ERROR)
        assert(resultList[1].localData)
        assert(resultList[1].warning is IllegalStateException && resultList[1].warning?.message == TAG_NETWORK_UNAVAILABLE)
        assert(resultList[1].timestamp > 0)
        assert(resultList[1].strategy.isSubclassOf(LocalDataAwareFirstStrategy::class))
        // Save
        verify(storage, times(0)).append(ArgumentMatchers.eq(TAG_NETWORK))
    }


}