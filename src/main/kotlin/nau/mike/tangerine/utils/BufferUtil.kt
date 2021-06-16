package nau.mike.tangerine.utils

import org.joml.Matrix4f
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil
import java.nio.ByteBuffer
import java.nio.FloatBuffer
import java.nio.IntBuffer

fun FloatArray.buffer(): FloatBuffer {
    val buffer = MemoryUtil.memAllocFloat(this.size)
    buffer.put(this).flip()

    return buffer
}

fun IntArray.buffer(): IntBuffer {
    val buffer = MemoryUtil.memAllocInt(this.size)
    buffer.put(this).flip()

    return buffer
}

fun ByteArray.buffer(): ByteBuffer {
    val buffer = MemoryUtil.memAlloc(this.size)
    buffer.put(this).flip()

    return buffer
}

fun Matrix4f.buffer(): FloatBuffer {
    MemoryStack.stackPush().use { stack ->
        val buffer = stack.mallocFloat(16)
        this.get(buffer)

        return buffer
    }
}
