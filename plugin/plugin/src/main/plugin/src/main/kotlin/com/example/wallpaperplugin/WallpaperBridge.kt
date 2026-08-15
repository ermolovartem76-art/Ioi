package com.example.wallpaperplugin

import android.app.WallpaperManager
import android.graphics.BitmapFactory
import org.godotengine.godot.Godot
import org.godotengine.godot.plugin.GodotPlugin
import org.godotengine.godot.plugin.UsedByGodot
import java.io.File

class WallpaperBridge(godot: Godot) : GodotPlugin(godot) {

    override fun getPluginName(): String {
        return "WallpaperBridge"
    }

    @UsedByGodot
    fun setWallpaperFromPath(path: String): Boolean {
        return try {
            val file = File(path)
            if (!file.exists()) {
                emitSignal("wallpaper_error", "Файл не найден: $path")
                return false
            }

            val bitmap = BitmapFactory.decodeFile(path)
            if (bitmap == null) {
                emitSignal("wallpaper_error", "Не удалось прочитать изображение")
                return false
            }

            val wallpaperManager = WallpaperManager.getInstance(activity)
            wallpaperManager.setBitmap(bitmap)

            emitSignal("wallpaper_set", true)
            true
        } catch (e: Exception) {
            emitSignal("wallpaper_error", e.message ?: "Неизвестная ошибка")
            false
        }
    }

    @UsedByGodot
    fun setWallpaperFromRes(resPath: String): Boolean {
        return setWallpaperFromPath(resPath)
    }

    override fun getPluginSignals(): MutableSet<org.godotengine.godot.plugin.SignalInfo> {
        return mutableSetOf(
            org.godotengine.godot.plugin.SignalInfo("wallpaper_set", Boolean::class.javaObjectType),
            org.godotengine.godot.plugin.SignalInfo("wallpaper_error", String::class.java)
        )
    }
}
