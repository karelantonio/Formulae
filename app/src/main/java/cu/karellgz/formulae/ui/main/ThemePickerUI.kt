package cu.karellgz.formulae.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import cu.karellgz.formulae.ui.theme.FormulaeTheme
import cu.karellgz.formulae.utils.Theme


@Preview
@Composable
fun ThemePickerPreview() {
    FormulaeTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            ThemePicker(rememberNavController(), Theme.BLACK_ON_WHITE) {}
        }
    }
}

@Composable
fun ThemePicker(ctl: NavHostController, selected: Theme?, onSelected: (Theme) -> Unit) {
    Dialog(onDismissRequest = { ctl.popBackStack() }) {
        Card {
            Column(modifier = Modifier.padding(24.dp)) {

                Text(
                    text = "Select the theme:",
                    modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 16.dp),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.headlineSmall
                )

                val themes: List<Pair<String, Theme>> = listOf(
                    Pair("White on black background", Theme.WHITE_ON_BLACK),
                    Pair("Black on white background", Theme.BLACK_ON_WHITE),
                )

                for (pr in themes) {
                    val name = pr.first
                    val theme = pr.second

                    TextButton(onClick = { onSelected(theme); ctl.popBackStack() }) {
                        Text(
                            " x ",
                            color = Color(android.graphics.Color.parseColor(theme.foreground)),
                            modifier = Modifier
                                .padding(6.dp)
                                .background(Color(android.graphics.Color.parseColor(theme.background)))
                        )
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