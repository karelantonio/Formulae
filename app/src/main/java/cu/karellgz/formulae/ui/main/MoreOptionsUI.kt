package cu.karellgz.formulae.ui.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import cu.karellgz.formulae.BuildConfig
import cu.karellgz.formulae.R
import cu.karellgz.formulae.ui.theme.FormulaeTheme


@Preview("More options preview")
@Composable
fun MoreOptionsPreview() {
    FormulaeTheme {
        Card(modifier = Modifier.fillMaxSize()) {
            MoreOptionsUI(ctl = rememberNavController())
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreOptionsUI(ctl: NavController) {
    AlertDialog(onDismissRequest = { ctl.popBackStack() }) {
        Card {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    "More options",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 16.dp)
                )

                TextButton(onClick = {
                    ctl.navigate("themePicker")
                }) {

                    Icon(
                        painter = painterResource(id = R.drawable.baseline_dark_mode_24),
                        contentDescription = "Choose the preview theme"
                    )

                    Text("Choose the preview theme")
                }

                TextButton(onClick = {
                    // Launch the telegram site
                }) {

                    Icon(
                        painter = painterResource(id = R.drawable.baseline_menu_book_24),
                        contentDescription = "Tutorial and more"
                    )

                    Text("Tutorials and more")
                }

                Text(
                    text = "Formulae ${BuildConfig.VERSION_NAME} (commit ${
                        BuildConfig.COMMIT.substring(
                            0..10
                        )
                    })",
                    modifier = Modifier.padding(0.dp, 24.dp, 0.dp, 0.dp)
                )
            }
        }
    }
}