package net.lustenauer.mysticwoods.screen

import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.github.quillraven.fleks.world
import ktx.assets.disposeSafely
import ktx.box2d.createWorld
import ktx.log.logger
import ktx.math.vec2
import net.lustenauer.mysticwoods.MysticWoods
import net.lustenauer.mysticwoods.MysticWoods.Companion.GAME_STAGE
import net.lustenauer.mysticwoods.MysticWoods.Companion.TEXTURE_ATLAS
import net.lustenauer.mysticwoods.assets.TextureAtlasAsset
import net.lustenauer.mysticwoods.component.ImageComponent.Companion.ImageComponentLister
import net.lustenauer.mysticwoods.component.PhysicComponent.Companion.PhysicComponentListener
import net.lustenauer.mysticwoods.event.MapChangeEvent
import net.lustenauer.mysticwoods.event.fire
import net.lustenauer.mysticwoods.input.PlayerKeyboardInputProcessor
import net.lustenauer.mysticwoods.system.*

class GameScreen(game: MysticWoods) : MysticWoodsScreen(game) {
    private val gameStage = game.gameStage
    private val textureAtlas = assets[TextureAtlasAsset.GAME_ATLAS.descriptor]
    private var currentMap: TiledMap? = null
    private val phWorld = createWorld(gravity = vec2()).apply {
        autoClearForces = false
    }

    private val entityWorld = world {
        injectables {
            add(phWorld)
            add(GAME_STAGE, gameStage)
            add(TEXTURE_ATLAS, textureAtlas)
        }

        components {
            add<ImageComponentLister>()
            add<PhysicComponentListener>()
        }

        systems {
            add<EntitySpawnSystem>()
            add<MoveSystem>()
            add<PhysicSystem>()
            add<AnimationSystem>()
            add<CameraSystem>()
            add<RenderSystem>()
            add<DebugSystem>()
        }
    }

    override fun show() {
        log.debug { "GameScreen gets shown" }

        entityWorld.systems.forEach {
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

        PlayerKeyboardInputProcessor(entityWorld, entityWorld.mapper())
    }

    override fun resize(width: Int, height: Int) {
        gameStage.viewport.update(width, height, true)
    }

    override fun render(delta: Float) {
        entityWorld.update(delta.coerceAtMost(0.25f))
    }

    override fun dispose() {
        gameStage.disposeSafely()
        textureAtlas.disposeSafely()
        currentMap.disposeSafely()
        entityWorld.dispose()
        phWorld.disposeSafely()
    }

    companion object {
        private val log = logger<GameScreen>()
    }
}
