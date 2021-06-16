package nau.mike.tangerine

import imgui.ImGui.*
import imgui.ImGuiIO
import imgui.flag.*
import imgui.gl3.ImGuiImplGl3
import imgui.glfw.ImGuiImplGlfw
import imgui.type.ImBoolean
import org.lwjgl.glfw.GLFW

class ImGuiLayer(private val window: Window) {

    private var glslVersion: String? = "#version 410"

    private val imGuiGLFW = ImGuiImplGlfw()
    private val imGuiGL3 = ImGuiImplGl3()

    val io: ImGuiIO

    init {
        createContext()
        imGuiGLFW.init(window.glfwWindow, true)
        imGuiGL3.init(glslVersion)

        io = getIO()
        io.configFlags = ImGuiConfigFlags.NavEnableKeyboard or ImGuiConfigFlags.DockingEnable
        io.backendFlags = ImGuiBackendFlags.HasMouseCursors
    }

    fun update() {
        if (!io.wantCaptureKeyboard || !io.wantCaptureMouse) {
            // TODO: update inputs here
        }
    }

    fun renderGui() {
        start()

        begin("Test")
        text("New text")
        end()
        begin("Test2")
        text("New text2")
        end()

        stop()
    }

    private fun start() {
        imGuiGLFW.newFrame()
        newFrame()

        setDockSpace()
    }

    private fun stop() {
        end()

        render()
        imGuiGL3.renderDrawData(getDrawData())

        if (io.hasConfigFlags(ImGuiConfigFlags.DockingEnable)) {
            val backupWindowPtr = GLFW.glfwGetCurrentContext()
            updatePlatformWindows()
            renderPlatformWindowsDefault()
            GLFW.glfwMakeContextCurrent(backupWindowPtr)
        }
    }

    fun clean() {
        imGuiGL3.dispose()
        imGuiGLFW.dispose()

        destroyContext()
    }

    private fun setDockSpace() {
        val windowFlags =
            ImGuiWindowFlags.MenuBar or ImGuiWindowFlags.NoDocking or ImGuiWindowFlags.NoTitleBar or
                    ImGuiWindowFlags.NoCollapse or ImGuiWindowFlags.NoResize or ImGuiWindowFlags.NoMove or
                    ImGuiWindowFlags.NoBringToFrontOnFocus or ImGuiWindowFlags.NoNavFocus
        setNextWindowPos(0.0f, 0.0f, ImGuiCond.Always)
        setNextWindowSize(window.width.toFloat(), window.height.toFloat())
        pushStyleVar(ImGuiStyleVar.WindowRounding, 0.0f)
        pushStyleVar(ImGuiStyleVar.WindowBorderSize, 0.0f)

        begin("ImGuiRender", ImBoolean(true), windowFlags)
        popStyleVar(2)

        dockSpace(getID("DockSpace"))
    }
}
