package com.quicklib.android.network.strategy

import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.MediatorLiveData
import com.google.firebase.firestore.DocumentReference
import com.quicklib.android.network.DataWrapper
import java.nio.charset.Charset
import java.util.TimeZone
import okio.buffer
import okio.source
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class FirebaseDatabaseReadListStrategyTest {

    @Mock
    lateinit var document: DocumentReference
    @Mock
    lateinit var liveData: MediatorLiveData<DataWrapper<TimeZone>>
    @Mock
    lateinit var mockLifecycleOwner: LifecycleRegistry

    @BeforeEach
    fun setUp() {
    }

    @AfterEach
    fun tearDown() {
    }

    private fun readAssetFile(filePath: String): String {
        val input = this.javaClass.classLoader.getResourceAsStream(filePath)
        val source = input.source().buffer()
        return source.readByteString().string(Charset.forName("utf-8"))
    }
}
