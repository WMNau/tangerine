package nau.mike.tangerine.nau.mike.tangerine

class Tangerine {

    private var window: Window = Window(800, 800, "Tangerine Game Engine")
    private var running: Boolean = false

    private fun init() {
    }

    private fun update() {}

    private fun render() {}

    private fun clean() {
    }

    private fun run() {
        init()

        while (!window.shouldClose()) {
            window.update()
            update()
            render()
            window.render()
        }

        running = false
        clean()
        window.clean()
    }

    fun start() {
        if (running) {
            return
        }

        running = true
        run()
    }
}
