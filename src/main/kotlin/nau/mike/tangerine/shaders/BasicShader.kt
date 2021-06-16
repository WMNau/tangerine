package nau.mike.tangerine.shaders

import nau.mike.tangerine.nau.mike.tangerine.Camera

class BasicShader : Shader("basic") {

    override fun bindAllAttributes() {
        super.bindAttribute(0, "position")
    }

    fun loadCameraMatrices(camera: Camera) {
        setUniform("uProjection", camera.projectionMatrix)
        setUniform("uView", camera.viewMatrix)
    }
}