package net.lustenauer.mysticwoods.screen

import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import ktx.app.KtxScreen
import ktx.async.KtxAsync
import net.lustenauer.mysticwoods.MysticWoods
import net.lustenauer.mysticwoods.assets.TextureAtlasAsset

class LoadingScreen(private val game: MysticWoods) : KtxScreen {
    override fun show() {
        // queue asset loading
        val assetRefs = mutableListOf(
            TextureAtlasAsset.values().map { game.assets.loadAsync(it.descriptor) },
//            TiledMapAsset.values().map { game.assets.loadAsync(it.descriptor) }
        ).flatten()

        // once assets are loading --> change to game Screen
        KtxAsync.launch {
            assetRefs.joinAll()
            assetsLoaded()
        }

        // ...
        // todo setup ui and display a loading bar
    }

    private fun assetsLoaded() {
        game.addScreen(GameScreen(game))
        game.setScreen<GameScreen>()
        game.removeScreen<LoadingScreen>()
        dispose()
    }
}
