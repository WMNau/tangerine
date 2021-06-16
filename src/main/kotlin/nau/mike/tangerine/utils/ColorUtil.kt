package nau.mike.tangerine.utils

import nau.mike.tangerine.isGLInitialized
import org.joml.Vector4f
import org.lwjgl.opengl.GL11C.glClearColor
import java.lang.Float.max
import java.lang.Float.min

@Suppress("unused")
object ColorUtil {

    val SKY_BLUE = toFloatRGBA(115, 215, 255, 255)
    val MIDNIGHT = rgba(0.07f, 0.13f, 0.17f, 1.0f)
    val BLACK = toFloatRGBA(0, 0, 0, 0)

    private fun toFloatRGBA(red: Int, green: Int, blue: Int, alpha: Int): Vector4f {
        val r = clamp(red / 255.0f, 0.0f, 1.0f)
        val g = clamp(green / 255.0f, 0.0f, 1.0f)
        val b = clamp(blue / 255.0f, 0.0f, 1.0f)
        val a = clamp(alpha / 255.0f, 0.0f, 1.0f)

        return rgba(r, g, b, a)
    }

    private fun rgba(red: Float, green: Float, blue: Float, alpha: Float): Vector4f {
        val r = clamp(red, 0.0f, 1.0f)
        val g = clamp(green, 0.0f, 1.0f)
        val b = clamp(blue, 0.0f, 1.0f)
        val a = clamp(alpha, 0.0f, 1.0f)

        return Vector4f(r, g, b, a)
    }

    fun clamp(value: Float, min: Float, max: Float): Float {
        return min(max, max(value, min))
    }
}

fun Vector4f.clearColor() {
    if (isGLInitialized) {
        val r = ColorUtil.clamp(this.x, 0.0f, 1.0f)
        val g = ColorUtil.clamp(this.y, 0.0f, 1.0f)
        val b = ColorUtil.clamp(this.z, 0.0f, 1.0f)
        val a = ColorUtil.clamp(this.w, 0.0f, 1.0f)

        glClearColor(r, g, b, a)
    }
}