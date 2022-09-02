package net.lustenauer.mysticwoods

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.utils.viewport.FitViewport
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.assets.async.AssetStorage
import ktx.assets.disposeSafely
import ktx.async.KtxAsync
import net.lustenauer.mysticwoods.const.Consts
import net.lustenauer.mysticwoods.screen.LoadingScreen

class MysticWoods : KtxGame<KtxScreen>() {
    val gameStage: Stage by lazy { Stage(ExtendViewport(Consts.V_WIDTH, Consts.V_Height)) }
    val uiStage: Stage by lazy { Stage(FitViewport(Consts.V_WIDTH_PIXELS, Consts.V_HEIGHT_PIXELS)) }
    val assets: AssetStorage by lazy {
        KtxAsync.initiate()
        AssetStorage()
    }

    override fun create() {
        Gdx.app.logLevel = Application.LOG_DEBUG
        addScreen(LoadingScreen(this))
        setScreen<LoadingScreen>()
    }

    override fun dispose() {
        assets.disposeSafely()
    }

    companion object {
        const val UNIT_SCALE = 1 / 16f
    }
}
