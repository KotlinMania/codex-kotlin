package ai.solace.coder.utils

import io.ktor.utils.io.*
import okio.Buffer
import okio.Source
import okio.Timeout
import kotlinx.coroutines.runBlocking

/**
 * Bridge between Ktor's ByteReadChannel and Okio's Source.
 * Note: read() will block the current thread if data is not available.
 * Since SSE processing runs in its own coroutine/thread, this is generally acceptable
 * for a transliteration-focused port.
 */
class ByteReadChannelSource(private val channel: ByteReadChannel) : Source {
    override fun read(sink: Buffer, byteCount: Long): Long {
        if (channel.isClosedForRead && channel.availableForRead == 0) return -1L
        
        val bytesToRead = byteCount.coerceAtMost(8192).toInt()
        val buffer = ByteArray(bytesToRead)
        
        // We use runBlocking here because Okio's Source.read is not a suspend function.
        // This is the "dishonest" part of bridging sync and async APIs, but necessary
        // to use Okio's streaming utilities on top of Ktor's async channels.
        val read = runBlocking {
            channel.readAvailable(buffer, 0, bytesToRead)
        }
        
        if (read <= 0) {
            return if (channel.isClosedForRead) -1L else 0L
        }
        
        sink.write(buffer, 0, read)
        return read.toLong()
    }

    override fun timeout(): Timeout = Timeout.NONE

    override fun close() {
        channel.cancel()
    }
}

fun ByteReadChannel.asSource(): Source = ByteReadChannelSource(this)
