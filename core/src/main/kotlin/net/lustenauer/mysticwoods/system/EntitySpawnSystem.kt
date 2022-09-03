package net.lustenauer.mysticwoods.system

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.Scaling
import com.github.quillraven.fleks.*
import ktx.app.gdxError
import ktx.box2d.box
import ktx.math.vec2
import ktx.tiled.layer
import ktx.tiled.x
import ktx.tiled.y
import net.lustenauer.mysticwoods.MysticWoods.Companion.TEXTURE_ATLAS
import net.lustenauer.mysticwoods.MysticWoods.Companion.UNIT_SCALE
import net.lustenauer.mysticwoods.component.*
import net.lustenauer.mysticwoods.component.PhysicComponent.Companion.physicCmpFromImage
import net.lustenauer.mysticwoods.event.MapChangeEvent

@AllOf([SpawnComponent::class])
class EntitySpawnSystem(
    @Qualifier(TEXTURE_ATLAS) private val textureAtlas: TextureAtlas,
    private val phWorld: World,
    private val spawnCmps: ComponentMapper<SpawnComponent>,
) : EventListener, IteratingSystem() {
    private val cachedCfgs = mutableMapOf<String, SpawnCfg>()
    private val cachedSizes = mutableMapOf<AnimationModel, Vector2>()

    override fun onTickEntity(entity: Entity) {
        with(spawnCmps[entity]) {
            val cfg = spawnCfg(name)
            val relativeSize = size(cfg.model)

            world.entity {
                val imageCmp = add<ImageComponent> {
                    image = Image().apply {
                        setScaling(Scaling.fill)
                        setPosition(location.x, location.y)
                        setSize(relativeSize.x, relativeSize.y)
                    }
                }
                add<AnimationComponent> {
                    nextAnimation(cfg.model, AnimationType.IDLE)
                }

                physicCmpFromImage(
                    phWorld,
                    imageCmp.image,
                    BodyDef.BodyType.DynamicBody
                ) { phCmp, width, height ->
                    box(width, height) {
                        isSensor = false
                    }
                }

                if (cfg.speedScaling > 0f){
                    add<MoveComponent> {
                        speed = DEFAULT_SPEED * cfg.speedScaling
                    }
                }

                if (name == PLAYER_NAME) {
                    add<PlayerComponent>()
                }
            }
        }
        world.remove(entity)
    }

    private fun spawnCfg(entityName: String): SpawnCfg = cachedCfgs.getOrPut(entityName) {
        when (entityName) {
            PLAYER_NAME -> SpawnCfg(AnimationModel.PLAYER)
            SLIME_NAME -> SpawnCfg(AnimationModel.SLIME)
            CHEST_NAME -> SpawnCfg(AnimationModel.CHEST)
            else -> gdxError("Entity $entityName has no SpawnCfg")
        }
    }

    private fun size(model: AnimationModel): Vector2 = cachedSizes.getOrPut(model) {

        val regions = textureAtlas.findRegions("${model.atlasKey}/${AnimationType.IDLE.atlasKey}")
        if (regions.isEmpty) {
            gdxError("There ara no regions for the idle animations of model $model")
        }
        val firstFrame = regions.first()
        vec2(firstFrame.originalWidth * UNIT_SCALE, firstFrame.originalHeight * UNIT_SCALE)
    }

    override fun handle(event: Event): Boolean {
        when (event) {
            is MapChangeEvent -> {
                val entityLayer = event.map.layer("entities")
                entityLayer.objects.forEach { mapObject ->
                    val name = mapObject.name ?: gdxError("MapObject $mapObject douse not hava a name!")
                    world.entity {
                        add<SpawnComponent> {
                            this.name = name
                            this.location.set(mapObject.x * UNIT_SCALE, mapObject.y * UNIT_SCALE)
                        }
                    }
                }
                return true
            }
        }
        return false
    }

    companion object {
        private const val PLAYER_NAME = "PLAYER"
        private const val SLIME_NAME = "SLIME"
        private const val CHEST_NAME = "CHEST"
    }
}
