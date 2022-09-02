package net.lustenauer.mysticwoods.screen

import ktx.app.KtxScreen
import ktx.assets.async.AssetStorage
import net.lustenauer.mysticwoods.MysticWoods

abstract class MysticWoodsScreen(
    val game: MysticWoods,
) : KtxScreen {
    val assets: AssetStorage = game.assets

    override fun resize(width: Int, height: Int) {
        game.gameStage.viewport.update(width, height, true)
        game.uiStage.viewport.update(width, height, true)
    }
}

