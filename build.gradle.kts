import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.10"
}

group = "nau.mike"
version = "1.0-SNAPSHOT"

val lwjglVersion = "3.2.3"
val jomlVersion = "1.10.1"
val imguiVersion = "1.83.0"
val macNatives = "natives-macos"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    testImplementation(kotlin("test"))

    implementation("org.jetbrains.kotlin", "kotlin-reflect", "1.2.51")

    // LWJGL
    implementation(platform("org.lwjgl:lwjgl-bom:$lwjglVersion"))

    implementation("org.lwjgl", "lwjgl")
    implementation("org.lwjgl", "lwjgl-glfw")
    implementation("org.lwjgl", "lwjgl-jemalloc")
    implementation("org.lwjgl", "lwjgl-openal")
    implementation("org.lwjgl", "lwjgl-opengl")
    implementation("org.lwjgl", "lwjgl-stb")
    implementation("org.lwjgl", "lwjgl-nfd")

    runtimeOnly("org.lwjgl", "lwjgl", classifier = macNatives)
    runtimeOnly("org.lwjgl", "lwjgl-glfw", classifier = macNatives)
    runtimeOnly("org.lwjgl", "lwjgl-jemalloc", classifier = macNatives)
    runtimeOnly("org.lwjgl", "lwjgl-openal", classifier = macNatives)
    runtimeOnly("org.lwjgl", "lwjgl-opengl", classifier = macNatives)
    runtimeOnly("org.lwjgl", "lwjgl-stb", classifier = macNatives)
    runtimeOnly("org.lwjgl", "lwjgl-nfd", classifier = macNatives)

    // Java OpenGL Math Library
    implementation("org.joml", "joml", jomlVersion)

    // ImGui
    implementation("io.github.spair", "imgui-java-binding", imguiVersion)
    implementation("io.github.spair", "imgui-java-lwjgl3", imguiVersion)
    implementation("io.github.spair", "imgui-java-$macNatives", imguiVersion)
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}