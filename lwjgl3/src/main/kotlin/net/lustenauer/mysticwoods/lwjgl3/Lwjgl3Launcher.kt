@file:JvmName("Lwjgl3Launcher")

package net.lustenauer.mysticwoods.lwjgl3

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import net.lustenauer.mysticwoods.MysticWoods

/** Launches the desktop (LWJGL3) application. */
fun main() {
    Lwjgl3Application(MysticWoods(), Lwjgl3ApplicationConfiguration().apply {
        setTitle("MysticWoods")
        setWindowedMode(1280, 720)
        setWindowIcon(*(arrayOf(128, 64, 32, 16).map { "libgdx$it.png" }.toTypedArray()))
        useVsync(false)
    })
}
