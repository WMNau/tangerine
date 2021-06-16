package nau.mike.tangerine

import nau.mike.tangerine.input.*
import nau.mike.tangerine.utils.ColorUtil
import nau.mike.tangerine.utils.clearColor
import org.lwjgl.glfw.Callbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL.createCapabilities
import org.lwjgl.opengl.GL11C.*
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil.NULL

const val targetAspectRatio = 16.0f / 9.0f

var aspectRatio: Float = 0.0f
var resized: Boolean = true
var isGLInitialized: Boolean = false

class Window(var width: Int, var height: Int, private val title: String) {

    var glfwWindow: Long = 0

    init {
        if (!isGLInitialized) {
            aspectRatio = width.toFloat() / height.toFloat()

            GLFWErrorCallback.createPrint(System.err).set()

            check(glfwInit()) { "Failed to initialize GLFW" }

            configureGLFW()
            createGLFWWindow()
            centerGLFWWindow()

            glfwMakeContextCurrent(glfwWindow)
            glfwSwapInterval(GLFW_TRUE)

            createCapabilities()
            isGLInitialized = true

            glEnable(GL_DEPTH_TEST)

            ColorUtil.SKY_BLUE.clearColor()

            addCallbacks()

            glfwShowWindow(glfwWindow)
        }
    }

    fun update() {
        MousePosition.update()

        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

        if (Keyboard.isReleased(GLFW_KEY_ESCAPE)) {
            glfwSetWindowShouldClose(glfwWindow, true)
        }
    }

    fun render() {
        glfwSwapBuffers(glfwWindow)
        glfwPollEvents()
    }

    fun shouldClose(): Boolean {
        return glfwWindowShouldClose(glfwWindow)
    }

    fun debugTitle(message: String) {
        glfwSetWindowTitle(glfwWindow, "$title $message")
    }

    fun clean() {
        Callbacks.glfwFreeCallbacks(glfwWindow)
        glfwDestroyWindow(glfwWindow)

        glfwTerminate()
        glfwSetErrorCallback(null)?.free()
    }

    private fun configureGLFW() {
        glfwDefaultWindowHints()
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE)
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE)

        // Mac specific flags
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 1)
        glfwWindowHint(GLFW_COCOA_RETINA_FRAMEBUFFER, GLFW_TRUE)
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE)
    }

    private fun createGLFWWindow() {
        glfwWindow = glfwCreateWindow(width, height, title, NULL, NULL)
        if (glfwWindow == NULL) {
            throw RuntimeException("Failed to create the GLFW window")
        }
    }

    private fun centerGLFWWindow() {
        MemoryStack.stackPush().use { stack ->
            val pWidth = stack.mallocInt(1)
            val pHeight = stack.mallocInt(1)

            glfwGetWindowSize(glfwWindow, pWidth, pHeight)

            val videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor())

            glfwSetWindowPos(
                glfwWindow,
                (videoMode!!.width() - pWidth[0]) / 2,
                (videoMode.height() - pHeight[0]) / 2
            )
        }
    }

    private fun addCallbacks() {
        glfwSetKeyCallback(glfwWindow, Keyboard)

        glfwSetMouseButtonCallback(glfwWindow, MouseButton)
        glfwSetCursorPosCallback(glfwWindow, MousePosition)
        glfwSetCursorEnterCallback(glfwWindow, MouseEnter)

        glfwSetScrollCallback(glfwWindow, MouseWheel)

        glfwSetFramebufferSizeCallback(glfwWindow) { _, width, height ->
            this.width = width
            this.height - height

            glViewport(0, 0, width, height)

            aspectRatio = width.toFloat() / height.toFloat()
            resized = true
        }

        glfwSetWindowAspectRatio(glfwWindow, 16, 9)
    }
}
