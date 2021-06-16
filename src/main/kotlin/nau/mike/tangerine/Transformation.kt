package nau.mike.tangerine

import org.joml.Matrix4f
import org.joml.Vector3f

class Transformation {

    var modelMatrix: Matrix4f = Matrix4f().identity()
    var projectionMatrix: Matrix4f = Matrix4f().identity()

    fun createModelMatrix(translation: Vector3f, rotation: Vector3f, scale: Float) {
        createModelMatrix(translation, rotation, Vector3f(scale))
    }

    private fun createModelMatrix(translation: Vector3f, rotation: Vector3f, scale: Vector3f) {
        modelMatrix = Matrix4f().identity()
            .translate(translation)
            .rotate(Math.toRadians(rotation.x.toDouble()).toFloat(), Vector3f(1.0f, 0.0f, 0.0f))
            .rotate(Math.toRadians(rotation.y.toDouble()).toFloat(), Vector3f(0.0f, 1.0f, 0.0f))
            .rotate(Math.toRadians(rotation.z.toDouble()).toFloat(), Vector3f(0.0f, 0.0f, 1.0f))
            .scale(scale)
    }

    fun createProjectionMatrix(fov: Float, aspect: Float, zNear: Float, zFar: Float) {
        projectionMatrix = Matrix4f().identity()
            .perspective(fov, aspect, zNear, zFar)
    }
}
