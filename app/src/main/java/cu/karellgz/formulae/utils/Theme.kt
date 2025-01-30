package cu.karellgz.formulae.utils

import android.os.Parcel
import android.os.Parcelable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

val themeSaver =
    Saver<Theme, List<String>>(save = { theme -> listOf(theme.foreground, theme.background) },
        restore = { list -> Theme(list[0], list[1]) })

val mutableStateThemeSaver =
    Saver<MutableState<Theme>, List<String>>(save = { theme -> listOf(theme.value.foreground, theme.value.background) },
        restore = { list -> mutableStateOf(Theme(list[0], list[1])) })

data class Theme(val foreground: String, val background: String) {

    companion object {
        val BLACK_ON_WHITE = Theme(foreground = "#000000", background = "#ffffff")
        val WHITE_ON_BLACK = Theme(foreground = "#ffffff", background = "#000000")

        @OptIn(ExperimentalStdlibApi::class)
        fun from(fore: Color, bg: Color): Theme {

            val foreInt = fore.toArgb().shl(8)
            val bgInt = bg.toArgb().shl(8)

            return Theme("#" + foreInt.toHexString(), "#" + bgInt.toHexString())
        }
    }
}
