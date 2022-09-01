package net.lustenauer.mysticwoods

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import ktx.app.KtxGame
import ktx.app.KtxScreen
import net.lustenauer.mysticwoods.screen.GameScreen

class MysticWoods : KtxGame<KtxScreen>() {

    override fun create() {
        Gdx.app.logLevel = Application.LOG_DEBUG
        addScreen(GameScreen())
        setScreen<GameScreen>()
    }

    override fun dispose() {


    }

    companion object {
        const val UNIT_SCALE = 1 / 16f
    }
}
