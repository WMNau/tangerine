package nau.mike.tangerine.scenes

import nau.mike.tangerine.Mesh
import nau.mike.tangerine.MeshData
import nau.mike.tangerine.nau.mike.tangerine.Camera
import nau.mike.tangerine.utils.buffer
import org.joml.Vector2f
import java.nio.FloatBuffer
import java.nio.IntBuffer

class LevelEditorScene : Scene() {

    private val vertices: FloatBuffer = floatArrayOf(
        50.5f, -50.5f, 0.0f,
        -50.5f, 50.5f, 0.0f,
        50.5f, 50.5f, 0.0f,
        -50.5f, -50.5f, 0.0f
    ).buffer()

    private val indices: IntBuffer = intArrayOf(
        2, 1, 0,
        0, 1, 3
    ).buffer()

    private val uvs: FloatBuffer = floatArrayOf(
        1.0f, 1.0f,
        1.0f, 0.0f,
        0.0f, 0.0f,
        0.0f, 1.0f
    ).buffer()

    private val mesh: Mesh = Mesh(MeshData(vertices, uvs, indices))

    override fun init() {
        camera = Camera(Vector2f(0.0f))
    }

    override fun update(deltaTime: Float) {
    }

    override fun render() {
        basicShader.start()
        basicShader.loadCameraMatrices(camera)
        mesh.draw()
        basicShader.stop()
    }

    override fun clean() {
        basicShader.clean()
        mesh.clean()
    }
}