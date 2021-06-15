@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package nau.mike.tangerine.input

import org.joml.Vector2f
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWCursorEnterCallbackI
import org.lwjgl.glfw.GLFWCursorPosCallbackI
import org.lwjgl.glfw.GLFWMouseButtonCallbackI
import org.lwjgl.glfw.GLFWScrollCallbackI

object MousePosition : GLFWCursorPosCallbackI {

    val previousPosition: Vector2f = Vector2f(-1.0f)
    val currentPosition: Vector2f = Vector2f(0.0f)

    val displayVector: Vector2f = Vector2f(0.0f)

    override fun invoke(window: Long, xpos: Double, ypos: Double) {
        currentPosition.x = xpos.toFloat()
        currentPosition.y = ypos.toFloat()
    }

    fun update() {
        displayVector.x = 0.0f
        displayVector.y = 0.0f

        if (previousPosition.x > 0.0f && previousPosition.y > 0.0f && MouseEnter.inWindow) {
            val deltaX = currentPosition.x - previousPosition.x
            val deltaY = currentPosition.y - previousPosition.y

            val rotateX = deltaX != 0.0f
            val rotateY = deltaY != 0.0f

            if (rotateX) {
                displayVector.y = deltaX
            }

            if (rotateY) {
                displayVector.x = deltaY
            }
        }

        previousPosition.x = currentPosition.x
        previousPosition.y = currentPosition.y
    }
}

object MouseEnter : GLFWCursorEnterCallbackI {

    var inWindow: Boolean = false

    override fun invoke(window: Long, entered: Boolean) {
        inWindow = entered
    }
}

object MouseButton : GLFWMouseButtonCallbackI {

    private val pressed: MutableMap<Int, Boolean> = HashMap()
    private val released: MutableMap<Int, Boolean> = HashMap()

    override fun invoke(window: Long, button: Int, action: Int, mods: Int) {
        pressed[button] = action == GLFW_PRESS || action == GLFW_REPEAT
        released[button] = action == GLFW_RELEASE
    }

    fun isPressed(button: Int): Boolean {
        var isPressed = pressed[button]

        if (isPressed == null) {
            isPressed = false
            pressed[button] = false
        }

        return isPressed
    }

    fun isReleased(button: Int): Boolean {
        var isReleased = released[button]

        if (isReleased == null) {
            isReleased = false
            released[button] = false
        }

        return isReleased
    }
}

object MouseWheel : GLFWScrollCallbackI {

    val wheel: Vector2f = Vector2f(0.0f)

    override fun invoke(window: Long, xoffset: Double, yoffset: Double) {
        wheel.x = xoffset.toFloat()
        wheel.y = yoffset.toFloat()
    }

    fun reset() {
        wheel.x = 0.0f
        wheel.y = 0.0f
    }
}