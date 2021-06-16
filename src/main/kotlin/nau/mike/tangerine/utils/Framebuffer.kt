package nau.mike.tangerine.utils

import nau.mike.tangerine.Texture
import org.lwjgl.opengl.GL30C.*

class Framebuffer(var width: Int, var height: Int) {

    val id = glGenFramebuffers()
    val texture: Texture

    init {
        bind()

        texture = Texture(width = width, height = height)
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, texture.id, 0)

        val rboID = glGenRenderbuffers()
        glBindRenderbuffer(GL_RENDERBUFFER, rboID)
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT32, width, height)
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, rboID)

        val success = glCheckFramebufferStatus(GL_FRAMEBUFFER)
        if (success != GL_FRAMEBUFFER_COMPLETE) {
            throw RuntimeException("Framebuffer is not complete")
        }

        glBindFramebuffer(GL_FRAMEBUFFER, 0)
    }

    fun bind() {
        glBindFramebuffer(GL_FRAMEBUFFER, id)
    }

    fun unBind() {
        glBindFramebuffer(GL_FRAMEBUFFER, 0)
    }
}