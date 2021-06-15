package nau.mike.tangerine.nau.mike.tangerine

import nau.mike.tangerine.input.*
import nau.mike.tangerine.nau.mike.tangerine.utils.ColorUtil
import nau.mike.tangerine.nau.mike.tangerine.utils.clearColor
import org.lwjgl.glfw.Callbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11C.*
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil.NULL

var aspectRatio: Float = 0.0f
var resized: Boolean = true
var isGLInitialized: Boolean = false

class Window(private var width: Int, private var height: Int, private val title: String) {

    private var window: Long = 0

    init {
        aspectRatio = width.toFloat() / height.toFloat()

        GLFWErrorCallback.createPrint(System.err).set()

        check(glfwInit()) { "Failed to initialize GLFW" }

        configureGLFW()
        createGLFWWindow()
        centerGLFWWindow()

        glfwMakeContextCurrent(window)
        glfwSwapInterval(GLFW_TRUE)

        GL.createCapabilities()
        isGLInitialized = true

        glEnable(GL_DEPTH_TEST)

        addCallbacks()

        ColorUtil.MIDNIGHT.clearColor()
        glfwShowWindow(window)
    }

    fun update() {
        glfwPollEvents()
        MousePosition.update()

        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

        if (Keyboard.isReleased(GLFW_KEY_ESCAPE)) {
            glfwSetWindowShouldClose(window, true)
        }
    }

    fun render() {
        glfwSwapBuffers(window)
    }

    fun shouldClose(): Boolean {
        return glfwWindowShouldClose(window)
    }

    fun clean() {
        Callbacks.glfwFreeCallbacks(window)
        glfwDestroyWindow(window)

        glfwTerminate()
        glfwSetErrorCallback(null)?.free()
    }

    private fun configureGLFW() {
        glfwDefaultWindowHints()
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE)

        // Mac specific flags
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 1)
        glfwWindowHint(GLFW_COCOA_RETINA_FRAMEBUFFER, GLFW_TRUE)
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE)
    }

    private fun createGLFWWindow() {
        window = glfwCreateWindow(width, height, title, NULL, NULL)
        if (window == NULL) {
            throw RuntimeException("Failed to create the GLFW window")
        }
    }

    private fun centerGLFWWindow() {
        MemoryStack.stackPush().use { stack ->
            val pWidth = stack.mallocInt(1)
            val pHeight = stack.mallocInt(1)

            glfwGetWindowSize(window, pWidth, pHeight)

            val videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor())

            glfwSetWindowPos(
                window,
                (videoMode!!.width() - pWidth[0]) / 2,
                (videoMode.height() - pHeight[0]) / 2
            )
        }
    }

    private fun addCallbacks() {
        glfwSetKeyCallback(window, Keyboard)

        glfwSetMouseButtonCallback(window, MouseButton)
        glfwSetCursorPosCallback(window, MousePosition)
        glfwSetCursorEnterCallback(window, MouseEnter)

        glfwSetScrollCallback(window, MouseWheel)

        glfwSetFramebufferSizeCallback(window) { _, width, height ->
            this.width = width
            this.height - height

            glViewport(0, 0, width, height)

            aspectRatio = width.toFloat() / height.toFloat()
            resized = true
        }
    }
}
