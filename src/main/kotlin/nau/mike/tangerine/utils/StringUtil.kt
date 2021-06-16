package nau.mike.tangerine.utils

import java.io.IOException
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.charset.Charset

fun String.getSource(): String {
    val sb = StringBuilder()
    val lines = this.readAllLines()

    lines.forEach { line -> sb.append(line).append('\n') }

    return sb.toString()
}

fun String.readAllLines(): MutableList<String> {
    val lines: MutableList<String> = ArrayList()
    val resource = this.getResourceAsStream()
    val reader = resource.bufferedReader(Charset.defaultCharset())

    for (line in reader.readLines()) {
        if (line.isNotEmpty()) {
            lines.add("$line\n")
        }
    }

    reader.close()

    return lines
}

fun String.getResourceAsStream(): InputStream {
    return {}.javaClass.getResourceAsStream(this) ?: throw IOException("Could not read from file $this")
}

fun String.getImageFileAsByteBuffer(): ByteBuffer {
    val resource = this.getResourceAsStream()
    val imageData = ByteArray(resource.available())
    resource.read(imageData)
    return imageData.buffer()
}
