package nau.mike.tangerine.input

import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWKeyCallbackI

@Suppress("unused")
object Keyboard : GLFWKeyCallbackI {

    private val pressed: MutableMap<Int, Boolean> = HashMap()
    private val released: MutableMap<Int, Boolean> = HashMap()

    override fun invoke(window: Long, key: Int, scancode: Int, action: Int, mods: Int) {
        pressed[key] = action == GLFW_PRESS || action == GLFW_REPEAT
        released[key] = action == GLFW_RELEASE
    }

    fun isPressed(key: Int): Boolean {
        return pressed[key] != null && pressed[key] == true
    }

    fun isReleased(key: Int): Boolean {
        return released[key] != null && released[key] == true
    }
}