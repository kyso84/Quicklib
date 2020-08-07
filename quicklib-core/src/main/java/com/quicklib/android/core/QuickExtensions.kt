package com.quicklib.android.core

import android.os.Build
import androidx.annotation.RequiresApi
import java.io.File
import java.util.concurrent.TimeUnit


/**
 * This method extract file's extension
 */
fun File.getExtension(): String {
    val sep = this.name.lastIndexOf('.')
    return if (this.isFile) {
        this.name.substring(sep + 1)
    } else {
        ""
    }
}

/**
 * This method extract file's root name (without extension)
 */
fun File.getRootName(): String =if (this.getExtension().isNotEmpty()) {
    this.name.replace(".${this.getExtension()}", "")
} else {
    this.name
}

/**
 * This method runs a command
 */
@RequiresApi(Build.VERSION_CODES.O)
fun String.runCommand(
        workingDir: File = File("."),
        timeoutAmount: Long = 1,
        timeoutUnit: TimeUnit = TimeUnit.MINUTES
): String? = try {
        ProcessBuilder(split("\\s".toRegex()))
                .directory(workingDir)
                .redirectOutput(ProcessBuilder.Redirect.PIPE)
                .redirectErrorStream(true)
                .redirectError(ProcessBuilder.Redirect.PIPE)
                .start().apply { waitFor(timeoutAmount, timeoutUnit) }
                .inputStream.bufferedReader().readText()
} catch (e: java.io.IOException) {
    throw e
} catch (e: InterruptedException) {
    throw e
}

