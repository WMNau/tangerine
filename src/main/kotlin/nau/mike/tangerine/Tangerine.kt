package nau.mike.tangerine

import nau.mike.tangerine.scenes.LevelEditorScene
import nau.mike.tangerine.scenes.LevelScene
import nau.mike.tangerine.scenes.Scene
import nau.mike.tangerine.utils.Timer

private var currentScene: Scene = LevelEditorScene()

class Tangerine {

    private var window: Window = Window(1080, 600, "Tangerine Game Engine")
//    private var imGuiLayer: ImGuiLayer = ImGuiLayer(window)

    private fun clean() {
//        imGuiLayer.clean()
        currentScene.clean()
    }

    fun run() {
        changeScene(0)

        while (!window.shouldClose()) {
            Timer.start()

            while (Timer.shouldUpdate()) {
                window.update()
                Timer.update()
                currentScene.update(Timer.delta)
//                imGuiLayer.update()
            }

//            imGuiLayer.renderGui()

            currentScene.render()
            window.render()
            Timer.render()

            if (Timer.shouldReset()) {
                window.debugTitle(Timer.getString())
                Timer.reset()
            }
        }

        clean()

        window.clean()
    }
}

fun changeScene(newScene: Int) {
    currentScene.clean()

    when (newScene) {
        0 -> {
            currentScene = LevelEditorScene()
        }
        1 -> {
            currentScene = LevelScene()
        }
    }

    currentScene.init()
}
