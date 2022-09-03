package net.lustenauer.mysticwoods.input

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys.*
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.World
import ktx.app.KtxInputAdapter
import net.lustenauer.mysticwoods.component.MoveComponent
import net.lustenauer.mysticwoods.component.PlayerComponent

class PlayerKeyboardInputProcessor(
    private val world: World,
    private val moveCmps: ComponentMapper<MoveComponent>
) : KtxInputAdapter {
    private var playerSin = 0f
    private var playerCos = 0f
    private val playerEntities = world.family(allOf = arrayOf(PlayerComponent::class))

    init {
        Gdx.input.inputProcessor = this
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode.isMovementKey()) {
            when (keycode) {
                UP -> playerSin = 1f
                DOWN -> playerSin = -1f
                RIGHT -> playerCos = 1f
                LEFT -> playerCos = -1f
            }
            updatePlayerMovement()
            return true
        }
        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        if (keycode.isMovementKey()) {
            when (keycode) {
                UP -> playerSin = if (Gdx.input.isKeyPressed(DOWN)) -1f else 0f
                DOWN -> playerSin = if (Gdx.input.isKeyPressed(UP)) 1f else 0f
                RIGHT -> playerCos = if (Gdx.input.isKeyPressed(LEFT)) -1f else 0f
                LEFT -> playerCos = if (Gdx.input.isKeyPressed(RIGHT)) 1f else 0f
            }
            updatePlayerMovement()
            return true
        }
        return false
    }

    private fun Int.isMovementKey(): Boolean {
        return this == UP || this == DOWN || this == LEFT || this == RIGHT
    }

    private fun updatePlayerMovement() {
        playerEntities.forEach { player ->
            with(moveCmps[player]) {
                cos = playerCos
                sin = playerSin
            }
        }
    }
}


