package net.lustenauer.mysticwoods.system

import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.quillraven.fleks.IntervalSystem
import com.github.quillraven.fleks.Qualifier
import ktx.assets.disposeSafely
import net.lustenauer.mysticwoods.MysticWoods.Companion.GAME_STAGE

class DebugSystem(
    private val phWorld: World,
    @Qualifier(GAME_STAGE) private val gameStage: Stage,
) : IntervalSystem(enabled = true) {
    private lateinit var box2DDebugRenderer: Box2DDebugRenderer

    init {
        if (enabled){
            box2DDebugRenderer = Box2DDebugRenderer()
        }
    }
    override fun onTick() {
        box2DDebugRenderer.render(phWorld, gameStage.camera.combined)
    }

    override fun onDispose() {
        if (enabled){
            box2DDebugRenderer.disposeSafely()
        }
    }
}
