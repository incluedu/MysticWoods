package net.lustenauer.mysticwoods.assets

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader

enum class TextureAtlasAsset(
    fileName: String,
    directory: String = "graphics",
    val descriptor: AssetDescriptor<TextureAtlas> = AssetDescriptor("$directory/$fileName", TextureAtlas::class.java)
) {
    GAME_ATLAS("game.atlas")
}

//enum class TiledMapAsset(
//    fileName: String,
//    directory: String = "maps",
//    val descriptor: AssetDescriptor<TiledMap> = AssetDescriptor(
//        TmxMapLoader().load("$directory/$fileName"),
//        TiledMap::class.java
//    )
//) {
//    DEMO_MAP("demo.tmx")
//}
