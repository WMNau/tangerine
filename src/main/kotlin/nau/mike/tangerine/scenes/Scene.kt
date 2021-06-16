package nau.mike.tangerine.scenes

import nau.mike.tangerine.nau.mike.tangerine.Camera
import nau.mike.tangerine.shaders.BasicShader

abstract class Scene {

    protected val basicShader: BasicShader = BasicShader()
    protected lateinit var camera: Camera

    open fun init() {
    }

    abstract fun update(deltaTime: Float)

    abstract fun render()

    abstract fun clean()
}