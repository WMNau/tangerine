package nau.mike.tangerine.shaders

import nau.mike.tangerine.utils.buffer
import nau.mike.tangerine.utils.readAllLines
import org.joml.Matrix4f
import org.joml.Vector2f
import org.joml.Vector3f
import org.joml.Vector4f
import org.lwjgl.opengl.GL20C.*
import org.lwjgl.opengl.GL32C.GL_GEOMETRY_SHADER

@Suppress("unused", "MemberVisibilityCanBePrivate")
abstract class Shader(fileName: String) {

    private val programId: Int = glCreateProgram()
    private val vertexId: Int
    private val fragmentId: Int
    private var geometryId: Int? = null

    private val uniforms: MutableMap<String, Int> = HashMap()

    init {
        val filePath = "/shaders/$fileName.glsl"
        val resource = filePath.readAllLines()

        val all: StringBuilder = StringBuilder()
        val vertexSource: StringBuilder = StringBuilder()
        val fragmentSource: StringBuilder = StringBuilder()
        var geometrySource: StringBuilder? = null

        var isAll = false
        var isVertexShader = false
        var isFragmentShader = false
        var isGeometryShader = false
        for (line in resource) {
            when {
                line.contains("#type all") -> {
                    isAll = true
                    isVertexShader = false
                    isFragmentShader = false
                    isGeometryShader = false
                }
                line.contains("#type vert") -> {
                    isAll = false
                    isVertexShader = true
                    isFragmentShader = false
                    isGeometryShader = false
                    vertexSource.append(all)
                }
                line.contains("#type frag") -> {
                    isAll = false
                    isVertexShader = false
                    isFragmentShader = true
                    isGeometryShader = false
                    fragmentSource.append(all)
                }
                line.contains("#type geo") -> {
                    isAll = false
                    isVertexShader = false
                    isFragmentShader = false
                    isGeometryShader = true

                    geometrySource = StringBuilder()
                    geometrySource.append(all)
                }
            }

            if (line.startsWith("#type")) {
                continue
            }

            when {
                isAll -> {
                    all.append(line)
                }
                isVertexShader -> {
                    vertexSource.append(line)
                }
                isFragmentShader -> {
                    fragmentSource.append(line)
                }
                isGeometryShader -> {
                    geometrySource?.append(line)
                }
            }
        }

        vertexId = createShader(vertexSource.toString(), GL_VERTEX_SHADER)
        fragmentId = createShader(fragmentSource.toString(), GL_FRAGMENT_SHADER)
        geometrySource?.let { geometryId = createShader(it.toString(), GL_GEOMETRY_SHADER) }

        init()
    }

    fun start() {
        glUseProgram(programId)
    }

    fun stop() {
        glUseProgram(0)
    }

    fun clean() {
        stop()

        glDetachShader(programId, vertexId)
        glDetachShader(programId, fragmentId)
        geometryId?.let { glDetachShader(programId, it) }

        glDeleteProgram(programId)
    }

    abstract fun bindAllAttributes()

    protected fun bindAttribute(attribute: Int, name: String) {
        glBindAttribLocation(programId, attribute, name)
    }

    protected fun setUniform(name: String, value: Boolean) {
        val result: Int = if (value) {
            1
        } else {
            0
        }

        setUniform(name, result)
    }

    protected fun setUniform(name: String, x: Int) {
        if (uniforms[name] == null) {
            setUniformLocation(name)
        }
        uniforms[name]?.let { glUniform1i(it, x) }
    }

    protected fun setUniform(name: String, x: Float) {
        if (uniforms[name] == null) {
            setUniformLocation(name)
        }
        uniforms[name]?.let { glUniform1f(it, x) }
    }

    protected fun setUniform(name: String, x: Float, y: Float) {
        if (uniforms[name] == null) {
            setUniformLocation(name)
        }
        uniforms[name]?.let { glUniform2f(it, x, y) }
    }

    protected fun setUniform(name: String, x: Float, y: Float, z: Float) {
        if (uniforms[name] == null) {
            setUniformLocation(name)
        }
        uniforms[name]?.let { glUniform3f(it, x, y, z) }
    }

    protected fun setUniform(name: String, x: Float, y: Float, z: Float, w: Float) {
        if (uniforms[name] == null) {
            setUniformLocation(name)
        }
        uniforms[name]?.let { glUniform4f(it, x, y, z, w) }
    }

    protected fun setUniform(name: String, value: Vector2f) {
        setUniform(name, value.x, value.y)
    }

    protected fun setUniform(name: String, value: Vector3f) {
        setUniform(name, value.x, value.y, value.z)
    }

    protected fun setUniform(name: String, value: Vector4f) {
        setUniform(name, value.x, value.y, value.z, value.w)
    }

    protected fun setUniform(name: String, value: Matrix4f) {
        if (uniforms[name] == null) {
            setUniformLocation(name)
        }
        uniforms[name]?.let { glUniformMatrix4fv(it, false, value.buffer()) }
    }

    private fun init() {
        bindAllAttributes()
        link()
    }

    private fun createShader(source: String, type: Int): Int {
        val id = glCreateShader(type)
        glShaderSource(id, source)
        glCompileShader(id)
        check(id, GL_COMPILE_STATUS, source)
        glAttachShader(programId, id)

        return id
    }

    private fun link() {
        glLinkProgram(programId)
        deleteShaders()
        check(programId, GL_LINK_STATUS)
    }

    private fun check(id: Int, pName: Int, source: String? = null) {
        val success = if (source == null) {
            glGetProgrami(id, pName)
        } else {
            glGetShaderi(id, pName)
        }

        if (success == GL_FALSE) {
            val length = if (source == null) {
                glGetProgrami(id, GL_INFO_LOG_LENGTH)
            } else {
                glGetShaderi(id, GL_INFO_LOG_LENGTH)
            }

            var message = "Could not "
            message += if (source == null) {
                "link program\n${glGetProgramInfoLog(id, length)}"
            } else {
                "load shader \n$source\n${glGetShaderInfoLog(id, length)}"
            }

            deleteShaders()
            clean()

            throw RuntimeException(message)
        }
    }

    private fun deleteShaders() {
        glDeleteShader(vertexId)
        glDeleteShader(fragmentId)
        geometryId?.let { glDeleteShader(it) }
    }

    private fun setUniformLocation(name: String) {
        val location = glGetUniformLocation(programId, name)
        uniforms[name] = location
    }
}