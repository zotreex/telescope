package ru.zotreex.telescope.player

import android.view.Surface
import androidx.annotation.IntDef
import androidx.compose.foundation.AndroidEmbeddedExternalSurface
import androidx.compose.foundation.AndroidExternalSurface
import androidx.compose.foundation.AndroidExternalSurfaceScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.media3.common.Player

@Composable
fun PlayerSurface(player: Player, surfaceType: @SurfaceType Int, modifier: Modifier = Modifier) {
    val onSurfaceCreated: (Surface) -> Unit = { surface -> player.setVideoSurface(surface) }
    val onSurfaceDestroyed: () -> Unit = { player.setVideoSurface(null) }
    val onSurfaceInitialized: AndroidExternalSurfaceScope.() -> Unit = {
        onSurface { surface, _, _ ->
            onSurfaceCreated(surface)
            surface.onDestroyed { onSurfaceDestroyed() }
        }
    }

    when (surfaceType) {
        SURFACE_TYPE_SURFACE_VIEW ->
            AndroidExternalSurface(modifier = modifier, onInit = onSurfaceInitialized)
        SURFACE_TYPE_TEXTURE_VIEW ->
            AndroidEmbeddedExternalSurface(modifier = modifier, onInit = onSurfaceInitialized)
        else -> throw IllegalArgumentException("Unrecognized surface type: $surfaceType")
    }
}

/**
 * The type of surface view used for media playbacks. One of [SURFACE_TYPE_SURFACE_VIEW] or
 * [SURFACE_TYPE_TEXTURE_VIEW].
 */
@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPE, AnnotationTarget.TYPE_PARAMETER)
@IntDef(SURFACE_TYPE_SURFACE_VIEW, SURFACE_TYPE_TEXTURE_VIEW)
annotation class SurfaceType

/** Surface type equivalent to [SurfaceView] . */
const val SURFACE_TYPE_SURFACE_VIEW = 1
/** Surface type equivalent to [TextureView]. */
const val SURFACE_TYPE_TEXTURE_VIEW = 2