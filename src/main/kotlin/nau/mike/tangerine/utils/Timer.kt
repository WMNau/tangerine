package nau.mike.tangerine.utils

object Timer {

    private var lastTime = System.nanoTime()
    private var timer = System.currentTimeMillis()

    private val ns = 1_000_000_000.0f / 60.0f
    var delta = 0.0f

    private var frames = 0
    private var updates = 0

    fun start() {
        val now = System.nanoTime()
        delta += (now - lastTime) / ns
        lastTime = now
    }

    fun shouldUpdate(): Boolean {
        return delta >= 1.0f
    }

    fun update() {
        updates++
        delta--
    }

    fun render() {
        frames++
    }

    fun shouldReset(): Boolean {
        return System.currentTimeMillis() - timer > 1000L
    }

    fun reset() {
        timer += 1000L
        updates = 0
        frames = 0
    }

    fun getString(): String {
        return " | UPS: $updates, FPS: $frames"
    }
}