package nau.mike.tangerine

import nau.mike.tangerine.utils.getImageFileAsByteBuffer
import org.lwjgl.opengl.GL11C.*
import org.lwjgl.opengl.GL30C.glGenerateMipmap
import org.lwjgl.stb.STBImage
import org.lwjgl.stb.STBImage.stbi_image_free
import org.lwjgl.stb.STBImage.stbi_load_from_memory
import org.lwjgl.system.MemoryStack.stackPush
import java.io.IOException
import java.nio.ByteBuffer

class Texture(
    name: String? = null,
    extension: String = "png",
    private var width: Int = 0,
    private var height: Int = 0
) {

    val id: Int

    private val nrChannels: Int

    init {
        val filePath = "/textures/$name.$extension"
        var data: ByteBuffer? = null

        if (name == null) {
            nrChannels = 0
        } else {
            stackPush().use {
                val w = it.mallocInt(1)
                val h = it.mallocInt(1)
                val c = it.mallocInt(1)

                val image = filePath.getImageFileAsByteBuffer()
                data = stbi_load_from_memory(image, w, h, c, 4)
                    ?: throw IOException("Could not load image from $filePath\n${STBImage.stbi_failure_reason()}")

                width = w.get(0)
                height = h.get(0)
                nrChannels = c.get(0)
            }
        }

        id = glGenTextures()
        glBindTexture(GL_TEXTURE_2D, id)

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)

        val format = if (nrChannels == 4) {
            GL_RGBA
        } else {
            GL_RGB
        }

        if (data == null) {
            glTexImage2D(GL_TEXTURE_2D, 0, format, width, height, 0, format, GL_UNSIGNED_BYTE, 0)
        } else {
            glTexImage2D(GL_TEXTURE_2D, 0, format, width, height, 0, format, GL_UNSIGNED_BYTE, data)
        }

        glGenerateMipmap(GL_TEXTURE_2D)
        glBindTexture(GL_TEXTURE_2D, 0)

        data?.let { stbi_image_free(it) }
    }
}