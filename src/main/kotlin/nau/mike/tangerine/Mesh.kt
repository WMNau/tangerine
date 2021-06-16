package nau.mike.tangerine

import org.lwjgl.opengl.GL45C.*
import java.nio.FloatBuffer
import java.nio.IntBuffer

class MeshData(
    val position: FloatBuffer,
    val uvs: FloatBuffer,
    val indices: IntBuffer
)

class Mesh(
    data: MeshData,
    private val textures: MutableList<Texture> = ArrayList()
) {

    private val vao: Int

    private val vbos: MutableList<Int> = ArrayList()
    private var attributes: Int = 0

    private var vertexCount: Int = data.indices.limit()

    init {
        vao = createVAO()

        bindIndicesBuffer(data.indices)
        storeDataInVertexAttribArray(data.position, 3)
        storeDataInVertexAttribArray(data.uvs, 2)

        unbind()
    }

    fun draw() {
        start()
        var i = 0
        textures.forEach { texture ->
            glActiveTexture(GL_TEXTURE0 + i)
            glBindTexture(GL_TEXTURE_2D, texture.id)

            i++
        }
        glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0)
        stop()
    }

    fun clean() {
        glDeleteVertexArrays(vao)
        glDeleteBuffers(vbos.toIntArray())
    }

    private fun start() {
        glBindVertexArray(vao)
        enableVertexAttribArrays()
    }

    private fun stop() {
        enableVertexAttribArrays(false)
        unbind()
    }

    private fun createVAO(): Int {
        val id = glGenVertexArrays()
        glBindVertexArray(id)

        return id
    }

    private fun storeDataInVertexAttribArray(data: FloatBuffer, size: Int) {
        val vbo = generateBuffer()

        glBindBuffer(GL_ARRAY_BUFFER, vbo)
        glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_DRAW)
        glVertexAttribPointer(attributes, size, GL_FLOAT, false, 0, 0)
        glEnableVertexAttribArray(attributes)
        glBindBuffer(GL_ARRAY_BUFFER, 0)

        attributes++
    }

    private fun bindIndicesBuffer(data: IntBuffer) {
        val vbo = generateBuffer()

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vbo)
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, data, GL_STATIC_DRAW)
    }

    private fun unbind() {
        glBindVertexArray(0)
    }

    private fun generateBuffer(): Int {
        val vbo = glGenBuffers()
        vbos.add(vbo)

        return vbo
    }

    private fun enableVertexAttribArrays(shouldEnable: Boolean = true) {
        for (i in 0 until attributes) {
            if (shouldEnable) {
                glEnableVertexAttribArray(i)
            } else {
                glDisableVertexAttribArray(i)
            }
        }
    }
}