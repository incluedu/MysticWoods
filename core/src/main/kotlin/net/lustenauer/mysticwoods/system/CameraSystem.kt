package net.lustenauer.mysticwoods.system

import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.quillraven.fleks.*
import ktx.tiled.height
import ktx.tiled.width
import net.lustenauer.mysticwoods.MysticWoods.Companion.GAME_STAGE
import net.lustenauer.mysticwoods.component.ImageComponent
import net.lustenauer.mysticwoods.component.PlayerComponent
import net.lustenauer.mysticwoods.event.MapChangeEvent

@AllOf([PlayerComponent::class, ImageComponent::class])
class CameraSystem(
    private val imageCmps: ComponentMapper<ImageComponent>,
    @Qualifier(GAME_STAGE) stage: Stage,
) : EventListener, IteratingSystem() {
    private var maxWidth = 0f
    private var maxHeight = 0f
    private val camera = stage.camera

    override fun onTickEntity(entity: Entity) {
        with(imageCmps[entity]) {
            val viewWidth = camera.viewportWidth * 0.5f
            val viewHeight = camera.viewportHeight * 0.5f
            camera.position.set(
                image.x.coerceIn(viewWidth, maxWidth - viewWidth),
                image.y.coerceIn(viewHeight, maxHeight - viewHeight),
                camera.position.z
            )
        }
    }

    override fun handle(event: Event): Boolean {
        if (event is MapChangeEvent) {
            maxWidth = event.map.width.toFloat()
            maxHeight = event.map.height.toFloat()
            return true
        }
        return false
    }
}
