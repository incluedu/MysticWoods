package net.lustenauer.mysticwoods.screen

import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.github.quillraven.fleks.World
import com.github.quillraven.fleks.world
import ktx.assets.disposeSafely
import ktx.log.logger
import net.lustenauer.mysticwoods.MysticWoods
import net.lustenauer.mysticwoods.assets.TextureAtlasAsset
import net.lustenauer.mysticwoods.component.AnimationComponent
import net.lustenauer.mysticwoods.component.AnimationModel
import net.lustenauer.mysticwoods.component.AnimationType
import net.lustenauer.mysticwoods.component.ImageComponent
import net.lustenauer.mysticwoods.component.ImageComponent.Companion.ImageComponentLister
import net.lustenauer.mysticwoods.const.Keys
import net.lustenauer.mysticwoods.event.MapChangeEvent
import net.lustenauer.mysticwoods.event.fire
import net.lustenauer.mysticwoods.system.AnimationSystem
import net.lustenauer.mysticwoods.system.RenderSystem

class GameScreen(game: MysticWoods) : MysticWoodsScreen(game) {
    private val gameStage = game.gameStage
    private val textureAtlas = assets[TextureAtlasAsset.GAME_ATLAS.descriptor]
    private var currentMap: TiledMap? = null
    private val world: World = world {
        injectables {
            add(Keys.GAME_STAGE, gameStage)
            add(Keys.TEXTURE_ATLAS, textureAtlas)
        }

        components {
            add<ImageComponentLister>()
        }

        systems {
            add<AnimationSystem>()
            add<RenderSystem>()
        }
    }

    override fun show() {
        log.debug { "GameScreen gets shown" }

        world.systems.forEach {
            if (it is EventListener) {
                gameStage.addListener(it)
            }
        }
//        assets[TiledMapAsset.DEMO_MAP.descriptor].apply {
//            gameStage.fire(MapChangeEvent(this))
//        }

        currentMap = TmxMapLoader().load("maps/demo.tmx").apply {
            gameStage.fire(MapChangeEvent(this))
        }

        world.entity {
            add<ImageComponent>() {
                image = Image().apply {
                    setSize(4f, 4f)
                }
            }
            add<AnimationComponent> {
                nextAnimation(AnimationModel.PLAYER, AnimationType.IDLE)
            }

        }

        world.entity {
            add<ImageComponent>() {
                image = Image().apply {
                    setSize(4f, 4f)
                    setPosition(12f, 0f)
                }
            }
            add<AnimationComponent> {
                nextAnimation(AnimationModel.SLIME, AnimationType.RUN)
            }
        }

    }

    override fun resize(width: Int, height: Int) {
        gameStage.viewport.update(width, height, true)
    }

    override fun render(delta: Float) {
        world.update(delta)
    }

    override fun dispose() {
        gameStage.disposeSafely()
        textureAtlas.disposeSafely()
        currentMap.disposeSafely()
        world.dispose()
    }

    companion object {
        private val log = logger<GameScreen>()
    }
}
