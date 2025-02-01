package cu.karellgz.formulae.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import cu.karellgz.formulae.R
import cu.karellgz.formulae.ui.theme.FormulaeTheme
import cu.karellgz.formulae.utils.Theme


@Preview
@Composable
fun ThemePickerPreview() {
    FormulaeTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            ThemePicker(rememberNavController(), SelectedTheme.Auto) {}
        }
    }
}

@Composable
fun ThemePicker(
    ctl: NavHostController,
    selected: SelectedTheme,
    onSelected: (SelectedTheme) -> Unit
) {
    Dialog(onDismissRequest = { ctl.popBackStack() }) {
        Card {
            Column(modifier = Modifier.padding(24.dp)) {

                Text(
                    text = stringResource(R.string.themepickerui_header),
                    modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 16.dp),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.headlineSmall
                )

                val themes: List<Pair<String, Pair<Theme?, SelectedTheme>>> = listOf(
                    Pair(
                        stringResource(R.string.themepickerui_white_on_black_background),
                        Pair(Theme.WHITE_ON_BLACK, SelectedTheme.Dark)
                    ),
                    Pair(
                        stringResource(R.string.themepickerui_black_on_white_background),
                        Pair(Theme.BLACK_ON_WHITE, SelectedTheme.Light)
                    ),
                    Pair(stringResource(R.string.themepickerui_system_default), Pair(null, SelectedTheme.Auto))
                )

                for (pr in themes) {
                    val name = pr.first
                    val theme = pr.second

                    Row(verticalAlignment = Alignment.CenterVertically) {

                        if (theme.first != null) {

                            val fg = theme.first!!.foreground
                            val bg = theme.first!!.background

                            Text(
                                " x ",
                                color = Color(android.graphics.Color.parseColor(fg)),
                                modifier = Modifier
                                    .padding(6.dp)
                                    .background(Color(android.graphics.Color.parseColor(bg)))
                            )
                        }

                        TextButton(
                            onClick = { onSelected(theme.second); ctl.popBackStack() },

                            enabled = theme.second != selected
                        ) {
                            Text(
                                name,
                                style = MaterialTheme.typography.labelLarge,
                                modifier = Modifier.weight(1f)
                            )

                        }
                    }
                }
            }
        }
    }
}

enum class SelectedTheme {
    Light,
    Dark,
    Auto;

    fun toTheme(isDark: Boolean): Theme {
        return if (this == Light) {
            Theme.BLACK_ON_WHITE
        } else if (this == Dark) {
            Theme.WHITE_ON_BLACK
        } else if (isDark) {
            Theme.WHITE_ON_BLACK
        } else {
            Theme.BLACK_ON_WHITE
        }
    }

    fun toShortForm(): String {
        return if (this == Light) {
            "light"
        } else if (this == Dark) {
            "dark"
        } else {
            "auto"
        }
    }
}