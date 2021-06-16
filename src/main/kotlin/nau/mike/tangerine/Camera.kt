package nau.mike.tangerine.nau.mike.tangerine

import org.joml.Matrix4f
import org.joml.Vector2f
import org.joml.Vector3f

class Camera(val position: Vector2f = Vector2f(0.0f)) {

    val projectionMatrix: Matrix4f
        get() {
            return Matrix4f().identity()
                .ortho(0.0f, 32.0f * 40.0f, 0.0f, 32.0f * 21.0f, 0.0f, 100.0f)
        }

    val viewMatrix: Matrix4f
        get() {
            val front = Vector3f(0.0f, 0.0f, -1.0f)
            val up = Vector3f(0.0f, 1.0f, 0.0f)

            return Matrix4f().identity()
                .lookAt(Vector3f(position.x, position.y, 20.0f), front.add(position.x, position.y, 0.0f), up)
        }
}