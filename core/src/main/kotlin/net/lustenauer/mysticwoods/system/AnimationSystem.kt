package net.lustenauer.mysticwoods.system

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.github.quillraven.fleks.*
import ktx.app.gdxError
import ktx.collections.map
import ktx.log.logger
import net.lustenauer.mysticwoods.component.AnimationComponent
import net.lustenauer.mysticwoods.component.AnimationComponent.Companion.NO_ANIMATION
import net.lustenauer.mysticwoods.component.ImageComponent
import net.lustenauer.mysticwoods.const.Keys

@AllOf([AnimationComponent::class, ImageComponent::class])
class AnimationSystem(
    @Qualifier(Keys.TEXTURE_ATLAS) private val textureAtlas: TextureAtlas,
    private val animationCmps: ComponentMapper<AnimationComponent>,
    private val imageCmps: ComponentMapper<ImageComponent>
) : IteratingSystem() {

    private val cachedAnimations = mutableMapOf<String, Animation<TextureRegionDrawable>>()

    override fun onTickEntity(entity: Entity) {
        val aniCmp = animationCmps[entity]

        if (aniCmp.nextAnimation == NO_ANIMATION) {
            aniCmp.stateTime += deltaTime
        } else {
            aniCmp.animation = animation(aniCmp.nextAnimation)
            aniCmp.stateTime = 0f
            aniCmp.nextAnimation = NO_ANIMATION
        }
        aniCmp.animation.playMode = aniCmp.playMode
        imageCmps[entity].image.drawable = aniCmp.animation.getKeyFrame(aniCmp.stateTime)
    }

    private fun animation(aniKeyPath: String): Animation<TextureRegionDrawable> {
        return cachedAnimations.getOrPut(aniKeyPath) {
            log.debug { "New animation is created for '$aniKeyPath'" }
            val regions = textureAtlas.findRegions(aniKeyPath)
            if (regions.isEmpty) {
                gdxError("There is no texture regions for $aniKeyPath")
            }
            Animation(DEFAULT_FRAME_DURATION, regions.map { TextureRegionDrawable(it) })
        }
    }

    companion object {
        private val log = logger<AnimationSystem>()
        private const val DEFAULT_FRAME_DURATION = 1 / 8f
    }

}
